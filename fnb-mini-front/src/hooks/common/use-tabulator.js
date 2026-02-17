import dayjs from 'dayjs'
import { useState } from 'react'
import { isReadRow } from '@/utils/tabulator/util'

/**
 * 그리드 공통에 정의된 기능 제공
 * @typedef UseTabulatorOptions
 * @type {React.Ref} 참조값 (required: true)
 * @param {UseTabulatorOptions}
 * @returns {
 *  getRows:        모든 로우 목록 반환
 *  getData:        모든 로우의 데이터 목록 반환
 *  selectedRows:   선택된 로우들의 Row Component 목록 반환
 *  selectedData:   선택된 로우들 Data Object 반환
 *  selectedFilteredRows: Grid내 검색결과(Filter)중에서 선택된 로우 목록 반환
 *  selectedFilteredData: Grid내 검색결과(Filter)중에서 선택된 로우 데이터 목록 반환
 *  editedData:     선택된 로우들의 변경된 항목들만 Data Object로 반환
 *  editedRowData:  상태 변경이 일어난 로우 항목들을 Data Object로 반환
 *  deleteRows:     선택된 로우들 삭제처리
 *  resetEditedRows: 변경된 로우들 초기화
 *  insertRow:      새로운 로우를 클릭된 셀의 상단에 추가
 *  createRow:      새로운 로우를 최상단에 추가
 *  updateRow:      해당 로우를 업데이트하고 status를 변경
 *  copySelectedRows: 선택된 로우들의 데이터를 클립보드에 복사
 *  openHeight:     해당 페이지의 그리드 펼침
 *  closeHeight:    이전 높이로 닫힘
 *  batchEditColumn: 선택된 셀의 값을 기준으로 해당 열 일괄변경
 *  searchFromServer: Server Data 검색
 *  searchFromGrid:  Grid내 검색
 *  resetData: 그리드 데이터 초기화
 *  downloadExcel: 엑셀 다운로드 (그리드에 보여지는 목록)
 * }
 */
