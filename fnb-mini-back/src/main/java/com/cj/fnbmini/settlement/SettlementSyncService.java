package com.cj.fnbmini.settlement;

import com.cj.fnbmini.settlement.dto.SettlementSyncResultDto;
import com.cj.fnbmini.settlement.entity.Settlement;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 정산 외부 API(SAP) 동기화 서비스
 *
 * 이 클래스에는 @Transactional이 없다!
 * → DB 커넥션을 점유하지 않고 외부 API만 호출
 * → 브랜드의 BrandSyncService와 동일한 패턴
 */
@Slf4j
@Service
public class SettlementSyncService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public SettlementSyncService(
            @Value("${external-api.base-url:http://localhost:18090}") String baseUrl,
            ObjectMapper objectMapper) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
        this.objectMapper = objectMapper;
    }

    /**
     * 외부 API(SAP) 동기화
     * 역분개 전표 + 신규 전표 순서대로 전송
     */
    public List<SettlementSyncResultDto> syncToExternalApi(Settlement settlement) {
        List<SettlementSyncResultDto> results = new ArrayList<>();

        // 1) 역분개 전표 전송 (기존 데이터 취소)
        SettlementSyncResultDto reverseResult = callApi(settlement, "REVERSE",
                "/api/settlements/reverse");
        results.add(reverseResult);

        // 2) 신규 전표 전송
        SettlementSyncResultDto normalResult = callApi(settlement, "SETTLE",
                "/api/settlements/normal");
        results.add(normalResult);

        return results;
    }

    /**
     * 보상 트랜잭션: 역분개 취소 요청
     * 역분개 성공 + 신규 실패 시 호출
     */
    public SettlementSyncResultDto cancelReverse(Settlement settlement) {
        return callApi(settlement, "CANCEL", "/api/settlements/cancel");
    }

    /**
     * 외부 API 호출 (에러를 throw하지 않고 result에 담아 반환)
     * 브랜드의 ShopApiClient.callApi()와 동일한 패턴
     */
    private SettlementSyncResultDto callApi(Settlement settlement, String syncType, String path) {
        SettlementSyncResultDto result = new SettlementSyncResultDto();
        result.setSettlementId(settlement.getId());
        result.setSyncType(syncType);

        Map<String, Object> requestBody = Map.of(
                "settlementId", settlement.getId(),
                "shopId", settlement.getShopId(),
                "salesDate", settlement.getSalesDate().toString(),
                "totalAmt", settlement.getTotalAmt(),
                "syncType", syncType
        );

        try {
            String requestPayload = objectMapper.writeValueAsString(requestBody);
            result.setRequestPayload(requestPayload);

            log.info("[SettlementSync] {} → POST {}", syncType, path);

            String responseBody = restClient.post()
                    .uri(path)
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            result.setResponsePayload(responseBody);

            // 응답 코드 검증
            Map<String, Object> response = objectMapper.readValue(responseBody, Map.class);
            String code = (String) response.get("code");

            if (code != null && code.startsWith("0")) {
                result.setSuccess(true);
                log.info("[SettlementSync] {} → SUCCESS (code={})", syncType, code);
            } else {
                String message = (String) response.get("message");
                result.setSuccess(false);
                result.setErrorMessage(message);
                log.warn("[SettlementSync] {} → FAILED (code={}, message={})", syncType, code, message);
            }

        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
            result.setResponsePayload("{\"code\":\"9999\",\"message\":\"" + e.getMessage() + "\"}");
            log.error("[SettlementSync] {} → ERROR: {}", syncType, e.getMessage());
        }

        return result;
    }
}
