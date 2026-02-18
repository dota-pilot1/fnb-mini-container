/**
 *
 */
package com.cj.freshway.fs.cps.common.interfacesap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.axis2.client.Options;
import org.apache.axis2.kernel.http.HTTPConstants;
import org.apache.axis2.transport.http.impl.httpclient4.HttpTransportPropertiesImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cj.freshway.fs.common.base.BaseException;
import com.cj.freshway.fs.cps.closing.closingmng.dto.ClosingAdjDto;
import com.cj.freshway.fs.cps.common.interfacesap.SI_COM0280_FS_SOServiceStub.DT_COM0280_FS;
import com.cj.freshway.fs.cps.common.interfacesap.SI_COM0280_FS_SOServiceStub.DT_COM0280_FS_response;
import com.cj.freshway.fs.cps.common.interfacesap.SI_COM0280_FS_SOServiceStub.IS_PARAM_RET_type0;
import com.cj.freshway.fs.cps.common.interfacesap.SI_COM0280_FS_SOServiceStub.IS_PARAM_type0;
import com.cj.freshway.fs.cps.common.interfacesap.SI_COM0280_FS_SOServiceStub.MT_COM0280_FS;
import com.cj.freshway.fs.cps.common.interfacesap.SI_COM0280_FS_SOServiceStub.MT_COM0280_FS_response;
import com.cj.freshway.fs.cps.common.interfacesap.dto.GdohResultDto;
import com.cj.freshway.fs.cps.common.interfacesap.dto.SapStatusDto;
import com.cj.freshway.fs.cps.common.interfacesap.dto.SearchDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SI_COM0280_FS_ClientSynchronousModule {
  private static final Log LOG = LogFactory.getLog(SI_COM0280_FS_ClientSynchronousModule.class);
  private static String sapAxis2Domain;
  private static String sapAxis2Sender;
  private static String sapAxis2Id;
  private static String sapAxis2Pw;

  @Autowired
  Com0280Service com0280Service;

  @Value("${sap.axis2.domain}")
  private void setSapAxis2Domain(String domain) {
    sapAxis2Domain = domain;
  }

  @Value("${sap.axis2.sender}")
  private void setSapAxis2Sender(String sender) {
    sapAxis2Sender = sender;
  }

  @Value("${sap.axis2.id}")
  private void setSapAxis2Id(String id) {
    sapAxis2Id = id;
  }

  @Value("${sap.axis2.pw}")
  private void setSapAxis2Pw(String pw) {
    sapAxis2Pw = pw;
  }

  /**
   * @interface COM0280 FS-254 - FI-107
   * @interfaceName 매출시재정보
   * @interfaceInfo 1. 신용카드 승인 연결 정보) 전송 2. 선수금 입금정보
   * @programID S-COM-FS-H-218
   * @author
   * @version 1.0
   * @throws BaseException
   * @since
   */
  @Transactional
  public List<Map<String, String>> moduleCallEAI(SearchDto inParams) throws BaseException {

    // IF returnDATA SET
    List<Map<String, String>> rtnListData = new ArrayList();
    RtnDataSet rtnData = null;

    // TODO Auto-generated method stub
    SI_COM0280_FS_SOServiceStub stub = null;

    String today = null;
    String time = null;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");

    Calendar currentDate = Calendar.getInstance();
    today = dateFormat.format(currentDate.getTime());
    time = timeFormat.format(currentDate.getTime());

    try {

      LOG.debug("=================================SI_COM0280_FS_ClientSynchronousModule START");

      // InterfaceID 설정 : WSDL 참고
      String interfaceId = "SI_COM0280_FS_SO";
      // interfaceNamespace 설정 : WSDL 참고
      String interfaceNamespace = "urn%3A%2F%2Fcom.fs.cjfreshway.co.kr%2FFI";
      // endpointUrl : 고정으로 아래 값을 사용
      String endpointUrl =
          sapAxis2Domain + "/XISOAPAdapter/MessageServlet?senderParty=&senderService=COM_"
              + sapAxis2Sender + "&receiverParty=&receiverService=&interface=" + interfaceId
              + "&interfaceNamespace=" + interfaceNamespace;

      // org.apache.axis2.AxisFault: Server Error 시 public SI_COMXXXX_FS_SOServiceStub()에 this와 비교
      LOG.info("endpointUrl:" + endpointUrl);
      stub = new SI_COM0280_FS_SOServiceStub(endpointUrl);

      // basic authentication configuration
      HttpTransportPropertiesImpl.Authenticator auth =
          new HttpTransportPropertiesImpl.Authenticator();
      auth.setUsername(sapAxis2Id);
      auth.setPassword(sapAxis2Pw);

      Options options = stub._getServiceClient().getOptions();
      options.setProperty(HTTPConstants.AUTHENTICATE, auth);
      options.setProperty(HTTPConstants.CHUNKED, false);
      // timeout setting, 5,000 ms = 5 second
      options.setProperty(HTTPConstants.SO_TIMEOUT, 300000);

      Map paramMap = new HashMap();

      paramMap.put("CO_ID", inParams.getCoId());
      paramMap.put("SHP_ID", inParams.getShopId());
      paramMap.put("PROC_YMD", inParams.getSalesDt());
      paramMap.put("USER_ID", inParams.getUserId());

      // 회사 ID
      String CO_ID = "";
      // 전표 전기 일자
      String SLIP_WACC_YMD = "";
      // 저장 위치 코드
      String STRG_LOC_CD = "";
      // 점포 ID
      String SHP_ID = "";
      // 사이트 ID
      String KUNNR = "";
      // 단말기 ID
      String IF_NO = "";
      // 일련번호
      String SEQ = "";
      // 생성/변경/삭제
      String CRET_CHG_DEL = "";
      // 전기 코드
      String WACC_CD = "";
      // 고객 번호
      String CUST_NO = "";
      // 카드사 코드
      String CRDCO_CD = "";
      // 통화 코드
      String CRRC_CD = "";
      // 현지 통화 금액
      String LCL_CRRC_AT = "";
      // 카드 승인 번호
      String CRD_ACK_NO = "";
      // 품목 텍스트
      String ITM_TXT = "";
      // 가맹점 번호
      String MBST_NO = "";
      // 신용카드 사용시간
      String CRDT_CRD_US_TM = "";
      // Interface ID
      String IF_ID = "";
      // 사번 8자리
      String EMP_NO = "";
      // 시스템 사용자 정보
      String SYS_USR_INFO = "";
      // TEXT1
      String TXT1 = "";
      // TEXT2
      String TXT2 = "";
      // TEXT3
      String TXT3 = "";
      // TEXT4
      String TXT4 = "";
      // TEXT5
      String TXT5 = "";
      // 신용카드 승인 일자
      String TR_CRDT_CRD_US_YMD = "";
      // 신용카드 승인 유형 코드
      String TR_DIV_CD = "";
      // 신용카드 번호
      String FI_CRDT_CRD_NO = "";
      // 구매처 ID
      String PURCO_ID = "";
      // 거래 은행 유형 코드
      String TRD_BNK_CL_CD = "";

      List<GdohResultDto> resultList = null;

      if ("Y".equals(inParams.getDelYN())) {
    	  resultList = com0280Service.selectGdohDelList(inParams);
      } else if ("Y".equals(inParams.getCancelYN())) {
    	  resultList = com0280Service.selectGdohCnclList(inParams);
      } else {
    	  resultList = com0280Service.selectGdohList(inParams);
      }

      IS_PARAM_type0[] send_param1 = new IS_PARAM_type0[resultList.size()];

      for (int index = 0; index < send_param1.length; index++) {
    	GdohResultDto rsltDto = resultList.get(index);
    	ObjectMapper objectMapper = new ObjectMapper();
    	Map<String, Object> map = objectMapper.convertValue(rsltDto, Map.class);

        // 회사 ID
        CO_ID = map.get("coId") == null ? "" : String.valueOf(map.get("coId"));
        // 전표전기일자
        SLIP_WACC_YMD =
            map.get("slipWaccYmd") == null ? "" : String.valueOf(map.get("slipWaccYmd"));
        // 저장위치코드
        STRG_LOC_CD = map.get("strgLocCd") == null ? "" : String.valueOf(map.get("strgLocCd"));
        // 점포 ID
        SHP_ID = map.get("shpId") == null ? "" : String.valueOf(map.get("shpId"));
        // 사이트 ID
        //KUNNR = map.get("kunnr") == null ? "" : String.valueOf(map.get("kunnr"));
        // 단말기 ID
        IF_NO = map.get("mchnId") == null ? "" : String.valueOf(map.get("mchnId"));

        // 일련번호
        SEQ = map.get("seq") == null ? "" : String.valueOf(map.get("gdohSlipNo"));
        // 생성/변경/삭제
        CRET_CHG_DEL =
            map.get("cretChgDel") == null ? "C" : String.valueOf(map.get("cretChgDel"));
        // 전기 코드
        WACC_CD = map.get("waccCd") == null ? "" : String.valueOf(map.get("waccCd"));
        // 고객 번호
        CUST_NO = map.get("custNo") == null ? "" : String.valueOf(map.get("custNo"));
        // 카드사 코드
        CRDCO_CD = map.get("crdcoCd") == null ? "" : String.valueOf(map.get("crdcoCd"));
        // 통화 코드
        CRRC_CD = map.get("crrcCd") == null ? "" : String.valueOf(map.get("crrcCd"));
        // 현지통화금액
        LCL_CRRC_AT = map.get("lclCrrcAt") == null ? "" : String.valueOf(map.get("lclCrrcAt"));
        // 카드승인번호
        CRD_ACK_NO = map.get("crdAckNo") == null ? "" : String.valueOf(map.get("crdAckNo"));
        // 품목 텍스트
        ITM_TXT = map.get("itmTxt") == null ? "" : String.valueOf(map.get("itmTxt"));
        // 가맹점 번호
        MBST_NO = map.get("mbstNo") == null ? "" : String.valueOf(map.get("mbstNo"));
        // 신용카드 사용시간
        CRDT_CRD_US_TM =
            map.get("crdtCrdUsTm") == null ? "" : String.valueOf(map.get("crdtCrdUsTm"));
        // Interface ID
        // IF_ID = map.get("IF_ID" ) == null ? "" : String.valueOf(map.get("IF_ID" ));
        // 사번 8자리
        EMP_NO = map.get("empNo") == null ? "" : String.valueOf(map.get("empNo"));
        // 시스템 사용자 정보
        SYS_USR_INFO =
            map.get("sysUsrInfo") == null ? "" : String.valueOf(map.get("sysUsrInfo"));
        // TEXT1
        TXT1 = map.get("txt1") == null ? "" : String.valueOf(map.get("txt1"));
        TXT2 = map.get("txt2") == null ? "" : String.valueOf(map.get("txt2"));
        TXT3 = map.get("txt3") == null ? "" : String.valueOf(map.get("txt3"));
        TXT4 = map.get("txt4") == null ? "" : String.valueOf(map.get("txt4"));
        TXT5 = map.get("txt5") == null ? "" : String.valueOf(map.get("txt5"));
        // 신용카드승인일자
        TR_CRDT_CRD_US_YMD =
            map.get("trCrdtCrdUsYmd") == null ? "" : String.valueOf(map.get("trCrdtCrdUsYmd"));
        // 신용카드승인유형코드
        TR_DIV_CD =
            map.get("trDivCd") == null ? "" : String.valueOf(map.get("trDivCd"));
        // 신용카드번호
        FI_CRDT_CRD_NO = map.get("crdNo") == null ? "" : String.valueOf(map.get("crdNo"));
        // 구매처ID
        PURCO_ID = map.get("purcoId") == null ? "" : String.valueOf(map.get("purcoId"));
        // 거래은행유형코드
        TRD_BNK_CL_CD =
            map.get("trdBnkClCd") == null ? "" : String.valueOf(map.get("trdBnkClCd"));

        send_param1[index] = new IS_PARAM_type0();
        // 회사 ID
        send_param1[index].setCO_ID(StringUtils.defaultString(CO_ID, ""));
        // 전표전기일자
        send_param1[index].setSLIP_WACC_YMD(StringUtils.defaultString(SLIP_WACC_YMD, ""));
        // 저장위치코드
        send_param1[index].setSTRG_LOC_CD(StringUtils.defaultString(STRG_LOC_CD, ""));
        // 점포 ID
        send_param1[index].setSHP_ID(StringUtils.defaultString(SHP_ID, ""));
        // 사이트 ID
        //send_param1[index].setKUNNR(StringUtils.defaultString(KUNNR, ""));
        // 단말기 ID
        send_param1[index].setIF_NO(StringUtils.defaultString(IF_NO, ""));
        // 일련번호
        send_param1[index].setSEQ(StringUtils.defaultString(SEQ, ""));
        // 생성/변경/삭제
        send_param1[index].setCRET_CHG_DEL(StringUtils.defaultString(CRET_CHG_DEL, ""));
        // 전기 코드
        send_param1[index].setWACC_CD(StringUtils.defaultString(WACC_CD, ""));
        // 고객 번호
        send_param1[index].setCUST_NO(StringUtils.defaultString(CUST_NO, ""));
        // 카드사 코드
        send_param1[index].setCRDCO_CD(StringUtils.defaultString(CRDCO_CD, ""));
        // 통화 코드
        send_param1[index].setCRRC_CD(StringUtils.defaultString(CRRC_CD, ""));
        // 현지 통화 금액
        send_param1[index].setLCL_CRRC_AT(StringUtils.defaultString(LCL_CRRC_AT, ""));
        // 카드 승인 번호
        send_param1[index].setCRD_ACK_NO(StringUtils.defaultString(CRD_ACK_NO, ""));
        // 품목 텍스트
        send_param1[index].setITM_TXT(StringUtils.defaultString(ITM_TXT, ""));
        // 가맹점 번호
        send_param1[index].setMBST_NO(StringUtils.defaultString(MBST_NO, ""));
        // 신용카드사용시간
        send_param1[index].setCRDT_CRD_US_TM(StringUtils.defaultString(CRDT_CRD_US_TM, ""));
        // Interface ID
        send_param1[index].setIF_ID(StringUtils.defaultString(IF_ID, ""));
        // 사번 8자리
        send_param1[index].setEMP_NO(StringUtils.defaultString(EMP_NO, ""));
        // 시스템사용자정보
        send_param1[index].setSYS_USR_INFO(StringUtils.defaultString(SYS_USR_INFO, ""));
        // TEXT1
        send_param1[index].setTXT1(StringUtils.defaultString(TXT1, ""));
        // TEXT2
        send_param1[index].setTXT2(StringUtils.defaultString(TXT2, ""));
        // TEXT3
        send_param1[index].setTXT3(StringUtils.defaultString(TXT3, ""));
        // TEXT4
        send_param1[index].setTXT4(StringUtils.defaultString(TXT4, ""));
        // TEXT5
        send_param1[index].setTXT5(StringUtils.defaultString(TXT5, ""));
        // 신용카드승인일자
        send_param1[index].setTR_CRDT_CRD_US_YMD(StringUtils.defaultString(TR_CRDT_CRD_US_YMD, ""));
        // 신용카드 승인 유형 코드
        send_param1[index].setTR_DIV_CD(StringUtils.defaultString(TR_DIV_CD, ""));
        // 신용카드번호
        send_param1[index].setFI_CRDT_CRD_NO(StringUtils.defaultString(FI_CRDT_CRD_NO, ""));
        // 구매처ID
        send_param1[index].setPURCO_ID(StringUtils.defaultString(PURCO_ID, ""));
        // 거래은행유형코드
        send_param1[index].setTRD_BNK_CL_CD(StringUtils.defaultString(TRD_BNK_CL_CD, ""));


        LOG.debug(" For Seq =" + index + "\n" + " 일자 ===> " + today + "\n" + " 시간 ===> " + time
            + "\n"  + " getCO_ID              회사 ID        		[" + send_param1[index].getCO_ID()
            + "]\n" + " getSLIP_WACC_YMD      전표 전기 일자     	[" + send_param1[index].getSLIP_WACC_YMD()
            + "]\n" + " getSTRG_LOC_CD        저장 위치 코드     	[" + send_param1[index].getSTRG_LOC_CD()
            + "]\n" + " getSHP_ID             점포 ID         	[" + send_param1[index].getSHP_ID()
            + "]\n" + " getIF_NO              단말기 ID          	[" + send_param1[index].getIF_NO()
            + "]\n" + " getSEQ                일련번호          	[" + send_param1[index].getSEQ()
            + "]\n" + " getCRET_CHG_DEL       생성/변경/삭제     	[" + send_param1[index].getCRET_CHG_DEL()
            + "]\n" + " getWACC_CD            전기 코드          	[" + send_param1[index].getWACC_CD()
            + "]\n" + " getCUST_NO            사이트 ID          	[" + send_param1[index].getCUST_NO()
            + "]\n" + " getCRDCO_CD           카드사 코드        	[" + send_param1[index].getCRDCO_CD()
            + "]\n" + " getCRRC_CD            통화 코드          	[" + send_param1[index].getCRRC_CD()
            + "]\n" + " getLCL_CRRC_AT        현지 통화 금액     	[" + send_param1[index].getLCL_CRRC_AT()
            + "]\n" + " getCRD_ACK_NO         카드 승인 번호     	[" + send_param1[index].getCRD_ACK_NO()
            + "]\n" + " getITM_TXT            품목 텍스트        	[" + send_param1[index].getITM_TXT()
            + "]\n" + " getMBST_NO            가맹점 번호        	[" + send_param1[index].getMBST_NO()
            + "]\n" + " getCRDT_CRD_US_TM     신용카드 사용시간      [" + send_param1[index].getCRDT_CRD_US_TM()
            + "]\n" + " getIF_ID              Interface ID      [" + send_param1[index].getIF_ID()
            + "]\n" + " getEMP_NO             사번 8자리         	[" + send_param1[index].getEMP_NO()
            + "]\n" + " getSYS_USR_INFO       시스템 사용자 정보 	    [" + send_param1[index].getSYS_USR_INFO()
            + "]\n" + " getTXT1               TEXT1        		[" + send_param1[index].getTXT1()
            + "]\n" + " getTXT2               TEXT2        		[" + send_param1[index].getTXT2()
            + "]\n" + " getTXT3               TEXT3        		[" + send_param1[index].getTXT3()
            + "]\n" + " getTXT4               TEXT4        		[" + send_param1[index].getTXT4()
            + "]\n" + " getTXT5               TEXT5        		[" + send_param1[index].getTXT5()
            + "]\n" + " getFI_CRDT_CRD_NO     FI_CRDT_CRD_NO   	[" + send_param1[index].getFI_CRDT_CRD_NO()
            + "]\n" + " getTR_CRDT_CRD_US_YMD TR_CRDT_CRD_US_YMD[" + send_param1[index].getTR_CRDT_CRD_US_YMD()
            + "]\n" + " getTR_DIV_CD          TR_DIV_CD        	[" + send_param1[index].getTR_DIV_CD()
            + "]\n" + " getPURCO_ID PURCO_ID                    [" + send_param1[index].getPURCO_ID()
            + "]\n" + " getTRD_BNK_CL_CD TRD_BNK_CL_CD          [" + send_param1[index].getTRD_BNK_CL_CD() + "]\n");
      }

      LOG.debug("send_param1.length : " + send_param1.length);

      // EAI 최대 건수 제한
      if (send_param1 != null) {
        // EAI에서 지정한 최대 건수
        if (send_param1.length > 10179) {
        	throw new BaseException("외부 시스템 통신 최대 처리 건수를 초과 하였습니다.");
        }
      } else {
    	  throw new BaseException("외부 시스템 전송 내역이 없습니다.");
      }

      if (send_param1.length > 0) {
        DT_COM0280_FS dataType = new DT_COM0280_FS();
        dataType.setXROWS(String.valueOf(send_param1.length));
        dataType.setXSYS("FI");
        dataType.setXDATS(today);
        dataType.setXTIMS(time);
        dataType.setIS_PARAM(send_param1);

        MT_COM0280_FS msgType = new MT_COM0280_FS();
        msgType.setMT_COM0280_FS(dataType);
        MT_COM0280_FS_response response = stub.sI_COM0280_FS_SO(msgType);

        DT_COM0280_FS_response responseData = new DT_COM0280_FS_response();
        responseData = response.getMT_COM0280_FS_response();

        IS_PARAM_RET_type0[] t_param_type = responseData.getIS_PARAM_RET();

        int successCnt = 0;
        int failCnt = 0;

        if (t_param_type == null) {
          LOG.debug("========================================= RETURN VAL NULL");
        }

        if (t_param_type != null) {
          // if(!"E".equals(t_param_type[0].getXSTAT())){
          for (int i = 0; i < t_param_type.length; i++) {
            LOG.debug(" For Seq =" + i + "\n" + " 일자 ===> " + today + "\n" + " 시간 ===> " + time
                + "\n"  + " IF_ID            IF 처리 번호         	[" + t_param_type[i].getIF_ID()
                + "]\n" + " SEQ              SEQ 번호            	[" + t_param_type[i].getSEQ()
                + "]\n" + " ACCT_SLIP_NO     회계 전표 번호        	[" + t_param_type[i].getACCT_SLIP_NO()
                + "]\n" + " IVS_JONL_SLIP_NO 역 분개 전표 번호      	[" + t_param_type[i].getIVS_JONL_SLIP_NO()
                + "]\n" + " XSTAT            EAI 처리 결과        	[" + t_param_type[i].getXSTAT()
                + "]\n" + " XMSGS            에러 메세지           	[" + t_param_type[i].getXMSGS()
                + "]\n");

            String STAT = t_param_type[i].getXSTAT();
            String ACCT_SLIP_NO = t_param_type[i].getACCT_SLIP_NO();
            String IVS_JONL_SLIP_NO = t_param_type[i].getIVS_JONL_SLIP_NO();
            CRET_CHG_DEL = send_param1[i].getCRET_CHG_DEL();

            paramMap.put("CO_ID", inParams.getCoId());
            paramMap.put("SHP_ID", inParams.getShopId());
            paramMap.put("SHP_ID", inParams.getSteId());
            paramMap.put("SLIP_WACC_YMD", t_param_type[i].getSLIP_WACC_YMD());
            paramMap.put("GDOH_SLIP_NO", t_param_type[i].getSEQ());
            paramMap.put("IF_ID", t_param_type[i].getIF_ID());
            paramMap.put("ACCT_SLIP_NO", ACCT_SLIP_NO);
            paramMap.put("IVS_JONL_SLIP_NO", IVS_JONL_SLIP_NO);
            paramMap.put("RTN_MSG", t_param_type[i].getXMSGS());

            // 이미 처리된 데이터 입니다. 오류시 전송 성공 처리
            if (STAT.equals("E")) {
              if (CRET_CHG_DEL != null && CRET_CHG_DEL.equals("D")) {
                if (IVS_JONL_SLIP_NO != null && !IVS_JONL_SLIP_NO.equals("")) {
                  STAT = "S";
                }
              } else {
                if (ACCT_SLIP_NO != null && !ACCT_SLIP_NO.equals("")) {
                  STAT = "S";
                }
              }
            }

            if (STAT.equals("S")) {   //전송성공일 경우
            	SapStatusDto instDto = new SapStatusDto();
            	instDto.setCoId(send_param1[0].getCO_ID());
            	instDto.setShopId(send_param1[0].getSHP_ID());
            	instDto.setOccrDt(send_param1[0].getSLIP_WACC_YMD());
            	instDto.setGdohSlipNo(t_param_type[i].getSEQ());
            	instDto.setSalesGdohWaccCd(send_param1[i].getWACC_CD());
            	instDto.setSapSalesGdohSlipNo(t_param_type[i].getACCT_SLIP_NO());
            	instDto.setIvsJonlSlipNo(IVS_JONL_SLIP_NO);
            	instDto.setIfId(t_param_type[i].getIF_ID());
            	instDto.setSapErrTxt(t_param_type[i].getXMSGS());
            	instDto.setSapSndSttsCd(STAT);
            	instDto.setRegrId(inParams.getUserId());
            	instDto.setDelYn(inParams.getDelYN());
            	instDto.setSapRtnMng(t_param_type[i].getXMSGS());

            	com0280Service.updateGdohStatus(instDto);

            	if ("N".equals(inParams.getDelYN())) {
            		//com0280Service.deleteTCBOM002(instDto);
            		com0280Service.updateSuccess(instDto);
                    String slipNo = com0280Service.getTcbom002No(inParams);
                    instDto.setGdohSlipNo(slipNo);
            		com0280Service.insertTCBOM002(instDto);
            	} else {
            		//com0280Service.deleteTCBOM002(instDto);
            		com0280Service.updateSuccess(instDto);
            	}

            	successCnt++;
            } else {
            	Map<String, String> m_returnData = new HashMap<String, String>();
                m_returnData.put("XMSGS", t_param_type[0].getXMSGS()); // 에러메시지
                rtnListData.add(m_returnData);

            	SapStatusDto instDto = new SapStatusDto();
            	instDto.setCoId(send_param1[0].getCO_ID());
            	instDto.setShopId(send_param1[0].getSHP_ID());
            	instDto.setOccrDt(send_param1[0].getSLIP_WACC_YMD());
            	instDto.setGdohSlipNo(t_param_type[i].getSEQ());
            	instDto.setSalesGdohWaccCd(send_param1[i].getWACC_CD());
            	instDto.setSapSalesGdohSlipNo("");
            	instDto.setIvsJonlSlipNo(IVS_JONL_SLIP_NO);
            	instDto.setIfId(t_param_type[i].getIF_ID());
            	instDto.setSapErrTxt(t_param_type[i].getXMSGS());
            	instDto.setSapSndSttsCd(STAT);
            	instDto.setRegrId(inParams.getUserId());
            	instDto.setDelYn(inParams.getDelYN());
            	instDto.setSapRtnMng(t_param_type[i].getXMSGS());

            	com0280Service.updateGdohStatus(instDto);

            	if ("N".equals(inParams.getDelYN())) {
            		//com0280Service.deleteTCBOM002(instDto);
            		com0280Service.updateFail(instDto);
                    String slipNo = com0280Service.getTcbom002No(inParams);
                    instDto.setGdohSlipNo(slipNo);
            		com0280Service.insertTCBOM002(instDto);
            	} else {
            		//com0280Service.deleteTCBOM002(instDto);
            		com0280Service.updateFail(instDto);
            	}

            	failCnt++;
            }
          }
        } else {
          Map<String, String> m_returnData = new HashMap<String, String>();
          m_returnData.put("XMSGS", t_param_type[0].getXMSGS()); // 에러메시지
          rtnListData.add(m_returnData);
        }

      } else {
        LOG.debug("I/F DATA NOT FOUND ! ");
      }

      paramMap.put("CO_ID", inParams.getCoId());
      paramMap.put("SHP_ID", inParams.getShopId());
      paramMap.put("SLIP_WACC_YMD", inParams.getSalesDt());
      paramMap.put("USER_ID", inParams.getUserId());

      if (inParams.getGdohSlipNo() != null) {
	      if (!inParams.getGdohSlipNo().isEmpty()) {
	    	SapStatusDto instDto = new SapStatusDto();
	      	instDto.setCoId(inParams.getCoId());
	      	instDto.setShopId(inParams.getShopId());
	      	instDto.setOccrDt(inParams.getSalesDt());
	      	instDto.setGdohSlipNo(inParams.getGdohSlipNo());
	      	instDto.setRegrId(inParams.getUserId());

	    	if ("N".equals(inParams.getDelYN())) {
	      		com0280Service.deleteTCBOM002(instDto);
	      		com0280Service.insertTCBOM002(instDto);
	      	} else {
	      		com0280Service.deleteTCBOM002(instDto);
	      	}
	      }
      }

      ClosingAdjDto instDto = new ClosingAdjDto();
      instDto.setCoId(inParams.getCoId());
  	  instDto.setShopId(inParams.getShopId());
  	  instDto.setOccrDt(inParams.getSalesDt());
  	  instDto.setRegrId(inParams.getUserId());

  	  //통합결제 시재 일마감
  	  //com0280Service.updateClosingGdoh(instDto);

  	  //I-Fresh 시재 일마감
      com0280Service.gdohBalAplyProcedure(instDto);

    } catch (Exception e) {
    	throw new BaseException(e);
    } finally {
      LOG.debug("I/F END ! ");
    }

    if (rtnListData == null || rtnListData.size() == 0) {
    	Map<String, String> m_returnData = new HashMap<String, String>();
        m_returnData.put("XMSGS", "시재 전송을 마쳤습니다.");
        rtnListData.add(m_returnData);
    }

    return rtnListData;
  }
}
