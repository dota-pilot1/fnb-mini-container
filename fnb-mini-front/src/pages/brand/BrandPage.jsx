import { useState, useRef } from 'react'
import { useForm } from 'react-hook-form'
import { toast } from 'sonner'
import BrandSearchForm from '@/components/brand/BrandSearchForm'
import BrandGrid from '@/components/brand/BrandGrid'
import BrandDetail from '@/components/brand/BrandDetail'
import { useTabulator } from '@/hooks/common/use-tabulator'
import { useBrandList, useSaveBrand, useRetrySync } from '@/hooks/brand/use-brand-queries'

/**
 * 브랜드 관리 페이지
 *
 * 실무: pages/cps/base/brandmng/BrandRtrv.jsx
 * - useState로 platformList, airstarList 관리
 * - useRef로 gridRef
 * - handler 함수들을 자식 컴포넌트에 콜백으로 전달
 *
 * Mini: 동일 패턴 + 좌우 2단 레이아웃 (실무: component-wrap-2)
 */
export default function BrandPage() {
  const gridRef = useRef(null)
  const { getData, selectedData, createRow, deleteRows, editedData } = useTabulator({ ref: gridRef })

  const { register, reset: resetSearch, getValues } = useForm({
    defaultValues: { brandCode: '', brandName: '', useYn: '', syncStatus: '' },
  })

  const [searchParams, setSearchParams] = useState({})
  const [selectedBrand, setSelectedBrand] = useState(null)
  const [isNew, setIsNew] = useState(false)

  const { data: brandList = [], isFetching, refetch } = useBrandList(searchParams)
  const saveBrand = useSaveBrand()
  const retrySync = useRetrySync()

  // 조회
  const handleSearch = () => {
    const params = getValues()
    const cleanParams = Object.fromEntries(
      Object.entries(params).filter(([, v]) => v !== '')
    )
    setSearchParams(cleanParams)
    // params가 같아도 refetch 가능하도록 직접 호출
    setTimeout(() => refetch(), 0)
    setSelectedBrand(null)
    setIsNew(false)
  }

  // 초기화
  const handleReset = () => {
    resetSearch()
    setSearchParams({})
    setSelectedBrand(null)
    setIsNew(false)
  }

  // 행 클릭 → 상세
  const handleRowClick = (brand) => {
    setSelectedBrand(brand)
    setIsNew(false)
  }

  // 신규
  const handleNew = () => {
    setSelectedBrand(null)
    setIsNew(true)
  }

  // 저장 (상세폼에서)
  const handleSave = async (data) => {
    try {
      const res = await saveBrand.mutateAsync(data)
      toast.success(res.message || '저장되었습니다.')
      refetch()
    } catch (e) {
      const msg = e.response?.data?.message || '저장 중 오류가 발생했습니다.'
      toast.error(msg)
    }
  }

  // 그리드 저장 (배치) - 향후 그리드 인라인 편집 시 사용
  const handleBatchSave = () => {
    toast.info('상세폼에서 개별 저장해 주세요.')
  }

  // 재시도
  const handleRetry = async (id) => {
    try {
      const res = await retrySync.mutateAsync(id)
      toast.success(res.message || '재시도가 요청되었습니다.')
      refetch()
    } catch (e) {
      const msg = e.response?.data?.message || '재시도 중 오류가 발생했습니다.'
      toast.error(msg)
    }
  }

  const loading = isFetching || saveBrand.isPending || retrySync.isPending

  return (
    <div className="p-4 space-y-4 max-w-[1600px] mx-auto">
      {/* 검색폼 */}
      <BrandSearchForm
        register={register}
        onSearch={handleSearch}
        onReset={handleReset}
      />

      {/* 좌우 2단 - 실무: .component-wrap-2 */}
      <div className="grid grid-cols-[1fr_1fr] gap-4">
        {/* 좌: 그리드 - 실무: .component.component-sm */}
        <BrandGrid
          ref={gridRef}
          data={brandList}
          onRowClick={handleRowClick}
          onNew={handleNew}
          onSave={handleBatchSave}
        />

        {/* 우: 상세 - 실무: .component.component-lg */}
        <BrandDetail
          brand={selectedBrand}
          onSave={handleSave}
          onRetry={handleRetry}
          isNew={isNew}
        />
      </div>

      {/* 로딩 오버레이 */}
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
