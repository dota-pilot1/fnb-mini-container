import { Module, Global, Logger } from '@nestjs/common';
import { drizzle } from 'drizzle-orm/postgres-js';
import postgres from 'postgres';
import * as schema from './schema.js';

export const DRIZZLE = Symbol('DRIZZLE');

/**
 * Drizzle ORM 모듈
 *
 * 별도 PostgreSQL 인스턴스 (포트 15433, DB: externaldb)
 * fnb-mini-back(15432/fnbmini)과 완전 분리된 외부 시스템 DB
 */
@Global()
@Module({
  providers: [
    {
      provide: DRIZZLE,
      useFactory: async () => {
        const logger = new Logger('DrizzleModule');

        const connectionString =
          process.env.DATABASE_URL ||
          'postgresql://external:external123@localhost:15433/externaldb';

        const client = postgres(connectionString);
        const db = drizzle(client, { schema });

        // 테이블 자동 생성 (개발 편의)
        await client`
          CREATE TABLE IF NOT EXISTS external_brand (
            id          BIGSERIAL PRIMARY KEY,
            brand_code  VARCHAR(20)  NOT NULL UNIQUE,
            brand_name  VARCHAR(100) NOT NULL,
            brand_name_en VARCHAR(100),
            brand_desc  VARCHAR(500),
            use_yn      CHAR(1)      NOT NULL DEFAULT 'Y',
            reg_dttm    TIMESTAMP    NOT NULL DEFAULT NOW(),
            upd_dttm    TIMESTAMP
          )
        `;

        logger.log('Drizzle ORM initialized - external_brand table ready (port 15433)');
        return db;
      },
    },
  ],
  exports: [DRIZZLE],
})
export class DrizzleModule {}
