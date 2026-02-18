import { useState, useRef } from 'react'
import { useForm } from 'react-hook-form'
import SyncHistorySearchForm from '@/components/sync-history/SyncHistorySearchForm'
import SyncHistoryGrid from '@/components/sync-history/SyncHistoryGrid'
import { useTabulator } from '@/hooks/common/use-tabulator'
import { useSyncHistoryList } from '@/hooks/sync-history/use-sync-history-queries'

/**
 * 동기화 이력 페이지
 *
 * 실무 패턴: SearchForm + Grid (1단 레이아웃)
 */
export default function SyncHistoryPage() {
  const gridRef = useRef(null)
  useTabulator({ ref: gridRef })

  const { register, reset: resetSearch, getValues } = useForm({
    defaultValues: { brandCode: '', syncStatus: '' },
  })

  const [searchParams, setSearchParams] = useState({})

  const { data: historyList = [], isFetching, refetch } = useSyncHistoryList(searchParams)

  const handleSearch = () => {
    const params = getValues()
    const cleanParams = Object.fromEntries(
      Object.entries(params).filter(([, v]) => v !== '')
    )
    setSearchParams(cleanParams)
    setTimeout(() => refetch(), 0)
  }

  const handleReset = () => {
    resetSearch()
    setSearchParams({})
  }

  return (
    <div className="p-4 space-y-4 max-w-[1600px] mx-auto">
      <SyncHistorySearchForm
        register={register}
        onSearch={handleSearch}
        onReset={handleReset}
      />

      <SyncHistoryGrid
        ref={gridRef}
        data={historyList}
      />

      {isFetching && (
        <div className="fixed inset-0 bg-black/20 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg px-6 py-4 shadow-lg">
            <p className="text-sm">조회 중...</p>
          </div>
        </div>
      )}
    </div>
  )
}
