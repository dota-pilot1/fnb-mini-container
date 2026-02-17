import { Navigate, Outlet } from 'react-router'
import { useAuth } from '@/context/AuthContext'

/**
 * 인증 보호 라우트
 *
 * 실무: axios 401 응답 시 document.location.href = '/' 로 리다이렉트
 * Mini: 라우트 레벨에서 토큰 체크 + Navigate
 */
export default function ProtectedRoute() {
  const { isAuthenticated } = useAuth()

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />
  }

  return <Outlet />
}
