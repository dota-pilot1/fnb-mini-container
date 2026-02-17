import axios from 'axios'

/**
 * Axios 인스턴스
 *
 * 실무: src/api/axios/index.js
 * - Bearer 토큰 인증, 180s 타임아웃, 401 리다이렉트
 *
 * Mini: 실무와 동일한 인터셉터 패턴, Vite 프록시 사용
 */
const axiosInstance = axios.create({
  baseURL: '',  // Vite proxy: /api → localhost:18080
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json; charset=utf-8',
  },
})

// Request 인터셉터: Bearer 토큰 자동 추가
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Response 인터셉터: 401 처리 + 타임아웃
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      const message = error.response.data?.message || '로그인 인증이 만료되었습니다. 다시 로그인해주세요.'
      alert(message)
      localStorage.removeItem('accessToken')
      localStorage.removeItem('userInfo')
      window.location.href = '/login'
      return new Promise(() => {})
    }

    if (error.code === 'ECONNABORTED') {
      alert('요청 시간이 초과되었습니다.')
    }

    return Promise.reject(error)
  }
)

export default axiosInstance
