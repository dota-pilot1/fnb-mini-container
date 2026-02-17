import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'

/**
 * 동기화 이력 검색폼
 *
 * 실무: BrandSearchForm과 동일 패턴 (search-detail > table th/td)
 */

const thStyle = {
  padding: '8px 12px',
  backgroundColor: '#f5f5f5',
  border: '1px solid #ddd',
  textAlign: 'left',
  fontWeight: 'normal',
  fontSize: '13px',
  whiteSpace: 'nowrap',
  width: '100px',
}

const tdStyle = {
  padding: '6px 10px',
  border: '1px solid #ddd',
}

export default function SyncHistorySearchForm({ register, onSearch, onReset }) {
  return (
    <div className="border rounded-lg bg-white p-4">
      <div className="flex justify-between items-center mb-3">
        <h3 className="font-medium text-base">동기화 이력</h3>
        <div className="flex gap-2">
          <Button variant="outline" size="sm" onClick={onReset}>
            초기화
          </Button>
          <Button size="sm" onClick={onSearch}>
            조회
          </Button>
        </div>
      </div>
      <table style={{ width: '100%', borderCollapse: 'collapse' }}>
        <tbody>
          <tr>
            <th style={thStyle}>브랜드코드</th>
            <td style={tdStyle}>
              <Input
                {...register('brandCode')}
                placeholder="브랜드코드"
                className="h-8"
              />
            </td>
            <th style={thStyle}>동기화상태</th>
            <td style={tdStyle}>
              <select
                {...register('syncStatus')}
                className="h-8 w-full rounded-md border border-input bg-transparent px-3 text-sm"
              >
                <option value="">전체</option>
                <option value="NONE">NONE</option>
                <option value="PENDING">PENDING</option>
                <option value="SUCCESS">SUCCESS</option>
                <option value="FAILED">FAILED</option>
              </select>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  )
}
