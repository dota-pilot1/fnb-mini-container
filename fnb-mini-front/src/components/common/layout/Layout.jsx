import { Outlet } from 'react-router'
import Header from './Header'
import { Toaster } from '@/components/ui/sonner'

/**
 * 메인 레이아웃
 *
 * 실무: components/common/layout/ContentsPage.jsx
 * - Header + MdiTab + section(LeftMenu + contents)
 *
 * Mini: Header + main(Outlet) 간소화
 */
export default function Layout() {
  return (
    <>
      <Header />
      <Toaster position="top-right" richColors />
      <main className="contents-wrapper">
        <Outlet />
      </main>
    </>
  )
}
