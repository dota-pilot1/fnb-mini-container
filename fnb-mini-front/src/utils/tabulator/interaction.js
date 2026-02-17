import { RowStatus } from '@/constants/row-status-const'
import { disableChildNodes, getInitialRowData, isCreatedOrInsertedRow, isReadRow } from './util'
import { v4 as uuidv4 } from 'uuid'

/**
 * Customizing한 기능들을 사용하기 위해서 필요한 값 또는 함수들을 생성
 * instance를 참조하기 위해서 Function Factory 형태 사용
 * @param {React.Ref} instance
 * @returns
 */
const TabulatorInteractionFactory = (instance) => {
  const clickedCell = null
  const initialData = []
  const totalCount = 0

  const updateRow = (row, updatedData) => {
    const rowData = row.getData()
    switch (rowData.status) {
      case RowStatus.READ.value:
      case RowStatus.UPDATE.value:
        return new Promise((resolve) => {
          row
            .update({
              ...rowData,
              status: RowStatus.UPDATE.value,
              statusName: RowStatus.UPDATE.name,
              ...updatedData,
            })
            .then(() => {
              row.select()
            })

          resolve(row)
        })
      case RowStatus.CREATE.value:
      case RowStatus.INSERT.value:
        return new Promise((resolve) => {
          row.update({
            ...rowData,
            ...updatedData,
          })

          resolve(row)
        })

      default:
        return new Promise((resolve, reject) => {
          resolve(row)
        })
    }
  }

  const deleteRow = (row) => {
    const rowData = row.getData()
    if (isCreatedOrInsertedRow(row)) {
      row.delete()
      return
    }
    row
      .update({
        ...rowData,
        status: RowStatus.DELETE.value,
        statusName: RowStatus.DELETE.name,
      })
      .then(() => {
        row.getCells().forEach((cell) => {
          const definition = cell.getColumn().getDefinition()
          if (definition.formatter !== 'rowSelection') {
            disableChildNodes(cell.getElement(), true)
          }
        })
      })
  }

  const insertRow = ({ defaultValues, row } = {}) => {
    if (!row && !instance.current.custom.clickedCell) {
      return Promise.reject('Row를 추가할 위치가 올바르지 않습니다.')
    }

    const { index } = instance.current.options || {}
    return instance.current
      .addRow(
        { [index]: uuidv4(), ...defaultValues, status: RowStatus.INSERT.value, statusName: RowStatus.INSERT.name },
        true,
        row || instance.current.custom.clickedCell.getRow(),
      )
      .then((row) => {
        row.select()
        return row
      })
  }

  const createRow = ({ defaultValues } = {}) => {
    const { index } = instance.current.options || {}
    return instance.current
      .addRow(
        {
          [index]: uuidv4(),
          ...defaultValues,
          status: RowStatus.CREATE.value,
          statusName: RowStatus.CREATE.name,
        },
        true,
        0,
      )
      .then((row) => {
        row.select()
        return row
      })
  }

  const batchEditColumn = () => {
    if (!instance.current.custom.clickedCell) {
      return
    }
    const table = instance.current.custom.clickedCell.getTable()
    const field = instance.current.custom.clickedCell.getField()
    const data = instance.current.custom.clickedCell.getData()
    const definition = instance.current.custom.clickedCell.getColumn().getDefinition()
    const formatter = definition?.formatter
    const name = definition.formatterParams?.name
    let compareKey = field
    if (formatter === 'radio') {
      compareKey = name
    }

    table.getRows().forEach((row) => {
      const rowData = row.getData()
      if (data[compareKey] === rowData[compareKey]) {
        return
      }
      table.custom
        .updateRow(row, {
          [compareKey]: data[compareKey],
        })
        .then((rc) => {
          if (formatter === 'radio') {
            rc.getCell(field).getElement().childNodes[0].checked = true
          }
        })
    })
  }

  const getEditedData = (fields = []) => {
    const index = instance.current.options.index
    return instance.current
      .getSelectedRows()
      .filter((row) => !isReadRow(row))
      .map((row) => {
        const rowData = row.getData()
        if (isCreatedOrInsertedRow(row)) {
          return {
            ...Object.keys(rowData).reduce((changes, key) => {
              if (rowData[key]) {
                changes[key] = rowData[key]
              }
              return changes
            }, {}),
            [index]: row.getIndex(),
          }
        }

        const beforeEditRowData = getInitialRowData(instance.current.custom.initialData, index, row.getIndex())

        return {
          [index]: beforeEditRowData[index],
          ...Object.keys(rowData).reduce((changes, key) => {
            if (beforeEditRowData[key] === rowData[key]) {
              if (fields.includes(key)) {
                changes[key] = rowData[key]
              }
              return changes
            }

            changes[key] = rowData[key]
            return changes
          }, {}),
        }
      })
  }

  return {
    clickedCell,
    totalCount,
    initialData,
    updateRow,
    deleteRow,
    insertRow,
    createRow,
    batchEditColumn,
    getEditedData,
  }
}

export default TabulatorInteractionFactory
