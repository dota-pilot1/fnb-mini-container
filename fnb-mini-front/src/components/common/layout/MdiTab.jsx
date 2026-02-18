import { useTab } from '@/context/TabContext'
import '@/assets/style/mdi-tab.scss'

/**
 * MDI 탭 바
 *
 * 실무: components/common/layout/MdiTab.jsx
 * - tabStore에서 탭 배열 읽어서 렌더링
 * - 탭 클릭 → handleChangeTab
 * - X 클릭 → handleCloseTab
 * - 전체닫기 버튼
 *
 * Mini: 동일한 구조
 */
export default function MdiTab() {
  const { tabs, activeTabId, changeTab, closeTab, closeAllTabs } = useTab()

  if (tabs.length === 0) return null

  return (
    <div className="mdi-tab">
      <ul className="mdi-tab-list">
        {tabs.map((tab) => (
          <li
            key={tab.id}
            className={`mdi-tab-item ${activeTabId === tab.id ? 'active' : ''}`}
          >
            <button
              className="mdi-tab-label"
              onClick={() => changeTab(tab.id)}
            >
              {tab.menuName}
            </button>
            <button
              className="mdi-tab-close"
              onClick={(e) => {
                e.stopPropagation()
                closeTab(tab.id)
              }}
            >
              ×
            </button>
          </li>
        ))}
      </ul>
      <button className="mdi-tab-close-all" onClick={closeAllTabs}>
        전체닫기
      </button>
    </div>
  )
}
