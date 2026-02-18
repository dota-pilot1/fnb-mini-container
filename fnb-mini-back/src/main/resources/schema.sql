-- 사용자
CREATE TABLE IF NOT EXISTS app_user (
    id            BIGSERIAL PRIMARY KEY,
    user_id       VARCHAR(50)  NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    username      VARCHAR(100) NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- 기본 계정
INSERT INTO app_user (user_id, password, username)
VALUES ('admin', 'admin', '관리자')
ON CONFLICT (user_id) DO NOTHING;

-- 브랜드 마스터
CREATE TABLE IF NOT EXISTS brand (
    id              BIGSERIAL PRIMARY KEY,
    brand_code      VARCHAR(20)  NOT NULL UNIQUE,
    brand_name      VARCHAR(100) NOT NULL,
    brand_name_en   VARCHAR(100),
    brand_desc      VARCHAR(500),
    use_yn          CHAR(1)      NOT NULL DEFAULT 'Y',

    -- 동기화 상태 추적 (실무에 없었던 핵심 개선)
    sync_status     VARCHAR(20)  NOT NULL DEFAULT 'NONE',   -- NONE/PENDING/SUCCESS/FAILED
    sync_retry_count INT         NOT NULL DEFAULT 0,
    last_sync_at    TIMESTAMP,
    last_sync_error VARCHAR(500),

    -- 낙관적 락 (실무에 없었던 핵심 개선)
    version         INT          NOT NULL DEFAULT 0,

    -- 감사 필드
    reg_id          VARCHAR(50),
    reg_dttm        TIMESTAMP    NOT NULL DEFAULT NOW(),
    upd_id          VARCHAR(50),
    upd_dttm        TIMESTAMP
);

-- 동기화 이력 (실패 추적 & 재시도 근거)
CREATE TABLE IF NOT EXISTS brand_sync_history (
    id              BIGSERIAL PRIMARY KEY,
    brand_id        BIGINT       NOT NULL REFERENCES brand(id),
    sync_type       VARCHAR(20)  NOT NULL,  -- REGISTER / UPDATE
    sync_status     VARCHAR(20)  NOT NULL,  -- PENDING / SUCCESS / FAILED
    request_payload TEXT,
    response_payload TEXT,
    error_message   VARCHAR(500),
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    completed_at    TIMESTAMP
);

-- ============================================================
-- 정산 시스템
-- ============================================================

-- 매장 마스터
CREATE TABLE IF NOT EXISTS shop (
    id          BIGSERIAL PRIMARY KEY,
    shop_code   VARCHAR(20)  NOT NULL UNIQUE,
    shop_name   VARCHAR(100) NOT NULL,
    use_yn      VARCHAR(1)   DEFAULT 'Y',
    created_at  TIMESTAMP    DEFAULT NOW()
);

-- 매장 초기 데이터
INSERT INTO shop (shop_code, shop_name) VALUES
    ('SHOP001', '강남점'),
    ('SHOP002', '홍대점'),
    ('SHOP003', '판교점')
ON CONFLICT (shop_code) DO NOTHING;

-- 일매출 (POS에서 올라온 원본 데이터)
CREATE TABLE IF NOT EXISTS daily_sales (
    id          BIGSERIAL PRIMARY KEY,
    shop_id     BIGINT       NOT NULL REFERENCES shop(id),
    sales_date  DATE         NOT NULL,
    cash_amt    BIGINT       DEFAULT 0,
    card_amt    BIGINT       DEFAULT 0,
    easy_amt    BIGINT       DEFAULT 0,
    total_amt   BIGINT       DEFAULT 0,
    created_at  TIMESTAMP    DEFAULT NOW(),
    UNIQUE(shop_id, sales_date)
);

-- 일매출 샘플 데이터
INSERT INTO daily_sales (shop_id, sales_date, cash_amt, card_amt, easy_amt, total_amt) VALUES
    (1, CURRENT_DATE - 3, 150000, 350000, 100000, 600000),
    (1, CURRENT_DATE - 2, 120000, 400000, 80000,  600000),
    (1, CURRENT_DATE - 1, 180000, 320000, 120000, 620000),
    (2, CURRENT_DATE - 3, 200000, 500000, 150000, 850000),
    (2, CURRENT_DATE - 2, 170000, 450000, 130000, 750000),
    (2, CURRENT_DATE - 1, 220000, 480000, 160000, 860000),
    (3, CURRENT_DATE - 3, 100000, 250000, 80000,  430000),
    (3, CURRENT_DATE - 2, 90000,  280000, 70000,  440000),
    (3, CURRENT_DATE - 1, 110000, 300000, 90000,  500000)
ON CONFLICT (shop_id, sales_date) DO NOTHING;

-- 정산 확정 (실무: SA_CLOSE_DAILY_SALES_M)
CREATE TABLE IF NOT EXISTS settlement (
    id               BIGSERIAL PRIMARY KEY,
    shop_id          BIGINT      NOT NULL REFERENCES shop(id),
    sales_date       DATE        NOT NULL,
    total_amt        BIGINT      DEFAULT 0,
    status           VARCHAR(20) DEFAULT 'READY',
                     -- READY / PROCESSING / SETTLED / FAILED
    version          INT         DEFAULT 0,
    idempotency_key  VARCHAR(64) UNIQUE,
    sap_sync_status  VARCHAR(20) DEFAULT 'NONE',
                     -- NONE / PENDING / SUCCESS / FAILED
    sap_sync_error   VARCHAR(500),
    settled_at       TIMESTAMP,
    settled_by       VARCHAR(50),
    created_at       TIMESTAMP   DEFAULT NOW(),
    UNIQUE(shop_id, sales_date)
);

-- 정산 원장 (실무: TFMCL006)
CREATE TABLE IF NOT EXISTS settlement_ledger (
    id              BIGSERIAL PRIMARY KEY,
    settlement_id   BIGINT      NOT NULL REFERENCES settlement(id),
    shop_id         BIGINT      NOT NULL REFERENCES shop(id),
    sales_date      DATE        NOT NULL,
    ledger_type     VARCHAR(10) NOT NULL,  -- NORMAL / REVERSE
    cash_amt        BIGINT      DEFAULT 0,
    card_amt        BIGINT      DEFAULT 0,
    easy_amt        BIGINT      DEFAULT 0,
    total_amt       BIGINT      DEFAULT 0,
    del_yn          VARCHAR(1)  DEFAULT 'N',
    created_at      TIMESTAMP   DEFAULT NOW()
);

-- 정산 동기화 이력
CREATE TABLE IF NOT EXISTS settlement_sync_history (
    id                BIGSERIAL PRIMARY KEY,
    settlement_id     BIGINT      NOT NULL REFERENCES settlement(id),
    sync_type         VARCHAR(20),   -- SETTLE / REVERSE / CANCEL
    sync_status       VARCHAR(20),   -- SUCCESS / FAILED
    request_payload   TEXT,
    response_payload  TEXT,
    error_message     VARCHAR(500),
    created_at        TIMESTAMP   DEFAULT NOW()
);

-- ============================================================
-- 메뉴 시스템
-- ============================================================

-- 메뉴 마스터
CREATE TABLE IF NOT EXISTS menu (
    id              BIGSERIAL PRIMARY KEY,
    parent_id       BIGINT       REFERENCES menu(id),  -- NULL = depth 3 (최상위)
    depth           INT          NOT NULL,              -- 3=헤더, 4=사이드 카테고리, 5=실제 메뉴
    menu_name       VARCHAR(100) NOT NULL,
    component_name  VARCHAR(100),                       -- depth 5만 값 있음 (예: 'BrandPage')
    sort_order      INT          NOT NULL DEFAULT 0,
    use_yn          CHAR(1)      NOT NULL DEFAULT 'Y'
);

-- depth 3: 헤더 메뉴
INSERT INTO menu (id, parent_id, depth, menu_name, component_name, sort_order, use_yn) VALUES
    (1,  NULL, 3, '기준 정보 관리', NULL, 1, 'Y'),
    (2,  NULL, 3, '동기화 이력',    NULL, 2, 'Y'),
    (3,  NULL, 3, '정산 관리',      NULL, 3, 'Y')
ON CONFLICT (id) DO NOTHING;

-- depth 4: 사이드바 카테고리
INSERT INTO menu (id, parent_id, depth, menu_name, component_name, sort_order, use_yn) VALUES
    (10, 1, 4, '브랜드', NULL, 1, 'Y'),
    (11, 1, 4, '시스템', NULL, 2, 'Y'),
    (20, 2, 4, '동기화', NULL, 1, 'Y'),
    (30, 3, 4, '매출',   NULL, 1, 'Y')
ON CONFLICT (id) DO NOTHING;

-- depth 5: 실제 메뉴 (탭에 등록)
INSERT INTO menu (id, parent_id, depth, menu_name, component_name, sort_order, use_yn) VALUES
    (100, 10, 5, '브랜드 관리', 'BrandPage',       1, 'Y'),
    (110, 11, 5, '메뉴 관리',   'MenuMngPage',     1, 'Y'),
    (200, 20, 5, '동기화 이력', 'SyncHistoryPage', 1, 'Y'),
    (300, 30, 5, '매출 정산',   'SettlementPage',  1, 'Y')
ON CONFLICT (id) DO NOTHING;
