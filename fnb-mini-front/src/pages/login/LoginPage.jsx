import { useEffect } from 'react'
import { useNavigate } from 'react-router'
import { useForm } from 'react-hook-form'
import { useAuth } from '@/context/AuthContext'
import { fetchLogin } from '@/api/auth/auth-fetch'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { Label } from '@/components/ui/label'
import '@/assets/style/login.scss'

/**
 * 로그인 페이지
 *
 * 실무: pages/Auth.jsx
 * - SSO Cookie 기반 자동 로그인
 * - /user/login → accessToken + refreshToken 저장 → /main 이동
 *
 * Mini: ID/PW 폼 로그인 (mock JWT)
 */
export default function LoginPage() {
  const { isAuthenticated, login } = useAuth()
  const navigate = useNavigate()
  const { register, handleSubmit, formState: { errors }, setError } = useForm({
    defaultValues: { userId: '', password: '' },
  })

  // 이미 로그인 상태면 리다이렉트
  useEffect(() => {
    if (isAuthenticated) {
      navigate('/brand', { replace: true })
    }
  }, [isAuthenticated, navigate])

  const onSubmit = async (data) => {
    try {
      const { accessToken, userInfo } = await fetchLogin(data)
      login(accessToken, userInfo)
      navigate('/brand', { replace: true })
    } catch (e) {
      const msg = e.response?.data?.message || '로그인 중 오류가 발생했습니다.'
      setError('root', { message: msg })
    }
  }

  return (
    <div className="login-page">
      <div className="login-box">
        <div className="login-header">
          <h1>FNB Mini</h1>
          <p>관리 시스템에 로그인하세요</p>
        </div>
        <form onSubmit={handleSubmit(onSubmit)} className="login-form">
          <div className="form-field">
            <Label htmlFor="userId">아이디</Label>
            <Input
              id="userId"
              placeholder="아이디를 입력하세요"
              {...register('userId', { required: '아이디를 입력하세요.' })}
              autoFocus
            />
            {errors.userId && <span className="error-text">{errors.userId.message}</span>}
          </div>
          <div className="form-field">
            <Label htmlFor="password">비밀번호</Label>
            <Input
              id="password"
              type="password"
              placeholder="비밀번호를 입력하세요"
              {...register('password', { required: '비밀번호를 입력하세요.' })}
            />
            {errors.password && <span className="error-text">{errors.password.message}</span>}
          </div>
          {errors.root && <div className="error-text center">{errors.root.message}</div>}
          <Button type="submit" className="w-full">
            로그인
          </Button>
          <p className="hint-text">
            계정이 없으신가요?{' '}
            <a href="/signup" onClick={(e) => { e.preventDefault(); navigate('/signup') }}>
              회원가입
            </a>
          </p>
        </form>
      </div>
    </div>
  )
}
