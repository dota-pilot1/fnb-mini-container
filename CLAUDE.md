# 프로젝트 구조

## 경로

| 역할 | 경로 |
|---|---|
| 메인 백엔드 | /Users/terecal/fnb-mini-container/fnb-mini-back |
| 외부 API 서버 | /Users/terecal/fnb-mini-container/fnb-external-api |
| 프론트엔드 | /Users/terecal/fnb-mini-container/fnb-mini-front |

## 기술 스택

### 백엔드 (fnb-mini-back)
- Spring Boot 3.5
- Java 21
- Maven (pom.xml)

### 프론트엔드 (fnb-mini-front)
- React 19
- React Router v7
- TanStack Query v5
- React Hook Form v7
- Tabulator (그리드)
- React Context (인증 상태)
- SCSS
- Vite

## 현재 메뉴 구조 (하드코딩 → DB 연동 예정)

### 현재 Header.jsx 하드코딩
```js
const menus = [
  { path: '/brand', name: '브랜드 관리' },
  { path: '/sync-history', name: '동기화 이력' },
  { path: '/settlement', name: '매출 정산' },
]
```

### 목표 구조 (실무 방식으로 리팩토링)
```
depth 3 (헤더)       → 클릭 시 사이드바만 갱신, 탭 열리지 않음
depth 4 (사이드바)   → 아코디언 카테고리, 클릭 시 펼치기/접기만
depth 5 (실제 메뉴) → 클릭 시 탭 등록 + 컴포넌트 렌더링
```

### DB 테이블 (menu)
```
id             INT          PK
parent_id      INT          NULL=최상위 / depth 3은 NULL
depth          INT          3, 4, 5
menu_name      VARCHAR      표시 이름
component_name VARCHAR      depth 5만 값 있음 (예: 'BrandPage')
sort_order     INT          순서
use_yn         CHAR(1)      Y/N
```

### 컴포넌트 레지스트리 방식 사용
- DB에는 컴포넌트 키 이름만 저장 (풀 경로 X)
- 프론트 registry에서 키 → lazy import 매핑
- 동적 경로 import 방식의 단점(번들러 최적화 불가, 런타임 오류) 회피

## 구현 로드맵

1. DB 테이블 생성 + 더미 데이터 insert
2. 백엔드 GET /api/menus → 트리 구조 반환
3. 프론트 메뉴 관리 페이지 (CRUD)
4. 탭 시스템 + 레이아웃 리팩토링 (LeftMenu, MdiTab 추가)
5. Header에서 하드코딩 → API 호출로 교체
6. 컴포넌트 레지스트리 연결

---

## 문서 파일 작성 주의사항

### 한글 파일 인코딩 문제

**증상:** .txt 파일에 한글 작성 후 열면 깨져 보임

**원인:**
- 일부 .txt 파일이 EUC-KR 인코딩으로 생성되어 있음
- Write 도구로 내용을 덮어써도 기존 파일의 인코딩을 따라가서 UTF-8로 저장되지 않음
- `file` 명령으로 확인 시 `Unicode text, UTF-8 text` 가 아닌 `data` 로 표시되면 깨진 것

**해결 방법:**
1. 깨진 파일 삭제 (`rm` 으로 완전 삭제)
2. Write 도구로 새로 생성 (삭제 후 생성하면 UTF-8로 정상 저장됨)

**확인 명령:**
```bash
file 파일경로   # Unicode text, UTF-8 text 이면 정상
               # data 이면 인코딩 깨진 것
```

**앞으로 한글 .txt 파일 작성 시:**
- 파일이 이미 존재하면 → 삭제 후 재생성
- 새 파일이면 → Write 도구로 바로 생성 (UTF-8 정상 저장)
