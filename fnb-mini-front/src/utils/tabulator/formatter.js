import { RowStatus } from '@/constants/row-status-const'
import { currencyFormatter, dateFormatter, datetimeFormatter, numberFormatter } from '@/utils/formatter-util'
import { disableChildNodes, isCreatedOrInsertedRow } from './util'

export const rownum = (cell, formatterParams, onRendered) => {
  const span = document.createElement('span')
  const table = cell.getColumn().getTable()
  const row = cell.getRow()

  if (isCreatedOrInsertedRow(row)) {
    return span
  }

  const { paginationMode } = table.options
  let num = 0
  if (paginationMode === 'local') {
    num = row.getPosition()
  }
  if (paginationMode === 'remote') {
    num = row.getPosition() + (table.getPage() - 1) * table.getPageSize()
  }

  span.innerText = num

  return span
}

export const title = (cell, formatterParams, onRendered) => {
  const span = document.createElement('span')
  span.className = 'link-underline'
  const cellValue = cell.getValue()
  if (cellValue) {
    span.innerText = cell.getValue()
  }
  return span
}

export const number = (cell, formatterParams, onRendered) => {
  return numberFormatter(cell.getValue())
}

export const currency = (cell, formatterParams, onRendered) => {
  return currencyFormatter(cell.getValue())
}

export const date = (cell, formatterParams, onRendered) => {
  return dateFormatter(cell.getValue())
}

export const dateTime = (cell, formatterParams, onRendered) => {
  return datetimeFormatter(cell.getValue())
}

export const checkbox = (cell, formatterParams, onRendered) => {
  const { check, uncheck, onChange } = formatterParams
  const checkbox = document.createElement('input')
  checkbox.type = 'checkbox'
  checkbox.setAttribute('aria-label', 'Select Checkbox')

  const cellValue = cell.getValue()
  if (cellValue === check) {
    checkbox.checked = true
  }
  checkbox.addEventListener('change', (e) => {
    cell.setValue(e.target.checked ? check : uncheck)
    onChange?.(cell)
  })

  return checkbox
}

export const button = (cell, formatterParams, onRendered) => {
  const { label, handleClick } = formatterParams
  const button = document.createElement('button')
  button.type = 'button'
  button.innerText = label
  button.addEventListener('click', (e) => handleClick(e, cell))

  return button
}

export const calcTotalText = (cell, formatterParams, onRendered) => {
  const span = document.createElement('span')
  span.innerText = '합계'
  return span
}

export const required = (cell, formatterParams, onRendered) => {
  const span = document.createElement('span')
  span.className = 'tabulator-col-title required'
  span.innerText = cell.getValue()
  return span
}

export const radio = (cell, formatterParams, onRendered) => {
  const row = cell.getRow()
  const radio = document.createElement('input')
  radio.type = 'radio'
  radio.setAttribute('aria-label', 'Select Radio')

  const { name, value } = formatterParams
  radio.name = `${row.getIndex()}-${name}`
  radio.value = value
  radio.checked = value === row.getData()[name]

  radio.addEventListener('change', (e) => {
    row._getSelf().setData({
      ...row.getData(),
      [name]: e.target.value,
    })
    cell._getSelf().dispatchExternal('cellEdited', cell)
  })

  return radio
}

export const rowFormatter = (row) => {
  const { status } = row.getData() || {}
  const element = row.getElement()
  element.classList.remove(RowStatus.CREATE.className)
  element.classList.remove(RowStatus.INSERT.className)
  element.classList.remove(RowStatus.UPDATE.className)
  element.classList.remove(RowStatus.DELETE.className)

  switch (status) {
    case RowStatus.CREATE.value:
      element.classList.add(RowStatus.CREATE.className)
      break
    case RowStatus.INSERT.value:
      element.classList.add(RowStatus.INSERT.className)
      break
    case RowStatus.UPDATE.value:
      element.classList.add(RowStatus.UPDATE.className)
      break
    case RowStatus.DELETE.value:
      element.classList.add(RowStatus.DELETE.className)
      break
    default:
  }

  if (element.classList.contains('tabulator-unselectable')) {
    disableChildNodes(element, true)
  }
}

export const fileInput = (cell, formatterParams, onRendered) => {
  const { label, handleClick, handleChange } = formatterParams

  const file = document.createElement('input')
  file.type = 'file'
  file.innerText = label
  file.style.display = 'none'
  file.addEventListener('change', (e) => handleChange(e, cell))

  const button = document.createElement('button')
  button.type = 'button'
  button.innerText = label
  button.addEventListener('click', (e) => handleClick(e, cell))

  const div = document.createElement('div')
  div.appendChild(file)
  div.appendChild(button)

  return div
}

export const inputbox = (cell, formatterParams, onRendered) => {
  const { handleChange, editable } = formatterParams

  let value = cell.getValue()

  if (!editable && (cell.getRow().getData().status !== 'N' && cell.getRow().getData().status !== 'C')) return value || ''

  const text = document.createElement('input')
  text.type = 'text'
  text.value = value
  text.style.width = '100%'

  text.addEventListener('change', (e) => handleChange(e, cell))

  const div = document.createElement('div')
  div.appendChild(text)

  return text
}
