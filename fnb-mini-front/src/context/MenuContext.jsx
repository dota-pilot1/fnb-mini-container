import { createContext, useContext, useState, useEffect } from 'react'
import { fetchMenuTree } from '@/api/menu/menu-fetch'

/**
 * 메뉴 Context
 *
 * 실무: Recoil fwMenuState, fwLeftMenuState
 * Mini: React Context로 동일한 역할
 *
 * headerMenus  : depth 3 목록 (Header 렌더링용)
 * leftMenus    : 현재 선택된 depth 3의 subMenus (LeftMenu 렌더링용)
 * activeHeaderId: 현재 활성화된 헤더 메뉴 id
 */
const MenuContext = createContext(null)

export function MenuProvider({ children }) {
  const [headerMenus, setHeaderMenus] = useState([])   // depth 3 배열
  const [leftMenus, setLeftMenus] = useState([])        // 선택된 depth 3의 subMenus
  const [activeHeaderId, setActiveHeaderId] = useState(null)

  // 앱 초기화 시 메뉴 트리 로드
  useEffect(() => {
    fetchMenuTree()
      .then((res) => {
        const menus = res.data ?? []
        setHeaderMenus(menus)
        // 첫 번째 헤더 메뉴를 기본 활성화
        if (menus.length > 0) {
          setActiveHeaderId(menus[0].id)
          setLeftMenus(menus[0].subMenus ?? [])
        }
      })
      .catch((err) => console.error('메뉴 로드 실패:', err))
  }, [])

  // 헤더 메뉴 클릭 → 사이드바 갱신
  const handleHeaderClick = (menu) => {
    setActiveHeaderId(menu.id)
    setLeftMenus(menu.subMenus ?? [])
  }

  // 탭 전환 시 해당 탭의 headerMenuId로 사이드바 동기화
  const syncLeftMenuByHeaderId = (headerMenuId) => {
    const found = headerMenus.find((m) => m.id === headerMenuId)
    if (found) {
      setActiveHeaderId(found.id)
      setLeftMenus(found.subMenus ?? [])
    }
  }

  return (
    <MenuContext.Provider value={{ headerMenus, leftMenus, activeHeaderId, handleHeaderClick, syncLeftMenuByHeaderId }}>
      {children}
    </MenuContext.Provider>
  )
}

export const useMenu = () => {
  const context = useContext(MenuContext)
  if (!context) throw new Error('useMenu must be used within a MenuProvider')
  return context
}
