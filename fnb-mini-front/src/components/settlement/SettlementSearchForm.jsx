import { useEffect, useState } from 'react'
import { Label } from '@/components/ui/label'
import { Button } from '@/components/ui/button'
import { fetchShopList } from '@/api/settlement/settlement-fetch'

export default function SettlementSearchForm({ register, onSearch, onReset }) {
  const [shops, setShops] = useState([])

  useEffect(() => {
    fetchShopList().then(setShops).catch(() => {})
  }, [])

  const selectClass =
    'h-9 w-full rounded-md border border-input bg-transparent px-3 text-sm'
  const inputClass =
    'h-9 w-full rounded-md border border-input bg-transparent px-3 text-sm'

  return (
    <div className="border rounded-lg bg-white p-4">
      <div className="flex justify-between items-center mb-3">
        <h3 className="text-lg font-semibold">매출 정산</h3>
        <div className="flex gap-2">
          <Button variant="outline" type="button" onClick={onReset}>
            초기화
          </Button>
          <Button variant="secondary" type="button" onClick={onSearch}>
            조회
          </Button>
        </div>
      </div>

      <div className="grid grid-cols-4 gap-4 items-end">
        <div className="flex flex-col gap-1">
          <Label>매장</Label>
          <select className={selectClass} {...register('shopId')}>
            <option value="">전체</option>
            {shops.map((s) => (
              <option key={s.id} value={s.id}>
                {s.shopName} ({s.shopCode})
              </option>
            ))}
          </select>
        </div>
        <div className="flex flex-col gap-1">
          <Label>매출일자(시작)</Label>
          <input type="date" className={inputClass} {...register('salesDateFrom')} />
        </div>
        <div className="flex flex-col gap-1">
          <Label>매출일자(종료)</Label>
          <input type="date" className={inputClass} {...register('salesDateTo')} />
        </div>
        <div className="flex flex-col gap-1">
          <Label>정산상태</Label>
          <select className={selectClass} {...register('status')}>
            <option value="">전체</option>
            <option value="READY">READY</option>
            <option value="PROCESSING">PROCESSING</option>
            <option value="SETTLED">SETTLED</option>
            <option value="FAILED">FAILED</option>
          </select>
        </div>
      </div>
    </div>
  )
}
