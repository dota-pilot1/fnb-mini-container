import { useState, useCallback, useRef } from 'react'
import { useForm } from 'react-hook-form'
import { toast } from 'sonner'
import SettlementSearchForm from '@/components/settlement/SettlementSearchForm'
import DailySalesGrid from '@/components/settlement/DailySalesGrid'
import SettlementGrid from '@/components/settlement/SettlementGrid'
import {
  fetchDailySales,
  fetchSettlementList,
  fetchSettle,
  fetchRetrySettlement,
} from '@/api/settlement/settlement-fetch'

export default function SettlementPage() {
  const dailySalesRef = useRef(null)
  const settlementRef = useRef(null)

  const { register, reset: resetSearch, getValues } = useForm({
    defaultValues: { shopId: '', salesDateFrom: '', salesDateTo: '', status: '' },
  })

  const [dailySalesList, setDailySalesList] = useState([])
  const [settlementList, setSettlementList] = useState([])
  const [selectedDailySales, setSelectedDailySales] = useState(null)
  const [selectedSettlement, setSelectedSettlement] = useState(null)
  const [loading, setLoading] = useState(false)

  // 조회
  const handleSearch = useCallback(async () => {
    setLoading(true)
    try {
      const params = getValues()
      const cleanParams = Object.fromEntries(
        Object.entries(params).filter(([, v]) => v !== '')
      )
      const [sales, settlements] = await Promise.all([
        fetchDailySales(cleanParams),
        fetchSettlementList(cleanParams),
      ])
      setDailySalesList(sales || [])
      setSettlementList(settlements || [])
      setSelectedDailySales(null)
      setSelectedSettlement(null)
    } catch (e) {
      toast.error('조회 중 오류가 발생했습니다.')
    } finally {
      setLoading(false)
    }
  }, [getValues])

  // 초기화
  const handleReset = () => {
    resetSearch()
    setDailySalesList([])
    setSettlementList([])
    setSelectedDailySales(null)
    setSelectedSettlement(null)
  }

  // 정산 실행
  const handleSettle = async () => {
    if (!selectedDailySales) {
      toast.warning('정산할 일매출 데이터를 선택해 주세요.')
      return
    }

    // ★ 멱등성 키: 프론트에서 UUID 생성
    const idempotencyKey = crypto.randomUUID()

    setLoading(true)
    try {
      const res = await fetchSettle({
        shopId: selectedDailySales.shopId,
        salesDate: selectedDailySales.salesDate,
        idempotencyKey,
      })
      toast.success(res.message || '정산이 완료되었습니다.')
      handleSearch()
    } catch (e) {
      const msg = e.response?.data?.message || '정산 중 오류가 발생했습니다.'
      toast.error(msg)
    } finally {
      setLoading(false)
    }
  }

  // 재시도
  const handleRetry = async () => {
    if (!selectedSettlement) {
      toast.warning('재시도할 정산 내역을 선택해 주세요.')
      return
    }
    if (selectedSettlement.sapSyncStatus !== 'FAILED') {
      toast.warning('FAILED 상태의 정산만 재시도할 수 있습니다.')
      return
    }

    setLoading(true)
    try {
      const res = await fetchRetrySettlement(selectedSettlement.id)
      toast.success(res.message || '재시도가 완료되었습니다.')
      handleSearch()
    } catch (e) {
      const msg = e.response?.data?.message || '재시도 중 오류가 발생했습니다.'
      toast.error(msg)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="p-4 space-y-4 max-w-[1600px] mx-auto">
      <SettlementSearchForm
        register={register}
        onSearch={handleSearch}
        onReset={handleReset}
      />

      <div className="grid grid-cols-[1fr_1fr] gap-4">
        <DailySalesGrid
          ref={dailySalesRef}
          data={dailySalesList}
          onRowClick={setSelectedDailySales}
        />
        <SettlementGrid
          ref={settlementRef}
          data={settlementList}
          onRowClick={setSelectedSettlement}
          onSettle={handleSettle}
          onRetry={handleRetry}
        />
      </div>

      {loading && (
        <div className="fixed inset-0 bg-black/20 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg px-6 py-4 shadow-lg">
            <p className="text-sm">처리 중...</p>
          </div>
        </div>
      )}
    </div>
  )
}
