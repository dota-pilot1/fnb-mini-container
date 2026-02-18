import { useQuery } from '@tanstack/react-query'
import { fetchSyncHistoryList } from '@/api/sync-history/sync-history-fetch'

export const syncHistoryKeys = {
  all: ['sync-history'],
  list: (params) => ['sync-history', 'list', params],
}

/**
 * 동기화 이력 목록 조회
 * enabled: false → 검색 버튼 클릭 시 refetch() 호출로 실행
 */
export function useSyncHistoryList(params) {
  return useQuery({
    queryKey: syncHistoryKeys.list(params),
    queryFn: () => fetchSyncHistoryList(params),
    enabled: false,
    select: (res) => res.data ?? [],
  })
}
