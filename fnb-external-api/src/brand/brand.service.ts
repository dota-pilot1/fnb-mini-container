import { Inject, Injectable, Logger } from '@nestjs/common';
import { eq } from 'drizzle-orm';
import { DRIZZLE } from '../drizzle/drizzle.module.js';
import { externalBrand } from '../drizzle/schema.js';
import { BrandRegisterDto, BrandUpdateDto } from './dto/brand.dto.js';

@Injectable()
export class BrandService {
  private readonly logger = new Logger(BrandService.name);

  constructor(@Inject(DRIZZLE) private readonly db: any) {}

  /**
   * 브랜드 등록 (실무 CP204 대응)
   *
   * fnb-mini-back → POST /api/brands/register → 여기
   * Airstar 시스템에 브랜드를 등록하는 것을 시뮬레이션
   */
  async registerBrand(dto: BrandRegisterDto) {
    this.logger.log(`[REGISTER] brandCode=${dto.brandCode}, brandName=${dto.brandName}`);

    // 지연 시뮬레이션 (1~3초 - 실제 외부 API 응답 시간 모사)
    await this.simulateDelay();

    // 실패 시뮬레이션 (20% 확률)
    this.simulateFailure();

    // DB 저장 (Drizzle)
    const [inserted] = await this.db
      .insert(externalBrand)
      .values({
        brandCode: dto.brandCode,
        brandName: dto.brandName,
        brandNameEn: dto.brandNameEn || null,
        brandDesc: dto.brandDesc || null,
        useYn: dto.useYn || 'Y',
      })
      .onConflictDoUpdate({
        target: externalBrand.brandCode,
        set: {
          brandName: dto.brandName,
          brandNameEn: dto.brandNameEn || null,
          brandDesc: dto.brandDesc || null,
          useYn: dto.useYn || 'Y',
          updDttm: new Date(),
        },
      })
      .returning();

    this.logger.log(`[REGISTER] SUCCESS - id=${inserted.id}`);
    return inserted;
  }

  /**
   * 브랜드 수정 (실무 CP206 대응)
   *
   * fnb-mini-back → PUT /api/brands/update → 여기
   */
  async updateBrand(dto: BrandUpdateDto) {
    this.logger.log(`[UPDATE] brandCode=${dto.brandCode}`);

    await this.simulateDelay();
    this.simulateFailure();

    const [updated] = await this.db
      .update(externalBrand)
      .set({
        brandName: dto.brandName,
        brandNameEn: dto.brandNameEn,
        brandDesc: dto.brandDesc,
        useYn: dto.useYn,
        updDttm: new Date(),
      })
      .where(eq(externalBrand.brandCode, dto.brandCode))
      .returning();

    if (!updated) {
      throw new Error(`Brand not found: ${dto.brandCode}`);
    }

    this.logger.log(`[UPDATE] SUCCESS - id=${updated.id}`);
    return updated;
  }

  /**
   * 전체 조회 (디버그용)
   */
  async findAll() {
    return this.db.select().from(externalBrand);
  }

  // ===== 시뮬레이션 =====

  /**
   * 1~3초 지연 시뮬레이션
   * 실무에서 Airstar API 응답이 1~3초 걸리는 것을 모사
   */
  private async simulateDelay(): Promise<void> {
    const delay = 1000 + Math.random() * 2000;
    this.logger.debug(`  ⏳ Simulating ${Math.round(delay)}ms API latency...`);
    await new Promise((resolve) => setTimeout(resolve, delay));
  }

  /**
   * 20% 확률 실패 시뮬레이션
   * 외부 시스템 장애, 타임아웃 등을 모사
   */
  private simulateFailure(): void {
    if (Math.random() < 0.2) {
      this.logger.warn('  ❌ Simulated failure!');
      throw new Error('External system unavailable - 외부 시스템 일시적 장애');
    }
  }
}
