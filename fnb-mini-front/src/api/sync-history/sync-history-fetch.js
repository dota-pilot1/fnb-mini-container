import axiosInstance from '@/lib/axios'

/**
 * 동기화 이력 API
 *
 * 백엔드 구현 전까지 기존 brands API에서 동기화 관련 데이터 조회
 * 백엔드 구현 시 별도 엔드포인트로 교체
 */

const API_BASE = '/api/brands'

/** 동기화 이력 목록 조회 (brands 데이터에서 추출) */
export const fetchSyncHistoryList = (params) => {
  return axiosInstance
    .get(API_BASE, { params })
    .then((response) => response.data)
    .catch((error) => {
      console.error('fetchSyncHistoryList error:', error)
      throw error
    })
}
