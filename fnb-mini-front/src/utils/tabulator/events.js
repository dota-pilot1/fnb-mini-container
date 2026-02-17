import { RowStatus } from '@/constants/row-status-const'
import {
  availableClipboardPaste,
  disableChildNodes,
  getInitialRowData,
  isDeletedRow,
  isReadRow,
  isUpdatedRow,
} from './util'

/**
 * Tabulator Event들을 customize하고 props로 전달받은 event들을 Intercept하여 중간처리 실행
 * instance를 참조하기 위해서 Function Factory 형태 사용
 * 이벤트 순서
 * (1) Column 정의
 * (2) Custom Event Binding
 * (3) events props
 */
const TabulatorEventFactory = ({ instance, events, setPageButtons, onChangeTotalCount } = {}) => {
  const cellEdited = (cell) => {
    const table = cell.getTable()
    const index = table.options.index
    const row = cell.getRow()
    if (isReadRow(row)) {
      table.custom.updateRow(row)
    } else if (isUpdatedRow(row)) {
      table.custom.getEditedData().forEach((editedData) => {
        delete editedData[index]
        delete editedData.status
        delete editedData.statusName

        if (Object.keys(editedData).length < 1) {
          row.deselect()
        }
      })
    }
    events.cellEdited?.(cell)
  }

  const cellClick = (e, cell) => {
    const columnDefinition = cell.getColumn().getDefinition()
    if (columnDefinition.editor) {
      if (availableClipboardPaste(columnDefinition.clipboard)) {
        cell.edit(true)
      }
      cell.edit(false)
    }
    if (instance.current.custom.clickedCell && instance.current.custom.clickedCell.getElement()) {
      const clickedElement = instance.current.custom.clickedCell.getElement()
      if (clickedElement) {
        clickedElement.classList.remove('marking-cell')
      }
    }
    if (cell.getElement()) {
      cell.getElement().classList.add('marking-cell')
      instance.current.custom.clickedCell = cell
    }
    events.cellClick?.(e, cell)
  }

  const cellDblClick = (e, cell) => {
    const definition = cell.getColumn().getDefinition()
    if (definition.editor && !isDeletedRow(cell.getRow())) {
      if (definition.editableFunction) {
        cell.edit(definition.editableFunction(cell))
      } else {
        cell.edit(true)
      }
    }
    events.cellDblClick?.(e, cell)
  }

  const rowClick = (e, row) => {
    const { tagName, type } = e.target
    if (tagName === 'INPUT') {
      if (type === 'checkbox' || type === 'radio') {
        return
      }
    }
    events.rowClick?.(e, row)
  }

  const rowDeselected = (row) => {
    const table = row.getTable()
    const index = table.options.index
    const { status } = row.getData()
    switch (status) {
      case RowStatus.CREATE.value:
        row.delete()
        break

      case RowStatus.INSERT.value:
        row.delete()
        break

      case RowStatus.UPDATE.value:
      case RowStatus.DELETE.value:
        const initialRowData = getInitialRowData(table.custom.initialData, index, row.getIndex())
        row
          .update({
            ...initialRowData,
            status: RowStatus.READ.value,
            statusName: RowStatus.READ.name,
          })
          .then(() => {
            row.getCells().forEach((cell) => {
              const column = cell.getColumn()
              const definition = column.getDefinition()
              if (definition?.formatter === 'radio') {
                const { name, value } = definition.formatterParams
                const restoreValue = row.getData()[name]
                const radioEl = cell.getElement().childNodes[0]
                radioEl.checked = value === restoreValue
              }
              disableChildNodes(cell.getElement(), false)
            })
          })

        break

      default:
    }
    events.rowDeselected?.(row)
  }

  const tableBuilt = () => {
    const pageModule = instance.current.modules.page
    const pagination = instance.current.options.pagination
    if (pagination) {
      pageModule._setPageButtons = setPageButtons
      pageModule._setPageButtons()
    }
  }

  const dataLoaded = (data) => {
    const { paginationMode } = instance.current.options || {}
    if (paginationMode === 'local') {
      instance.current.custom.totalCount = data?.length || 0
    }
    if (paginationMode === 'remote') {
      instance.current.custom.totalCount = instance.current.modules.page?.remoteRowCountEstimate || 0
    }

    onChangeTotalCount?.(instance.current.custom.totalCount)

    instance.current.custom.initialData = data?.map((r) => {
      return {
        ...r,
      }
    })

    events.dataLoaded?.(data)
  }

  return {
    ...events,
    cellEdited,
    cellClick,
    cellDblClick,
    rowClick,
    rowDeselected,
    tableBuilt,
    dataLoaded,
  }
}

export default TabulatorEventFactory
