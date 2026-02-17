import { BrowserRouter, Routes, Route, Navigate } from 'react-router'
import { AuthProvider } from '@/context/AuthContext'
import ProtectedRoute from '@/components/common/layout/ProtectedRoute'
import Layout from '@/components/common/layout/Layout'
import LoginPage from '@/pages/login/LoginPage'
import SignupPage from '@/pages/signup/SignupPage'
import BrandPage from '@/pages/brand/BrandPage'
import SyncHistoryPage from '@/pages/sync-history/SyncHistoryPage'

/**
 * 실무: router/index.js → createBrowserRouter
 *   / → Auth (로그인)
 *   /main → App (ContentsPage: Header + LeftMenu + MdiTab + Content)
 *
 * Mini: BrowserRouter + Routes
 *   /login → LoginPage
 *   /brand → BrandPage (protected)
 *   /sync-history → SyncHistoryPage (protected)
 */
export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signup" element={<SignupPage />} />
          <Route element={<ProtectedRoute />}>
            <Route element={<Layout />}>
              <Route path="/brand" element={<BrandPage />} />
              <Route path="/sync-history" element={<SyncHistoryPage />} />
            </Route>
          </Route>
          <Route path="*" element={<Navigate to="/brand" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}
