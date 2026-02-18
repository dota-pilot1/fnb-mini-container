import { BrowserRouter, Routes, Route, Navigate } from 'react-router'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { AuthProvider } from '@/context/AuthContext'
import { MenuProvider } from '@/context/MenuContext'
import { TabProvider } from '@/context/TabContext'
import ProtectedRoute from '@/components/common/layout/ProtectedRoute'
import Layout from '@/components/common/layout/Layout'
import LoginPage from '@/pages/login/LoginPage'
import SignupPage from '@/pages/signup/SignupPage'

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 1,
      staleTime: 0,
    },
  },
})

/**
 * 실무: router/index.js → createBrowserRouter
 *   / → Auth (로그인)
 *   /main → App (ContentsPage: Header + LeftMenu + MdiTab + Content)
 *   실제 페이지 전환은 라우터가 아닌 MdiTab이 관리 (URL /main 고정)
 *
 * Mini: 동일한 구조로 리팩토링
 *   /login → LoginPage
 *   /main  → Layout (탭 SPA)
 *   각 페이지는 라우터가 아닌 componentRegistry + TabContext로 관리
 */
export default function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/signup" element={<SignupPage />} />
            <Route element={<ProtectedRoute />}>
              <Route
                path="/main"
                element={
                  <MenuProvider>
                    <TabProvider>
                      <Layout />
                    </TabProvider>
                  </MenuProvider>
                }
              />
            </Route>
            <Route path="*" element={<Navigate to="/main" replace />} />
          </Routes>
        </BrowserRouter>
      </AuthProvider>
    </QueryClientProvider>
  )
}
