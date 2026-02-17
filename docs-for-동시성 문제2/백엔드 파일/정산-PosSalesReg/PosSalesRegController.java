package com.cj.freshway.fs.cps.closing.closingmng;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cj.freshway.fs.common.base.BaseException;
import com.cj.freshway.fs.common.base.GridResponse;
import com.cj.freshway.fs.cps.closing.closingmng.dto.PosSalesRegDto;
import com.cj.freshway.fs.cps.common.base.FscpsBaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author jangjaehyun
 * @description
 * 
 *              <pre>
 *              POS매출등록
 *              </pre>
 */
@Slf4j
@RestController
@RequestMapping("/api/closing/closingmng") // //api/base/vanmstmng/v1.0/selectVanMstMngLst
@Tag(name = "cps/closing/closingmng", description = "cps/closing/closingmng")
public class PosSalesRegController extends FscpsBaseController {
  @Autowired
  PosSalesRegService posSalesRegService;

  /**
   * <pre>
   * POS매출등록 정산 상세내역
   * </pre>
   *
   * @author jangjaehyun
   * @param PosSalesRegDto
   * @return
   * @throws BaseException
   */
  @PostMapping("v1.0/calcDtl")
  public GridResponse<Integer, Integer, Object> selectCalcDtl(@RequestBody PosSalesRegDto dto)
      throws BaseException {
    return posSalesRegService.selectCalcDtl(dto);
  }

  /**
   * <pre>
   * POS매출등록 매출전송 내역
   * </pre>
   *
   * @author jangjaehyun
   * @param PosSalesRegDto
   * @return
   * @throws BaseException
   */
  @PostMapping("v1.0/sendSales")
  public GridResponse<Integer, Integer, Object> selectSendSales(@RequestBody PosSalesRegDto dto)
      throws BaseException {
    return posSalesRegService.selectSendSales(dto);
  }

  /**
   * <pre>
   * POS매출등록 시재전송 내역
   * </pre>
   *
   * @author jangjaehyun
   * @param PosSalesRegDto
   * @return
   * @throws BaseException
   */
  @PostMapping("v1.0/sendGdoh")
  public GridResponse<Integer, Integer, Object> selectSendGdoh(@RequestBody PosSalesRegDto dto)
      throws BaseException {
    return posSalesRegService.selectSendGdoh(dto);
  }

  /**
   * <pre>
   * POS매출등록 매출 세부내역
   * </pre>
   *
   * @author jangjaehyun
   * @param PosSalesRegDto
   * @return
   * @throws BaseException
   */
  @PostMapping("v1.0/paySalesLst")
  public GridResponse<Integer, Integer, Object> selectPaySalesLst(@RequestBody PosSalesRegDto dto)
      throws BaseException {
    return posSalesRegService.selectPaySalesLst(dto);
  }

  /**
   * <pre>
   * POS매출등록 결제수단별 매출
   * </pre>
   *
   * @author jangjaehyun
   * @param PosSalesRegDto
   * @return
   * @throws BaseException
   */
  @PostMapping("v1.0/paymentLst")
  public GridResponse<Integer, Integer, Object> selectPaymentLst(@RequestBody PosSalesRegDto dto)
      throws BaseException {
    return posSalesRegService.selectPaymentLst(dto);
  }

  /**
   * <pre>
   * POS매출등록 결제수단별 매출 상세
   * </pre>
   *
   * @author jangjaehyun
   * @param PosSalesRegDto
   * @return
   * @throws BaseException
   */
  @PostMapping("v1.0/paymentDtlLst")
  public GridResponse<Integer, Integer, Object> selectPaymentDtlLst(@RequestBody PosSalesRegDto dto)
      throws BaseException {
    return posSalesRegService.selectPaymentDtlLst(dto);
  }

  /**
   * <pre>
   * POS매출등록 매출 전송
   * </pre>
   *
   * @author jangjaehyun
   * @param PosSalesRegDto
   * @return
   * @throws BaseException
   */
  @PostMapping("v1.0/insertPosSales")
  public GridResponse<Integer, Integer, Object> insertPosSales(@RequestBody PosSalesRegDto dto)
      throws BaseException {
    return posSalesRegService.insertPosSales(dto);
  }

  /**
   * <pre>
   * POS매출등록 대사 차이건수 조회
   * </pre>
   *
   * @author jangjaehyun
   * @param PosSalesRegDto
   * @return
   * @throws BaseException
   */
  @PostMapping("v1.0/selectPosSapCompareCnt")
  public GridResponse<Integer, Integer, Object> selectPosSapCompareCnt(
      @RequestBody PosSalesRegDto dto) throws BaseException {
    return posSalesRegService.selectPosSapCompareCnt(dto);
  }

  /**
   * <pre>
   * POS매출등록 마감여부
   * </pre>
   *
   * @author jangjaehyun
   * @param PosSalesRegDto
   * @return
   * @throws BaseException
   */
  @PostMapping("v1.0/closeYn")
  public GridResponse<Integer, Integer, Object> selectCloseYn(@RequestBody PosSalesRegDto dto)
      throws BaseException {
    return posSalesRegService.selectCloseYn(dto);
  }

  /**
   * <pre>
   * POS매출마감 외상 세금계산서 발행 저장/수정
   * </pre>
   *
   * @author jangjaehyun
   * @param CrdtCardAckGdohRegDto
   * @return
   * @throws BaseException
   */
  @PostMapping("v1.0/insertOncrdtTaxPblLst")
  public void insertOncrdtTaxPblLst(@RequestBody List<PosSalesRegDto> dtoLst) throws BaseException {
    posSalesRegService.insertOncrdtTaxPblLst(dtoLst);
  }

  /**
   * <pre>
   * POS매출마감 재집계 처리
   * </pre>
   *
   * @author jangjaehyun
   * @param PosSalesRegDto
   * @return
   * @throws BaseException
   */
  @PostMapping("v1.0/deleteSalesSum")
  public boolean deleteSalesSum(@RequestBody PosSalesRegDto dto) throws BaseException {
    return posSalesRegService.deletePosSalesSum(dto);
  }



}
