import { Body, Controller, Get, Post, Put, Logger } from '@nestjs/common';
import { BrandService } from './brand.service.js';
import {
  BrandRegisterDto,
  BrandUpdateDto,
  ExternalApiResponse,
} from './dto/brand.dto.js';

/**
 * 외부 API 브랜드 엔드포인트
 *
 * fnb-mini-back의 ShopApiClient가 여기로 HTTP 요청을 보낸다.
 *
 * 실무 대응:
 * - POST /api/brands/register  ← CP204 (입점사 브랜드 등록)
 * - PUT  /api/brands/update    ← CP206 (입점사 브랜드 수정)
 *
 * 응답 형식: { code: "0200", message: "success", data: {...} }
 * 실무 MiddlewareResDto 와 동일한 구조
 */
@Controller('brands')
export class BrandController {
  private readonly logger = new Logger(BrandController.name);

  constructor(private readonly brandService: BrandService) {}

  /**
   * 브랜드 등록 (CP204 대응)
   */
  @Post('register')
  async register(
    @Body() dto: BrandRegisterDto,
  ): Promise<ExternalApiResponse> {
    this.logger.log(`POST /api/brands/register - brandCode=${dto.brandCode}`);

    try {
      const result = await this.brandService.registerBrand(dto);
      return ExternalApiResponse.created(result);
    } catch (error) {
      this.logger.error(`Register failed: ${error.message}`);
      return ExternalApiResponse.error(error.message);
    }
  }

  /**
   * 브랜드 수정 (CP206 대응)
   */
  @Put('update')
  async update(
    @Body() dto: BrandUpdateDto,
  ): Promise<ExternalApiResponse> {
    this.logger.log(`PUT /api/brands/update - brandCode=${dto.brandCode}`);

    try {
      const result = await this.brandService.updateBrand(dto);
      return ExternalApiResponse.success(result);
    } catch (error) {
      this.logger.error(`Update failed: ${error.message}`);
      return ExternalApiResponse.error(error.message);
    }
  }

  /**
   * 전체 조회 (디버그/확인용)
   */
  @Get()
  async findAll(): Promise<ExternalApiResponse> {
    const brands = await this.brandService.findAll();
    return ExternalApiResponse.success(brands);
  }
}
