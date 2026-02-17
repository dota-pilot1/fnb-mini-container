import { pgTable, bigserial, varchar, char, timestamp } from 'drizzle-orm/pg-core';

/**
 * 외부 시스템(Airstar)의 브랜드 테이블
 *
 * fnb-mini-back에서 브랜드를 등록/수정하면
 * 이 테이블에 동기화된다.
 * 실무: Airstar 플랫폼이 관리하는 입점사 브랜드
 */
export const externalBrand = pgTable('external_brand', {
  id: bigserial('id', { mode: 'number' }).primaryKey(),
  brandCode: varchar('brand_code', { length: 20 }).notNull().unique(),
  brandName: varchar('brand_name', { length: 100 }).notNull(),
  brandNameEn: varchar('brand_name_en', { length: 100 }),
  brandDesc: varchar('brand_desc', { length: 500 }),
  useYn: char('use_yn', { length: 1 }).notNull().default('Y'),
  regDttm: timestamp('reg_dttm').notNull().defaultNow(),
  updDttm: timestamp('upd_dttm'),
});
