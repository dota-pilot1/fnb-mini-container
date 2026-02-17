package com.cj.fnbmini.external;

import com.cj.fnbmini.brand.dto.BrandSyncResultDto;
import com.cj.fnbmini.brand.entity.Brand;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

/**
 * 외부 API 클라이언트
 *
 * 실무: middlewareApiManager.sendRequest(CP204/CP206)
 *       → 미들웨어 서버 → Airstar 플랫폼 (3-hop)
 *
 * Mini: RestClient → fnb-external-api (NestJS, 포트 18090) (2-hop)
 *       실무의 RestTemplate 대신 Spring 6+ RestClient 사용
 *
 * fnb-external-api가 실제로 DB에 저장하고, 지연/실패를 시뮬레이션한다.
 * 응답 형식: { code: "0200", message: "success", data: {...} }
 */
@Slf4j
@Component
public class ShopApiClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public ShopApiClient(
            @Value("${external-api.base-url:http://localhost:18090}") String baseUrl,
            ObjectMapper objectMapper) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
        this.objectMapper = objectMapper;
        log.info("[ShopApiClient] 외부 API 서버: {}", baseUrl);
    }

    /**
     * 브랜드 등록 (실무: CP204)
     * POST /api/brands/register → fnb-external-api
     */
    public BrandSyncResultDto registerBrand(Brand brand) {
        return callApi(brand, "REGISTER", "/api/brands/register", "POST");
    }

    /**
     * 브랜드 수정 (실무: CP206)
     * PUT /api/brands/update → fnb-external-api
     */
    public BrandSyncResultDto updateBrand(Brand brand) {
        return callApi(brand, "UPDATE", "/api/brands/update", "PUT");
    }

    private BrandSyncResultDto callApi(Brand brand, String syncType, String path, String method) {
        BrandSyncResultDto result = new BrandSyncResultDto();
        result.setBrandId(brand.getId());
        result.setSyncType(syncType);

        // 요청 본문 구성
        Map<String, String> requestBody = Map.of(
                "brandCode", brand.getBrandCode(),
                "brandName", brand.getBrandName(),
                "brandNameEn", brand.getBrandNameEn() != null ? brand.getBrandNameEn() : "",
                "brandDesc", brand.getBrandDesc() != null ? brand.getBrandDesc() : "",
                "useYn", brand.getUseYn() != null ? brand.getUseYn() : "Y"
        );

        try {
            String requestPayload = objectMapper.writeValueAsString(requestBody);
            result.setRequestPayload(requestPayload);

            log.info("[ShopApiClient] {} brand '{}' → {} {}",
                    syncType, brand.getBrandCode(), method, path);

            // 실제 HTTP 호출 (RestClient)
            String responseBody;
            if ("PUT".equals(method)) {
                responseBody = restClient.put()
                        .uri(path)
                        .header("Content-Type", "application/json")
                        .body(requestBody)
                        .retrieve()
                        .body(String.class);
            } else {
                responseBody = restClient.post()
                        .uri(path)
                        .header("Content-Type", "application/json")
                        .body(requestBody)
                        .retrieve()
                        .body(String.class);
            }

            result.setResponsePayload(responseBody);

            // 응답 코드 검증 (실무에서 빠져있던 핵심 개선)
            Map<String, Object> response = objectMapper.readValue(responseBody, Map.class);
            String code = (String) response.get("code");

            if (code != null && code.startsWith("0")) {
                // 0200, 0201, 0202 → 성공
                result.setSuccess(true);
                log.info("[ShopApiClient] {} brand '{}' → SUCCESS (code={})",
                        syncType, brand.getBrandCode(), code);
            } else {
                // 9999 등 → 외부 시스템에서 에러 반환
                String message = (String) response.get("message");
                result.setSuccess(false);
                result.setErrorMessage(message);
                log.warn("[ShopApiClient] {} brand '{}' → FAILED (code={}, message={})",
                        syncType, brand.getBrandCode(), code, message);
            }

        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
            result.setResponsePayload("{\"code\":\"9999\",\"message\":\"" + e.getMessage() + "\"}");
            log.error("[ShopApiClient] {} brand '{}' → ERROR: {}",
                    syncType, brand.getBrandCode(), e.getMessage());
        }

        return result;
    }
}
