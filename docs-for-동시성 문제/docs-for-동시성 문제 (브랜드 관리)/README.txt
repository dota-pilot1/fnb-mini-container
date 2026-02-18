================================================================
fnb-mini-back 동시성 문제 해결 패턴 정리
================================================================

이 프로젝트에서 구현된 8가지 동시성/트랜잭션 패턴.
각 폴더에 상세 문서가 있음.


■ 실무 문제 요약

  @Transactional 하나로 DB 저장 + 외부 API 호출을 감쌈
  → 외부 API 3~6초 응답 대기 동안 DB 커넥션 점유
  → HikariCP 풀 고갈 → 시스템 전체 마비
  → 에러 추적 불가, 재시도 불가, 동시 수정 시 데이터 유실


■ 구현된 패턴 (8가지)

  ┌───┬──────────────────────────────┬──────────────────────────────────┐
  │ # │ 패턴                         │ 해결하는 문제                     │
  ├───┼──────────────────────────────┼──────────────────────────────────┤
  │ 1 │ 트랜잭션 3분할                │ 커넥션 풀 고갈 (6초 → 80ms)      │
  │ 2 │ 낙관적 락                    │ 동시 수정 시 데이터 유실 (Lost    │
  │   │ (version + WHERE)            │ Update)                          │
  │ 3 │ Self-Proxy 패턴              │ 같은 클래스 내 @Transactional     │
  │   │ (@Lazy @Autowired self)      │ 무시되는 문제                     │
  │ 4 │ 동기화 상태 추적              │ 실패한 건을 찾을 수 없는 문제     │
  │   │ (NONE→PENDING→SUCCESS/FAILED)│                                  │
  │ 5 │ 이력 테이블                   │ 에러 원인 추적 불가              │
  │   │ (brand_sync_history)         │ (요청/응답 페이로드 보존)         │
  │ 6 │ 자동 재시도                   │ 일시적 장애 시 수동 개입 필요     │
  │   │ (5분 스케줄러, max 3회)       │                                  │
  │ 7 │ 수동 재시도                   │ 관리자 즉시 재시도 필요           │
  │   │ (POST /api/brands/{id}/retry)│                                  │
  │ 8 │ 외부 API 에러 핸들링          │ API 에러 시 전체 TX 롤백 +       │
  │   │ (응답 검증 + try-catch)       │ DB 데이터까지 손실               │
  └───┴──────────────────────────────┴──────────────────────────────────┘


■ 핵심 흐름 (브랜드 저장)

  POST /api/brands
       │
       ▼
  saveBrands()                    ← 오케스트레이터 (TX 없음)
       │
       ├─ TX#1: saveBrandsToDb()  ← DB 저장 + PENDING + 낙관적 락
       │        커밋 → 커넥션 반환 (~50ms)
       │
       ├─ NO TX: syncToExternalApi()  ← 외부 API 호출
       │         커넥션 점유 없음 (~3-6초)
       │         에러 시 catch → FAILED 처리
       │
       └─ TX#2: updateSyncResults()   ← 결과 반영 + 이력 저장
                커밋 → 커넥션 반환 (~30ms)


■ 폴더 구조

  docs-for-동시성 문제/
  ├── README.txt (이 파일)
  ├── 1-트랜잭션-분리/       TX 3분할로 커넥션 점유 75배 개선
  ├── 2-낙관적-락/           version 컬럼으로 동시 수정 충돌 감지
  ├── 3-self-proxy-패턴/     @Lazy @Autowired self로 AOP 프록시 보장
  ├── 4-동기화-상태-추적/     sync_status 상태 머신
  ├── 5-이력-테이블/          request/response 페이로드 보존
  ├── 6-자동-재시도/          5분 스케줄러, max 3회
  ├── 7-수동-재시도/          POST /api/brands/{id}/retry
  └── 8-외부API-에러-핸들링/  응답 코드 검증 + 에러 격리


■ 관련 소스 파일 (fnb-mini-back)

  src/main/java/com/cj/fnbmini/
  ├── brand/
  │   ├── BrandService.java        ← 트랜잭션 3분할 + 낙관적 락 + self-proxy
  │   ├── BrandSyncService.java    ← TX 없이 외부 API 호출
  │   ├── BrandSyncScheduler.java  ← 5분 자동 재시도
  │   ├── BrandController.java     ← 수동 재시도 API
  │   ├── BrandMapper.java         ← MyBatis 매퍼
  │   └── entity/
  │       ├── Brand.java           ← version, syncStatus 필드
  │       └── BrandSyncHistory.java← 이력 엔티티
  └── external/
      └── ShopApiClient.java       ← 외부 API 호출 + 에러 핸들링

  src/main/resources/
  ├── mappers/brand/BrandMapper.xml ← updateBrandWithVersion, updateSyncStatus
  └── schema.sql                    ← brand(version, sync_*), brand_sync_history


■ 추가 연습 가능한 패턴 (미구현)

  - 멱등성 키: 클라이언트 재시도 시 중복 API 호출 방지
  - 보상 트랜잭션: 외부 API 성공 → DB 실패 시 API 취소
  - 비관적 락: SELECT ... FOR UPDATE (동시 승인 방지)
  - 분산 락: Redis/Zookeeper (멀티 인스턴스 환경)
  - 지수 백오프: 재시도 간격 점진 증가 (1분 → 5분 → 30분)
  - 서킷 브레이커: 외부 API 장시간 다운 시 호출 차단
