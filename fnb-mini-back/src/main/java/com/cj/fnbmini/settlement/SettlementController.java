package com.cj.fnbmini.settlement;

import com.cj.fnbmini.common.dto.ApiResponse;
import com.cj.fnbmini.settlement.dto.SettlementReqDto;
import com.cj.fnbmini.settlement.dto.SettlementSearchDto;
import com.cj.fnbmini.settlement.entity.DailySales;
import com.cj.fnbmini.settlement.entity.Settlement;
import com.cj.fnbmini.settlement.entity.Shop;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settlements")
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;

    /** 매장 목록 */
    @GetMapping("/shops")
    public ApiResponse<List<Shop>> getShops() {
        return ApiResponse.ok(settlementService.getShopList());
    }

    /** 일매출 데이터 조회 */
    @GetMapping("/daily-sales")
    public ApiResponse<List<DailySales>> getDailySales(SettlementSearchDto searchDto) {
        return ApiResponse.ok(settlementService.getDailySalesList(searchDto));
    }

    /** 정산 내역 조회 */
    @GetMapping
    public ApiResponse<List<Settlement>> getSettlements(SettlementSearchDto searchDto) {
        return ApiResponse.ok(settlementService.getSettlementList(searchDto));
    }

    /** 정산 실행 (핵심 API) */
    @PostMapping("/settle")
    public ApiResponse<Settlement> settle(@RequestBody SettlementReqDto req) {
        Settlement result = settlementService.settle(req);
        return ApiResponse.ok(result, "정산이 완료되었습니다.");
    }

    /** 정산 SAP 재시도 */
    @PostMapping("/{id}/retry")
    public ApiResponse<Settlement> retry(@PathVariable Long id) {
        Settlement result = settlementService.retrySettlement(id);
        return ApiResponse.ok(result, "재시도가 완료되었습니다.");
    }
}
