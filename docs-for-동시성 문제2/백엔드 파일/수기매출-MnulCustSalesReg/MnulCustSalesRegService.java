package com.cj.freshway.fs.cps.closing.closingmng;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import com.cj.freshway.fs.common.base.GridResponse;
import com.cj.freshway.fs.cps.closing.closingmng.dto.MnulAggCponDcDto;
import com.cj.freshway.fs.cps.closing.closingmng.dto.MnulAggPayCardDto;
import com.cj.freshway.fs.cps.closing.closingmng.dto.MnulAggPayCashDto;
import com.cj.freshway.fs.cps.closing.closingmng.dto.MnulAggPayDto;
import com.cj.freshway.fs.cps.closing.closingmng.dto.MnulAggPayEasyDto;
import com.cj.freshway.fs.cps.closing.closingmng.dto.MnulAggPayOncrdtDto;
import com.cj.freshway.fs.cps.closing.closingmng.dto.MnulAggPrmtDcMenuDto;
import com.cj.freshway.fs.cps.closing.closingmng.dto.MnulAggPrmtDcSubDto;
import com.cj.freshway.fs.cps.closing.closingmng.dto.MnulAggStePosDto;
import com.cj.freshway.fs.cps.closing.closingmng.dto.MnulAggTimeSteDto;
import com.cj.freshway.fs.cps.closing.closingmng.dto.MnulCustSalesRegSearchDto;
import com.cj.freshway.fs.cps.closing.closingmng.dto.MnulTrCardDto;
import com.cj.freshway.fs.cps.closing.closingmng.dto.MnulTrCashDto;
import com.cj.freshway.fs.cps.closing.closingmng.dto.MnulTrHeaderDto;
import com.cj.freshway.fs.cps.closing.closingmng.dto.MnulTrPayDto;
import com.cj.freshway.fs.cps.closing.closingmng.dto.MnulTrTradeAccDto;
import com.cj.freshway.fs.cps.closing.closingmng.entity.MnulCustSalesRegEntity;
import com.cj.freshway.fs.cps.closing.closingmng.exception.MnulCustSalesRegException;
import com.cj.freshway.fs.cps.closing.closingmng.exception.MnulMenuSalesRegException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class MnulCustSalesRegService {
  @Autowired
  MnulCustSalesRegService self;

  @Autowired
  MnulCustSalesRegMapper mnulCustSalesRegMapper;

  @Autowired
  MnulTrRegMapper mnulTrRegMapper;

  @Autowired
  MnulSalesAggRegMapper mnulSalesAggRegMapper;

  @Autowired
  Environment message;

  /**
   * <pre>
   * 수기매출등록 목록 조회
   * </pre>
   *
   * @author Hakmin.Park
   * @param MnulCustSalesRegSearchDto
   * @return
   * @throws MnulCustSalesRegException
   */
  public GridResponse<Integer, Integer, Object> selectMnulCustSalesRegList(
      MnulCustSalesRegSearchDto mnulCustSalesRegSearchDto) throws MnulCustSalesRegException {
    Integer count = 0;
    String monthCloseYnResult = "";
    String dayCloseYnResult = "";

    try {
      List<MnulCustSalesRegEntity> uprcGdrList =
          mnulCustSalesRegMapper.selectMnulCustSalesRegList(mnulCustSalesRegSearchDto);

      if (!ObjectUtils.isEmpty(uprcGdrList)) {
        count = uprcGdrList.size();
      }

      GridResponse<Integer, Integer, Object> response = new GridResponse<>();
      
      monthCloseYnResult = mnulCustSalesRegMapper.selectMnulCustSalesRegMonthCloseYn(mnulCustSalesRegSearchDto);

      dayCloseYnResult = mnulCustSalesRegMapper.selectMnulCustSalesRegDayCloseYn(mnulCustSalesRegSearchDto);

      if ("Y".equalsIgnoreCase(monthCloseYnResult)) {
          response.setLastPage(9);
      } else if (dayCloseYnResult != null && !dayCloseYnResult.isEmpty()) {
        String[] closeYn = dayCloseYnResult.split(",");
        if ("N".equalsIgnoreCase(closeYn[0]) || "1".equalsIgnoreCase(closeYn[1])) {
          response.setLastPage(1);
        } else {
          response.setLastPage(0);
        }
      } else {
        response.setLastPage(0);
      }

      response.setLastRow(count);
      response.setData(uprcGdrList);
      return response;
    } catch (Exception e) {
      throw new MnulCustSalesRegException(e);
    }
  }

  /**
   * <pre>
  * 수기매출등록 등록/수정
   * </pre>
   * 
   * @author Hakmin.Park
   * @return List<mnulCustSalesRegEntity>
   * @throws MnulMenuSalesRegException
   * 
   */
  @Transactional
  public void updateMnulCustSalesRegList(MnulCustSalesRegEntity mnulCustSalesRegEntity)
      throws MnulCustSalesRegException {

    try {
      List<MnulCustSalesRegEntity> mnulCustSalesRegList =
          mnulCustSalesRegEntity.getMnulCustSalesRegList();

      for (MnulCustSalesRegEntity mnulCustSalesReg : mnulCustSalesRegList) {
        String status = mnulCustSalesReg.getStatus();

        if ("M".equals(status)) {
          // 전표번호 채번
          String salesSlipNo = mnulCustSalesRegMapper.selectMnulCustSalesSlipNo(mnulCustSalesReg);
          mnulCustSalesReg.setSalesSlipNo(salesSlipNo);
          // 수기매출 저장
          mnulCustSalesRegMapper.insertMnulCustSalesReg(mnulCustSalesReg);

//          // tr header 정보 설정 / 저장
//          MnulTrHeaderDto mnulTrHeaderDto = setTrHeaderInfo(mnulCustSalesReg, salesSlipNo);
//          mnulTrRegMapper.insertMnulTrHeaderReg(mnulTrHeaderDto);
//          // tr pay 정보 설정 / 저장
//          MnulTrPayDto mnulTrPayDto = setTrPayInfo(mnulCustSalesReg, mnulTrHeaderDto);
//          mnulTrRegMapper.insertMnulTrPayReg(mnulTrPayDto);
//
//          if ("21".equals(mnulCustSalesReg.getSalesBlncClCd())) {
//            // 외상. tr trade acc 정보 설정 / 저장
//            MnulTrTradeAccDto mnulTrTradeAccDto =
//                setTrTradeAccInfo(mnulCustSalesReg, mnulTrHeaderDto);
//            mnulTrRegMapper.insertMnulTrTradeAccReg(mnulTrTradeAccDto);
//          } else if ("22".equals(mnulCustSalesReg.getSalesBlncClCd())) {
//            // 현금. tr cash 정보 설정 / 저장
//            MnulTrCashDto mnulTrCashDto = setTrCashInfo(mnulCustSalesReg, mnulTrHeaderDto);
//            mnulTrRegMapper.insertMnulTrCashReg(mnulTrCashDto);
//          } else if ("23".equals(mnulCustSalesReg.getSalesBlncClCd())) {
//            // 카드. tr card 정보 설정 / 저장
//            MnulTrCardDto mnulTrCardDto = setTrCardInfo(mnulCustSalesReg, mnulTrHeaderDto);
//            mnulTrRegMapper.insertMnulTrCardReg(mnulTrCardDto);
//          }
//
//          // SA_STE_POS_SALES_S 정보 설정 / 저장
//          MnulAggStePosDto mnulAggStePosDto = setMnulAggStePosInfo(mnulCustSalesReg);
//          mnulSalesAggRegMapper.insertSaStePosSalesReg(mnulAggStePosDto);
//          // SA_TIME_STE_SALES_S 정보 설정 / 저장
//          MnulAggTimeSteDto mnulAggTimeSteDto = setMnulAggTimeSteInfo(mnulCustSalesReg);
//          mnulSalesAggRegMapper.insertSaTimeSteSalesReg(mnulAggTimeSteDto);
//          // SA_PAY_SALES_S 정보 설정 / 저장
//          MnulAggPayDto mnulAggPayDto = setMnulAggPayInfo(mnulCustSalesReg);
//          mnulSalesAggRegMapper.insertSaPaySalesReg(mnulAggPayDto);
//
//          if ("21".equals(mnulCustSalesReg.getSalesBlncClCd())) {
//            // 외상.
//            // SA_PAY_SALES_ONCRDT_S 정보 설정 / 저장
//            MnulAggPayOncrdtDto mnulAggPayOncrdtDto = setMnulAggOncrdtInfo(mnulCustSalesReg);
//            mnulSalesAggRegMapper.insertSaPaySalesOncrdtReg(mnulAggPayOncrdtDto);
//          } else if ("22".equals(mnulCustSalesReg.getSalesBlncClCd())) {
//            // 현금.
//            // SA_PAY_SALES_CASH_S 정보 설정 / 저장
//            MnulAggPayCashDto MnulAggPayCashDto = setMnulAggCashInfo(mnulCustSalesReg);
//            mnulSalesAggRegMapper.insertSaPaySalesCashReg(MnulAggPayCashDto);
//          } else if ("23".equals(mnulCustSalesReg.getSalesBlncClCd())) {
//            // 카드.
//            // SA_PAY_SALES_CARD_S 정보 설정 / 저장
//            MnulAggPayCardDto mnulAggPayCardDto = setMnulAggCardInfo(mnulCustSalesReg);
//            mnulSalesAggRegMapper.insertSaPaySalesCardReg(mnulAggPayCardDto);
//          }

          // SA_PAY_SALES_EASY_S 정보 설정 / 저장
          // MnulAggPayEasyDto mnulAggPayEasyDto = setMnulAggPayEasyInfo(mnulCustSalesReg);
          // SA_PRMT_DC_SUB_SALES_S 정보 설정 / 저장
          // MnulAggPrmtDcSubDto MnulAggPrmtDcSubDto = setMnulPrmtDcSubInfo(mnulCustSalesReg);
          // SA_PRMT_DC_MENU_SALES_S 정보 설정 / 저장
          // MnulAggPrmtDcMenuDto MnulAggPrmtDcMenuDto = setMnulPrmtDcMenuInfo(mnulCustSalesReg);
          // SA_CPON_DC_SALES_S 정보 설정 / 저장
          // MnulAggCponDcDto MnulAggCponDcDto = setMnulCponDcInfo(mnulCustSalesReg);
        } else if ("U".equals(status)) {
          mnulCustSalesRegMapper.updateMnulCustSalesReg(mnulCustSalesReg);
        }
      }

    } catch (Exception e) {
      throw new MnulCustSalesRegException(e);
    }
  }

  /**
   * <pre>
   * tr header 정보 설정
   * </pre>
   * 
   * @author sunho.kim
   * @return List<mnulCustSalesRegEntity>
   * @throws MnulCustSalesRegException
   * 
   */
  private MnulTrHeaderDto setTrHeaderInfo(MnulCustSalesRegEntity mnulCustSalesRegEntity,
      String salesSlipNo) throws MnulCustSalesRegException {

    MnulTrHeaderDto mnulTrHeaderDto = new MnulTrHeaderDto();
    mnulTrHeaderDto.setCoId(mnulCustSalesRegEntity.getCoId());
    mnulTrHeaderDto.setShopId(mnulCustSalesRegEntity.getShopId());
    mnulTrHeaderDto.setSteId(mnulCustSalesRegEntity.getSteId());
    mnulTrHeaderDto.setSalesDt(mnulCustSalesRegEntity.getSalesDt());
    mnulTrHeaderDto.setPosNo("9999");
    String tranNo = mnulTrRegMapper.selectMnulTranNo(mnulTrHeaderDto);
    mnulTrHeaderDto.setTranNo(tranNo);
    mnulTrHeaderDto.setSalesSlipNo(salesSlipNo);
    mnulTrHeaderDto.setRegTypeCd("1");
    mnulTrHeaderDto
        .setDealSect(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt()) >= 0 ? "0" : "1");
    mnulTrHeaderDto.setDealType("0");
    mnulTrHeaderDto.setDealMode("01");
    mnulTrHeaderDto
        .setRfndYn(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt()) >= 0 ? "" : "Y");
    mnulTrHeaderDto.setSalesAmt(Math.abs(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt())));
    mnulTrHeaderDto.setDcAmt(0);
    mnulTrHeaderDto
        .setActlSalesAmt(Math.abs(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt())));
    mnulTrHeaderDto.setPrmtDcGoodsAmt(0);
    mnulTrHeaderDto.setPrmtDcSubAmt(0);
    mnulTrHeaderDto.setTruncAmt(0);
    mnulTrHeaderDto.setOrgCcId("");
    mnulTrHeaderDto.setOrgShopId("");
    mnulTrHeaderDto.setOrgSteId("");
    mnulTrHeaderDto.setOrgSaleDt("");
    mnulTrHeaderDto.setOrgTranNo("");
    mnulTrHeaderDto.setRegrId(mnulCustSalesRegEntity.getRegrId());

    return mnulTrHeaderDto;
  }

  /**
   * <pre>
   * tr pay 정보 설정
   * </pre>
   * 
   * @author sunho.kim
   * @return List<mnulCustSalesRegEntity>
   * @throws MnulCustSalesRegException
   * 
   */
  private MnulTrPayDto setTrPayInfo(MnulCustSalesRegEntity mnulCustSalesRegEntity,
      MnulTrHeaderDto mnulTrHeaderDto) throws MnulCustSalesRegException {

    String payCd = null;
    if ("21".equals(mnulCustSalesRegEntity.getSalesBlncClCd())) {
      // 외상
      payCd = "205";
    } else if ("22".equals(mnulCustSalesRegEntity.getSalesBlncClCd())) {
      // 현금
      payCd = "201";
    } else if ("23".equals(mnulCustSalesRegEntity.getSalesBlncClCd())) {
      // 카드
      payCd = "203";
    }

    MnulTrPayDto mnulTrPayDto = new MnulTrPayDto();
    mnulTrPayDto.setCoId(mnulTrHeaderDto.getCoId());
    mnulTrPayDto.setShopId(mnulTrHeaderDto.getShopId());
    mnulTrPayDto.setSteId(mnulTrHeaderDto.getSteId());
    mnulTrPayDto.setSalesDt(mnulTrHeaderDto.getSalesDt());
    mnulTrPayDto.setPosNo(mnulTrHeaderDto.getPosNo());
    mnulTrPayDto.setTranNo(mnulTrHeaderDto.getTranNo());
    mnulTrPayDto.setPaySeq(0);
    mnulTrPayDto.setPayCd(payCd);
    mnulTrPayDto.setSalesAmt(mnulTrHeaderDto.getSalesAmt());
    mnulTrPayDto.setRcvAmt(0);
    mnulTrPayDto.setCashChngAmt(0);
    mnulTrPayDto.setRegrId(mnulCustSalesRegEntity.getRegrId());

    return mnulTrPayDto;
  }

  /**
   * <pre>
   * tr card 정보 설정
   * </pre>
   * 
   * @author sunho.kim
   * @return List<mnulCustSalesRegEntity>
   * @throws MnulCustSalesRegException
   * 
   */
  private MnulTrCardDto setTrCardInfo(MnulCustSalesRegEntity mnulCustSalesRegEntity,
      MnulTrHeaderDto mnulTrHeaderDto) throws MnulCustSalesRegException {

    MnulTrCardDto mnulTrCardDto = new MnulTrCardDto();
    mnulTrCardDto.setCoId(mnulTrHeaderDto.getCoId());
    mnulTrCardDto.setShopId(mnulTrHeaderDto.getShopId());
    mnulTrCardDto.setSteId(mnulTrHeaderDto.getSteId());
    mnulTrCardDto.setSalesDt(mnulTrHeaderDto.getSalesDt());
    mnulTrCardDto.setPosNo(mnulTrHeaderDto.getPosNo());
    mnulTrCardDto.setTranNo(mnulTrHeaderDto.getTranNo());
    mnulTrCardDto.setIdtTypeCd("1");
    mnulTrCardDto.setAppDt(mnulCustSalesRegEntity.getSalesDt());
    mnulTrCardDto.setSaleAmt(Math.abs(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt())));
    if ("0".equals(mnulCustSalesRegEntity.getTaxClCd())) {
      // 면세
      mnulTrCardDto.setTaxbleAmt(0);
    } else {
      // 과세
      mnulTrCardDto
          .setTaxbleAmt(Math.abs(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt())));
    }
    mnulTrCardDto.setVatAmt(Math.abs(Integer.valueOf(mnulCustSalesRegEntity.getVatAmt())));
    if ("0".equals(mnulCustSalesRegEntity.getTaxClCd())) {
      // 면세
      mnulTrCardDto
          .setTaxfreeAmt(Math.abs(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt())));
    } else {
      // 과세
      mnulTrCardDto.setTaxfreeAmt(0);
    }
    mnulTrCardDto.setDcAmt(0);
    mnulTrCardDto.setDcRate(0);
    mnulTrCardDto.setNonDcAmt(0);
    mnulTrCardDto.setUseAmt(0);
    mnulTrCardDto.setRemaindAmt(0);
    mnulTrCardDto.setFlag(null);
    mnulTrCardDto.setUseCnt(0);
    mnulTrCardDto.setLimitSysnc(null);
    mnulTrCardDto.setCjCstId(null);
    mnulTrCardDto.setAllianceFlag(null);
    mnulTrCardDto.getBrandCd();
    mnulTrCardDto.setInstalMonth(null);
    mnulTrCardDto.setTid(mnulCustSalesRegEntity.getMchnNo());
    mnulTrCardDto.setInputTp(null);
    mnulTrCardDto.setIdtCd(mnulCustSalesRegEntity.getCrdtCrdCd());
    mnulTrCardDto.setIdtNm(null);
    mnulTrCardDto.setIdtTp(null);
    mnulTrCardDto.setApprNo(mnulCustSalesRegEntity.getCrdtCrdAckNo());
    mnulTrCardDto.setApprVan(null);
    mnulTrCardDto.setUniqNo(null);
    mnulTrCardDto.setStoreId(mnulCustSalesRegEntity.getCrdcoMbstNo());
    mnulTrCardDto.setCmpCd(null);
    mnulTrCardDto.setCmpNm(null);
    mnulTrCardDto.setPurCorpCd(mnulCustSalesRegEntity.getCrdcoCd());
    mnulTrCardDto.setPurCorpNm(null);
    mnulTrCardDto.setBalanceAmt(null);
    mnulTrCardDto.setSavePoint(null);
    mnulTrCardDto.setUsePoint(null);
    mnulTrCardDto.setTotPoint(null);
    mnulTrCardDto.setOrgBillDt(null);
    mnulTrCardDto.setOrgApprNo(null);
    mnulTrCardDto.setSignFlag(null);
    mnulTrCardDto.setRegrId(mnulCustSalesRegEntity.getRegrId());

    return mnulTrCardDto;
  }

  /**
   * <pre>
   * tr cash 정보 설정
   * </pre>
   * 
   * @author sunho.kim
   * @return List<mnulCustSalesRegEntity>
   * @throws MnulCustSalesRegException
   * 
   */
  private MnulTrCashDto setTrCashInfo(MnulCustSalesRegEntity mnulCustSalesRegEntity,
      MnulTrHeaderDto mnulTrHeaderDto) throws MnulCustSalesRegException {

    MnulTrCashDto mnulTrCashDto = new MnulTrCashDto();
    mnulTrCashDto.setCoId(mnulTrHeaderDto.getCoId());
    mnulTrCashDto.setShopId(mnulTrHeaderDto.getShopId());
    mnulTrCashDto.setSteId(mnulTrHeaderDto.getSteId());
    mnulTrCashDto.setSalesDt(mnulTrHeaderDto.getSalesDt());
    mnulTrCashDto.setPosNo(mnulTrHeaderDto.getPosNo());
    mnulTrCashDto.setTranNo(mnulTrHeaderDto.getTranNo());
    mnulTrCashDto.setPayAmt(Math.abs(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt())));
    mnulTrCashDto.setRcvAmt(0);
    mnulTrCashDto.setChngAmt(0);
    mnulTrCashDto.setRegrId(mnulCustSalesRegEntity.getRegrId());

    return mnulTrCashDto;
  }

  /**
   * <pre>
   * tr 외상 정보 설정
   * </pre>
   * 
   * @author sunho.kim
   * @return List<mnulCustSalesRegEntity>
   * @throws MnulCustSalesRegException
   * 
   */
  private MnulTrTradeAccDto setTrTradeAccInfo(MnulCustSalesRegEntity mnulCustSalesRegEntity,
      MnulTrHeaderDto mnulTrHeaderDto) throws MnulCustSalesRegException {

    MnulTrTradeAccDto mnulTrTradeAccDto = new MnulTrTradeAccDto();
    mnulTrTradeAccDto.setCoId(mnulTrHeaderDto.getCoId());
    mnulTrTradeAccDto.setShopId(mnulTrHeaderDto.getShopId());
    mnulTrTradeAccDto.setSteId(mnulTrHeaderDto.getSteId());
    mnulTrTradeAccDto.setSalesDt(mnulTrHeaderDto.getSalesDt());
    mnulTrTradeAccDto.setPosNo(mnulTrHeaderDto.getPosNo());
    mnulTrTradeAccDto.setTranNo(mnulTrHeaderDto.getTranNo());
    mnulTrTradeAccDto.setPayAmt(Math.abs(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt())));
    mnulTrTradeAccDto.setCustId(mnulCustSalesRegEntity.getCustId());
    mnulTrTradeAccDto.setCredPayTypeCd("00");
    mnulTrTradeAccDto.setRegrId(mnulCustSalesRegEntity.getRegrId());

    return mnulTrTradeAccDto;
  }

  /**
   * <pre>
   * 사이트POS매출집계정보 설정
   * </pre>
   * 
   * @author sunho.kim
   * @return List<mnulCustSalesRegEntity>
   * @throws MnulCustSalesRegException
   * 
   */
  private MnulAggStePosDto setMnulAggStePosInfo(MnulCustSalesRegEntity mnulCustSalesRegEntity)
      throws MnulCustSalesRegException {

    MnulAggStePosDto mnulAggStePosDto = new MnulAggStePosDto();
    mnulAggStePosDto.setCoId(mnulCustSalesRegEntity.getCoId());
    mnulAggStePosDto.setShopId(mnulCustSalesRegEntity.getShopId());
    mnulAggStePosDto.setSteId(mnulCustSalesRegEntity.getSteId());
    mnulAggStePosDto.setSalesDt(mnulCustSalesRegEntity.getSalesDt());
    mnulAggStePosDto.setPosNo("9999");
    mnulAggStePosDto
        .setDealTypeCd(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt()) >= 0 ? "1" : "2");
    mnulAggStePosDto.setRegTypeCd("MNU");
    mnulAggStePosDto.setSalesCnt(1);
    mnulAggStePosDto.setSalesAmt(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt()));
    mnulAggStePosDto.setMenuSalesQty(0);
    mnulAggStePosDto.setDcCnt(0);
    mnulAggStePosDto.setDcAmt(0);
    mnulAggStePosDto.setActlSalesAmt(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt()));
    mnulAggStePosDto.setVatAmt(Integer.valueOf(mnulCustSalesRegEntity.getVatAmt()));
    mnulAggStePosDto.setNetSalesAmt(Integer.valueOf(mnulCustSalesRegEntity.getNetSalesAmt()));
    mnulAggStePosDto.setRegrId(mnulCustSalesRegEntity.getRegrId());

    return mnulAggStePosDto;
  }

  /**
   * <pre>
   * 시간대별사이트매출집계 정보 설정
   * </pre>
   * 
   * @author sunho.kim
   * @return List<mnulCustSalesRegEntity>
   * @throws MnulCustSalesRegException
   * 
   */
  private MnulAggTimeSteDto setMnulAggTimeSteInfo(MnulCustSalesRegEntity mnulCustSalesRegEntity)
      throws MnulCustSalesRegException {

    MnulAggTimeSteDto mnulAggTimeSteDto = new MnulAggTimeSteDto();
    mnulAggTimeSteDto.setCoId(mnulCustSalesRegEntity.getCoId());
    mnulAggTimeSteDto.setShopId(mnulCustSalesRegEntity.getShopId());
    mnulAggTimeSteDto.setSteId(mnulCustSalesRegEntity.getSteId());
    mnulAggTimeSteDto.setSalesDt(mnulCustSalesRegEntity.getSalesDt());
    mnulAggTimeSteDto.setSatmStCd(null);
    mnulAggTimeSteDto
        .setDealTypeCd(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt()) >= 0 ? "1" : "2");
    mnulAggTimeSteDto.setRegTypeCd("MNU");
    mnulAggTimeSteDto.setSalesCnt(1);
    mnulAggTimeSteDto.setSalesAmt(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt()));
    mnulAggTimeSteDto.setDcAmt(0);
    mnulAggTimeSteDto.setActlSalesAmt(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt()));
    mnulAggTimeSteDto.setVatAmt(Integer.valueOf(mnulCustSalesRegEntity.getVatAmt()));
    mnulAggTimeSteDto.setNetSalesAmt(Integer.valueOf(mnulCustSalesRegEntity.getNetSalesAmt()));
    mnulAggTimeSteDto.setRegrId(mnulCustSalesRegEntity.getRegrId());

    return mnulAggTimeSteDto;
  }

  /**
   * <pre>
   * 결제수단별매출집계 정보 설정
   * </pre>
   * 
   * @author sunho.kim
   * @return List<mnulCustSalesRegEntity>
   * @throws MnulCustSalesRegException
   * 
   */
  private MnulAggPayDto setMnulAggPayInfo(MnulCustSalesRegEntity mnulCustSalesRegEntity)
      throws MnulCustSalesRegException {

    String payCd = null;
    if ("21".equals(mnulCustSalesRegEntity.getSalesBlncClCd())) {
      // 외상
      payCd = "205";
    } else if ("22".equals(mnulCustSalesRegEntity.getSalesBlncClCd())) {
      // 현금
      payCd = "201";
    } else if ("23".equals(mnulCustSalesRegEntity.getSalesBlncClCd())) {
      // 카드
      payCd = "203";
    }

    MnulAggPayDto mnulAggPayDto = new MnulAggPayDto();
    mnulAggPayDto.setCoId(mnulCustSalesRegEntity.getCoId());
    mnulAggPayDto.setShopId(mnulCustSalesRegEntity.getShopId());
    mnulAggPayDto.setSteId(mnulCustSalesRegEntity.getSteId());
    mnulAggPayDto.setPosNo("9999");
    mnulAggPayDto.setSalesDt(mnulCustSalesRegEntity.getSalesDt());
    mnulAggPayDto.setPayCd(payCd);
    mnulAggPayDto
        .setApprTypeCd(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt()) >= 0 ? "1" : "2");
    mnulAggPayDto.setRegTypeCd("MNU");
    mnulAggPayDto.setSalesCnt(1);
    mnulAggPayDto.setSalesAmt(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt()));
    mnulAggPayDto.setRegrId(mnulCustSalesRegEntity.getRegrId());

    return mnulAggPayDto;
  }

  /**
   * <pre>
   * 카드사별결제매출집계 정보 설정
   * </pre>
   * 
   * @author sunho.kim
   * @return List<mnulCustSalesRegEntity>
   * @throws MnulCustSalesRegException
   * 
   */
  private MnulAggPayCardDto setMnulAggCardInfo(MnulCustSalesRegEntity mnulCustSalesRegEntity)
      throws MnulCustSalesRegException {

    MnulAggPayCardDto mnulAggPayCardDto = new MnulAggPayCardDto();
    mnulAggPayCardDto.setCoId(mnulCustSalesRegEntity.getCoId());
    mnulAggPayCardDto.setShopId(mnulCustSalesRegEntity.getShopId());
    mnulAggPayCardDto.setSteId(mnulCustSalesRegEntity.getSteId());
    mnulAggPayCardDto.setPosNo("9999");
    mnulAggPayCardDto.setSalesDt(mnulCustSalesRegEntity.getSalesDt());
    mnulAggPayCardDto.setPurCorpCd(mnulCustSalesRegEntity.getCrdcoCd());
    mnulAggPayCardDto
        .setApprTypeCd(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt()) >= 0 ? "1" : "2");
    mnulAggPayCardDto.setRegTypeCd("MNU");
    mnulAggPayCardDto.setSalesCnt(1);
    mnulAggPayCardDto.setSalesAmt(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt()));
    mnulAggPayCardDto.setRegrId(mnulCustSalesRegEntity.getRegrId());

    return mnulAggPayCardDto;
  }

  /**
   * <pre>
   * 현금결제매출집계 정보 설정
   * </pre>
   * 
   * @author sunho.kim
   * @return List<mnulCustSalesRegEntity>
   * @throws MnulCustSalesRegException
   * 
   */
  private MnulAggPayCashDto setMnulAggCashInfo(MnulCustSalesRegEntity mnulCustSalesRegEntity)
      throws MnulCustSalesRegException {

    MnulAggPayCashDto mnulAggPayCashDto = new MnulAggPayCashDto();
    mnulAggPayCashDto.setCoId(mnulCustSalesRegEntity.getCoId());
    mnulAggPayCashDto.setShopId(mnulCustSalesRegEntity.getShopId());
    mnulAggPayCashDto.setSteId(mnulCustSalesRegEntity.getSteId());
    mnulAggPayCashDto.setPosNo("9999");
    mnulAggPayCashDto.setSalesDt(mnulCustSalesRegEntity.getSalesDt());
    mnulAggPayCashDto.setCurTypeCd("KRW");
    mnulAggPayCashDto
        .setApprTypeCd(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt()) >= 0 ? "1" : "2");
    mnulAggPayCashDto.setRegTypeCd("MNU");
    mnulAggPayCashDto.setSalesCnt(1);
    mnulAggPayCashDto.setSalesAmt(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt()));
    mnulAggPayCashDto.setRegrId(mnulCustSalesRegEntity.getRegrId());

    return mnulAggPayCashDto;
  }

  /**
   * <pre>
   * 고객사별외상결제매출집계 정보 설정
   * </pre>
   * 
   * @author sunho.kim
   * @return List<mnulCustSalesRegEntity>
   * @throws MnulCustSalesRegException
   * 
   */
  private MnulAggPayOncrdtDto setMnulAggOncrdtInfo(MnulCustSalesRegEntity mnulCustSalesRegEntity)
      throws MnulCustSalesRegException {

    MnulAggPayOncrdtDto mnulAggPayOncrdtDto = new MnulAggPayOncrdtDto();
    mnulAggPayOncrdtDto.setCoId(mnulCustSalesRegEntity.getCoId());
    mnulAggPayOncrdtDto.setShopId(mnulCustSalesRegEntity.getShopId());
    mnulAggPayOncrdtDto.setSteId(mnulCustSalesRegEntity.getSteId());
    mnulAggPayOncrdtDto.setPosNo("9999");
    mnulAggPayOncrdtDto.setCustId(mnulCustSalesRegEntity.getCustId());
    mnulAggPayOncrdtDto.setSalesDt(mnulCustSalesRegEntity.getSalesDt());
    mnulAggPayOncrdtDto
        .setApprTypeCd(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt()) >= 0 ? "1" : "2");
    mnulAggPayOncrdtDto.setRegTypeCd("MNU");
    mnulAggPayOncrdtDto.setSalesCnt(1);
    mnulAggPayOncrdtDto.setSalesAmt(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt()));
    mnulAggPayOncrdtDto.setRegrId(mnulCustSalesRegEntity.getRegrId());

    return mnulAggPayOncrdtDto;
  }

  /**
   * <pre>
   * 간편결제매출집계 정보 설정
   * </pre>
   * 
   * @author sunho.kim
   * @return List<mnulCustSalesRegEntity>
   * @throws MnulCustSalesRegException
   * 
   */
  private MnulAggPayEasyDto setMnulAggPayEasyInfo(MnulCustSalesRegEntity mnulCustSalesRegEntity)
      throws MnulCustSalesRegException {

    MnulAggPayEasyDto mnulAggPayEasyDto = new MnulAggPayEasyDto();
    mnulAggPayEasyDto.setCoId(mnulCustSalesRegEntity.getCoId());
    mnulAggPayEasyDto.setShopId(mnulCustSalesRegEntity.getShopId());
    mnulAggPayEasyDto.setSteId(mnulCustSalesRegEntity.getSteId());
    mnulAggPayEasyDto.setPosNo("9999");
    mnulAggPayEasyDto.setEasyPayTypeCd(null);
    mnulAggPayEasyDto.setSalesDt(mnulCustSalesRegEntity.getSalesDt());
    mnulAggPayEasyDto
        .setApprTypeCd(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt()) >= 0 ? "1" : "2");
    mnulAggPayEasyDto.setRegTypeCd("MNU");
    mnulAggPayEasyDto.setSalesCnt(1);
    mnulAggPayEasyDto.setSalesAmt(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt()));
    mnulAggPayEasyDto.setRegrId(mnulCustSalesRegEntity.getRegrId());

    return mnulAggPayEasyDto;
  }

  /**
   * <pre>
   * 소계할인행사별매출집계 정보 설정
   * </pre>
   * 
   * @author sunho.kim
   * @return List<mnulCustSalesRegEntity>
   * @throws MnulCustSalesRegException
   * 
   */
  private MnulAggPrmtDcSubDto setMnulPrmtDcSubInfo(MnulCustSalesRegEntity mnulCustSalesRegEntity)
      throws MnulCustSalesRegException {

    MnulAggPrmtDcSubDto mnulAggPrmtDcSubDto = new MnulAggPrmtDcSubDto();
    mnulAggPrmtDcSubDto.setCoId(mnulCustSalesRegEntity.getCoId());
    mnulAggPrmtDcSubDto.setShopId(mnulCustSalesRegEntity.getShopId());
    mnulAggPrmtDcSubDto.setSteId(mnulCustSalesRegEntity.getSteId());
    mnulAggPrmtDcSubDto.setPosNo("9999");
    mnulAggPrmtDcSubDto.setPrmtId(null);
    mnulAggPrmtDcSubDto.setSalesDt(mnulCustSalesRegEntity.getSalesDt());
    mnulAggPrmtDcSubDto
        .setDealTypeCd(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt()) >= 0 ? "1" : "2");
    mnulAggPrmtDcSubDto.setRegTypeCd("MNU");
    mnulAggPrmtDcSubDto.setDcCnt(0);
    mnulAggPrmtDcSubDto.setDcAmt(0);
    mnulAggPrmtDcSubDto.setSalesAmt(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt()));
    mnulAggPrmtDcSubDto.setActlSalesAmt(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt()));
    mnulAggPrmtDcSubDto.setRegrId(mnulCustSalesRegEntity.getRegrId());

    return mnulAggPrmtDcSubDto;
  }

  /**
   * <pre>
   * 상품할인행사별매출집계 정보 설정
   * </pre>
   * 
   * @author sunho.kim
   * @return List<mnulCustSalesRegEntity>
   * @throws MnulCustSalesRegException
   * 
   */
  private MnulAggPrmtDcMenuDto setMnulPrmtDcMenuInfo(MnulCustSalesRegEntity mnulCustSalesRegEntity)
      throws MnulCustSalesRegException {

    MnulAggPrmtDcMenuDto mnulAggPrmtDcMenuDto = new MnulAggPrmtDcMenuDto();
    mnulAggPrmtDcMenuDto.setCoId(mnulCustSalesRegEntity.getCoId());
    mnulAggPrmtDcMenuDto.setShopId(mnulCustSalesRegEntity.getShopId());
    mnulAggPrmtDcMenuDto.setSteId(mnulCustSalesRegEntity.getSteId());
    mnulAggPrmtDcMenuDto.setPosNo("9999");
    mnulAggPrmtDcMenuDto.setPrmtId(null);
    mnulAggPrmtDcMenuDto.setSalesDt(mnulCustSalesRegEntity.getSalesDt());
    mnulAggPrmtDcMenuDto
        .setDealTypeCd(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt()) >= 0 ? "1" : "2");
    mnulAggPrmtDcMenuDto.setRegTypeCd("MNU");
    mnulAggPrmtDcMenuDto.setDcCnt(0);
    mnulAggPrmtDcMenuDto.setDcAmt(0);
    mnulAggPrmtDcMenuDto.setSalesAmt(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt()));
    mnulAggPrmtDcMenuDto.setActlSalesAmt(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt()));
    mnulAggPrmtDcMenuDto.setRegrId(mnulCustSalesRegEntity.getRegrId());

    return mnulAggPrmtDcMenuDto;
  }

  /**
   * <pre>
   * 쿠폰할인매출집계 정보 설정
   * </pre>
   * 
   * @author sunho.kim
   * @return List<mnulCustSalesRegEntity>
   * @throws MnulCustSalesRegException
   * 
   */
  private MnulAggCponDcDto setMnulCponDcInfo(MnulCustSalesRegEntity mnulCustSalesRegEntity)
      throws MnulCustSalesRegException {

    MnulAggCponDcDto mnulAggCponDcDto = new MnulAggCponDcDto();
    mnulAggCponDcDto.setCoId(mnulCustSalesRegEntity.getCoId());
    mnulAggCponDcDto.setShopId(mnulCustSalesRegEntity.getShopId());
    mnulAggCponDcDto.setSteId(mnulCustSalesRegEntity.getSteId());
    mnulAggCponDcDto.setPosNo("9999");
    mnulAggCponDcDto.setSalesDt(mnulCustSalesRegEntity.getSalesDt());
    mnulAggCponDcDto
        .setDealTypeCd(Integer.valueOf(mnulCustSalesRegEntity.getTotSalesAmt()) >= 0 ? "1" : "2");
    mnulAggCponDcDto.setRegTypeCd("MNU");
    mnulAggCponDcDto.setDcCnt(0);
    mnulAggCponDcDto.setDcAmt(0);
    mnulAggCponDcDto.setRegrId(mnulCustSalesRegEntity.getRegrId());

    return mnulAggCponDcDto;
  }

  /**
   * <pre>
  * 수기매출등록 매출확정
   * </pre>
   * 
   * @author Hakmin.Park
   * @return List<mnulCustSalesRegEntity>
   * @throws MnulCustSalesRegException
   * 
   */
  @Transactional
  public void confirmMnulCustSalesRegList(MnulCustSalesRegEntity mnulCustSalesRegEntity)
      throws MnulCustSalesRegException {

    try {
      List<MnulCustSalesRegEntity> mnulCustSalesRegList =
          mnulCustSalesRegEntity.getMnulCustSalesRegList();

      for (MnulCustSalesRegEntity mnulCustSalesReg : mnulCustSalesRegList) {
        mnulCustSalesRegMapper.confirmMnulCustSalesReg(mnulCustSalesReg);
      }
    } catch (Exception e) {
      throw new MnulCustSalesRegException(e);
    }
  }

  /**
   * <pre>
  * 수기매출등록 매출취소
   * </pre>
   * 
   * @author Hakmin.Park
   * @return List<mnulCustSalesRegEntity>
   * @throws MnulMenuSalesRegException
   * 
   */
  @Transactional
  public void cancelMnulCustSalesRegList(MnulCustSalesRegEntity mnulCustSalesRegEntity)
      throws MnulCustSalesRegException {

    try {
      List<MnulCustSalesRegEntity> mnulCustSalesRegList =
          mnulCustSalesRegEntity.getMnulCustSalesRegList();

      for (MnulCustSalesRegEntity mnulCustSalesReg : mnulCustSalesRegList) {
        mnulCustSalesRegMapper.cancelMnulCustSalesReg(mnulCustSalesReg);
      }
    } catch (Exception e) {
      throw new MnulCustSalesRegException(e);
    }
  }

}
