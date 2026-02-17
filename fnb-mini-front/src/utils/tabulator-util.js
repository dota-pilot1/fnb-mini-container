export const rowColorChangeUtil = (row) => {
  row
    .getTable()
    .getRows()
    .forEach((r) => {
      r.getElement().style.backgroundColor = '#fff'
    })

  row.getElement().style.backgroundColor = '#eff8fc'
}

export const rowColorBackUtil = (table) => {
  table
    .getRows()
    .forEach((r) => {
      r.getElement().style.backgroundColor = '#fff'
    })
}

export const rowTextColorChangeUtil = (row) => {
  row
    .getCells()
    .forEach((c) => {
      c.getElement().style.color = '#ef151e'
    })
}
