import { createContext, useContext, useState, useCallback } from 'react'

const AuthContext = createContext(null)

/**
 * 인증 Context Provider
 *
 * 실무: Recoil + sessionStorage (user-info-storage-util.js)
 * Mini: React Context + localStorage
 */
export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem('accessToken'))
  const [user, setUser] = useState(() => {
    const stored = localStorage.getItem('userInfo')
    return stored ? JSON.parse(stored) : null
  })

  const isAuthenticated = !!token

  const login = useCallback((accessToken, userInfo) => {
    localStorage.setItem('accessToken', accessToken)
    localStorage.setItem('userInfo', JSON.stringify(userInfo))
    setToken(accessToken)
    setUser(userInfo)
  }, [])

  const logout = useCallback(() => {
    localStorage.removeItem('accessToken')
    localStorage.removeItem('userInfo')
    setToken(null)
    setUser(null)
  }, [])

  return (
    <AuthContext.Provider value={{ token, user, isAuthenticated, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}
