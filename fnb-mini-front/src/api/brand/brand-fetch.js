import axiosInstance from '@/lib/axios'

/**
 * 브랜드 API 호출
 *
 * 실무: src/api/cps/base/brandmng/BrandRtrv-fetch.js
 * - axiosInstance.post() 로 전부 POST 호출
 * - response.data.data 추출
 *
 * Mini: REST 규칙 준수 (GET/POST), response.data 직접 반환
 */

const API_BASE = '/api/brands'

/** 목록 조회 */
export const fetchBrandList = (params) => {
  return axiosInstance
    .get(API_BASE, { params })
    .then((response) => response.data)
    .catch((error) => {
      console.error('fetchBrandList error:', error)
      throw error
    })
}

/** 단건 조회 */
export const fetchBrandDetail = (id) => {
  return axiosInstance
    .get(`${API_BASE}/${id}`)
    .then((response) => response.data)
    .catch((error) => {
      console.error('fetchBrandDetail error:', error)
      throw error
    })
}

/** 저장 (C/U/D 배치) */
export const fetchSaveBrands = (brands) => {
  return axiosInstance
    .post(API_BASE, brands)
    .then((response) => response.data)
    .catch((error) => {
      console.error('fetchSaveBrands error:', error)
      throw error
    })
}

/** 수동 재시도 */
export const fetchRetrySync = (id) => {
  return axiosInstance
    .post(`${API_BASE}/${id}/retry`)
    .then((response) => response.data)
    .catch((error) => {
      console.error('fetchRetrySync error:', error)
      throw error
    })
}

/** 실패 목록 조회 */
export const fetchFailedSyncBrands = () => {
  return axiosInstance
    .get(`${API_BASE}/sync-failed`)
    .then((response) => response.data)
    .catch((error) => {
      console.error('fetchFailedSyncBrands error:', error)
      throw error
    })
}
