import { Body, Controller, Post, Logger } from '@nestjs/common';
import { ExternalApiResponse } from '../brand/dto/brand.dto.js';

/**
 * 정산 외부 API 엔드포인트 (SAP 시뮬레이터)
 *
 * fnb-mini-back의 SettlementSyncService가 여기로 HTTP 요청을 보낸다.
 *
 * - POST /api/settlements/reverse  ← 역분개 전표 전송
 * - POST /api/settlements/normal   ← 신규 전표 전송
 * - POST /api/settlements/cancel   ← 보상 취소 요청
 *
 * 지연 시뮬레이션 (2~4초) + 랜덤 실패 (10%)
 */
@Controller('settlements')
export class SettlementController {
  private readonly logger = new Logger(SettlementController.name);

  /** 역분개 전표 전송 */
  @Post('reverse')
  async reverse(@Body() body: any): Promise<ExternalApiResponse> {
    this.logger.log(
      `POST /api/settlements/reverse - shopId=${body.shopId}, date=${body.salesDate}`,
    );
    return this.processWithDelay(body, 'REVERSE');
  }

  /** 신규 전표 전송 */
  @Post('normal')
  async normal(@Body() body: any): Promise<ExternalApiResponse> {
    this.logger.log(
      `POST /api/settlements/normal - shopId=${body.shopId}, date=${body.salesDate}`,
    );
    return this.processWithDelay(body, 'NORMAL');
  }

  /** 보상 취소 요청 */
  @Post('cancel')
  async cancel(@Body() body: any): Promise<ExternalApiResponse> {
    this.logger.log(
      `POST /api/settlements/cancel - shopId=${body.shopId}, date=${body.salesDate}`,
    );
    // 취소는 항상 성공 (보상은 실패하면 안 됨)
    await this.delay(1000, 2000);
    return ExternalApiResponse.success(
      { settlementId: body.settlementId, action: 'CANCELLED' },
      'reverse cancelled',
    );
  }

  private async processWithDelay(
    body: any,
    type: string,
  ): Promise<ExternalApiResponse> {
    // 2~4초 지연 (SAP 응답 시뮬레이션)
    await this.delay(2000, 4000);

    // 10% 확률로 실패
    if (Math.random() < 0.1) {
      this.logger.warn(`${type} FAILED (random failure)`);
      return ExternalApiResponse.error(
        `SAP ${type} processing failed: temporary error`,
      );
    }

    return ExternalApiResponse.success(
      {
        settlementId: body.settlementId,
        shopId: body.shopId,
        salesDate: body.salesDate,
        type,
        processedAt: new Date().toISOString(),
      },
      `${type} processed successfully`,
    );
  }

  private delay(min: number, max: number): Promise<void> {
    const ms = Math.floor(Math.random() * (max - min + 1)) + min;
    return new Promise((resolve) => setTimeout(resolve, ms));
  }
}
