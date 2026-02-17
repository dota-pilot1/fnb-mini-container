import React, { forwardRef, useRef } from 'react'
import { useEffect } from 'react'
import { TabulatorFull } from 'tabulator-tables'
import { v4 as uuidv4 } from 'uuid'
import 'tabulator-tables/dist/css/tabulator_simple.min.css'
import '@/assets/style/grid.scss'
import { RowStatus } from '@/constants/row-status-const'
import { availableClipboardPaste, generateHeaderMenu, isReadRow } from '@/utils/tabulator/util'
import TabulatorInteractionFactory from '@/utils/tabulator/interaction'
import * as Formatters from '@/utils/tabulator/formatter'
import * as Editors from '@/utils/tabulator/editor'
import TabulatorEventFactory from '@/utils/tabulator/events'
import { fetchTabulatorData } from '@/api/tabulator'

/**
 * TabulatorFull을 customizing한 component
 */
export default forwardRef(function Tabulator(props, ref) {
  const initialize = useRef(false)
  const el = useRef(null)
  const id = useRef(props.id || uuidv4())
  const instance = ref
  const prevDataJsonString = useRef(null)

  const initTabulator = () => {
    const config = {
      ...props,
      columns: props.columns?.map((column) => redefinePropsColumn(column)),
      rowFormatter: Formatters.rowFormatter,
      ajaxRequestFunc,
    }

    deleteUnnecessaryConfigPropeties(config)
    defineDefaultOptions()
    extendModules()

    instance.current = new TabulatorFull(el.current, config)

    bindingCustomeInteraction()
    bindingPropsEvents(props.events)
  }

  const redefinePropsColumn = (column) => {
    const copy = { ...column }
    if (copy.headerMenu) {
      copy.headerMenu = () => generateHeaderMenu(instance.current.getColumns())
    }
    if (copy.editable) {
      if (typeof copy.editable === 'function') {
        copy.editableFunction = copy.editable
      }
    }
    return copy
  }

  const ajaxRequestFunc = (url, config, params) => {
    return new Promise((resolve, reject) => {
      fetchTabulatorData({ url, config, params })
        .then((response) => {
          resolve(response.data)
        })
        .catch((error) => reject(error))
    })
  }

  const deleteUnnecessaryConfigPropeties = (config) => {
    delete config['id']
    delete config['events']
    delete config['defaultOptions']
    delete config['setPageButtons']
    delete config['onChangeTotalCount']
  }

  const defineDefaultOptions = () => {
    if (props.marginTop)
      TabulatorFull.defaultOptions.height = `calc(${props.heightPersent || 100}% - ${props.marginTop})`
    else TabulatorFull.defaultOptions.height = `100%`
    TabulatorFull.defaultOptions.layout = 'fitColumns'
    TabulatorFull.defaultOptions.placeholder = ''
    TabulatorFull.defaultOptions.clipboardCopyRowRange = 'selected'
    TabulatorFull.defaultOptions.clipboardCopyStyled = false
    TabulatorFull.defaultOptions.groupToggleElement = 'header'
    TabulatorFull.defaultOptions.columnHeaderVertAlign = 'middle'
    TabulatorFull.defaultOptions.columnCalcs = 'both'
    TabulatorFull.defaultOptions.downloadRowRange = 'active'
    TabulatorFull.defaultOptions.dataLoader = false
    TabulatorFull.defaultOptions.movableColumns = true
    TabulatorFull.defaultOptions.downloadConfig = {
      columnHeaders: true,
      columnGroups: true,
      rowGroups: true,
      columnCalcs: true,
      dataTree: true,
    }

    TabulatorFull.defaultOptions.columnDefaults = {
      headerSortStartingDir: 'desc',
      headerSort: false,
      headerHozAlign: 'center',
      hozAlign: 'center',
      editable: false,
    }

    TabulatorFull.defaultOptions.clipboardPasteParser = (clipboard) => {
      const columnClipboardOption = instance.current.custom.clickedCell.getColumn().getDefinition().clipboard
      if (!availableClipboardPaste(columnClipboardOption)) {
        return []
      }

      const visibleColumns = instance.current.columnManager.getColumns().filter((col) => col.visible)
      const rowPosition = instance.current.getRowPosition(instance.current.custom.clickedCell.getRow())
      const columnPosition = visibleColumns.findIndex(
        (col) => col.field === instance.current.custom.clickedCell.getField(),
      )

      const displayRowCount = instance.current.rowManager.displayRowsCount
      let newDisplayRowCount = 0
      const isLastItem = (idx, arr) => idx === arr.length - 1

      return clipboard
        .split('\r\n')
        .map((row) => row.split('\t'))
        .filter((cols, idx, arr) => !isLastItem(idx, arr) || (isLastItem(idx, arr) && cols[0].length))
        .map((pastedRow, rowIndex) => {
          const mappedRowIndex = rowPosition + rowIndex
          const pasteRowPositon =
            displayRowCount >= mappedRowIndex ? mappedRowIndex : displayRowCount + ++newDisplayRowCount

          return pastedRow.slice(0, 1).reduce(
            (acc, pasteCol, colIndex) => {
              return {
                ...acc,
                [visibleColumns[columnPosition + colIndex].field]: pasteCol,
              }
            },
            { rowNum: pasteRowPositon },
          )
        })
    }

    TabulatorFull.defaultOptions.clipboardPasteAction = (rowData) => {
      return new Promise((resolve, reject) => {
        const displayRowCount = instance.current.getDataCount()
        const rows = []

        if (!rowData || !rowData.length) {
          resolve([])
        }

        const addRows = []

        for (const pastedRowData of rowData) {
          const rowNum = pastedRowData.rowNum

          if (rowNum <= displayRowCount) {
            const target = instance.current.getRowFromPosition(rowNum)
            if (isReadRow(target)) {
              pastedRowData.status = RowStatus.UPDATE.value
              pastedRowData.statusName = RowStatus.UPDATE.name
            }

            target.update(pastedRowData).then(() => {
              rows.push(target)
              target.select()
            })
          } else {
            addRows.unshift(pastedRowData)
          }
        }

        if (addRows.length) {
          instance.current.rowManager.addRows(addRows, false, addRows[0].rowNum).then((addedRows) => {
            addedRows.forEach((addedRow) => rows.push(addedRow.getComponent()))
          })
        }

        resolve(rows)
      })
    }

    if (props.defaultOptions) {
      for (let optionKey of Object.keys(props.defaultOptions)) {
        TabulatorFull.defaultOptions[optionKey] = props.defaultOptions[optionKey]
      }
    }
  }

  const extendModules = () => {
    TabulatorFull.extendModule('page', 'pageCounters', {
      rowpage(pageSize, currentRow, currentPage, totalRows, totalPages) {
        if (pageSize && currentPage && totalRows) {
          return `<span class="fc-gray">전체 ${totalRows} 중</span> <span class="fw-bold">${(currentPage - 1) * pageSize + 1
            }-${totalRows}</span>`
        }
      },
    })

    TabulatorFull.extendModule('format', 'formatters', {
      rownum: Formatters.rownum,
      title: Formatters.title,
      number: Formatters.number,
      currency: Formatters.currency,
      date: Formatters.date,
      dateTime: Formatters.dateTime,
      checkbox: Formatters.checkbox,
      button: Formatters.button,
      calcTotalText: Formatters.calcTotalText,
      required: Formatters.required,
      radio: Formatters.radio,
      fileInput: Formatters.fileInput,
      inputbox: Formatters.inputbox,
    })

    TabulatorFull.extendModule('edit', 'editors', {
      date: Editors.date,
    })
  }

  const bindingCustomeInteraction = () => {
    instance.current.custom = TabulatorInteractionFactory(instance)
  }

  const bindingPropsEvents = (events = {}) => {
    const customEvents = customizingEvents(events)
    Object.keys(customEvents).forEach((eventName) => {
      const handler = customEvents[eventName]
      instance.current.on(eventName, handler)
    })
  }

  const customizingEvents = (events) => {
    return TabulatorEventFactory({
      instance,
      events,
      setPageButtons: props.setPageButtons,
      onChangeTotalCount: props.onChangeTotalCount,
    })
  }

  useEffect(() => {
    if (!initialize.current) {
      initTabulator()
      initialize.current = true
      prevDataJsonString.current = JSON.stringify(props.data)
    }
  })

  useEffect(() => {
    const propsDataJsonString = JSON.stringify(props.data)

    if (prevDataJsonString.current !== propsDataJsonString) {
      if (!instance.current.options.pagination) {
        instance.current.replaceData(props.data)
        prevDataJsonString.current = propsDataJsonString
      }
      if (props.data) {
        instance.current.replaceData(props.data)
        prevDataJsonString.current = propsDataJsonString
      }
    }
  }, [instance, props.data])

  useEffect(() => {
    if (props.openAllGroupHeader !== undefined && instance && instance.current && props.data && props.data.length > 0) {
      instance.current.blockRedraw()
      let groups = instance.current.getGroups()
      for (let i = 0; i < groups.length; i++) {
        if (props.openAllGroupHeader === false) {
          groups[i].hide()
        } else {
          groups[i].show()
        }
      }
      instance.current.restoreRedraw()
    }
  }, [instance, props.data, props.openAllGroupHeader])

  return <div id={id.current} ref={el} />
})
