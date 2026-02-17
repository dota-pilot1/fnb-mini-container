import { useEffect, useState } from 'react'
import { fetchShopList } from '@/api/settlement/settlement-fetch'

export default function SettlementSearchForm({ register, onSearch, onReset }) {
  const [shops, setShops] = useState([])

  useEffect(() => {
    fetchShopList().then(setShops).catch(() => {})
  }, [])

  return (
    <div className="border rounded-lg bg-white p-4">
      <div className="grid-title-total">
        <div className="grid-title">
          <span className="in-title">매출 정산</span>
        </div>
      </div>
      <div className="search-form">
        <div className="search-row">
          <label>매장</label>
          <select {...register('shopId')}>
            <option value="">전체</option>
            {shops.map((s) => (
              <option key={s.id} value={s.id}>
                {s.shopName} ({s.shopCode})
              </option>
            ))}
          </select>

          <label>매출일자(시작)</label>
          <input type="date" {...register('salesDateFrom')} />

          <label>매출일자(종료)</label>
          <input type="date" {...register('salesDateTo')} />

          <label>정산상태</label>
          <select {...register('status')}>
            <option value="">전체</option>
            <option value="READY">READY</option>
            <option value="PROCESSING">PROCESSING</option>
            <option value="SETTLED">SETTLED</option>
            <option value="FAILED">FAILED</option>
          </select>
        </div>
        <div className="search-btn-area">
          <button type="button" onClick={onSearch}>조회</button>
          <button type="button" onClick={onReset}>초기화</button>
        </div>
      </div>
    </div>
  )
}
