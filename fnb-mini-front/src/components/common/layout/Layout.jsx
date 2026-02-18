import { Suspense, useEffect } from 'react'
import { useTab } from '@/context/TabContext'
import { useMenu } from '@/context/MenuContext'
import Header from './Header'
import LeftMenu from './LeftMenu'
import MdiTab from './MdiTab'
import { Toaster } from '@/components/ui/sonner'
import componentRegistry from '@/componentRegistry'

/**
 * 메인 레이아웃
 *
 * 실무: components/common/layout/ContentsPage.jsx
 * - Header + MdiTab + section(LeftMenu + contents)
 *
 * Mini: 동일한 구조
 * - 본문: activeTab의 componentName → 레지스트리에서 컴포넌트 렌더링
 * - Outlet 제거 (탭 SPA 방식, URL /main 고정)
 * - 탭 전환 시 syncLeftMenuByHeaderId로 사이드바 동기화
 */
export default function Layout() {
  const { tabs, activeTabId, registerTabChangeCallback } = useTab()
  const { syncLeftMenuByHeaderId } = useMenu()

  // 탭 전환 콜백을 TabContext에 등록
  useEffect(() => {
    registerTabChangeCallback(syncLeftMenuByHeaderId)
  }, [syncLeftMenuByHeaderId]) // eslint-disable-line
  const activeTab = tabs.find((t) => t.id === activeTabId)
  const ActiveComponent = activeTab ? componentRegistry[activeTab.componentName] : null

  return (
    <>
      <Header />
      <Toaster position="top-right" richColors />
      <MdiTab />
      <div className="main-body">
        <LeftMenu />
        <main className="contents-area">
          {ActiveComponent ? (
            <Suspense fallback={<div className="p-4 text-sm text-gray-400">로딩 중...</div>}>
              {/* key로 탭별 독립 인스턴스 유지 */}
              <ActiveComponent key={activeTab.id} />
            </Suspense>
          ) : (
            <div className="dashboard">
              <p>좌측 메뉴에서 항목을 선택하세요.</p>
            </div>
          )}
        </main>
      </div>
    </>
  )
}
