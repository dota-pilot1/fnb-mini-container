import { useNavigate } from 'react-router'
import { useAuth } from '@/context/AuthContext'
import { useMenu } from '@/context/MenuContext'
import '@/assets/style/header.scss'

/**
 * 헤더 GNB
 *
 * 실무: components/common/layout/Header.jsx
 * - fwMenuState에서 depth 3 메뉴 읽어서 렌더링
 * - 클릭 → handleActive(id) → fwLeftMenuState 업데이트
 *
 * Mini: MenuContext에서 headerMenus 읽어서 렌더링
 * - 하드코딩 menus 배열 → DB 데이터로 교체
 * - NavLink → button (탭 SPA 방식, URL 이동 없음)
 */
export default function Header() {
  const { user, logout } = useAuth()
  const { headerMenus, activeHeaderId, handleHeaderClick } = useMenu()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <div className="header">
      <div className="header-first-line">
        <div className="header-menu">
          <h1 className="logo">FNB Mini</h1>
          <ul className="header-1depth">
            {headerMenus.map((menu) => (
              <li key={menu.id}>
                <button
                  className={`btn-text ${activeHeaderId === menu.id ? 'active' : ''}`}
                  onClick={() => handleHeaderClick(menu)}
                >
                  {menu.menuName}
                </button>
              </li>
            ))}
          </ul>
        </div>
        <div className="nav-right-menu">
          <p className="user-name">
            {user?.username || 'Guest'}
            <span className="welcome">환영합니다.</span>
          </p>
          <button className="btn-logout" type="button" onClick={handleLogout}>
            로그아웃
          </button>
        </div>
      </div>
    </div>
  )
}
