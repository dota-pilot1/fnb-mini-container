import axiosInstance from '@/lib/axios'

/**
 * 인증 API
 *
 * POST /api/auth/login  → JWT 토큰 반환
 * POST /api/auth/signup → 회원가입
 */

export const fetchLogin = (data) =>
  axiosInstance.post('/api/auth/login', data).then((res) => res.data.data)

export const fetchSignup = (data) =>
  axiosInstance.post('/api/auth/signup', data).then((res) => res.data.data)
