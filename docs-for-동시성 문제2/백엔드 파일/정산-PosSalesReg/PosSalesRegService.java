package com.cj.freshway.fs.cps.closing.closingmng;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import com.cj.freshway.fs.common.base.GridResponse;
import com.cj.freshway.fs.cps.closing.closingmng.dto.ClosingAdjDto;
import com.cj.freshway.fs.cps.closing.closingmng.dto.PosSalesRegDto;
import com.cj.freshway.fs.cps.closing.closingmng.entity.MnulCustSalesRegEntity;
import com.cj.freshway.fs.cps.closing.closingmng.entity.PosSalesRegEntity;
import com.cj.freshway.fs.cps.closing.closingmng.exception.PosSalesRegException;
import com.cj.freshway.fs.cps.common.interfacesap.SI_COM0280_FS_ClientSynchronousModule;
import com.cj.freshway.fs.cps.common.interfacesap.dto.SearchDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class PosSalesRegService {

  @Autowired
  PosSalesRegService self;

  @Autowired
  PosSalesRegMapper posSalesRegMapper;

  @Autowired
  ClosingAdjMapper closingAdjMapper;

  @Autowired
  MnulCustSalesRegMapper mnulCustSalesRegMapper;

  @Autowired
  SI_COM0280_FS_ClientSynchronousModule module;

  /**
   * @author jangjaehyun
   * @param PosSalesRegDto
   * @return
   * @throws PosSalesRegException
   * @description
   *
   *              <pre>
   * POS매출등록 정산상세내역 조회
   *              </pre>
   */
  public GridResponse<Integer, Integer, Object> selectCalcDtl(PosSalesRegDto dto)
      throws PosSalesRegException {
    Integer count = 0;

    try {
      PosSalesRegEntity result = posSalesRegMapper.selectStePosSales(dto);

      GridResponse<Integer, Integer, Object> response = new GridResponse<>();
      response.setLastRow(count);
      response.setLastPage(0);
      response.setData(result);
      return response;
    } catch (Exception e) {
      throw new PosSalesRegException(e);
    }
  }

  /**
   * @author jangjaehyun
   * @param PosSalesRegDto
   * @return
   * @throws PosSalesRegException
   * @description
   *
   *              <pre>
   * POS매출등록 매출전송 조회
   *              </pre>
   */
  public GridResponse<Integer, Integer, Object> selectSendSales(PosSalesRegDto dto)
      throws PosSalesRegException {
    Integer count = 0;

    try {
      PosSalesRegEntity result = new PosSalesRegEntity();

      PosSalesRegEntity noSendSalesDto = posSalesRegMapper.selectNoSendSales(dto);
      PosSalesRegEntity sendSalesCompDto = posSalesRegMapper.selectSendSalesComp(dto);

      result.setNoSendSales(noSendSalesDto.getNoSendSales());
      result.setSendSalesComp(sendSalesCompDto.getSendSalesComp());
      result.setSendSalesIncomp(sendSalesCompDto.getSendSalesIncomp());

      GridResponse<Integer, Integer, Object> response = new GridResponse<>();
      response.setLastRow(count);
      response.setLastPage(0);
      response.setData(result);
      return response;
    } catch (Exception e) {
      throw new PosSalesRegException(e);
    }
  }

  /**
   * @author jangjaehyun
   * @param PosSalesRegDto
   * @return
   * @throws PosSalesRegException
   * @description
   *
   *              <pre>
   * POS매출등록 시재전송 조회
   *              </pre>
   */
  public GridResponse<Integer, Integer, Object> selectSendGdoh(PosSalesRegDto dto)
      throws PosSalesRegException {
    Integer count = 0;

    try {
      PosSalesRegEntity result = new PosSalesRegEntity();

      PosSalesRegEntity noSendGdohDto = posSalesRegMapper.selectNoSendGdoh(dto);
      PosSalesRegEntity sendGdohCompDto = posSalesRegMapper.selectSendGdohComp(dto);

      if (ObjectUtils.isEmpty(sendGdohCompDto)) {
        result.setSendGdohComp("0");
        result.setSendGdohIncomp("0");
      } else {
        result.setSendGdohComp(sendGdohCompDto.getSendGdohComp());
        result.setSendGdohIncomp(sendGdohCompDto.getSendGdohIncomp());
      }

      if (ObjectUtils.isEmpty(noSendGdohDto)) {
        result.setNoSendGdoh("0");
      } else {
        result.setNoSendGdoh(noSendGdohDto.getNoSendGdoh());
      }

      GridResponse<Integer, Integer, Object> response = new GridResponse<>();
      response.setLastRow(count);
      response.setLastPage(0);
      response.setData(result);
      return response;
    } catch (Exception e) {
      throw new PosSalesRegException(e);
    }
  }

  /**
   * @author jangjaehyun
   * @param PosSalesRegDto
   * @return
   * @throws PosSalesRegException
   * @description
   *
   *              <pre>
   * POS매출등록 매출 세부내역
   *              </pre>
   */
  public GridResponse<Integer, Integer, Object> selectPaySalesLst(PosSalesRegDto dto)
      throws PosSalesRegException {
    Integer count = 0;

    try {
      List<PosSalesRegDto> list = posSalesRegMapper.selectPaySalesLst(dto);

      if (!ObjectUtils.isEmpty(list)) {
        count = list.size();
      }

      GridResponse<Integer, Integer, Object> response = new GridResponse<>();
      response.setLastRow(count);
      response.setLastPage(0);
      response.setData(list);
      return response;
    } catch (Exception e) {
      throw new PosSalesRegException(e);
    }
  }

  /**
   * @author jangjaehyun
   * @param PosSalesRegDto
   * @return
   * @throws PosSalesRegException
   * @description
   *
   *              <pre>
   * POS매출등록 결제수단별 내역
   *              </pre>
   */
  public GridResponse<Integer, Integer, Object> selectPaymentLst(PosSalesRegDto dto)
      throws PosSalesRegException {
    Integer count = 0;

    try {
      List<PosSalesRegDto> list = posSalesRegMapper.selectPaymentLst(dto);

      if (!ObjectUtils.isEmpty(list)) {
        count = list.size();
      }

      GridResponse<Integer, Integer, Object> response = new GridResponse<>();
      response.setLastRow(count);
      response.setLastPage(0);
      response.setData(list);
      return response;
    } catch (Exception e) {
      throw new PosSalesRegException(e);
    }
  }

  /**
   * @author jangjaehyun
   * @param PosSalesRegDto
   * @return
   * @throws PosSalesRegException
   * @description
   *
   *              <pre>
   * POS매출등록 결제수단별 매출 상세
   *              </pre>
   */
  public GridResponse<Integer, Integer, Object> selectPaymentDtlLst(PosSalesRegDto dto)
      throws PosSalesRegException {
    Integer count = 0;

    try {

      List<PosSalesRegDto> list = new ArrayList<>();

      switch (dto.getPayCd()) {
        case "201":
          list = posSalesRegMapper.selectPaySalesCashLst(dto);
          break;
        case "203":
          list = posSalesRegMapper.selectPaySalesCardLst(dto);
          break;
        case "205":
          list = posSalesRegMapper.selectPaySalesOncrdtLst(dto);
          break;
        case "208":     
          list = posSalesRegMapper.selectPaySalesServiceLst(dto);
          break;  
        default:
          list = posSalesRegMapper.selectPaySalesCashLst(dto);
      }

      if (!ObjectUtils.isEmpty(list)) {
        count = list.size();
      }

      GridResponse<Integer, Integer, Object> response = new GridResponse<>();
      response.setLastRow(count);
      response.setLastPage(0);
      response.setData(list);
      return response;
    } catch (Exception e) {
      throw new PosSalesRegException(e);
    }
  }

  /**
   * @author jangjaehyun
   * @param PosSalesRegDto
   * @return
   * @throws PosSalesRegException
   * @description
   *
   *              <pre>
   * POS매출등록 마감여부 조회
   *              </pre>
   */
  public GridResponse<Integer, Integer, Object> selectCloseYn(PosSalesRegDto dto)
      throws PosSalesRegException {
    Integer count = 0;

    try {
      PosSalesRegEntity result = posSalesRegMapper.selectCloseYn(dto);
      PosSalesRegEntity result2 = posSalesRegMapper.selectOncrdtCustCnt(dto);

      result2.setTaxBilYn(posSalesRegMapper.selectTaxBilYn(dto).getTaxBilYn());
      result2.setSalesLogCnt(posSalesRegMapper.selectSaleLogCnt(dto).getSalesLogCnt());
      result2.setCancelRsnDescNullCnt(
          Integer.toString(posSalesRegMapper.selectCancelRsnDescNullCnt(dto)));

      if (result != null) {
        result.setOncrdtCustCnt(result2.getOncrdtCustCnt());
        result.setTaxBilYn(result2.getTaxBilYn());
        result.setSalesLogCnt(result2.getSalesLogCnt());
        result.setCancelRsnDescNullCnt(result2.getCancelRsnDescNullCnt());
      } else {
        result = result2;
      }

      GridResponse<Integer, Integer, Object> response = new GridResponse<>();
      response.setLastRow(count);
      response.setLastPage(0);
      response.setData(result);
      return response;
    } catch (Exception e) {
      throw new PosSalesRegException(e);
    }
  }

  /**
   * POS매출등록 매출 전송
   *
   * @param PosSalesRegDto
   * @returnsss
   */
  @Transactional
  public GridResponse<Integer, Integer, Object> insertPosSales(PosSalesRegDto dto)
      throws PosSalesRegException {

    try {
      // 대사후 차이가 없을때 전송 및 저장
      int compareResultCnt = posSalesRegMapper.selectPosSapCompareCnt(dto);

      GridResponse<Integer, Integer, Object> response = new GridResponse<>();

      if (compareResultCnt == 0) {
        // fscps.SA_CLOSE_DAILY_SALES_M 저장

        // 1) 이전 일매출 데이터로부터 'del_yn = 'Y'로 한 세트 추가 생성 (취소용)
        posSalesRegMapper.insertReversePosSales(dto);
        // 2) 1)의 취소한 일매출 데이터로부터 FS매출 데이터 생성 (역분개용)
        posSalesRegMapper.insertDelTFMCL006(dto);
        // 3) FS일매출 원 데이터 'del_yn = 'Y'로 업데이트
        posSalesRegMapper.deleteTFMCL006(dto);
        // 4) 원 일매출 데이터 'del_yn = 'Y'로 업데이트
        posSalesRegMapper.deletePosSales(dto);
        // 5) TR데이터로부터 일매출 데이터 생성
        posSalesRegMapper.insertPosSales(dto);
        // 6) 5의)데이터로부터 FS매출 데이터 생성
        posSalesRegMapper.insertTFMCL006(dto);

        // fscps.SA_CLOSING_ADJ_M (일마감) 저장
        //ClosingAdjDto closingAdjDto = new ClosingAdjDto();
        //closingAdjDto.setCoId(dto.getCoId());
        //closingAdjDto.setShopId(dto.getShopId());
        //closingAdjDto.setOccrDt(dto.getSalesDt());
        //closingAdjMapper.insertClosingAdjReg(closingAdjDto);

        // procedure
        MnulCustSalesRegEntity mnulCustSalesRegEntity = new MnulCustSalesRegEntity();
        mnulCustSalesRegEntity.setCoId(dto.getCoId());
        mnulCustSalesRegEntity.setShopId(dto.getShopId());
        mnulCustSalesRegEntity.setSteId(dto.getSteId());
        mnulCustSalesRegEntity.setSalesDt(dto.getSalesDt());
        mnulCustSalesRegMapper.callSalesNextProc(mnulCustSalesRegEntity);

        // SAP 취소 I/F
        SearchDto inParams = new SearchDto();
        inParams.setCoId(dto.getCoId());
        inParams.setShopId(dto.getShopId());
        inParams.setSteId(dto.getSteId());
        inParams.setSalesDt(dto.getSalesDt());
        inParams.setUserId(dto.getUserId());
        inParams.setDelYN("Y");
        inParams.setCancelYN("N");
        inParams.setCrtTypeCd("S");
        module.moduleCallEAI(inParams);
        // List<Map<String, String>> rtnData = module.moduleCallEAI(inParams);

        // SAP I/F
        posSalesRegMapper.insertPosGdoh(dto);

        // SearchDto inParams = new SearchDto();
        inParams.setCoId(dto.getCoId());
        inParams.setShopId(dto.getShopId());
        inParams.setSteId(dto.getSteId());
        inParams.setSalesDt(dto.getSalesDt());
        inParams.setUserId(dto.getUserId());
        inParams.setDelYN("N");
        inParams.setCancelYN("N");
        inParams.setCrtTypeCd("S");
        module.moduleCallEAI(inParams);
        // ist<Map<String, String>> rtnData = module.moduleCallEAI(inParams);
      }

      response.setLastRow(0);
      response.setLastPage(0);
      response.setData(compareResultCnt);

      return response;
    } catch (Exception e) {
      throw new PosSalesRegException(e);
    }

  }

  /**
   * 외상 세금계산서 발행 리스트 저장
   *
   * @param List<CrdtCardAckGdohRegDto>
   * @returnsss
   */
  @Transactional
  public void insertOncrdtTaxPblLst(List<PosSalesRegDto> dtoLst) throws PosSalesRegException {
    for (PosSalesRegDto dto : dtoLst) {
      self.insertOncrdtTaxPbl(dto);
    }
  }

  /**
   * 외상 세금계산서 발행 저장
   *
   * @param List<CrdtCardAckGdohRegDto>
   * @returnsss
   */
  @Transactional
  public void insertOncrdtTaxPbl(PosSalesRegDto dto) throws PosSalesRegException {
    posSalesRegMapper.insertOncrdtTaxPbl(dto);
  }

  /**
   * POS매출등록 매출 전송
   *
   * @param PosSalesRegDto
   * @returnsss
   */
  public GridResponse<Integer, Integer, Object> selectPosSapCompareCnt(PosSalesRegDto dto)
      throws PosSalesRegException {
    int compareResultCnt = posSalesRegMapper.selectPosSapCompareCnt(dto);

    GridResponse<Integer, Integer, Object> response = new GridResponse<>();

    response.setLastRow(0);
    response.setLastPage(0);
    response.setData(compareResultCnt);

    return response;

  }


  /**
   * POS 매출 재집계
   *
   * @param PosSalesRegDto
   * @returnsss
   */
  @Transactional
  public Boolean deletePosSalesSum(PosSalesRegDto dto) throws PosSalesRegException {

    try {
      // 기존 집계데이터 삭제 / 트란데이터 미집계 처리
      posSalesRegMapper.deletePosSalesSum(dto);

      return true;
    } catch (Exception e) {
      log.error("[ERROR] deletePosSalesSum 처리 중 에러 발생, ", e);
      throw new PosSalesRegException(e);
    }
  }
}
