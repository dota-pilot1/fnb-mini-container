/**
 * 브랜드 등록/수정 요청 DTO
 *
 * 실무: PartnerBrandApiRegReqDto
 * - brandMid (입점사 브랜드 식별자)
 * - brandCode (플랫폼 브랜드 코드)
 * - useYn
 *
 * Mini: 더 풍부한 필드 전달
 */
export class BrandRegisterDto {
  brandCode: string;
  brandName: string;
  brandNameEn?: string;
  brandDesc?: string;
  useYn?: string;
}

export class BrandUpdateDto {
  brandCode: string;
  brandName?: string;
  brandNameEn?: string;
  brandDesc?: string;
  useYn?: string;
}

/**
 * 외부 API 공통 응답
 *
 * 실무: MiddlewareResDto
 * - code: "0200" (성공), "9999" (에러)
 * - message
 * - data
 */
export class ExternalApiResponse<T = any> {
  code: string;
  message: string;
  data?: T;

  static success<T>(data?: T, message = 'success'): ExternalApiResponse<T> {
    const res = new ExternalApiResponse<T>();
    res.code = '0200';
    res.message = message;
    res.data = data;
    return res;
  }

  static created<T>(data?: T): ExternalApiResponse<T> {
    const res = new ExternalApiResponse<T>();
    res.code = '0201';
    res.message = 'created';
    res.data = data;
    return res;
  }

  static error(message: string, code = '9999'): ExternalApiResponse {
    const res = new ExternalApiResponse();
    res.code = code;
    res.message = message;
    return res;
  }
}
