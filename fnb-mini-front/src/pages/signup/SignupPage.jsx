import { useNavigate } from 'react-router'
import { useForm } from 'react-hook-form'
import { fetchSignup } from '@/api/auth/auth-fetch'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { Label } from '@/components/ui/label'
import '@/assets/style/login.scss'

export default function SignupPage() {
  const navigate = useNavigate()
  const { register, handleSubmit, formState: { errors }, setError } = useForm({
    defaultValues: { userId: '', password: '', username: '' },
  })

  const onSubmit = async (data) => {
    try {
      await fetchSignup(data)
      alert('회원가입이 완료되었습니다.')
      navigate('/login')
    } catch (e) {
      const msg = e.response?.data?.message || '회원가입 중 오류가 발생했습니다.'
      setError('root', { message: msg })
    }
  }

  return (
    <div className="login-page">
      <div className="login-box">
        <div className="login-header">
          <h1>FNB Mini</h1>
          <p>새 계정을 만드세요</p>
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
          <div className="form-field">
            <Label htmlFor="username">이름</Label>
            <Input
              id="username"
              placeholder="이름을 입력하세요"
              {...register('username', { required: '이름을 입력하세요.' })}
            />
            {errors.username && <span className="error-text">{errors.username.message}</span>}
          </div>
          {errors.root && <div className="error-text center">{errors.root.message}</div>}
          <Button type="submit" className="w-full">
            회원가입
          </Button>
          <p className="hint-text">
            이미 계정이 있으신가요?{' '}
            <a href="/login" onClick={(e) => { e.preventDefault(); navigate('/login') }}>
              로그인
            </a>
          </p>
        </form>
      </div>
    </div>
  )
}
