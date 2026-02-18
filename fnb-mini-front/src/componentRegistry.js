import { lazy } from 'react'

/**
 * 컴포넌트 레지스트리
 *
 * 실무: App.jsx에서 lazy(() => import(`@/${componentName}`)) 동적 경로 방식
 * - 단점: 번들러 최적화 불가, 런타임 오류, IDE 추적 불가
 *
 * Mini: 키 → lazy import 정적 매핑 방식
 * - DB에는 키 이름만 저장 (예: 'BrandPage')
 * - 정적 분석 가능, code splitting 동작, 오타 즉시 감지
 *
 * 새 페이지 추가 시 여기에 등록하면 됨
 */
const componentRegistry = {
  BrandPage:       lazy(() => import('@/pages/brand/BrandPage')),
  SyncHistoryPage: lazy(() => import('@/pages/sync-history/SyncHistoryPage')),
  SettlementPage:  lazy(() => import('@/pages/settlement/SettlementPage')),
  MenuMngPage:     lazy(() => import('@/pages/menu/MenuMngPage')),
}

export default componentRegistry
