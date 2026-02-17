import dayjs from 'dayjs'

/**
 * @typedef EditorParams
 * @type {string} format
 * @type {string} max (yyyy-MM-dd)
 * @type {string} min (yyyy-MM-dd)
 * @param {CellComponent} cell
 * @param {function} onRendered
 * @param {function} success
 * @param {function} cancel
 * @param {EditorParams} editorParams
 * @returns {HTMLElement} input
 */
export const date = (cell, onRendered, success, cancel, editorParams) => {
  const { format = 'YYYY-MM-DD', max = '9999-12-31', min = '1111-01-01' } = editorParams || {}

  const cellValue = dayjs(cell.getValue()).format(format)
  const input = document.createElement('input')

  input.setAttribute('type', 'date')
  input.setAttribute('max', max)
  input.setAttribute('min', min)
  input.setAttribute('pattern', 'd{4}-d{2}-d{2}')

  input.style.padding = '4px'
  input.style.width = '100%'
  input.style.boxSizing = 'border-box'

  input.value = cellValue

  onRendered(() => {
    input.focus()
    input.style.height = '100%'
  })

  const handleSubmit = () => {
    if (input.value) {
      const f = dayjs(input.value).format(format)
      if (dayjs(f).isAfter(dayjs(max))) {
        input.value = max
        return
      }
      if (dayjs(f).isBefore(dayjs(min))) {
        input.value = min
        return
      }

      success(f)
    } else {
      cancel()
    }
  }

  const handleChange = () => {
    if (input.value) {
      if (dayjs(input.value).isAfter(dayjs(max))) {
        input.value = dayjs(max).format(format)
      }
    } else {
      cancel()
    }
  }

  const handleBlur = () => {
    handleSubmit()
  }

  input.addEventListener('change', handleChange)
  input.addEventListener('blur', handleBlur)

  input.addEventListener('keydown', (e) => {
    if (e.key === 'Enter') {
      handleSubmit()
    } else if (e.key === 'Esc' || e.key === 'Escape') {
      cancel()
    }
  })

  return input
}
