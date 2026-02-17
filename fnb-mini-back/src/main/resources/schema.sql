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