export const useTabulator = ({ ref } = {}) => {
  const [height, setHeight] = useState()

  const getRows = () => {
    return ref.current.getRows()
  }

  const getData = () => {
    return ref.current.getData()
  }

  const selectedRows = () => {
    return ref.current.getSelectedRows()
  }

  const selectedData = () => {
    return ref.current.getSelectedData()
  }

  const selectedFilteredRows = () => {
    return ref.current.rowManager
      .getDisplayRows()
      .map((row) => row.getComponent())
      .filter((row) => row.isSelected())
  }

  const selectedFilteredData = () => {
    return ref.current.rowManager
      .getDisplayRows()
      .map((row) => row.getComponent())
      .filter((row) => row.isSelected())
      .map((row) => row.getData())
  }

  /**
   * 변경된 항목의 field들만 반환
   * {withFields: [추가로 받을 keys]} 전달시 해당 field data도 같이 반환
   * @typedef EditedDataOptions
   * @type {array} withFields 변경된 항목 이외에 추가로 필요한 필드명
   * @param {EditedDataOptions} options
   */
  const editedData = (options) => {
    const { withFields } = options || {}
    return ref.current.custom.getEditedData(withFields)
  }

  const editedRowData = () => {
    return selectedRows()
      .filter((row) => !isReadRow(row))
      .map((row) => row.getData())
  }

  const deleteRows = () => {
    ref.current.getSelectedRows().forEach((row) => {
      ref.current.custom.deleteRow(row)
    })
  }

  const resetEditedRows = () => {
    ref.current.getSelectedRows().forEach((row) => {
      row.deselect()
    })
  }

  /**
   * @typedef InsertRowOptions
   * @type {object} defaultValues 기본값
   * @type {RowComponent} row 추가할 기준 row
   * @param {InsertRowOptions}
   * @returns {Promise} 추가한 Row를 전달하는 Promise
   */
  const insertRow = (options) => {
    return ref.current.custom.insertRow(options)
  }

  /**
   * @typedef CreateRowOptions
   * @type {object} defaultValues 기본값
   * @param {CreateRowOptions} options
   * @returns {Promise} 추가한 Row를 전달하는 Promise
   */
  const createRow = (options) => {
    return ref.current.custom.createRow(options)
  }

  /**
   * @param {RowComponent} row 업데이트 할 대상 RowComponent
   * @param {object} data 업데이트 할 데이터
   */
  const updateRow = (row, data) => {
    ref.current.custom.updateRow(row, data)
  }

  const copySelectedRows = () => {
    ref.current.copyToClipboard()
  }

  const openHeight = () => {
    if (ref.current.options.height === '100%') {
      return false
    }
    setHeight(ref.current.options.height)
    ref.current.setHeight('100%')
  }

  const closeHeight = () => {
    if (ref.current.options.height !== '100%') {
      return false
    }
    ref.current.setHeight(height)
  }

  const batchEditColumn = () => {
    ref.current.custom.batchEditColumn()
  }

  /**
   * @param {object} params 검색조건
   * @param {object} config Request Config
   */
  const searchFromServer = (params, config) => {
    ref.current.options.ajaxParams = params
    return ref.current.setData(ref.current.getAjaxUrl(), {}, config)
  }

  /**
   * @param {object} params 검색조건
   * @param {object} config Request Config
   * @param {function} func 데이터 세팅 이후 실행 될 함수
   */
  const searchFromServerThen = (params, config, func) => {
    ref.current.options.ajaxParams = params
    ref.current.setData(ref.current.getAjaxUrl(), {}, config).then(() => func())
  }

  /**
   * @param {string} keyword 검색어
   * @param {array} fields 검색 대상 목록
   * @param {string} comparator 비교연산자 [=, !=, like, starts, ends, <, <=, >, >=, regex]
   */
  const searchFromGrid = (keyword, fields = [], comparator = '=') => {
    ref.current.setFilter([
      fields.map((field) => {
        return {
          field,
          type: comparator,
          value: keyword,
        }
      }),
    ])
  }

  const resetData = () => {
    ref.current.clearData()

    const { paginationMode } = ref.current.options
    if (paginationMode === 'remote') {
      ref.current.modules.page.max = 1
      ref.current.modules.page.remoteRowCountEstimate = 0
      ref.current.modules.page._setPageButtons()
      ref.current.modules.page._setPageCounter()
    }
  }

  /**
   * @param {string} filename 파일명
   * @param {object} options Download Options
   */
  const downloadExcel = (filename, options) => {
    const documentProcessing = (workbook) => {
      workbook.SheetNames.forEach((sheetName) => {
        const sheet = workbook.Sheets[sheetName]
        Object.keys(sheet).forEach((key) => {
          if (sheet[key].v === 'null') {
            sheet[key].v = ''
          }
          if (typeof sheet[key].v === 'string') {
            sheet[key].v = sheet[key].v
              .replace(/<[^>]+>/g, '')
              .replace('&lt;', '<')
              .replace('&gt;', '>')
              .replace('&amp;', '&')
              .replace('&apos;', "'")
              .replace('&quot;', '"')
          }
        })
      })

      const { documentProcessing: documentProcessingOption } = options || {}

      return documentProcessingOption ? documentProcessingOption(workbook) : workbook
    }

    ref.current.download(
      'xlsx',
      `${filename}_${dayjs().format('YYYYMMDD')}.xlsx`,
      { ...options, documentProcessing },
    )
  }

  return {
    getRows,
    getData,
    selectedRows,
    selectedData,
    selectedFilteredRows,
    selectedFilteredData,
    editedData,
    editedRowData,
    deleteRows,
    resetEditedRows,
    insertRow,
    createRow,
    updateRow,
    copySelectedRows,
    openHeight,
    closeHeight,
    batchEditColumn,
    searchFromServer,
    searchFromServerThen,
    searchFromGrid,
    resetData,
    downloadExcel,
  }
}
