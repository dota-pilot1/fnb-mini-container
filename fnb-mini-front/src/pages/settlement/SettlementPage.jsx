import { useState, useRef } from 'react'
import { useForm } from 'react-hook-form'
import { toast } from 'sonner'
import SettlementSearchForm from '@/components/settlement/SettlementSearchForm'
import DailySalesGrid from '@/components/settlement/DailySalesGrid'
import SettlementGrid from '@/components/settlement/SettlementGrid'
import {
  useDailySales,
  useSettlementList,
  useSettle,
  useRetrySettlement,
} from '@/hooks/settlement/use-settlement-queries'

export default function SettlementPage() {
  const dailySalesRef = useRef(null)
  const settlementRef = useRef(null)

  const { register, reset: resetSearch, getValues } = useForm({
    defaultValues: { shopId: '', salesDateFrom: '', salesDateTo: '', status: '' },
  })

  const [searchParams, setSearchParams] = useState({})
  const [selectedDailySales, setSelectedDailySales] = useState(null)
  const [selectedSettlement, setSelectedSettlement] = useState(null)

  const { data: dailySalesList = [], isFetching: isFetchingDailySales, refetch: refetchDailySales } = useDailySales(searchParams)
  const { data: settlementList = [], isFetching: isFetchingSettlements, refetch: refetchSettlements } = useSettlementList(searchParams)
  const settle = useSettle()
  const retrySettlement = useRetrySettlement()

  const refetchAll = () => {
    refetchDailySales()
    refetchSettlements()
  }

  // 조회
  const handleSearch = () => {
    const params = getValues()
    const cleanParams = Object.fromEntries(
      Object.entries(params).filter(([, v]) => v !== '')
    )
    setSearchParams(cleanParams)
    setTimeout(() => refetchAll(), 0)
    setSelectedDailySales(null)
    setSelectedSettlement(null)
  }

  // 초기화
  const handleReset = () => {
    resetSearch()
    setSearchParams({})
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

    try {
      const res = await settle.mutateAsync({
        shopId: selectedDailySales.shopId,
        salesDate: selectedDailySales.salesDate,
        idempotencyKey,
      })
      toast.success(res.message || '정산이 완료되었습니다.')
      refetchAll()
    } catch (e) {
      const msg = e.response?.data?.message || '정산 중 오류가 발생했습니다.'
      toast.error(msg)
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

    try {
      const res = await retrySettlement.mutateAsync(selectedSettlement.id)
      toast.success(res.message || '재시도가 완료되었습니다.')
      refetchAll()
    } catch (e) {
      const msg = e.response?.data?.message || '재시도 중 오류가 발생했습니다.'
      toast.error(msg)
    }
  }

  const loading = isFetchingDailySales || isFetchingSettlements || settle.isPending || retrySettlement.isPending

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
