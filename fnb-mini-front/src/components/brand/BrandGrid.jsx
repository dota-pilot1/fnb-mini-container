import { forwardRef, useState } from 'react'
import SimpleTabulator from '@/components/common/tabulator/SimpleTabulator'
import { rowColorChangeUtil } from '@/utils/tabulator-util'

/**
 * 브랜드 그리드 (목록)
 *
 * 실무: components/cps/base/brandmng/BrandRtrvAirstarGrid.jsx
 * - SimpleTabulator (forwardRef, columns, data, events)
 * - .grid-title-total > 총 N건
 * - rowClick → rowColorChangeUtil
 */
export default forwardRef(function BrandGrid({ data, onRowClick, onNew, onSave }, gridRef) {
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
    },
    {
      title: '브랜드명(EN)',
      field: 'brandNameEn',
      sorter: 'string',
    },
    {
      title: '사용여부',
      field: 'useYn',
      sorter: 'string',
      hozAlign: 'center',
      width: 80,
    },
    {
      title: '동기화',
      field: 'syncStatus',
      sorter: 'string',
      hozAlign: 'center',
      width: 100,
    },
    {
      title: '재시도',
      field: 'syncRetryCount',
      sorter: 'number',
      hozAlign: 'center',
      width: 60,
      formatter(cell) {
        return `${cell.getValue() || 0}회`
      },
    },
  ]

  return (
    <div className="border rounded-lg bg-white p-4">
      {/* 그리드 타이틀 - 실무: .grid-title-total */}
      <div className="grid-title-total">
        <div className="grid-title">
          <span className="in-title">브랜드 목록</span>
          <span className="total-count">
            총 <strong>{totalCount}</strong>건
          </span>
        </div>
        <div className="grid-btn-area">
          <button type="button" onClick={onNew}>
            신규
          </button>
          <button type="button" onClick={onSave}>
            저장
          </button>
        </div>
      </div>

      {/* SimpleTabulator 그리드 */}
      <SimpleTabulator
        id="brandGridRef"
        ref={gridRef}
        columns={columns}
        data={data}
        index="id"
        clipboard={true}
        columnHeaderVertAlign="bottom"
        placeholder="조회된 데이터가 없습니다."
        layout="fitColumns"
        height="400px"
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
