import dayjs from 'dayjs'

export const numberFormatter = (val) => {
  if (val === null || val === undefined || val === '') return ''
  return Number(val).toLocaleString()
}

export const currencyFormatter = (val) => {
  if (val === null || val === undefined || val === '') return ''
  return Number(val).toLocaleString() + '원'
}

export const dateFormatter = (val) => {
  if (!val) return ''
  return dayjs(val).format('YYYY-MM-DD')
}

export const datetimeFormatter = (val) => {
  if (!val) return ''
  return dayjs(val).format('YYYY-MM-DD HH:mm:ss')
}
