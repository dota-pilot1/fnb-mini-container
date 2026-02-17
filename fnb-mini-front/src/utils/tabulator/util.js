import { RowStatus } from '@/constants/row-status-const'

export const isReadRow = (row) => {
  const { status } = row.getData()
  return status === RowStatus.READ.value
}

export const isCreatedOrInsertedRow = (row) => {
  const { status } = row.getData()
  return status === RowStatus.CREATE.value || status === RowStatus.INSERT.value
}

export const isUpdatedRow = (row) => {
  const { status } = row.getData()
  return status === RowStatus.UPDATE.value
}

export const isDeletedRow = (row) => {
  const { status } = row.getData()
  return status === RowStatus.DELETE.value
}

export const disableChildNodes = (element, disabled) => {
  const childNodes = element.getElementsByTagName('*')
  for (const node of childNodes) {
    node.disabled = disabled
  }
}

export const getInitialRowData = (data, index, value) => {
  return data.find((rowData) => rowData[index] === value)
}

export const generateHeaderMenu = (columns) => {
  const menu = []
  const useHeaderMenucolumns = columns.filter((col) => col.getDefinition().headerMenu)

  for (let col of useHeaderMenucolumns) {
    const wrap = document.createElement('div')

    const checkbox = document.createElement('input')
    checkbox.type = 'checkbox'
    checkbox.id = col.getField()
    checkbox.defaultChecked = col.isVisible()

    const label = document.createElement('label')
    label.htmlFor = col.getField()
    label.innerText = col.getDefinition().title

    wrap.appendChild(checkbox)
    wrap.appendChild(label)

    menu.push({
      label: wrap,
      action(e) {
        e.stopPropagation()
        col.toggle()
        checkbox.checked = col.isVisible()
      },
    })
  }

  return menu
}

export const availableClipboardPaste = (clipboard = false) => {
  if (typeof clipboard === 'function') {
    return clipboard()
  }
  if (typeof clipboard === 'string') {
    return clipboard === 'paste'
  }
  return clipboard
}
