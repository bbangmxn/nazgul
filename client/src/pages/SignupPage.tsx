import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuthStore } from '@/stores/authStore'

export default function SignupPage() {
  const navigate = useNavigate()
  const { signup, isLoading } = useAuthStore()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [nickname, setNickname] = useState('')
  const [error, setError] = useState('')

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')

    if (password.length < 6) {
      setError('비밀번호는 6자 이상이어야 합니다.')
      return
    }

    try {
      await signup(email, password, nickname)
      navigate('/')
    } catch (err: any) {
      setError(err.response?.data?.message || '가입에 실패했습니다.')
    }
  }

  return (
    <div className="min-h-screen flex flex-col items-center justify-center px-4">
      <div className="w-full max-w-sm space-y-8">
        {/* Logo */}
        <div className="text-center">
          <h1 className="text-4xl font-bold">Nazgul</h1>
          <p className="mt-2 text-secondary">새로운 취미 친구를 만나보세요</p>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="space-y-4">
          {error && (
            <div className="p-3 rounded-xl bg-red-100 dark:bg-red-900/20 text-red-600 dark:text-red-400 text-sm">
              {error}
            </div>
          )}

          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="이메일"
            className="input"
            required
          />

          <input
            type="text"
            value={nickname}
            onChange={(e) => setNickname(e.target.value)}
            placeholder="닉네임"
            className="input"
            required
            minLength={2}
            maxLength={20}
          />

          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="비밀번호 (6자 이상)"
            className="input"
            required
            minLength={6}
          />

          <button
            type="submit"
            disabled={isLoading}
            className="btn-primary w-full"
          >
            {isLoading ? '가입 중...' : '가입하기'}
          </button>
        </form>

        {/* Links */}
        <div className="text-center text-sm">
          <span className="text-secondary">이미 계정이 있으신가요? </span>
          <Link to="/login" className="text-accent hover:underline">
            로그인
          </Link>
        </div>
      </div>
    </div>
  )
}
