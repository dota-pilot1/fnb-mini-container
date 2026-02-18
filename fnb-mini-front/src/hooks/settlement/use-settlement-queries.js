import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import {
  fetchDailySales,
  fetchSettlementList,
  fetchSettle,
  fetchRetrySettlement,
} from '@/api/settlement/settlement-fetch'

export const settlementKeys = {
  all: ['settlements'],
  dailySales: (params) => ['settlements', 'daily-sales', params],
  list: (params) => ['settlements', 'list', params],
}

/**
 * 일매출 목록 조회
 * enabled: false → 검색 버튼 클릭 시 refetch() 호출로 실행
 */
export function useDailySales(params) {
  return useQuery({
    queryKey: settlementKeys.dailySales(params),
    queryFn: () => fetchDailySales(params),
    enabled: false,
    select: (data) => data ?? [],
  })
}

/**
 * 정산 목록 조회
 * enabled: false → 검색 버튼 클릭 시 refetch() 호출로 실행
 */
export function useSettlementList(params) {
  return useQuery({
    queryKey: settlementKeys.list(params),
    queryFn: () => fetchSettlementList(params),
    enabled: false,
    select: (data) => data ?? [],
  })
}

/** 정산 실행 */
export function useSettle() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (data) => fetchSettle(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: settlementKeys.all })
    },
  })
}

/** 정산 재시도 */
export function useRetrySettlement() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (id) => fetchRetrySettlement(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: settlementKeys.all })
    },
  })
}
