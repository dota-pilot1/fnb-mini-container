import { createContext, useContext, useState } from 'react'

/**
 * 탭 Context
 *
 * 실무: Recoil tabStore, currentTabIndexStore
 * Mini: React Context로 동일한 역할
 *
 * tabs         : 열린 탭 배열 [{ id, menuId, menuName, componentName, headerMenuId }]
 * activeTabId  : 현재 활성 탭 id
 * onTabChange  : 탭 전환 시 사이드바 동기화 콜백 (MenuContext에서 주입)
 */
const TabContext = createContext(null)

const MAX_TABS = 12

export function TabProvider({ children }) {
  const [tabs, setTabs] = useState([])
  const [activeTabId, setActiveTabId] = useState(null)
  const [onTabChangeCb, setOnTabChangeCb] = useState(null)

  // MenuContext에서 사이드바 동기화 콜백 등록
  const registerTabChangeCallback = (cb) => {
    setOnTabChangeCb(() => cb)
  }

  // depth 5 메뉴 클릭 → 탭 추가 or 기존 탭 활성화
  // headerMenuId: 현재 활성 헤더(depth 3)의 id (LeftMenu에서 전달)
  const addTab = (menu, headerMenuId) => {
    const exists = tabs.find((t) => t.menuId === menu.id)
    if (exists) {
      setActiveTabId(exists.id)
      onTabChangeCb?.(exists.headerMenuId)
      return
    }
    if (tabs.length >= MAX_TABS) {
      alert(`탭은 최대 ${MAX_TABS}개까지 열 수 있습니다.`)
      return
    }
    const newTab = {
      id:            crypto.randomUUID(),
      menuId:        menu.id,
      menuName:      menu.menuName,
      componentName: menu.componentName,
      headerMenuId,  // 어느 헤더 메뉴 소속인지 저장
    }
    setTabs((prev) => [...prev, newTab])
    setActiveTabId(newTab.id)
  }

  // 탭 닫기
  const closeTab = (tabId) => {
    setTabs((prev) => {
      const next = prev.filter((t) => t.id !== tabId)
      if (activeTabId === tabId) {
        const lastTab = next.length > 0 ? next[next.length - 1] : null
        setActiveTabId(lastTab?.id ?? null)
        onTabChangeCb?.(lastTab?.headerMenuId ?? null)
      }
      return next
    })
  }

  // 탭 전환 → 사이드바 동기화
  const changeTab = (tabId) => {
    setActiveTabId(tabId)
    const tab = tabs.find((t) => t.id === tabId)
    onTabChangeCb?.(tab?.headerMenuId ?? null)
  }

  // 전체 닫기
  const closeAllTabs = () => {
    setTabs([])
    setActiveTabId(null)
  }

  return (
    <TabContext.Provider value={{ tabs, activeTabId, addTab, closeTab, changeTab, closeAllTabs, registerTabChangeCallback }}>
      {children}
    </TabContext.Provider>
  )
}

export const useTab = () => {
  const context = useContext(TabContext)
  if (!context) throw new Error('useTab must be used within a TabProvider')
  return context
}
