import { forwardRef, useState } from 'react'
import SimpleTabulator from '@/components/common/tabulator/SimpleTabulator'
import { rowColorChangeUtil } from '@/utils/tabulator-util'

export default forwardRef(function SettlementGrid({ data, onRowClick, onSettle, onRetry }, ref) {
  const [totalCount, setTotalCount] = useState(0)

  const columns = [
    { title: 'No.', field: 'rowNum', formatter: 'rownum', width: 50, hozAlign: 'center' },
    { title: '매장', field: 'shopName', sorter: 'string', width: 100 },
    { title: '매출일자', field: 'salesDate', sorter: 'string', hozAlign: 'center', width: 110 },
    { title: '합계', field: 'totalAmt', sorter: 'number', hozAlign: 'right', width: 110,
      formatter: 'money', formatterParams: { thousand: ',', precision: 0 } },
    { title: '상태', field: 'status', sorter: 'string', hozAlign: 'center', width: 100 },
    { title: 'SAP동기화', field: 'sapSyncStatus', sorter: 'string', hozAlign: 'center', width: 100 },
    { title: '정산일시', field: 'settledAt', sorter: 'string', width: 140 },
  ]

  return (
    <div className="border rounded-lg bg-white p-4">
      <div className="grid-title-total">
        <div className="grid-title">
          <span className="in-title">정산 내역</span>
          <span className="total-count">
            총 <strong>{totalCount}</strong>건
          </span>
        </div>
        <div className="grid-btn-area">
          <button type="button" onClick={onSettle}>정산 실행</button>
          <button type="button" onClick={onRetry}>재시도</button>
        </div>
      </div>
      <SimpleTabulator
        ref={ref}
        columns={columns}
        data={data}
        index="id"
        placeholder="정산 내역이 없습니다."
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
