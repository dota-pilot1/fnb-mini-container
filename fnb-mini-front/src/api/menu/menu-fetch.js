import axiosInstance from '@/lib/axios'

const API_BASE = '/api/menu'

/** 메뉴 트리 조회 (Header/LeftMenu 렌더링용) */
export const fetchMenuTree = () => {
  return axiosInstance
    .get(`${API_BASE}/tree`)
    .then((response) => response.data)
    .catch((error) => {
      console.error('fetchMenuTree error:', error)
      throw error
    })
}

/** 메뉴 flat 목록 조회 (메뉴 관리 페이지용) */
export const fetchMenuList = () => {
  return axiosInstance
    .get(`${API_BASE}/list`)
    .then((response) => response.data)
    .catch((error) => {
      console.error('fetchMenuList error:', error)
      throw error
    })
}

/** 메뉴 저장 (신규/수정) */
export const fetchSaveMenu = (menuDto) => {
  return axiosInstance
    .post(`${API_BASE}/save`, menuDto)
    .then((response) => response.data)
    .catch((error) => {
      console.error('fetchSaveMenu error:', error)
      throw error
    })
}

/** 메뉴 삭제 */
export const fetchDeleteMenu = (id) => {
  return axiosInstance
    .delete(`${API_BASE}/${id}`)
    .then((response) => response.data)
    .catch((error) => {
      console.error('fetchDeleteMenu error:', error)
      throw error
    })
}
