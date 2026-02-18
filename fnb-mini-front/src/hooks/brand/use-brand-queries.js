import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { fetchBrandList, fetchSaveBrands, fetchRetrySync } from '@/api/brand/brand-fetch'

export const brandKeys = {
  all: ['brands'],
  list: (params) => ['brands', 'list', params],
}

/**
 * 브랜드 목록 조회
 * enabled: false → 검색 버튼 클릭 시 refetch() 호출로 실행
 */
export function useBrandList(params) {
  return useQuery({
    queryKey: brandKeys.list(params),
    queryFn: () => fetchBrandList(params),
    enabled: false,
    select: (res) => res.data ?? [],
  })
}

/** 브랜드 저장 */
export function useSaveBrand() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (data) => fetchSaveBrands([data]),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: brandKeys.all })
    },
  })
}

/** 동기화 재시도 */
export function useRetrySync() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (id) => fetchRetrySync(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: brandKeys.all })
    },
  })
}
