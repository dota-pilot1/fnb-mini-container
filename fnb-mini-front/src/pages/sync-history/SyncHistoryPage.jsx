import { useState, useCallback, useRef } from 'react'
import { useForm } from 'react-hook-form'
import { toast } from 'sonner'
import SyncHistorySearchForm from '@/components/sync-history/SyncHistorySearchForm'
import SyncHistoryGrid from '@/components/sync-history/SyncHistoryGrid'
import { useTabulator } from '@/hooks/common/use-tabulator'
import { fetchSyncHistoryList } from '@/api/sync-history/sync-history-fetch'

/**
 * 동기화 이력 페이지
 *
 * 실무 패턴: SearchForm + Grid (1단 레이아웃)
 */
export default function SyncHistoryPage() {
  const gridRef = useRef(null)
  const { getData } = useTabulator({ ref: gridRef })

  const { register, reset: resetSearch, getValues } = useForm({
    defaultValues: { brandCode: '', syncStatus: '' },
  })

  const [historyList, setHistoryList] = useState([])
  const [loading, setLoading] = useState(false)

  const handleSearch = useCallback(async () => {
    setLoading(true)
    try {
      const params = getValues()
      const cleanParams = Object.fromEntries(
        Object.entries(params).filter(([, v]) => v !== '')
      )
      const res = await fetchSyncHistoryList(cleanParams)
      setHistoryList(res.data || [])
    } catch (e) {
      toast.error('조회 중 오류가 발생했습니다.')
    } finally {
      setLoading(false)
    }
  }, [getValues])

  const handleReset = () => {
    resetSearch()
    setHistoryList([])
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

      {loading && (
        <div className="fixed inset-0 bg-black/20 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg px-6 py-4 shadow-lg">
            <p className="text-sm">조회 중...</p>
          </div>
        </div>
      )}
    </div>
  )
}
