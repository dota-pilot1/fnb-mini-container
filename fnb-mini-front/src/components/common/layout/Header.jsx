import { NavLink, useNavigate } from 'react-router'
import { useAuth } from '@/context/AuthContext'
import '@/assets/style/header.scss'

/**
 * 헤더 네비게이션
 *
 * 실무: components/common/layout/Header.jsx
 * - .header > .header-first-line > .header-menu (로고 + GNB) + .nav-right-menu (사용자정보)
 * - useFwTab 훅으로 메뉴 활성화
 *
 * Mini: NavLink로 간소화, 동일한 시각 구조
 */

const menus = [
  { path: '/brand', name: '브랜드 관리' },
  { path: '/sync-history', name: '동기화 이력' },
]

export default function Header() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <div className="header">
      <div className="header-first-line">
        <div className="header-menu">
          <h1
            className="logo"
            onClick={() => navigate('/brand')}
            style={{ cursor: 'pointer' }}
          >
            FNB Mini
          </h1>
          <ul className="header-1depth">
            {menus.map((menu) => (
              <li key={menu.path}>
                <NavLink
                  to={menu.path}
                  className={({ isActive }) =>
                    `btn-text ${isActive ? 'active' : ''}`
                  }
                >
                  {menu.name}
                </NavLink>
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
