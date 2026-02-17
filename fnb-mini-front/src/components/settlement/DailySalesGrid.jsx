import { forwardRef, useState } from 'react'
import SimpleTabulator from '@/components/common/tabulator/SimpleTabulator'
import { rowColorChangeUtil } from '@/utils/tabulator-util'

export default forwardRef(function DailySalesGrid({ data, onRowClick }, ref) {
  const [totalCount, setTotalCount] = useState(0)

  const columns = [
    { title: 'No.', field: 'rowNum', formatter: 'rownum', width: 50, hozAlign: 'center' },
    { title: '매장', field: 'shopName', sorter: 'string', width: 100 },
    { title: '매출일자', field: 'salesDate', sorter: 'string', hozAlign: 'center', width: 110 },
    { title: '현금', field: 'cashAmt', sorter: 'number', hozAlign: 'right',
      formatter: 'money', formatterParams: { thousand: ',', precision: 0 } },
    { title: '카드', field: 'cardAmt', sorter: 'number', hozAlign: 'right',
      formatter: 'money', formatterParams: { thousand: ',', precision: 0 } },
    { title: '간편결제', field: 'easyAmt', sorter: 'number', hozAlign: 'right',
      formatter: 'money', formatterParams: { thousand: ',', precision: 0 } },
    { title: '합계', field: 'totalAmt', sorter: 'number', hozAlign: 'right',
      formatter: 'money', formatterParams: { thousand: ',', precision: 0 } },
  ]

  return (
    <div className="border rounded-lg bg-white p-4">
      <div className="grid-title-total">
        <div className="grid-title">
          <span className="in-title">일매출 데이터</span>
          <span className="total-count">
            총 <strong>{totalCount}</strong>건
          </span>
        </div>
      </div>
      <SimpleTabulator
        ref={ref}
        columns={columns}
        data={data}
        index="id"
        placeholder="조회된 데이터가 없습니다."
        layout="fitColumns"
        height="350px"
        events={{
          rowClick(e, row) {
            rowColorChangeUtil(row)
            onRowClick?.(row.getData())
          },
          dataLoaded(data) {
            setTotalCount(data?.length || 0)
          },
        }}
      />
    </div>
  )
})
