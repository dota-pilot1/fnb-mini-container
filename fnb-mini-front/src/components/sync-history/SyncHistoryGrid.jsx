import { forwardRef, useState } from 'react'
import SimpleTabulator from '@/components/common/tabulator/SimpleTabulator'
import { rowColorChangeUtil } from '@/utils/tabulator-util'

/**
 * 동기화 이력 그리드
 *
 * 실무: SimpleTabulator + forwardRef 패턴
 */
export default forwardRef(function SyncHistoryGrid({ data, onRowClick }, gridRef) {
  const [totalCount, setTotalCount] = useState(0)

  const columns = [
    {
      title: 'No.',
      field: 'rowNum',
      formatter: 'rownum',
      width: 50,
      hozAlign: 'center',
    },
    {
      title: '브랜드코드',
      field: 'brandCode',
      sorter: 'string',
      hozAlign: 'center',
      width: 120,
    },
    {
      title: '브랜드명',
      field: 'brandName',
      sorter: 'string',
      width: 150,
    },
    {
      title: '동기화상태',
      field: 'syncStatus',
      sorter: 'string',
      hozAlign: 'center',
      width: 100,
    },
    {
      title: '재시도횟수',
      field: 'syncRetryCount',
      sorter: 'number',
      hozAlign: 'center',
      width: 80,
      formatter(cell) {
        return `${cell.getValue() || 0}회`
      },
    },
    {
      title: '마지막 오류',
      field: 'lastSyncError',
      sorter: 'string',
      hozAlign: 'left',
      formatter(cell) {
        const val = cell.getValue()
        if (!val) return '-'
        const span = document.createElement('span')
        span.style.color = '#ef4444'
        span.style.fontSize = '12px'
        span.innerText = val.length > 50 ? val.substring(0, 50) + '...' : val
        span.title = val
        return span
      },
    },
    {
      title: '등록일시',
      field: 'regDttm',
      sorter: 'string',
      hozAlign: 'center',
      width: 160,
    },
  ]

  return (
    <div className="border rounded-lg bg-white p-4">
      <div className="grid-title-total">
        <div className="grid-title">
          <span className="in-title">동기화 이력 목록</span>
          <span className="total-count">
            총 <strong>{totalCount}</strong>건
          </span>
        </div>
      </div>

      <SimpleTabulator
        id="syncHistoryGridRef"
        ref={gridRef}
        columns={columns}
        data={data}
        index="id"
        clipboard={true}
        columnHeaderVertAlign="bottom"
        placeholder="조회된 데이터가 없습니다."
        layout="fitColumns"
        height="500px"
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
