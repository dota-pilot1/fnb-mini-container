import { useState } from 'react'
import { useMenu } from '@/context/MenuContext'
import { useTab } from '@/context/TabContext'
import '@/assets/style/left-menu.scss'

/**
 * 사이드바 (LeftMenu)
 *
 * 실무: components/common/layout/LeftMenu.jsx
 * - depth 4: 아코디언 카테고리 (접기/펼치기)
 * - depth 5: 실제 메뉴 (클릭 → 탭 추가)
 *
 * Mini: 동일한 구조
 */
export default function LeftMenu() {
  const { leftMenus, activeHeaderId } = useMenu()
  const { addTab, activeTabId, tabs } = useTab()
  const [openCategories, setOpenCategories] = useState({})

  // depth 4 카테고리 토글
  const toggleCategory = (id) => {
    setOpenCategories((prev) => ({
      ...prev,
      [id]: !(prev[id] ?? true), // undefined도 true로 간주하여 반전
    }))
  }

  // 현재 활성 탭의 menuId
  const activeMenuId = tabs.find((t) => t.id === activeTabId)?.menuId

  return (
    <nav className="left-menu">
      <ul className="left-menu-list">
        {/* 각 메뉴를 카테고리라 가정하고 하위 메뉴들을 재귀적으로 렌더링 */}
        {leftMenus.map((category) => {
          const isOpen = openCategories[category.id] ?? true // 기본 펼침 (undefined → true)
          return (
            <li key={category.id} className="left-menu-category">
              {/* depth 4: 카테고리 헤더 */}
              <button
                className="left-menu-category-btn"
                onClick={() => toggleCategory(category.id)}
              >
                <span>{category.menuName}</span>
                <span className={`left-menu-arrow ${isOpen ? 'open' : ''}`}>▾</span>
              </button>

              {/* depth 5: 실제 메뉴 아이템 */}
              {isOpen && (
                <ul className="left-menu-items">
                  {(category.subMenus ?? []).map((item) => (
                    <li key={item.id}>
                      <button
                        className={`left-menu-item ${activeMenuId === item.id ? 'active' : ''}`}
                        onClick={() => addTab(item, activeHeaderId)}
                      >
                        {item.menuName}
                      </button>
                    </li>
                  ))}
                </ul>
              )}
            </li>
          )
        })}
      </ul>
    </nav>
  )
}
