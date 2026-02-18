package com.cj.freshway.fs.cps.adapter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cj.freshway.fs.common.base.BaseException;
import com.cj.freshway.fs.common.utils.UtilsDAmo;
import com.cj.freshway.fs.cps.adapter.dto.PosOpenDto;
import com.cj.freshway.fs.cps.adapter.dto.AlimTalkDto;
import com.cj.freshway.fs.cps.adapter.dto.CashierInfoInqDto;
import com.cj.freshway.fs.cps.adapter.dto.CashierLoginDto;
import com.cj.freshway.fs.cps.adapter.dto.ConnerBusySetDto;
import com.cj.freshway.fs.cps.adapter.dto.ConnerSoldOutSetDto;
import com.cj.freshway.fs.cps.adapter.dto.ConnerSoldoutBuyDto;
import com.cj.freshway.fs.cps.adapter.dto.CouponInqDto;
import com.cj.freshway.fs.cps.adapter.dto.CheckSavingStampsDto;
import com.cj.freshway.fs.cps.adapter.dto.EmpPayDto;
import com.cj.freshway.fs.cps.adapter.dto.JnlDataSendDto;
import com.cj.freshway.fs.cps.adapter.dto.JnlListInqDto;
import com.cj.freshway.fs.cps.adapter.dto.KvsKpsDeviceInfoInqDto;
import com.cj.freshway.fs.cps.adapter.dto.MenuInfoDto;
import com.cj.freshway.fs.cps.adapter.dto.MenuSoldoutSetDto;
import com.cj.freshway.fs.cps.adapter.dto.PartCalcDataInqDto;
import com.cj.freshway.fs.cps.adapter.dto.PosStatusDto;
import com.cj.freshway.fs.cps.adapter.dto.TranCallDto;
import com.cj.freshway.fs.cps.adapter.dto.TranDataSendDto;
import com.cj.freshway.fs.cps.adapter.dto.TranNoDto;
import com.cj.freshway.fs.cps.adapter.dto.UrgencyMenuDto;
import com.cj.freshway.fs.cps.adapter.dto.MenuSoldOutIqnDto;
import com.cj.freshway.fs.cps.adapter.dto.IncheonAirportTranCallDto;

import com.cj.freshway.fs.cps.adapter.entity.AdapterCommonResEntity;
import com.cj.freshway.fs.cps.adapter.entity.AdapterCommonSoldOutSetEntity;
import com.cj.freshway.fs.cps.adapter.entity.AdapterCommonDataSendEntity;
import com.cj.freshway.fs.cps.adapter.entity.PosOpenResEntity;
import com.cj.freshway.fs.cps.adapter.entity.CashierLoginResEntity;
import com.cj.freshway.fs.cps.adapter.entity.CommonCodeEntity;
import com.cj.freshway.fs.cps.adapter.entity.MenuListEntity;
import com.cj.freshway.fs.cps.adapter.entity.MenuListInqEntity;
import com.cj.freshway.fs.cps.adapter.entity.PosStatusResEntiry;
import com.cj.freshway.fs.cps.adapter.entity.PosStatusUpdateEntity;
import com.cj.freshway.fs.cps.adapter.entity.SystemDateTimeEntity;
import com.cj.freshway.fs.cps.adapter.entity.AdapterCommonEntity;
import com.cj.freshway.fs.cps.adapter.entity.PosStatusEntity;
import com.cj.freshway.fs.cps.adapter.entity.CashierInfoEntity;
import com.cj.freshway.fs.cps.adapter.entity.PosVersionEntity;
import com.cj.freshway.fs.cps.adapter.entity.UrgencyMenuResEntity;

import io.swagger.v3.oas.annotations.media.Schema;

import com.cj.freshway.fs.cps.adapter.entity.ConnerSoldoutBuyResEntity;
import com.cj.freshway.fs.cps.adapter.entity.CouponIdEntity;
import com.cj.freshway.fs.cps.adapter.entity.SavingStampsInfoEntity;
import com.cj.freshway.fs.cps.adapter.entity.CouponInfoEntity;
import com.cj.freshway.fs.cps.adapter.entity.JnlListEntity;
import com.cj.freshway.fs.cps.adapter.entity.ConnerListEntity;
import com.cj.freshway.fs.cps.adapter.entity.ConnerListInqEntity;
import com.cj.freshway.fs.cps.adapter.entity.TranCallResEntity;
import com.cj.freshway.fs.cps.adapter.entity.TranCallEntity;
import com.cj.freshway.fs.cps.adapter.entity.TranHeaderEntity;
import com.cj.freshway.fs.cps.adapter.entity.TranNoResEntity;
import com.cj.freshway.fs.cps.adapter.entity.TranNoEntity;
import com.cj.freshway.fs.cps.adapter.entity.ConnerBusySetEntity;
import com.cj.freshway.fs.cps.adapter.entity.JnlListParamEntity;
import com.cj.freshway.fs.cps.adapter.entity.KvsKpsDeviceInfoInqEntity;
import com.cj.freshway.fs.cps.adapter.entity.MenuInfoParamEntity;
import com.cj.freshway.fs.cps.adapter.entity.JnlListInqResEntity;
import com.cj.freshway.fs.cps.adapter.entity.JnlListInqListEntity;
import com.cj.freshway.fs.cps.adapter.entity.CouponInqResEntity;
import com.cj.freshway.fs.cps.adapter.entity.CheckSavingStampsResEntity;
import com.cj.freshway.fs.cps.adapter.entity.CouponParamEntity;
import com.cj.freshway.fs.cps.adapter.entity.CheckSavingStampsParamEntity;
import com.cj.freshway.fs.cps.adapter.entity.AlimTalkParamEntity;
import com.cj.freshway.fs.cps.adapter.entity.AlimTalkMsgTempletEntity;
import com.cj.freshway.fs.cps.adapter.entity.AlimTalkSendEntity;
import com.cj.freshway.fs.cps.adapter.entity.MenuSoldOutIqnResEntity;
import com.cj.freshway.fs.cps.adapter.entity.MenuSoldOutListEntity;
import com.cj.freshway.fs.cps.adapter.entity.PartCaclDataInqInqEntity;
import com.cj.freshway.fs.cps.adapter.entity.MenuSoldOutInqListEntity;
import com.cj.freshway.fs.cps.adapter.entity.MenuInfoResEntity;
import com.cj.freshway.fs.cps.adapter.entity.MenuInfoEntity;
import com.cj.freshway.fs.cps.adapter.entity.PartCalcDataInqParamEntity;
import com.cj.freshway.fs.cps.adapter.entity.PartCalcDataInqResEntity;
import com.cj.freshway.fs.cps.adapter.entity.PartCalcDataInqEntity;
import com.cj.freshway.fs.cps.adapter.entity.EmpPayResEntity;
import com.cj.freshway.fs.cps.adapter.entity.EmpPayEntity;
import com.cj.freshway.fs.cps.adapter.entity.EmpPayParamEntity;
import com.cj.freshway.fs.cps.adapter.entity.CashierInfoInqResEntity;
import com.cj.freshway.fs.cps.adapter.entity.CashierInfoInqListInqEntity;
import com.cj.freshway.fs.cps.adapter.entity.CashierInfoInqEntity;
import com.cj.freshway.fs.cps.adapter.entity.KvsKpsDeviceInfoInqResEntity;
import com.cj.freshway.fs.cps.adapter.entity.KvsKpsDeviceInfoListInqEntity;
import com.cj.freshway.fs.cps.adapter.entity.IncheonAirportTranCallResEntity;
import com.cj.freshway.fs.cps.adapter.entity.IncheonAirportTranCallEntity;
import com.cj.freshway.fs.cps.adapter.service.SoldoutRedisCacheService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author SungDo.Lee
 * @description
 *
 *              <pre>
 * 포스 서버 어뎁터 관리 서비스
 *              </pre>
 */

@Slf4j
@Service
@Transactional(readOnly = true)
public class AdapterService {


  @Autowired
  AdapterMapper adaterMapper;
  
  @Autowired
  SoldoutRedisCacheService soldoutRedisCacheService;
  
  /**
   * 영업개시 (CP001)
   * @param posOpenDto
   * @return
   * @throws BaseException
   */
  @Transactional
  public PosOpenResEntity posOpen(
		  PosOpenDto posOpenDto) throws BaseException {
	  try {
		  // 어뎁터 공통 엔티티
		  AdapterCommonEntity adpaterCommonEntity = new AdapterCommonEntity();
		  adpaterCommonEntity.setCoId(posOpenDto.getPv_co_id());
		  adpaterCommonEntity.setShopId(posOpenDto.getPv_shop_id());
		  adpaterCommonEntity.setSteId(posOpenDto.getPv_ste_id());
		  adpaterCommonEntity.setSaleDt(posOpenDto.getPv_sale_dt());
		  adpaterCommonEntity.setPosNo(posOpenDto.getPv_pos_no());
		  adpaterCommonEntity.setTranNo(posOpenDto.getPv_tran_no());
		  adpaterCommonEntity.setCashierNo("");
		  adpaterCommonEntity.setSeqNo("");
		  
		  // 응답 엔티티
		  PosOpenResEntity posOpenEntity = new PosOpenResEntity();
		  posOpenEntity.setErr_cd("S");
		  posOpenEntity.setErr_msg_ctt("");
		  posOpenEntity.setInstanceid(posOpenDto.getPv_instanceid());
		  posOpenEntity.setPv_procid(posOpenDto.getPv_procid());
		  posOpenEntity.setPv_req_dtm(posOpenDto.getPv_req_dtm());
		  posOpenEntity.setPv_req_emp_id(posOpenDto.getPv_req_emp_id());
		  posOpenEntity.setPv_co_id(posOpenDto.getPv_co_id());
		  posOpenEntity.setPv_sys_dt("");
		  posOpenEntity.setPv_sys_tm("");
		  
		  // 시스템 일자 시간 얻음
		  SystemDateTimeEntity systemDateTimeEntity = adaterMapper.getSystemNowDateTime(adpaterCommonEntity);
		  if(systemDateTimeEntity == null)
		  {
			  posOpenEntity.setPv_res_cd("9999");
			  posOpenEntity.setErr_msg_ctt("시스템 일자/시간 조회 에러");
			  return posOpenEntity;
		  }
		  
		  // 포스 정보 조회
		  String posNo = adaterMapper.getFscpsCheckPosMaster(adpaterCommonEntity);
		  if(posNo == null || posNo.equals(""))
		  {
			  posOpenEntity.setPv_res_cd("0100");
			  posOpenEntity.setErr_msg_ctt("해당포스 없음");
			  return posOpenEntity;
		  }
			  
		  // 포스 상태 조회
		  PosStatusEntity posStatusEntity = adaterMapper.getFscpsPosStatus(adpaterCommonEntity);
		  if(posStatusEntity != null)
		  {  
			  // 영업개시 체크
			  if(posStatusEntity.getSalesDt().compareTo(posOpenDto.getPv_sale_dt()) == 0 && posStatusEntity.getSodYn().compareTo("Y") == 0)
			  {
				  posOpenEntity.setPv_res_cd("0110");
				  posOpenEntity.setErr_msg_ctt("이미 영업개시 완료");
				  return posOpenEntity;
			  }
			  else if(posStatusEntity.getSalesDt().compareTo(posOpenDto.getPv_sale_dt()) == 0 && posStatusEntity.getEodYn().compareTo("Y") == 0)
			  {
				  posOpenEntity.setPv_res_cd("0130");
				  posOpenEntity.setErr_msg_ctt("이미 영업마감 완료");
				  return posOpenEntity;
			  }
			  else if(posStatusEntity.getSalesDt().compareTo(posOpenDto.getPv_sale_dt()) < 0 && posStatusEntity.getEodYn().compareTo("Y") != 0)
			  {
				  posOpenEntity.setPv_res_cd("0120");
				  posOpenEntity.setErr_msg_ctt("전일 영업마감 미완료");
				  return posOpenEntity;
			  }
			  else if(posStatusEntity.getSalesDt().compareTo(posOpenDto.getPv_sale_dt()) > 0)
			  {
				  posOpenEntity.setPv_res_cd("0140");
				  posOpenEntity.setErr_msg_ctt("영업일자 오류");
				  return posOpenEntity;
			  }
			  
			// 영업개시 상태 갱신
			adaterMapper.updatePosOpen(adpaterCommonEntity);
		  }
		  else
		  {
			// 영업개시 상태 추가
			adaterMapper.insertPosOpen(adpaterCommonEntity);
		  }
		  
		  // 정상 응답
		  posOpenEntity.setPv_res_cd("0000");
		  posOpenEntity.setErr_msg_ctt("정상");
		  posOpenEntity.setPv_sys_dt(systemDateTimeEntity.getNowDate());
		  posOpenEntity.setPv_sys_tm(systemDateTimeEntity.getNowTime());
		  
		  return posOpenEntity;
	  
	  } catch (Exception e) {
			throw new BaseException(e);
	  }
  }
  
  /**
   * 영업마감 (CP002)
   * @param posOpenDto
   * @return
   * @throws BaseException
   */
  @Transactional
  public PosOpenResEntity posClose(
		  PosOpenDto posOpenDto) throws BaseException {
	  try {
		  // 어뎁터 공통 엔티티
		  AdapterCommonEntity adpaterCommonEntity = new AdapterCommonEntity();
		  adpaterCommonEntity.setCoId(posOpenDto.getPv_co_id());
		  adpaterCommonEntity.setShopId(posOpenDto.getPv_shop_id());
		  adpaterCommonEntity.setSteId(posOpenDto.getPv_ste_id());
		  adpaterCommonEntity.setSaleDt(posOpenDto.getPv_sale_dt());
		  adpaterCommonEntity.setPosNo(posOpenDto.getPv_pos_no());
		  adpaterCommonEntity.setTranNo(posOpenDto.getPv_tran_no());
		  adpaterCommonEntity.setCashierNo("");
		  
		  // 응답 엔티티
		  PosOpenResEntity posOpenEntity = new PosOpenResEntity();
		  posOpenEntity.setErr_cd("S");
		  posOpenEntity.setErr_msg_ctt("");
		  posOpenEntity.setInstanceid(posOpenDto.getPv_instanceid());
		  posOpenEntity.setPv_procid(posOpenDto.getPv_procid());
		  posOpenEntity.setPv_req_dtm(posOpenDto.getPv_req_dtm());
		  posOpenEntity.setPv_req_emp_id(posOpenDto.getPv_req_emp_id());
		  posOpenEntity.setPv_co_id(posOpenDto.getPv_co_id());
		  posOpenEntity.setPv_sys_dt("");
		  posOpenEntity.setPv_sys_tm("");
		  
		  // 시스템 일자 시간 얻음
		  SystemDateTimeEntity systemDateTimeEntity = adaterMapper.getSystemNowDateTime(adpaterCommonEntity);
		  if(systemDateTimeEntity == null)
		  {
			  posOpenEntity.setPv_res_cd("9999");
			  posOpenEntity.setErr_msg_ctt("시스템 일자/시간 조회 에러");
			  return posOpenEntity;
		  }
		  
		  // 포스 정보 조회
		  String posNo = adaterMapper.getFscpsCheckPosMaster(adpaterCommonEntity);
		  if(posNo == null || posNo.equals(""))
		  {
			  posOpenEntity.setPv_res_cd("0100");
			  posOpenEntity.setErr_msg_ctt("해당포스 없음");
			  return posOpenEntity;
		  }
			  
		  // 포스 상태 조회
		  PosStatusEntity posStatusEntity = adaterMapper.getFscpsPosStatus(adpaterCommonEntity);
		  if(posStatusEntity != null)
		  {  
			  //if(posStatusEntity.getSalesDt().compareTo(posOpenDto.getPv_sale_dt()) != 0)
			  //{
			  //	  posOpenEntity.setPv_res_cd("0140");
			  //	  posOpenEntity.setErr_msg_ctt("영업일자 오류");
			  //	  return posOpenEntity;
			  //} 
				  
			  if(posStatusEntity.getSalesDt().compareTo(posOpenDto.getPv_sale_dt()) == 0 && posStatusEntity.getSodYn().compareTo("N") == 0)
			  {
				  posOpenEntity.setPv_res_cd("0111");
				  posOpenEntity.setErr_msg_ctt("영업개시전");
				  return posOpenEntity;
			  }
			  else if(posStatusEntity.getSalesDt().compareTo(posOpenDto.getPv_sale_dt()) < 0 && posStatusEntity.getEodYn().compareTo("Y") == 0)
			  {
				  posOpenEntity.setPv_res_cd("0130");
				  posOpenEntity.setErr_msg_ctt("이미 영업마감 완료");
				  return posOpenEntity;
			  }
			  
			// 영업마감 상태 갱신
			adaterMapper.updatePosClose(adpaterCommonEntity);
		  }
		  else
		  {
			  posOpenEntity.setPv_res_cd("9999");
			  posOpenEntity.setErr_msg_ctt("시스템 에러");
			  return posOpenEntity;
		  }
		  
		  // 정상 응답
		  posOpenEntity.setPv_res_cd("0000");
		  posOpenEntity.setErr_msg_ctt("정상");
		  posOpenEntity.setPv_sys_dt(systemDateTimeEntity.getNowDate());
		  posOpenEntity.setPv_sys_tm(systemDateTimeEntity.getNowTime());
		  
		  return posOpenEntity;
	  
	  } catch (Exception e) {
			throw new BaseException(e);
	  }
  }
  
  /**
   * 판매원 로그인 (CP003)
   * @param cashierLoginDto
   * @return
   * @throws BaseException
   */
  @Transactional
  public CashierLoginResEntity cashierLogin(
		  CashierLoginDto cashierLoginDto) throws BaseException {
	  try {
		  // 어뎁터 공통 엔티티
		  AdapterCommonEntity adpaterCommonEntity = new AdapterCommonEntity();
		  adpaterCommonEntity.setCoId(cashierLoginDto.getPv_co_id());
		  adpaterCommonEntity.setShopId(cashierLoginDto.getPv_shop_id());
		  adpaterCommonEntity.setSteId(cashierLoginDto.getPv_ste_id());
		  adpaterCommonEntity.setSaleDt(cashierLoginDto.getPv_sale_dt());
		  adpaterCommonEntity.setPosNo(cashierLoginDto.getPv_pos_no());
		  adpaterCommonEntity.setTranNo(cashierLoginDto.getPv_tran_no());
		  adpaterCommonEntity.setCashierNo(cashierLoginDto.getPv_cashier_no());
		  
		  // 응답 엔티티
		  CashierLoginResEntity cashierLoginEntity = new CashierLoginResEntity();
		  cashierLoginEntity.setErr_cd("S");
		  cashierLoginEntity.setErr_msg_ctt("");
		  cashierLoginEntity.setInstanceid(cashierLoginDto.getPv_instanceid());
		  cashierLoginEntity.setPv_procid(cashierLoginDto.getPv_procid());
		  cashierLoginEntity.setPv_req_dtm(cashierLoginDto.getPv_req_dtm());
		  cashierLoginEntity.setPv_req_emp_id(cashierLoginDto.getPv_req_emp_id());
		  cashierLoginEntity.setPv_co_id(cashierLoginDto.getPv_co_id());
		  cashierLoginEntity.setPv_stat_cd("");
		  cashierLoginEntity.setPv_signon_pos_no("");
		  
		  // 포스 정보 조회
		  String posNo = adaterMapper.getFscpsCheckPosMaster(adpaterCommonEntity);
		  if(posNo == null || posNo.equals(""))
		  {
			  cashierLoginEntity.setPv_res_cd("0100");
			  cashierLoginEntity.setErr_msg_ctt("해당포스 없음");
			  return cashierLoginEntity;
		  }
		  
		  // 포스 상태 조회
		  PosStatusEntity posStatusEntity = adaterMapper.getFscpsPosStatus(adpaterCommonEntity);
		  if(posStatusEntity == null)
		  {
			  cashierLoginEntity.setPv_res_cd("0101");
			  cashierLoginEntity.setErr_msg_ctt("사용 할 수 없는 포스");
			  return cashierLoginEntity;
		  }
		  
		  // 영업일자 체크
		  if(posStatusEntity.getSalesDt().compareTo(cashierLoginDto.getPv_sale_dt()) != 0)
		  {
			  cashierLoginEntity.setPv_res_cd("0101");
			  cashierLoginEntity.setErr_msg_ctt("영업일자 오류");
			  return cashierLoginEntity;
		  }
		  
		  // 여업개시 체크
		  if(posStatusEntity.getSodYn().compareTo("Y") != 0)
		  {
			  cashierLoginEntity.setPv_res_cd("0101");
			  cashierLoginEntity.setErr_msg_ctt("영업개시 오류");
			  return cashierLoginEntity;
		  }
		  
		  // 포스 상태 체크
		  //if(posStatusEntity.getSalesDt().compareTo(cashierLoginDto.getPv_sale_dt()) != 0 ||
     	  //   posStatusEntity.getSodYn().compareTo("Y") != 0 ||
		  //	 posStatusEntity.getEodYn().compareTo("N") != 0)
		  //{
		  //   cashierLoginEntity.setPv_res_cd("0101");
		  //   cashierLoginEntity.setErr_msg_ctt("사용 할 수 없는 포스");
		  //   return cashierLoginEntity;
		  //}
		  
		  // 판매원 정보 조회
		  CashierInfoEntity cashierInfoEntity = adaterMapper.getFscpsCashierMaster(adpaterCommonEntity);
		  if(cashierInfoEntity == null)
		  {
			  cashierLoginEntity.setPv_res_cd("0200");
			  cashierLoginEntity.setErr_msg_ctt("해당 판매원 없음");
			  return cashierLoginEntity;
		  }
		  
		  // 상태 체크
		  if(cashierInfoEntity.getLoginSttCd().equals("1") == true)
		  {
			  cashierLoginEntity.setPv_res_cd("0220");
			  cashierLoginEntity.setPv_signon_pos_no(cashierInfoEntity.getLoginPosNo());
			  cashierLoginEntity.setErr_msg_ctt("타 포스 로그인 상태");
			  return cashierLoginEntity;
		  }
		  else if(cashierInfoEntity.getLoginSttCd().equals("3") == true)
		  {
			  cashierLoginEntity.setPv_res_cd("0220");
			  cashierLoginEntity.setErr_msg_ctt("이미마감된 판매원");
			  return cashierLoginEntity;
		  }
		  
		  // 로그인 상태 갱신
		  adaterMapper.updateCashierLogin(adpaterCommonEntity);
		  
		  // 정상 응답
		  cashierLoginEntity.setPv_res_cd("0000");
		  cashierLoginEntity.setErr_msg_ctt("정상");
		  cashierLoginEntity.setPv_stat_cd(cashierInfoEntity.getLoginSttCd());
		  cashierLoginEntity.setPv_signon_pos_no(cashierLoginDto.getPv_pos_no());
		  
		  return cashierLoginEntity;
	  
	  } catch (Exception e) {
			throw new BaseException(e);
	  }
  }
  
  
  /**
   * 판매원 로그오프 (CP004)
   * @param cashierLoginDto
   * @return
   * @throws BaseException
   */
  @Transactional
  public CashierLoginResEntity cashierLogoff(
		  CashierLoginDto cashierLoginDto) throws BaseException {
	  try {
		  // 어뎁터 공통 엔티티
		  AdapterCommonEntity adpaterCommonEntity = new AdapterCommonEntity();
		  adpaterCommonEntity.setCoId(cashierLoginDto.getPv_co_id());
		  adpaterCommonEntity.setShopId(cashierLoginDto.getPv_shop_id());
		  adpaterCommonEntity.setSteId(cashierLoginDto.getPv_ste_id());
		  adpaterCommonEntity.setSaleDt(cashierLoginDto.getPv_sale_dt());
		  adpaterCommonEntity.setPosNo(cashierLoginDto.getPv_pos_no());
		  adpaterCommonEntity.setTranNo(cashierLoginDto.getPv_tran_no());
		  adpaterCommonEntity.setCashierNo(cashierLoginDto.getPv_cashier_no());
		  
		  // 응답 엔티티
		  CashierLoginResEntity cashierLoginEntity = new CashierLoginResEntity();
		  cashierLoginEntity.setErr_cd("S");
		  cashierLoginEntity.setErr_msg_ctt("");
		  cashierLoginEntity.setInstanceid(cashierLoginDto.getPv_instanceid());
		  cashierLoginEntity.setPv_procid(cashierLoginDto.getPv_procid());
		  cashierLoginEntity.setPv_req_dtm(cashierLoginDto.getPv_req_dtm());
		  cashierLoginEntity.setPv_req_emp_id(cashierLoginDto.getPv_req_emp_id());
		  cashierLoginEntity.setPv_co_id(cashierLoginDto.getPv_co_id());
		  cashierLoginEntity.setPv_stat_cd("");
		  cashierLoginEntity.setPv_signon_pos_no("");
		  
		  /******************************************************************************
		  // 포스 정보 조회
		  String posNo = adaterMapper.getFscpsCheckPosMaster(adpaterCommonEntity);
		  if(posNo == null || posNo.equals(""))
		  {
			  cashierLoginEntity.setPv_res_cd("0100");
			  cashierLoginEntity.setErr_msg_ctt("해당포스 없음");
			  return cashierLoginEntity;
		  }
		  
		  // 포스 상태 조회
		  PosStatusEntity posStatusEntity = adaterMapper.getFscpsPosStatus(adpaterCommonEntity);
		  if(posStatusEntity == null)
		  {
			  cashierLoginEntity.setPv_res_cd("0101");
			  cashierLoginEntity.setErr_msg_ctt("사용 할 수 없는 포스");
			  return cashierLoginEntity;
		  }
		  
		  // 포스 상태 체크
		  if(posStatusEntity.getSalesDt().compareTo(cashierLoginDto.getPv_sale_dt()) != 0 ||
     	     posStatusEntity.getSodYn().compareTo("Y") != 0 ||
			 posStatusEntity.getEodYn().compareTo("N") != 0)
		  {
			  cashierLoginEntity.setPv_res_cd("0101");
			  cashierLoginEntity.setErr_msg_ctt("사용 할 수 없는 포스");
			  return cashierLoginEntity;
		  }
		  
		  // 판매원 정보 조회
		  CashierInfoEntity cashierInfoEntity = adaterMapper.getFscpsCashierMaster(adpaterCommonEntity);
		  if(cashierInfoEntity == null)
		  {
			  cashierLoginEntity.setPv_res_cd("0200");
			  cashierLoginEntity.setErr_msg_ctt("해당 판매원 없음");
			  return cashierLoginEntity;
		  }
		  ******************************************************************************/
		  
		  // 로그인 상태 갱신
		  adaterMapper.updateCashierLogoff(adpaterCommonEntity);
		  
		  // 정상 응답
		  cashierLoginEntity.setPv_stat_cd("2");
		  cashierLoginEntity.setPv_res_cd("0000");
		  cashierLoginEntity.setErr_msg_ctt("정상");
		  cashierLoginEntity.setPv_signon_pos_no("");
		  
		  return cashierLoginEntity;
	  
	  } catch (Exception e) {
			throw new BaseException(e);
	  }
  }
  
  
  /**
   * 포스상태 조회 (CP006)
   * @param posStatusDto
   * @return
   * @throws BaseException
   */
  @Transactional
  public PosStatusResEntiry posStatus(
		  PosStatusDto posStatusDto) throws BaseException {
	  try {
		  // 어뎁터 공통 엔티티
		  AdapterCommonEntity adpaterCommonEntity = new AdapterCommonEntity();
		  adpaterCommonEntity.setCoId(posStatusDto.getPv_co_id());
		  adpaterCommonEntity.setShopId(posStatusDto.getPv_shop_id());
		  adpaterCommonEntity.setSteId(posStatusDto.getPv_ste_id());
		  adpaterCommonEntity.setSaleDt(posStatusDto.getPv_sale_dt());
		  adpaterCommonEntity.setPosNo(posStatusDto.getPv_pos_no());
		  adpaterCommonEntity.setTranNo(posStatusDto.getPv_tran_no());
		  adpaterCommonEntity.setCashierNo(posStatusDto.getPv_cashier_no());
		  // 응답 엔티티
		  PosStatusResEntiry posStatusResEntiry = new PosStatusResEntiry();
		  posStatusResEntiry.setErr_cd("S");
		  posStatusResEntiry.setErr_msg_ctt("");
		  posStatusResEntiry.setInstanceid(posStatusDto.getPv_instanceid());
		  posStatusResEntiry.setPv_procid(posStatusDto.getPv_procid());
		  posStatusResEntiry.setPv_req_dtm(posStatusDto.getPv_req_dtm());
		  posStatusResEntiry.setPv_req_emp_id(posStatusDto.getPv_req_emp_id());
		  posStatusResEntiry.setPv_co_id(posStatusDto.getPv_co_id());
		  posStatusResEntiry.setPv_res_cd("");
		  posStatusResEntiry.setPv_s3_url("");
		  posStatusResEntiry.setPv_bucket("");
		  posStatusResEntiry.setPv_access_key_id("");
		  posStatusResEntiry.setPv_sys_dt("");
		  posStatusResEntiry.setPv_sys_tm("");
		  posStatusResEntiry.setPv_pgm_file_nm("");
		  posStatusResEntiry.setPv_avi_file_nm("");
		  posStatusResEntiry.setPv_mst_ver("");
		  posStatusResEntiry.setPv_pgm_ver("");
		  posStatusResEntiry.setPv_avi_ver("");
		  posStatusResEntiry.setPv_note_ver("");
		  posStatusResEntiry.setPv_emgc_seq_no("");
		  
		  // 시스템 일자 시간 얻음
		  SystemDateTimeEntity systemDateTimeEntity = adaterMapper.getSystemNowDateTime(adpaterCommonEntity);
		  if(systemDateTimeEntity == null)
		  {
			  posStatusResEntiry.setPv_res_cd("9999");
			  posStatusResEntiry.setErr_msg_ctt("시스템 일자/시간 조회 에러");
			  return posStatusResEntiry;
		  }
		  
		  // 프로그램버전
		  PosVersionEntity pgmVersionEntity = adaterMapper.getFscpsProgramVersionInfo(adpaterCommonEntity);
		  if(pgmVersionEntity != null)
		  {
			  String progFileName = pgmVersionEntity.getFileNm();
			  int locSlash = progFileName.lastIndexOf('/');
			  String progFilePathUrl = progFileName.substring(locSlash + 1);
				
			  posStatusResEntiry.setPv_pgm_file_nm(progFilePathUrl);
			  posStatusResEntiry.setPv_pgm_ver(pgmVersionEntity.getVerSion());
		  }
		  else
		  {
			  posStatusResEntiry.setPv_pgm_file_nm("");
			  posStatusResEntiry.setPv_pgm_ver("00000000000000");
		  }
		  
		  // 마스터버전
		  PosVersionEntity mstVersionEntity = adaterMapper.getFscpsMasterVersionInfo(adpaterCommonEntity);
		  if(mstVersionEntity != null)
		  {
			  posStatusResEntiry.setPv_mst_ver(mstVersionEntity.getVerSion());
		  }
		  else
		  {
			  posStatusResEntiry.setPv_mst_ver("00000000000000");
		  }
		  
		  // 동영상버전
		  PosVersionEntity aviVersionEntity = adaterMapper.getFscpsVideoVersionInfo(adpaterCommonEntity);
		  if(aviVersionEntity != null)
		  {
			  posStatusResEntiry.setPv_avi_ver(aviVersionEntity.getVerSion());
		  }
		  else
		  {
			  posStatusResEntiry.setPv_avi_ver("00000000000000");
		  }
		  
		  // 공지사항버전
		  PosVersionEntity noticeVersionEntity = adaterMapper.getFscpsNoteVersionInfo(adpaterCommonEntity);
		  if(noticeVersionEntity != null)
		  {
			  posStatusResEntiry.setPv_note_ver(noticeVersionEntity.getVerSion());
		  }
		  else
		  {
			  posStatusResEntiry.setPv_avi_ver("00000000000000");
		  }
		  
		  // 긴급마스터 순번
		  Integer urgentSeqNo = adaterMapper.getFscpsUrgentMaster(adpaterCommonEntity);
		  if(urgentSeqNo != null)
		  {
			  if(urgentSeqNo < 0)
			  {
				  urgentSeqNo = 0;
			  }  
		  }
		  else
		  {
			  urgentSeqNo = 0;
		  }
		  posStatusResEntiry.setPv_emgc_seq_no(urgentSeqNo.toString());
		  
		  // S3정보
		  CommonCodeEntity listS3Info = adaterMapper.getS3AddressInfo(adpaterCommonEntity);
		  if(listS3Info != null)
		  {
			  posStatusResEntiry.setPv_s3_url(listS3Info.getCodeRef1());
			  posStatusResEntiry.setPv_bucket(listS3Info.getCodeRef2());
			  posStatusResEntiry.setPv_access_key_id(listS3Info.getCodeRef3());
			  posStatusResEntiry.setPv_secret_access_key(listS3Info.getCodeRef4());
		  }
		  
		  // 매장번호
		  String shopTno = adaterMapper.getFscpsShopTno(adpaterCommonEntity);
		  if(shopTno == null) 
		  {
			  posStatusResEntiry.setPv_shop_t_no("");
		  }
		  else
		  {
			  shopTno = shopTno.trim();
			  
			  if(shopTno.equals("") == true)
			  {
				  posStatusResEntiry.setPv_shop_t_no("");  
			  }
			  else
			  {
				  posStatusResEntiry.setPv_shop_t_no(UtilsDAmo.dAmoDesc(shopTno.trim()));  
			  }
		  }
		  
		  // 포스 관리자호
		  String posMgrTno = adaterMapper.getFscpsPosMgrTno(adpaterCommonEntity);
		  if(posMgrTno == null)  
		  {
			  posStatusResEntiry.setPv_mgr_t_no("");
		  }
		  else
		  {
			  posMgrTno = posMgrTno.trim();
			  
			  if(posMgrTno.equals("") == true)
			  {
				  posStatusResEntiry.setPv_mgr_t_no("");
			  }
			  else
			  {
				  posStatusResEntiry.setPv_mgr_t_no(UtilsDAmo.dAmoDesc(posMgrTno.trim()));
			  }
		  }
		  
		  // 포스상태 갱신값 설정
		  PosStatusUpdateEntity posStatusUpdateEntity = new PosStatusUpdateEntity();
		  posStatusUpdateEntity.setCoId(posStatusDto.getPv_co_id());
		  posStatusUpdateEntity.setShopId(posStatusDto.getPv_shop_id());
		  posStatusUpdateEntity.setSteId(posStatusDto.getPv_ste_id());
		  posStatusUpdateEntity.setSaleDt(posStatusDto.getPv_sale_dt());
		  posStatusUpdateEntity.setPosNo(posStatusDto.getPv_pos_no());
		  posStatusUpdateEntity.setTranNo(posStatusDto.getPv_tran_no());
		  posStatusUpdateEntity.setPosStTm(posStatusDto.getPv_pos_st_tm());
		  posStatusUpdateEntity.setSodYn(posStatusDto.getPv_sod_yn());
		  posStatusUpdateEntity.setSodTm(posStatusDto.getPv_sod_tm());
		  posStatusUpdateEntity.setEodYn(posStatusDto.getPv_eod_yn());
		  posStatusUpdateEntity.setEodTm(posStatusDto.getPv_eod_tm());
		  posStatusUpdateEntity.setCashierNo(posStatusDto.getPv_cashier_no());
		  posStatusUpdateEntity.setMstVer(posStatusResEntiry.getPv_mst_ver());
		  posStatusUpdateEntity.setPgmVer(posStatusResEntiry.getPv_pgm_ver());
		  posStatusUpdateEntity.setAviVer(posStatusResEntiry.getPv_avi_ver());
		  posStatusUpdateEntity.setNoteVer(posStatusResEntiry.getPv_note_ver());
		  posStatusUpdateEntity.setEmgcSeqNo(posStatusResEntiry.getPv_emgc_seq_no());
		  if(adaterMapper.updatePosStatusInfo(posStatusUpdateEntity) <= 0)
		  {
			  if(adaterMapper.insertPosStatusInfo(posStatusUpdateEntity) <= 0)
			  {
				  
			  }
		  }
		  
		  posStatusResEntiry.setPv_sys_dt(systemDateTimeEntity.getNowDate());
		  posStatusResEntiry.setPv_sys_tm(systemDateTimeEntity.getNowTime());
		  posStatusResEntiry.setPv_res_cd("0000");
		  posStatusResEntiry.setErr_msg_ctt("정상");
		  
		  return posStatusResEntiry;
	  
	  } catch (Exception e) {
			throw new BaseException(e);
	  }
  }
  
  
  /**
   * 긴급메뉴 조회 (CP007)
   * @param urgencyMenuDto
   * @return
   * @throws BaseException
   */
  public UrgencyMenuResEntity urgencyMenu(
		  UrgencyMenuDto urgencyMenuDto) throws BaseException {
	  try {
		  // 어뎁터 공통 엔티티
		  AdapterCommonEntity adpaterCommonEntity = new AdapterCommonEntity();
		  adpaterCommonEntity.setCoId(urgencyMenuDto.getPv_co_id());
		  adpaterCommonEntity.setShopId(urgencyMenuDto.getPv_shop_id());
		  adpaterCommonEntity.setSteId(urgencyMenuDto.getPv_ste_id());
		  adpaterCommonEntity.setSaleDt(urgencyMenuDto.getPv_req_dtm().substring(0, 8));
		  adpaterCommonEntity.setPosNo("");
		  adpaterCommonEntity.setTranNo("");
		  adpaterCommonEntity.setCashierNo("");
		  adpaterCommonEntity.setSeqNo(urgencyMenuDto.getPv_cur_seq_no());
		  
		  // 응답 엔티티
		  UrgencyMenuResEntity urgencyMenuResEntity = new UrgencyMenuResEntity();
		  urgencyMenuResEntity.setErr_cd("S");
		  urgencyMenuResEntity.setErr_msg_ctt("");
		  urgencyMenuResEntity.setInstanceid(urgencyMenuDto.getPv_instanceid());
		  urgencyMenuResEntity.setPv_procid(urgencyMenuDto.getPv_procid());
		  urgencyMenuResEntity.setPv_req_dtm(urgencyMenuDto.getPv_req_dtm());
		  urgencyMenuResEntity.setPv_req_emp_id(urgencyMenuDto.getPv_req_emp_id());
		  
		  // 긴급메뉴마스터 조회
		  List<MenuListEntity> listMenuListEntity = adaterMapper.getFscpsUrgentMenuMaster(adpaterCommonEntity);
		  
		  if(listMenuListEntity != null)
		  {
			  for(int nLoop = 0 ; nLoop < listMenuListEntity.size(); nLoop++)
			  {  
				  MenuListInqEntity menuListInqEntity = new MenuListInqEntity();
				  menuListInqEntity.setPv_menu_tp(listMenuListEntity.get(nLoop).getMenuTp());
				  menuListInqEntity.setPv_menu_cd(listMenuListEntity.get(nLoop).getMenuCd());
				  urgencyMenuResEntity.getPv_menu_list().add(menuListInqEntity);
			  }
		  }
		  
		  return urgencyMenuResEntity;
	  
	  } catch (Exception e) {
			throw new BaseException(e);
	  }
  }
  
  /**
   * 메뉴조회 (CP008)
   * @param menuInfoDto
   * @return
   * @throws BaseException
   */
  public MenuInfoResEntity menuInfo(
		  MenuInfoDto menuInfoDto) throws BaseException {
	  try {
		  // 어뎁터 공통 엔티티
		  MenuInfoParamEntity menuInfoParamEntity = new MenuInfoParamEntity();
		  menuInfoParamEntity.setCoId(menuInfoDto.getPv_co_id());
		  menuInfoParamEntity.setShopId(menuInfoDto.getPv_shop_id());
		  menuInfoParamEntity.setSteId(menuInfoDto.getPv_ste_id());
		  menuInfoParamEntity.setMenuTp(menuInfoDto.getPv_menu_tp());
		  menuInfoParamEntity.setMenuId(menuInfoDto.getPv_menu_cd());
		  
		  // 응답 엔티티
		  MenuInfoResEntity menuInfoResEntity = new MenuInfoResEntity();
		  menuInfoResEntity.setErr_cd("S");
		  menuInfoResEntity.setErr_msg_ctt("");
		  menuInfoResEntity.setInstanceid(menuInfoDto.getPv_instanceid());
		  menuInfoResEntity.setPv_co_id(menuInfoDto.getPv_co_id());		  
		  menuInfoResEntity.setPv_procid(menuInfoDto.getPv_procid());
		  menuInfoResEntity.setPv_req_dtm(menuInfoDto.getPv_req_dtm());
		  menuInfoResEntity.setPv_req_emp_id(menuInfoDto.getPv_req_emp_id());
		  
		  // 긴급메뉴마스터 조회
		  MenuInfoEntity menuInfoEntity = adaterMapper.getFscpsMenuInfo(menuInfoParamEntity);
		  
		  if(menuInfoEntity != null)
		  {
			  menuInfoResEntity.setPv_menu_tp(menuInfoEntity.getMenuTypeCd());
			  menuInfoResEntity.setPv_menu_cd(menuInfoEntity.getMenuId());
			  menuInfoResEntity.setPv_scrin_menu_nm(menuInfoEntity.getScrinMenuNm());
			  menuInfoResEntity.setPv_scrin_menu_nm_en(menuInfoEntity.getScrinMenuNmEn());
			  menuInfoResEntity.setPv_scrin_menu_nm_ch(menuInfoEntity.getScrinMenuNmCh());
			  menuInfoResEntity.setPv_scrin_menu_nm_jp(menuInfoEntity.getScrinMenuNmJp());
			  menuInfoResEntity.setPv_menu_img_file_path(menuInfoEntity.getMenuImgFilePathUrl());
			  menuInfoResEntity.setPv_menu_img_file_nm(menuInfoEntity.getMenuImgFileNm());
			  menuInfoResEntity.setPv_sold_out_yn(menuInfoEntity.getSoldOutYn());
			  menuInfoResEntity.setPv_main_fdingr(menuInfoEntity.getMainFdingr());
			  menuInfoResEntity.setPv_main_fdingr_en(menuInfoEntity.getMainFdingrEn());
			  menuInfoResEntity.setPv_main_fdingr_ch(menuInfoEntity.getMainFdingrCh());
			  menuInfoResEntity.setPv_main_fdingr_jp(menuInfoEntity.getMainFdingrJp());
			  menuInfoResEntity.setPv_oriplc(menuInfoEntity.getOriplc());
			  menuInfoResEntity.setPv_oriplc_en(menuInfoEntity.getOriplcEn());
			  menuInfoResEntity.setPv_oriplc_ch(menuInfoEntity.getOriplcCh());
			  menuInfoResEntity.setPv_oriplc_jp(menuInfoEntity.getOriplcJp());
			  menuInfoResEntity.setPv_menu_desc(menuInfoEntity.getMenuDesc());
			  menuInfoResEntity.setPv_menu_desc_en(menuInfoEntity.getMenuDescEn());
			  menuInfoResEntity.setPv_menu_desc_ch(menuInfoEntity.getMenuDescCh());
			  menuInfoResEntity.setPv_menu_desc_jp(menuInfoEntity.getMenuDescJp());
			  menuInfoResEntity.setPv_menu_calo(menuInfoEntity.getMenuCalo());
			  menuInfoResEntity.setPv_menu_natrium(menuInfoEntity.getMenuNatrium());
			  menuInfoResEntity.setPv_menu_cbh(menuInfoEntity.getMenuCbh());
			  menuInfoResEntity.setPv_menu_prot(menuInfoEntity.getMenuProt());
			  menuInfoResEntity.setPv_menu_fat(menuInfoEntity.getMenuFat());
			  menuInfoResEntity.setPv_menu_vitamin(menuInfoEntity.getMenuVitamin());
			  menuInfoResEntity.setPv_menu_min(menuInfoEntity.getMenuMin());
			  menuInfoResEntity.setPv_hot_yn(menuInfoEntity.getHotYn());
			  menuInfoResEntity.setPv_halal_yn(menuInfoEntity.getHalalYn());
			  menuInfoResEntity.setPv_vegan_yn(menuInfoEntity.getVeganYn());
			  menuInfoResEntity.setPv_pk_psbt_yn(menuInfoEntity.getPkPsbtYn());
			  //menuInfoResEntity.setPv_cj_emps_dc_obj_yn(menuInfoEntity.getCjEmpsDcObjYn());
			  //menuInfoResEntity.setPv_resi_emp_dc_obj_yn(menuInfoEntity.getResiEmpDcObjYn());
			  menuInfoResEntity.setPv_tax_type_cd(menuInfoEntity.getTaxtTypeCd());
			  menuInfoResEntity.setPv_sale_uprc(menuInfoEntity.getSaleUprc());
			  menuInfoResEntity.setPv_menu_label(menuInfoEntity.getMenuLabel());
			  menuInfoResEntity.setPv_quick_yn(menuInfoEntity.getQuickYn());
			  menuInfoResEntity.setPv_best_yn(menuInfoEntity.getBestYn());
			  menuInfoResEntity.setPv_set_prdt_menu_uprc(menuInfoEntity.getSetPrdtMenuUprc());
			  menuInfoResEntity.setPv_airp_corp_itm_cd(menuInfoEntity.getAirpCorpItmCd());
			  menuInfoResEntity.setPv_bar_no(menuInfoEntity.getBarNo());
			  menuInfoResEntity.setPv_kiosk_aply_yn(menuInfoEntity.getKioskAplyYn());
			  menuInfoResEntity.setPv_menu_did_disp_yn(menuInfoEntity.getMenuDidDispYn()); // kgss25 20241220a 메뉴정보조회 칼럼 추가
			  menuInfoResEntity.setPv_sub_dc_aply_yn(menuInfoEntity.getSubDcAplyYn());
			  menuInfoResEntity.setPv_pos_key_font_color_rgb(menuInfoEntity.getPosKeyFontColorRgb());
			  menuInfoResEntity.setPv_pos_key_bg_color_rgb(menuInfoEntity.getPosKeyBgColorRgb());
			  menuInfoResEntity.setPv_scrin_menu_nm_2(menuInfoEntity.getScrinMenuNm2()); // kgss25 20250106b 화면메뉴명2 칼럼 추가
		  }
		  else
		  {
			  menuInfoResEntity.setPv_res_cd("0200");
			  menuInfoResEntity.setErr_msg_ctt("해당 메뉴 없음");
			  return menuInfoResEntity;
		  }
			  
		  menuInfoResEntity.setPv_res_cd("0000");
		  menuInfoResEntity.setErr_msg_ctt("정상");
		  return menuInfoResEntity;
	  
	  } catch (Exception e) {
			throw new BaseException(e);
	  }
  }
  
  
  /**
   * 코너 SoldOut 및 혼잡도 조회 (CP009)
   * @param connerSoldoutBuyDto
   * @return
   * @throws BaseException
   * 
   * - 인천공항용 혼잡도 항목 추가(2026.02.04) 
   */
  public ConnerSoldoutBuyResEntity getConnerSoldoutBuy(
		  ConnerSoldoutBuyDto connerSoldoutBuyDto) throws BaseException {
	  try {
		  // 어뎁터 공통 엔티티
		  AdapterCommonEntity adpaterCommonEntity = new AdapterCommonEntity();
		  adpaterCommonEntity.setCoId(connerSoldoutBuyDto.getPv_co_id());
		  adpaterCommonEntity.setShopId(connerSoldoutBuyDto.getPv_shop_id());
		  adpaterCommonEntity.setSteId(connerSoldoutBuyDto.getPv_ste_id());
		  adpaterCommonEntity.setSaleDt("");
		  adpaterCommonEntity.setPosNo("");
		  adpaterCommonEntity.setTranNo("");
		  adpaterCommonEntity.setCashierNo("");
		  adpaterCommonEntity.setSeqNo("");
		  
		  // 응답 엔티티
		  ConnerSoldoutBuyResEntity connerSoldoutBuyResEntity = new ConnerSoldoutBuyResEntity();
		  connerSoldoutBuyResEntity.setErr_cd("S");
		  connerSoldoutBuyResEntity.setErr_msg_ctt("");
		  connerSoldoutBuyResEntity.setInstanceid(connerSoldoutBuyDto.getPv_instanceid());
		  connerSoldoutBuyResEntity.setPv_procid(connerSoldoutBuyDto.getPv_procid());
		  connerSoldoutBuyResEntity.setPv_req_dtm(connerSoldoutBuyDto.getPv_req_dtm());
		  connerSoldoutBuyResEntity.setPv_req_emp_id(connerSoldoutBuyDto.getPv_req_emp_id());
		  
		  // 코너 Sold-Out 및 혼잡도 조회
		  List<ConnerListEntity> listConnerListEntity = null;
		  
		  // param_nm이 null이 아니고 "redis"이면 Redis 조회, null이거나 다른 값이면 DB 조회
		  if(connerSoldoutBuyDto.getParam_nm() != null && "redis".equalsIgnoreCase(connerSoldoutBuyDto.getParam_nm()))
		  {
			  // Redis 조회
			  listConnerListEntity = soldoutRedisCacheService.getCornerBusyFromRedis(
				  connerSoldoutBuyDto.getPv_co_id(),
				  connerSoldoutBuyDto.getPv_shop_id(),
				  connerSoldoutBuyDto.getPv_ste_id()
			  );
			  
			  // Redis 조회 실패 시 DB 조회로 fallback
			  if(listConnerListEntity == null)
			  {
				  log.info("Redis 조회 실패, DB 조회로 fallback - CP009: coId={}, shopId={}, steId={}", 
					  connerSoldoutBuyDto.getPv_co_id(), 
					  connerSoldoutBuyDto.getPv_shop_id(), 
					  connerSoldoutBuyDto.getPv_ste_id());
				  listConnerListEntity = adaterMapper.getFscpsCrnrList(adpaterCommonEntity);
			  }
			  else
			  {
				  log.debug("Redis 조회 성공 - CP009: coId={}, shopId={}, steId={}, 건수={}", 
					  connerSoldoutBuyDto.getPv_co_id(), 
					  connerSoldoutBuyDto.getPv_shop_id(), 
					  connerSoldoutBuyDto.getPv_ste_id(),
					  listConnerListEntity.size());
			  }
		  }
		  else
		  {
			  // param_nm이 null이거나 "redis"가 아니면 DB 조회
			  listConnerListEntity = adaterMapper.getFscpsCrnrList(adpaterCommonEntity);
		  }
		  
		  if(listConnerListEntity != null)
		  {
			  for(int nLoop = 0 ; nLoop < listConnerListEntity.size(); nLoop++)
			  {
				  ConnerListInqEntity connerListInqEntity = new ConnerListInqEntity();
				  connerListInqEntity.setPv_crnr_id(listConnerListEntity.get(nLoop).getCrnrId());
				  connerListInqEntity.setPv_sold_out(listConnerListEntity.get(nLoop).getSoldOut());
				  connerListInqEntity.setPv_busy_tp(listConnerListEntity.get(nLoop).getBusyTp());
				  connerListInqEntity.setPv_busy_set_tp(listConnerListEntity.get(nLoop).getBusySetTp());
				  connerListInqEntity.setPv_inch_busy_tp(listConnerListEntity.get(nLoop).getInchBusyTp());//인천공항용 혼잡도 추가
				  
				  connerSoldoutBuyResEntity.getPv_crnr_list().add(connerListInqEntity);
			  }
		  }
		  
		  return connerSoldoutBuyResEntity;
	  
	  } catch (Exception e) {
			throw new BaseException(e);
	  }
  }
  
  /**
   * 메뉴 SoldOut 조회 (CP010)
   * @param menuSoldOutIqnDto
   * @return
   * @throws BaseException
   */
  public MenuSoldOutIqnResEntity getMenuSoldout(
		  MenuSoldOutIqnDto menuSoldOutIqnDto) throws BaseException {
	  try {
		  // 어뎁터 공통 엔티티
		  ConnerBusySetEntity connerBusySetEntity = new ConnerBusySetEntity();
		  connerBusySetEntity.setCoId(menuSoldOutIqnDto.getPv_co_id());
		  connerBusySetEntity.setShopId(menuSoldOutIqnDto.getPv_shop_id());
		  connerBusySetEntity.setSteId(menuSoldOutIqnDto.getPv_ste_id());
		  connerBusySetEntity.setCrnrId(menuSoldOutIqnDto.getPv_crnr_id());;
		  connerBusySetEntity.setBusyTp("");
		  connerBusySetEntity.setBusySetTp("");
		  
		  // 응답 엔티티
		  MenuSoldOutIqnResEntity menuSoldOutIqnResEntity = new MenuSoldOutIqnResEntity();
		  menuSoldOutIqnResEntity.setErr_cd("S");
		  menuSoldOutIqnResEntity.setErr_msg_ctt("");
		  menuSoldOutIqnResEntity.setInstanceid(menuSoldOutIqnDto.getPv_instanceid());
		  menuSoldOutIqnResEntity.setPv_procid(menuSoldOutIqnDto.getPv_procid());
		  menuSoldOutIqnResEntity.setPv_req_dtm(menuSoldOutIqnDto.getPv_req_dtm());
		  menuSoldOutIqnResEntity.setPv_req_emp_id(menuSoldOutIqnDto.getPv_req_emp_id());
		  
		  List<MenuSoldOutListEntity> listMenuSoldOutListEntity = null;
		  
		  // param_nm이 null이 아니고 "redis"이면 Redis 조회, null이거나 다른 값이면 DB 조회
		  if(menuSoldOutIqnDto.getParam_nm() != null && "redis".equalsIgnoreCase(menuSoldOutIqnDto.getParam_nm()))
		  {
			  // Redis 조회
			  if(menuSoldOutIqnDto.getPv_crnr_id() == null || menuSoldOutIqnDto.getPv_crnr_id().equals(""))
			  {
				  // 일반 메뉴 SoldOut 조회
				  listMenuSoldOutListEntity = soldoutRedisCacheService.getMenuSoldOutFromRedis(
					  menuSoldOutIqnDto.getPv_co_id(),
					  menuSoldOutIqnDto.getPv_shop_id(),
					  menuSoldOutIqnDto.getPv_ste_id()
				  );
			  }
			  else
			  {
				  // 코너 메뉴 SoldOut 조회
				  listMenuSoldOutListEntity = soldoutRedisCacheService.getCornerMenuSoldOutFromRedis(
					  menuSoldOutIqnDto.getPv_co_id(),
					  menuSoldOutIqnDto.getPv_shop_id(),
					  menuSoldOutIqnDto.getPv_ste_id(),
					  menuSoldOutIqnDto.getPv_crnr_id()
				  );
			  }
			  
			  // Redis 조회 실패 시 DB 조회로 fallback
			  if(listMenuSoldOutListEntity == null)
			  {
				  log.info("Redis 조회 실패, DB 조회로 fallback - CP010: coId={}, shopId={}, steId={}, crnrId={}", 
					  menuSoldOutIqnDto.getPv_co_id(), 
					  menuSoldOutIqnDto.getPv_shop_id(), 
					  menuSoldOutIqnDto.getPv_ste_id(),
					  menuSoldOutIqnDto.getPv_crnr_id());
				  
				  if(menuSoldOutIqnDto.getPv_crnr_id() == null || menuSoldOutIqnDto.getPv_crnr_id().equals(""))
				  {
					  listMenuSoldOutListEntity = adaterMapper.getFscpsMenuSoldOutList(connerBusySetEntity);
				  }
				  else
				  {
					  listMenuSoldOutListEntity = adaterMapper.getFscpsConnerMenuSoldOutList(connerBusySetEntity);
				  }
			  }
			  else
			  {
				  log.debug("Redis 조회 성공 - CP010: coId={}, shopId={}, steId={}, crnrId={}, 건수={}", 
					  menuSoldOutIqnDto.getPv_co_id(), 
					  menuSoldOutIqnDto.getPv_shop_id(), 
					  menuSoldOutIqnDto.getPv_ste_id(),
					  menuSoldOutIqnDto.getPv_crnr_id(),
					  listMenuSoldOutListEntity.size());
			  }
		  }
		  else
		  {
			  // param_nm이 null이거나 "redis"가 아니면 DB 조회
			  if(menuSoldOutIqnDto.getPv_crnr_id() == null || menuSoldOutIqnDto.getPv_crnr_id().equals(""))
			  {
				  listMenuSoldOutListEntity = adaterMapper.getFscpsMenuSoldOutList(connerBusySetEntity);
			  }
			  else
			  {
				  listMenuSoldOutListEntity = adaterMapper.getFscpsConnerMenuSoldOutList(connerBusySetEntity);
			  }
		  }
		  
		  if(listMenuSoldOutListEntity != null)
		  {
			  for(int nLoop = 0 ; nLoop < listMenuSoldOutListEntity.size(); nLoop++)
			  {
				  MenuSoldOutInqListEntity menuSoldOutInqListEntity = new MenuSoldOutInqListEntity();
				  menuSoldOutInqListEntity.setPv_menu_cd(listMenuSoldOutListEntity.get(nLoop).getMenuCd());
				  menuSoldOutInqListEntity.setPv_menu_tp(listMenuSoldOutListEntity.get(nLoop).getMenuTp());
				  menuSoldOutInqListEntity.setPv_sold_out(listMenuSoldOutListEntity.get(nLoop).getSoldOut());
				  menuSoldOutIqnResEntity.getPv_menu_list().add(menuSoldOutInqListEntity);
			  }
		  }
		  
		  return menuSoldOutIqnResEntity;
	  
	  } catch (Exception e) {
			throw new BaseException(e);
	  }
  }
  
  /**
   * 트란호출 (CP011)
   * @param tranCallDto
   * @return
   * @throws BaseException
   */
  public TranCallResEntity tranCall(
		  TranCallDto tranCallDto) throws BaseException {
	  try {
		  // 어뎁터 공통 엔티티
		  AdapterCommonEntity adpaterCommonEntity = new AdapterCommonEntity();
		  adpaterCommonEntity.setCoId(tranCallDto.getPv_org_co_id());
		  adpaterCommonEntity.setShopId(tranCallDto.getPv_org_shop_id());
		  adpaterCommonEntity.setSteId(tranCallDto.getPv_org_ste_id());
		  adpaterCommonEntity.setSaleDt(tranCallDto.getPv_org_sale_dt());
		  adpaterCommonEntity.setPosNo(tranCallDto.getPv_org_pos_no());
		  adpaterCommonEntity.setTranNo(tranCallDto.getPv_org_tran_no());
		  adpaterCommonEntity.setCashierNo("");
		  adpaterCommonEntity.setSeqNo("");
		  
		  // 응답 엔티티
		  TranCallResEntity tranCallResEntity = new TranCallResEntity();
		  tranCallResEntity.setErr_cd("S");
		  tranCallResEntity.setErr_msg_ctt("");
		  tranCallResEntity.setInstanceid(tranCallDto.getPv_instanceid());
		  tranCallResEntity.setPv_procid(tranCallDto.getPv_procid());
		  tranCallResEntity.setPv_req_dtm(tranCallDto.getPv_req_dtm());
		  tranCallResEntity.setPv_req_emp_id(tranCallDto.getPv_req_emp_id());
		  tranCallResEntity.setPv_res_cd("");
		  tranCallResEntity.setPv_co_id(tranCallDto.getPv_org_co_id());		  
		  // 트란호출 조회
		  TranCallEntity tranCallEntity = adaterMapper.getFscpsTranLog(adpaterCommonEntity);
		  
		  if(tranCallEntity == null)
		  {
			  tranCallResEntity.setPv_res_cd("0520");
			  tranCallResEntity.setErr_msg_ctt("해당거래 없음");
			  
			  return tranCallResEntity;
		  }
		  
		  if(tranCallEntity.getProcTp().compareTo("1") != 0)
		  {
			  tranCallResEntity.setPv_res_cd("0530");
			  tranCallResEntity.setErr_msg_ctt("매출 미갱신");
			  
			  return tranCallResEntity;
		  }
		  
		  // 트란헤더 조회
		  TranHeaderEntity tranHeaderEntity = adaterMapper.getFscpsTranHeader(adpaterCommonEntity);
		  if(tranHeaderEntity == null)
		  {
			  tranCallResEntity.setPv_res_cd("0530");
			  tranCallResEntity.setErr_msg_ctt("매출 미갱신");
			  
			  return tranCallResEntity;
		  }
		  
		  // 반품여부체크
		  if(tranHeaderEntity.getDealSect().compareTo("1") == 0)
		  {
			  tranCallResEntity.setPv_res_cd("0510");
			  tranCallResEntity.setErr_msg_ctt("반품거래");
			  return tranCallResEntity;
		  }
		
		  // 원거래 반품여부 체크
		  if(tranHeaderEntity.getRfndYn().compareTo("Y") == 0)
		  {
			  tranCallResEntity.setPv_res_cd("0500");
			  tranCallResEntity.setErr_msg_ctt("기환불거래");
			  return tranCallResEntity;
		  }
		
		  tranCallResEntity.setPv_res_cd("0000");
		  tranCallResEntity.setErr_msg_ctt("정상");
		  tranCallResEntity.setPv_org_tran_no("");  // 미사용필드
		  tranCallResEntity.setPv_tran_data(tranCallEntity.getTranData());
		  tranCallResEntity.setPv_tran_data_len(tranCallEntity.getTranDataLen());
		  return tranCallResEntity;
	  
	  } catch (Exception e) {
			throw new BaseException(e);
	  }
  }
  
  /**
   * 거래번호 조회 (CP012)
   * @param tranNoDto
   * @return
   * @throws BaseException
   */
  public TranNoResEntity tranNo(
		  TranNoDto tranNoDto) throws BaseException {
	  try {
		  // 어뎁터 공통 엔티티
		  AdapterCommonEntity adpaterCommonEntity = new AdapterCommonEntity();
		  adpaterCommonEntity.setCoId(tranNoDto.getPv_co_id());
		  adpaterCommonEntity.setShopId(tranNoDto.getPv_shop_id());
		  adpaterCommonEntity.setSteId(tranNoDto.getPv_ste_id());
		  adpaterCommonEntity.setSaleDt(tranNoDto.getPv_sale_dt());
		  adpaterCommonEntity.setPosNo(tranNoDto.getPv_pos_no());
		  adpaterCommonEntity.setTranNo("");
		  adpaterCommonEntity.setCashierNo("");
		  adpaterCommonEntity.setSeqNo("");
		  
		  // 응답 엔티티
		  TranNoResEntity tranNoResEntity = new TranNoResEntity();
		  tranNoResEntity.setErr_cd("S");
		  tranNoResEntity.setErr_msg_ctt("");
		  tranNoResEntity.setInstanceid(tranNoDto.getPv_instanceid());
		  tranNoResEntity.setPv_procid(tranNoDto.getPv_procid());
		  tranNoResEntity.setPv_req_dtm(tranNoDto.getPv_req_dtm());
		  tranNoResEntity.setPv_req_emp_id(tranNoDto.getPv_req_emp_id());
		  tranNoResEntity.setPv_co_id(tranNoDto.getPv_co_id());
		  tranNoResEntity.setPv_res_cd("");
		  
		  // 트란거래번호 조회
		  TranNoEntity tranNoEntity = adaterMapper.getFscpsTranNo(adpaterCommonEntity);
		  
		  if(tranNoEntity == null)
		  {
			  tranNoResEntity.setPv_res_cd("0520");
			  tranNoResEntity.setErr_msg_ctt("해당거래 없음");			  
			  return tranNoResEntity;
		  }
		
		  tranNoResEntity.setPv_res_cd("0000");
		  tranNoResEntity.setErr_msg_ctt("정상");
		  tranNoResEntity.setPv_st_tran_no(tranNoEntity.getStTranNo());
		  tranNoResEntity.setPv_ed_tran_no(tranNoEntity.getEdTranNo());
		  return tranNoResEntity;
	  
	  } catch (Exception e) {
			throw new BaseException(e);
	  }
  }
  
  
  /**
   * 쿠폰조회 (CP013)
   * @param couponInqDto
   * @return
   * @throws BaseException
   */
  @Transactional
  public CouponInqResEntity couponInq(
		  CouponInqDto couponInqDto) throws BaseException {
	  try {
		  // 어뎁터 공통 엔티티
		  AdapterCommonEntity adpaterCommonEntity = new AdapterCommonEntity();
		  adpaterCommonEntity.setCoId(couponInqDto.getPv_co_id());
		  adpaterCommonEntity.setShopId(couponInqDto.getPv_shop_id());
		  adpaterCommonEntity.setSteId(couponInqDto.getPv_ste_id());
		  adpaterCommonEntity.setSaleDt("");
		  adpaterCommonEntity.setPosNo("");
		  adpaterCommonEntity.setTranNo("");
		  adpaterCommonEntity.setCashierNo("");
		  
		  // 쿠폰 엔티티
		  CouponParamEntity couponParamEntity = new CouponParamEntity();
		  couponParamEntity.setCoId(couponInqDto.getPv_co_id());
		  couponParamEntity.setShopId(couponInqDto.getPv_shop_id());
		  couponParamEntity.setSteId(couponInqDto.getPv_ste_id());
		  couponParamEntity.setCponNo(couponInqDto.getPv_cpon_no());
		  couponParamEntity.setInqTp(couponInqDto.getPv_inq_tp());
		  
		  // 응답 엔티티
		  CouponInqResEntity couponInqResEntity = new CouponInqResEntity();
		  couponInqResEntity.setErr_cd("S");
		  couponInqResEntity.setErr_msg_ctt("");
		  couponInqResEntity.setInstanceid(couponInqDto.getPv_instanceid());
		  couponInqResEntity.setPv_procid(couponInqDto.getPv_procid());
		  couponInqResEntity.setPv_req_dtm(couponInqDto.getPv_req_dtm());
		  couponInqResEntity.setPv_req_emp_id(couponInqDto.getPv_req_emp_id());
		  couponInqResEntity.setPv_co_id(couponInqDto.getPv_co_id());
		  couponInqResEntity.setPv_res_cd("");
		  
		  // 시스템 일자 시간 얻음
		  SystemDateTimeEntity systemDateTimeEntity = adaterMapper.getSystemNowDateTime(adpaterCommonEntity);
		  
		  // 쿠폰ID 조회
		  CouponIdEntity couponIdEntity = adaterMapper.getFscpsCouponId(couponParamEntity);
		  if(couponIdEntity == null)
		  {
			  couponInqResEntity.setPv_res_cd("0001");
			  couponInqResEntity.setErr_msg_ctt("해당쿠폰 없음");
			  return couponInqResEntity;
		  }
		  couponIdEntity.setCoId(couponInqDto.getPv_co_id());
		  couponIdEntity.setShopId(couponInqDto.getPv_shop_id());
		  couponIdEntity.setSteId(couponInqDto.getPv_ste_id());
		  couponIdEntity.setPblCponNo(couponInqDto.getPv_cpon_no());		  
		  
		  // 사용점포
		  //List<CouponCountEntity> listCouponCountEntity = adaterMapper.getFscpsAvailableStoreCount(couponIdEntity);
		  int countShop = adaterMapper.getFscpsAvailableStoreCount(couponIdEntity);
		  if(countShop <= 0)
		  {
			  couponInqResEntity.setPv_res_cd("0003");
			  couponInqResEntity.setErr_msg_ctt("사용 불가 점포");
			  return couponInqResEntity;
		  }
		  
		  // 쿠폰명
		  String couponNm  = adaterMapper.getFscpsCouponName(couponIdEntity);
		  
		  // 쿠폰정보조회
		  CouponInfoEntity couponInfoEntity = adaterMapper.getFscpsCouponInfo(couponIdEntity);
		  if(couponInfoEntity == null)
		  {
			  couponInqResEntity.setPv_res_cd("0001");
			  couponInqResEntity.setErr_msg_ctt("해당쿠폰없음");
			  return couponInqResEntity;
		  }
		  
		  // 사용기간체크
		  if(systemDateTimeEntity.getNowDate().compareTo(couponInfoEntity.getUsePsbtStartDt()) < 0 ||
		     systemDateTimeEntity.getNowDate().compareTo(couponInfoEntity.getUsePsbtTmntDt()) > 0)
		  {
			  couponInqResEntity.setPv_res_cd("0002");
			  couponInqResEntity.setErr_msg_ctt("사용기간에러");
			  return couponInqResEntity;
		  }
		  
		  // 쿠폰사용취소, 재사용, 폐기가 아니면서 쿠폰상태가 사용이면 에러 처리
		  if(couponInqDto.getPv_inq_tp().compareTo("2") != 0 && 
			 couponInqDto.getPv_inq_tp().compareTo("3") != 0 &&
			 couponInqDto.getPv_inq_tp().compareTo("9") != 0 &&
			 couponInfoEntity.getCponSttCd().compareTo("5") == 0)
		  {
			  couponInqResEntity.setPv_res_cd("0004");
			  couponInqResEntity.setErr_msg_ctt("이미 사용한 쿠폰");
			  return couponInqResEntity;
		  }
		  
		  // 폐기 쿠폰 체크
		  if(couponInfoEntity.getCponSttCd().compareTo("9") == 0)
		  {
			  couponInqResEntity.setPv_res_cd("0005");
			  couponInqResEntity.setErr_msg_ctt("폐기된 쿠폰");
			  return couponInqResEntity;
		  }
		  
		  // 쿠폰상태 설정
		  if(couponInqDto.getPv_inq_tp().compareTo("1") == 0 ||    // 쿠폰사용
			 couponInqDto.getPv_inq_tp().compareTo("3") == 0)      // 쿠폰재사용
		  {
			  couponIdEntity.setCponSttCd("5");  // 사용
		  }
		  else if(couponInqDto.getPv_inq_tp().compareTo("2") == 0) // 쿠폰사용취소
		  {
			  couponIdEntity.setCponSttCd("0");  // 발행
		  }
		  else if(couponInqDto.getPv_inq_tp().compareTo("9") == 0) // 쿠폰폐기
		  {
			  couponIdEntity.setCponSttCd("9");  // 발행
		  }
		  
		  // 쿠폰조회가 아닐때만 쿠폰상태 갱신
		  if(couponInqDto.getPv_inq_tp().compareTo("0") != 0)
		  {
			  // 쿠폰상태갱신
			  if(adaterMapper.updateCouponState(couponIdEntity) <= 0)
			  {
				  couponInqResEntity.setPv_res_cd("9999");
				  couponInqResEntity.setErr_msg_ctt("쿠폰상태 갱신 실패");
				  return couponInqResEntity;
			  }
		  }
		  
		  couponInqResEntity.setPv_res_cd("0000");
		  couponInqResEntity.setErr_msg_ctt("정상");
		  
		  couponInqResEntity.setPv_dc_type_cd(couponInfoEntity.getDcTypeCD());
		  couponInqResEntity.setPv_dc_amt(couponInfoEntity.getDcAmt());
		  couponInqResEntity.setPv_dc_rate(couponInfoEntity.getDcRate());
		  couponInqResEntity.setPv_cpon_nm(couponNm);
		  couponInqResEntity.setPv_err_msg("정상");
		  return couponInqResEntity;	  
	  } catch (Exception e) {
			throw new BaseException(e);
	  }
  }
    
  /**
   * kgss25 20250211a
   * 스템프적립 유무조회 (CP014)
   * @param checkSavingStampsDto
   * @return
   * @throws BaseException
   */
  @Transactional
  public CheckSavingStampsResEntity checkSavingStamps(
		  CheckSavingStampsDto checkSavingStampsDto) throws BaseException {
	  try {
		  // 어뎁터 공통 엔티티
		  AdapterCommonEntity adpaterCommonEntity = new AdapterCommonEntity();
		  adpaterCommonEntity.setCoId(checkSavingStampsDto.getPv_co_id());
		  adpaterCommonEntity.setShopId(checkSavingStampsDto.getPv_shop_id());
		  adpaterCommonEntity.setSteId(checkSavingStampsDto.getPv_ste_id());
		  adpaterCommonEntity.setSaleDt("");
		  adpaterCommonEntity.setPosNo("");
		  adpaterCommonEntity.setTranNo("");
		  adpaterCommonEntity.setCashierNo("");
		  
		  // 스템프적립 유무 엔티티
		  CheckSavingStampsParamEntity checkSavingStampsParamEntity = new CheckSavingStampsParamEntity();
		  checkSavingStampsParamEntity.setCoId(checkSavingStampsDto.getPv_co_id());
		  checkSavingStampsParamEntity.setShopId(checkSavingStampsDto.getPv_shop_id());
		  checkSavingStampsParamEntity.setSteId(checkSavingStampsDto.getPv_ste_id());
		  checkSavingStampsParamEntity.setApprNo(checkSavingStampsDto.getPv_appr_no());
		  
		  // 응답 엔티티
		  CheckSavingStampsResEntity checkSavingStampsResEntity = new CheckSavingStampsResEntity();
		  checkSavingStampsResEntity.setErr_cd("S");
		  checkSavingStampsResEntity.setErr_msg_ctt("");
		  checkSavingStampsResEntity.setInstanceid(checkSavingStampsDto.getPv_instanceid());
		  checkSavingStampsResEntity.setPv_procid(checkSavingStampsDto.getPv_procid());
		  checkSavingStampsResEntity.setPv_req_dtm(checkSavingStampsDto.getPv_req_dtm());
		  checkSavingStampsResEntity.setPv_req_emp_id(checkSavingStampsDto.getPv_req_emp_id());
		  checkSavingStampsResEntity.setPv_co_id(checkSavingStampsDto.getPv_co_id());
		  checkSavingStampsResEntity.setPv_res_cd("");
		  
		  // 스템프적립 정보조회
		  SavingStampsInfoEntity savingStampsInfoEntity = adaterMapper.getFscpsStampsSavingInfo(checkSavingStampsParamEntity);
		  if(savingStampsInfoEntity != null)
		  {
			  checkSavingStampsResEntity.setPv_res_cd("0001");
			  checkSavingStampsResEntity.setErr_msg_ctt("해당적립정보있음");
			  
			  checkSavingStampsResEntity.setPv_ste_id(savingStampsInfoEntity.getSteId());
			  checkSavingStampsResEntity.setPv_sale_dt(savingStampsInfoEntity.getSaleDt());
			  checkSavingStampsResEntity.setPv_pos_no(savingStampsInfoEntity.getPosNo());
			  checkSavingStampsResEntity.setPv_tran_no(savingStampsInfoEntity.getTranNo());
			  
			  return checkSavingStampsResEntity;
		  }
		  
		  checkSavingStampsResEntity.setPv_res_cd("0000");
		  checkSavingStampsResEntity.setErr_msg_ctt("정상(적립정보없음)");
		  
		  checkSavingStampsResEntity.setPv_ste_id("");
		  checkSavingStampsResEntity.setPv_sale_dt("");
		  checkSavingStampsResEntity.setPv_pos_no("");
		  checkSavingStampsResEntity.setPv_tran_no("");

		  return checkSavingStampsResEntity;	  
	  } catch (Exception e) {
			throw new BaseException(e);
	  }
  }
    
  /**
   * 코너 혼잡도 설정 (CP100)
   * @param connerBusySetDto
   * @return
   * @throws BaseException
   */
  @Transactional
  public AdapterCommonResEntity connerBusySet(
		  ConnerBusySetDto connerBusySetDto) throws BaseException {
	  try {
		  // 코너혼잡도 엔티티
		  ConnerBusySetEntity cnnerBusySetEntity = new ConnerBusySetEntity();
		  cnnerBusySetEntity.setCoId(connerBusySetDto.getPv_co_id());
		  cnnerBusySetEntity.setShopId(connerBusySetDto.getPv_shop_id());
		  cnnerBusySetEntity.setSteId(connerBusySetDto.getPv_ste_id());
		  cnnerBusySetEntity.setCrnrId(connerBusySetDto.getPv_crnr_id());
		  cnnerBusySetEntity.setBusyTp(connerBusySetDto.getPv_busy_tp());
		  cnnerBusySetEntity.setBusySetTp(connerBusySetDto.getPv_busy_set_tp());
		  
		  // 응답 엔티티
		  AdapterCommonResEntity adapterCommonResEntity = new AdapterCommonResEntity();
		  adapterCommonResEntity.setErr_cd("S");
		  adapterCommonResEntity.setErr_msg_ctt("");
		  adapterCommonResEntity.setInstanceid(connerBusySetDto.getPv_instanceid());
		  adapterCommonResEntity.setPv_procid(connerBusySetDto.getPv_procid());
		  adapterCommonResEntity.setPv_req_dtm(connerBusySetDto.getPv_req_dtm());
		  adapterCommonResEntity.setPv_req_emp_id(connerBusySetDto.getPv_req_emp_id());
		  adapterCommonResEntity.setPv_co_id(connerBusySetDto.getPv_co_id());
		  adapterCommonResEntity.setPv_res_cd("");
		  
		  // 코너혼잡도설정
		  if(adaterMapper.updateCornerBusy(cnnerBusySetEntity) <= 0)
		  {
			  adapterCommonResEntity.setPv_res_cd("0701");
			  adapterCommonResEntity.setErr_msg_ctt("해당 코너가 없습니다.");
			  return adapterCommonResEntity;
		  }
		  
		  //코너 혼잡도 설정 변경시 Redis 갱신저장 추가 (인청공항 혼잡도 추가)
		  //1.코너목록 조회-사이트 하위 코너들 리스트  	  
		  AdapterCommonEntity adpaterCommonEntity = new AdapterCommonEntity();
		  adpaterCommonEntity.setCoId(connerBusySetDto.getPv_co_id());
		  adpaterCommonEntity.setShopId(connerBusySetDto.getPv_shop_id());
		  adpaterCommonEntity.setSteId(connerBusySetDto.getPv_ste_id());
		  List<ConnerListEntity> listConnerListEntity = adaterMapper.getFscpsCrnrList(adpaterCommonEntity);
		  		  
		  //2.레디스 저장 추가
		  soldoutRedisCacheService.setCornerBusyToRedis(connerBusySetDto.getPv_co_id(), 
				  										connerBusySetDto.getPv_shop_id(), 
				  										connerBusySetDto.getPv_ste_id(), 
				  										listConnerListEntity);		  
		  
		  adapterCommonResEntity.setPv_res_cd("0000");
		  adapterCommonResEntity.setErr_msg_ctt("정상");
		  return adapterCommonResEntity;	  
	  } catch (Exception e) {
			throw new BaseException(e);
	  }
  }
  
  /**
   * 코너 SoldOut 설정 (CP101)
   * @param connerSoldOutSetDto
   * @return
   * @throws BaseException
   */
  @Transactional
  public AdapterCommonResEntity connerSoldOutSet(
		  ConnerSoldOutSetDto connerSoldOutSetDto) throws BaseException {
	  try {
		  // 코너혼잡도 엔티티
		  AdapterCommonSoldOutSetEntity adapterCommonSoldOutSetEntity = new AdapterCommonSoldOutSetEntity();
		  adapterCommonSoldOutSetEntity.setCoId(connerSoldOutSetDto.getPv_co_id());
		  adapterCommonSoldOutSetEntity.setShopId(connerSoldOutSetDto.getPv_shop_id());
		  adapterCommonSoldOutSetEntity.setSteId(connerSoldOutSetDto.getPv_ste_id());
		  adapterCommonSoldOutSetEntity.setCommonId(connerSoldOutSetDto.getPv_crnr_id());
		  adapterCommonSoldOutSetEntity.setSoldOut(connerSoldOutSetDto.getPv_sold_out());
		  
		  // 응답 엔티티
		  AdapterCommonResEntity adapterCommonResEntity = new AdapterCommonResEntity();
		  adapterCommonResEntity.setErr_cd("S");
		  adapterCommonResEntity.setErr_msg_ctt("");
		  adapterCommonResEntity.setInstanceid(connerSoldOutSetDto.getPv_instanceid());
		  adapterCommonResEntity.setPv_procid(connerSoldOutSetDto.getPv_procid());
		  adapterCommonResEntity.setPv_req_dtm(connerSoldOutSetDto.getPv_req_dtm());
		  adapterCommonResEntity.setPv_req_emp_id(connerSoldOutSetDto.getPv_req_emp_id());
		  adapterCommonResEntity.setPv_co_id(connerSoldOutSetDto.getPv_co_id());
		  adapterCommonResEntity.setPv_res_cd("");
		  
		  // 코너 Sold-Out 설정
		  if(adaterMapper.updateCornerSoldOut(adapterCommonSoldOutSetEntity) <= 0)
		  {
			  adapterCommonResEntity.setPv_res_cd("0701");
			  adapterCommonResEntity.setErr_msg_ctt("해당 코너가 없습니다.");
			  return adapterCommonResEntity;
		  }
		  
		  adapterCommonResEntity.setPv_res_cd("0000");
		  adapterCommonResEntity.setErr_msg_ctt("정상");
		  return adapterCommonResEntity;	  
	  } catch (Exception e) {
			throw new BaseException(e);
	  }
  }
  
  /**
   * 메뉴 SoldOut 설정 (CP102)
   * @param menuSoldoutSetDto
   * @return
   * @throws BaseException
   */
  @Transactional
  public AdapterCommonResEntity menuSoldOutSet(
		  MenuSoldoutSetDto menuSoldoutSetDto) throws BaseException {
	  try {
		  // 코너혼잡도 엔티티
		  AdapterCommonSoldOutSetEntity adapterCommonSoldOutSetEntity = new AdapterCommonSoldOutSetEntity();
		  adapterCommonSoldOutSetEntity.setCoId(menuSoldoutSetDto.getPv_co_id());
		  adapterCommonSoldOutSetEntity.setShopId(menuSoldoutSetDto.getPv_shop_id());
		  adapterCommonSoldOutSetEntity.setSteId(menuSoldoutSetDto.getPv_ste_id());
		  adapterCommonSoldOutSetEntity.setCommonId(menuSoldoutSetDto.getPv_menu_cd());
		  adapterCommonSoldOutSetEntity.setSoldOut(menuSoldoutSetDto.getPv_sold_out());
		  
		  // 응답 엔티티
		  AdapterCommonResEntity adapterCommonResEntity = new AdapterCommonResEntity();
		  adapterCommonResEntity.setErr_cd("S");
		  adapterCommonResEntity.setErr_msg_ctt("");
		  adapterCommonResEntity.setInstanceid(menuSoldoutSetDto.getPv_instanceid());
		  adapterCommonResEntity.setPv_procid(menuSoldoutSetDto.getPv_procid());
		  adapterCommonResEntity.setPv_req_dtm(menuSoldoutSetDto.getPv_req_dtm());
		  adapterCommonResEntity.setPv_req_emp_id(menuSoldoutSetDto.getPv_req_emp_id());
		  adapterCommonResEntity.setPv_co_id(menuSoldoutSetDto.getPv_co_id());
		  adapterCommonResEntity.setPv_res_cd("");
		  
		  // 메뉴 Sold-Out 설정
		  if(adaterMapper.updateMenuSoldOut(adapterCommonSoldOutSetEntity) <= 0)
		  {
			  adapterCommonResEntity.setPv_res_cd("0702");
			  adapterCommonResEntity.setErr_msg_ctt("해당 메뉴가 없습니다.");
			  return adapterCommonResEntity;
		  }
		  
		  adapterCommonResEntity.setPv_res_cd("0000");
		  adapterCommonResEntity.setErr_msg_ctt("정상");
		  return adapterCommonResEntity;	  
	  } catch (Exception e) {
			throw new BaseException(e);
	  }
  }
  
  /**
   * 사원결제 조회 (CP103)
   * @param empPayDto
   * @return
   * @throws BaseException
   */
  public EmpPayResEntity empPay(
		  EmpPayDto empPayDto ) throws BaseException {
	  try {
		  // 파라메타 엔티티
		  EmpPayParamEntity empPayParamEntity = new EmpPayParamEntity();
		  empPayParamEntity.setCoId(empPayDto.getPv_co_id());
		  empPayParamEntity.setShopId(empPayDto.getPv_shop_id());
		  empPayParamEntity.setEmpCardNo(empPayDto.getPv_emp_card_no());
		  
		  // 응답 엔티티
		  EmpPayResEntity empPayResEntity = new EmpPayResEntity();
		  empPayResEntity.setErr_cd("S");
		  empPayResEntity.setErr_msg_ctt("");
		  empPayResEntity.setInstanceid(empPayDto.getPv_instanceid());
		  empPayResEntity.setPv_procid(empPayDto.getPv_procid());
		  empPayResEntity.setPv_req_dtm(empPayDto.getPv_req_dtm());
		  empPayResEntity.setPv_req_emp_id(empPayDto.getPv_req_emp_id());
		  empPayResEntity.setPv_co_id(empPayDto.getPv_co_id());
		  empPayResEntity.setPv_res_cd("");
		  
		  EmpPayEntity empPayEntity = adaterMapper.getEmpPay(empPayParamEntity);
		  if(empPayEntity == null)
		  {
			  empPayResEntity.setPv_res_cd("0520");
			  empPayResEntity.setErr_msg_ctt("사원 없음");
			  return empPayResEntity;	
		  }
		  
		  // 퇴사일자
		  String rsDt = empPayEntity.getRsDt();
		  if(rsDt != null)
		  {
			  rsDt = rsDt.trim();
			  
			  if(rsDt.equals("") == false)
			  {
				  if(empPayEntity.getSysDt().compareTo(rsDt) >= 0)
				  {
					  empPayResEntity.setPv_res_cd("0001");
					  empPayResEntity.setErr_msg_ctt("퇴사한 사원");
					  return empPayResEntity;	
				  }
			  }
		  }
		  
		  // 사원정보설정
		  empPayResEntity.setPv_emp_com_cd(empPayEntity.getEmpComCd());
		  empPayResEntity.setPv_emp_com_nm(empPayEntity.getEmpComNm());
		  empPayResEntity.setPv_emp_dept_cd(empPayEntity.getEmpDeptCd());
		  empPayResEntity.setPv_emp_dept_nm(empPayEntity.getEmpDeptNm());
		  empPayResEntity.setPv_emp_no(empPayEntity.getEmpNo());
		  empPayResEntity.setPv_emp_nm(empPayEntity.getEmpNm());
		  empPayResEntity.setPv_dc_rate(empPayEntity.getDcRate());
		  empPayResEntity.setPv_cred_cust_id(empPayEntity.getCredCustId());
		  
		  empPayResEntity.setPv_res_cd("0000");
		  empPayResEntity.setErr_msg_ctt("정상");
		  return empPayResEntity;	  
	  } catch (Exception e) {
			throw new BaseException(e);
	  }
  }
  
  /**
   * 알림톡 (CP701)
   * @param alimTalkDto
   * @return
   * @throws BaseException
   */
  @Transactional
  public AdapterCommonResEntity alimTalk(
		  AlimTalkDto alimTalkDto) throws BaseException {
	  try {
		  String alimTitle = "";
		  String alimContent = "";
		  
		  String callback = "";
		  String profileKey = "";
		  String templateCode = "";
		
		  // 메시지 Null 체크
		  if(alimTalkDto.getPv_msg1() == null)
		  {
			  alimTalkDto.setPv_msg1("");
		  }
		  if(alimTalkDto.getPv_msg2() == null)
		  {
			  alimTalkDto.setPv_msg2("");
		  }
		  if(alimTalkDto.getPv_msg3() == null)
		  {
			  alimTalkDto.setPv_msg3("");
		  }
		  if(alimTalkDto.getPv_msg4() == null)
		  {
			  alimTalkDto.setPv_msg4("");
		  }
		  if(alimTalkDto.getPv_msg5() == null)
		  {
			  alimTalkDto.setPv_msg5("");
		  }
		  if(alimTalkDto.getPv_msg6() == null)
		  {
			  alimTalkDto.setPv_msg6("");
		  }
		  if(alimTalkDto.getPv_msg7() == null)
		  {
			  alimTalkDto.setPv_msg7("");
		  }
		  if(alimTalkDto.getPv_msg8() == null)
		  {
			  alimTalkDto.setPv_msg8("");
		  }
		  if(alimTalkDto.getPv_msg9() == null)
		  {
			  alimTalkDto.setPv_msg9("");
		  }
		  if(alimTalkDto.getPv_msg10() == null)
		  {
			  alimTalkDto.setPv_msg10("");
		  }
		  
		  // 어뎁터 공통 엔티티
		  AdapterCommonEntity adpaterCommonEntity = new AdapterCommonEntity();
		  adpaterCommonEntity.setCoId(alimTalkDto.getPv_co_id());
		  adpaterCommonEntity.setShopId(alimTalkDto.getPv_shop_id());
		  adpaterCommonEntity.setSteId(alimTalkDto.getPv_ste_id());
		  adpaterCommonEntity.setSaleDt("");
		  adpaterCommonEntity.setPosNo("");
		  adpaterCommonEntity.setTranNo("");
		  adpaterCommonEntity.setCashierNo("");
		  
		  // 알림톡 파라메서
		  AlimTalkParamEntity alimTalkParamEntity = new AlimTalkParamEntity();
		  alimTalkParamEntity.setCoId(alimTalkDto.getPv_co_id());
		  alimTalkParamEntity.setInqTp(alimTalkDto.getPv_inq_tp());
		  
		  // 응답 엔티티
		  AdapterCommonResEntity adapterCommonResEntity = new AdapterCommonResEntity();
		  adapterCommonResEntity.setErr_cd("S");
		  adapterCommonResEntity.setErr_msg_ctt("");
		  adapterCommonResEntity.setInstanceid(alimTalkDto.getPv_instanceid());
		  adapterCommonResEntity.setPv_procid(alimTalkDto.getPv_procid());
		  adapterCommonResEntity.setPv_req_dtm(alimTalkDto.getPv_req_dtm());
		  adapterCommonResEntity.setPv_req_emp_id(alimTalkDto.getPv_req_emp_id());
		  adapterCommonResEntity.setPv_co_id(alimTalkDto.getPv_co_id());
		  adapterCommonResEntity.setPv_res_cd("");
		  
		  // 공통코드 조회
		  CommonCodeEntity commonCodeEntity = adaterMapper.getAlimTalkCommonCode(alimTalkDto.getPv_inq_tp());
		  if(commonCodeEntity != null)
		  {
			  templateCode = commonCodeEntity.getCodeRef1();
			  profileKey = commonCodeEntity.getCodeRef2();
			  callback = commonCodeEntity.getCodeRef3();
		  }
		  
		  // 알림톡 템플릿 조회
		  AlimTalkMsgTempletEntity alimTalkMsgTempletEntity = adaterMapper.getAlimTalkTemplet(alimTalkParamEntity);
		  if(alimTalkMsgTempletEntity == null)
		  {
			  adapterCommonResEntity.setPv_res_cd("0001");
			  adapterCommonResEntity.setErr_msg_ctt("알림톡 템플릿 조회 실패");
			  return adapterCommonResEntity;	  
		  }
		  
		  // 알림톡 템플릿 
		  alimTitle = alimTalkMsgTempletEntity.getTpltTtl();
		  alimContent = alimTalkMsgTempletEntity.getTpltCn();
		  
		  // 알림톡 메시지 변환
		  alimContent = alimContent.replace("#{1}", alimTalkDto.getPv_msg1());
		  alimContent = alimContent.replace("#{2}", alimTalkDto.getPv_msg2());
		  alimContent = alimContent.replace("#{3}", alimTalkDto.getPv_msg3());
		  alimContent = alimContent.replace("#{4}", alimTalkDto.getPv_msg4());
		  alimContent = alimContent.replace("#{5}", alimTalkDto.getPv_msg5());
		  alimContent = alimContent.replace("#{6}", alimTalkDto.getPv_msg6());
		  alimContent = alimContent.replace("#{7}", alimTalkDto.getPv_msg7());
		  alimContent = alimContent.replace("#{8}", alimTalkDto.getPv_msg8());
		  alimContent = alimContent.replace("#{9}", alimTalkDto.getPv_msg9());
		  alimContent = alimContent.replace("#{10}", alimTalkDto.getPv_msg10());
		
		  // 메시지 전송 순번 얻음
		  //Integer kkoMsgSeq = adaterMapper.getKkoMsgSeq(adpaterCommonEntity);
		  //Integer kkoMsgLogSeq = adaterMapper.getKkoMsgLogSeq(adpaterCommonEntity);
		  //if(kkoMsgLogSeq > kkoMsgSeq)
		  //{
		  //	  kkoMsgSeq = kkoMsgLogSeq; 
		  //}
		  
		  // 알림톡 전송
		  AlimTalkSendEntity alimTalkSendEntity = new AlimTalkSendEntity();
		  alimTalkSendEntity.setPhonNo(alimTalkDto.getPv_idt_no());
		  alimTalkSendEntity.setMsgKey("");
		  alimTalkSendEntity.setMsgData(alimContent);
		  alimTalkSendEntity.setFailedMsg(alimContent);
		  alimTalkSendEntity.setFailedSubject(alimTitle);
		  alimTalkSendEntity.setCallBack(callback);
		  alimTalkSendEntity.setProfileKey(profileKey);
		  alimTalkSendEntity.setTemplateCode(templateCode);
		  
		  if(adaterMapper.insertKkoMsg(alimTalkSendEntity) <= 0)
		  {
			  adapterCommonResEntity.setPv_res_cd("0001");
			  adapterCommonResEntity.setErr_msg_ctt("알림톡 저장 실패");
			  return adapterCommonResEntity;	
		  }
		  
		  adapterCommonResEntity.setPv_res_cd("0000");
		  adapterCommonResEntity.setErr_msg_ctt("정상");
		  return adapterCommonResEntity;	  
	  } catch (Exception e) {
			throw new BaseException(e);
	  }
  }
  
  /**
   * 저널 목록 조회 (CP802)
   * @param jnlListInqDto
   * @return
   * @throws BaseException
   */
  public JnlListInqResEntity jlnListInq(
		  JnlListInqDto jnlListInqDto) throws BaseException {
	  try {
		// 어뎁터 공통 엔티티
		  AdapterCommonEntity adpaterCommonEntity = new AdapterCommonEntity();
		  adpaterCommonEntity.setCoId(jnlListInqDto.getPv_co_id());
		  adpaterCommonEntity.setShopId(jnlListInqDto.getPv_shop_id());
		  adpaterCommonEntity.setSteId(jnlListInqDto.getPv_ste_id());
		  adpaterCommonEntity.setSaleDt(jnlListInqDto.getPv_sale_dt());
		  adpaterCommonEntity.setPosNo(jnlListInqDto.getPv_pos_no());
		  adpaterCommonEntity.setTranNo("");
		  adpaterCommonEntity.setCashierNo("");
		  
		  // 저널데이터 엔티티
		  JnlListParamEntity jnlListParamEntity = new JnlListParamEntity();
		  jnlListParamEntity.setCoId(jnlListInqDto.getPv_co_id());
		  jnlListParamEntity.setShopId(jnlListInqDto.getPv_shop_id());
		  jnlListParamEntity.setSteId(jnlListInqDto.getPv_ste_id());
		  jnlListParamEntity.setSaleDt(jnlListInqDto.getPv_sale_dt());
		  jnlListParamEntity.setPosNo(jnlListInqDto.getPv_pos_no());
		  jnlListParamEntity.setStTranNo(jnlListInqDto.getPv_st_tran_no());
		  jnlListParamEntity.setEnTranNo(jnlListInqDto.getPv_en_tran_no());
		  jnlListParamEntity.setReqType(jnlListInqDto.getPv_req_type());
		  
		  // 응답 엔티티
		  JnlListInqResEntity jnlListInqResEntity = new JnlListInqResEntity();
		  jnlListInqResEntity.setErr_cd("S");
		  jnlListInqResEntity.setErr_msg_ctt("");
		  jnlListInqResEntity.setInstanceid(jnlListInqDto.getPv_instanceid());
		  jnlListInqResEntity.setPv_procid(jnlListInqDto.getPv_procid());
		  jnlListInqResEntity.setPv_req_dtm(jnlListInqDto.getPv_req_dtm());
		  jnlListInqResEntity.setPv_req_emp_id(jnlListInqDto.getPv_req_emp_id());
		  jnlListInqResEntity.setPv_co_id(jnlListInqDto.getPv_co_id());
		  jnlListInqResEntity.setPv_res_cd("");
		  
		  // 포스 정보 조회
		  String posNo = adaterMapper.getFscpsCheckPosMaster(adpaterCommonEntity);
		  if(posNo == null || posNo.equals(""))
		  {
			  jnlListInqResEntity.setPv_res_cd("0100");
			  jnlListInqResEntity.setErr_msg_ctt("해당포스 없음");
			  return jnlListInqResEntity;
		  }
		  
		  // 저널 목록 조회
		  List<JnlListEntity> listJnlListEntity =  adaterMapper.getFscpsJnlDataList(jnlListParamEntity);
		  if(listJnlListEntity == null || listJnlListEntity.size() <= 0)
		  {
			  jnlListInqResEntity.setPv_res_cd("0010");
			  jnlListInqResEntity.setErr_msg_ctt("조회데이터없음");
			  return jnlListInqResEntity;
		  }
		  
		  // 저널데이터 응답 Entity 설정
		  for(int nLoop = 0 ; nLoop < listJnlListEntity.size() ; nLoop++)
		  {
			  JnlListInqListEntity jnlListInqListEntity = new JnlListInqListEntity();
			  
			  jnlListInqListEntity.setPv_co_id(listJnlListEntity.get(nLoop).getCoId());
			  jnlListInqListEntity.setPv_shop_id(listJnlListEntity.get(nLoop).getShopId());
			  jnlListInqListEntity.setPv_ste_id(listJnlListEntity.get(nLoop).getSteId());
			  jnlListInqListEntity.setPv_sale_dt(listJnlListEntity.get(nLoop).getSaleDt());
			  jnlListInqListEntity.setPv_pos_no(listJnlListEntity.get(nLoop).getPosNo());
			  jnlListInqListEntity.setPv_tran_no(listJnlListEntity.get(nLoop).getTranNo());
			  jnlListInqListEntity.setPv_sys_dt(listJnlListEntity.get(nLoop).getSysDt());
			  jnlListInqListEntity.setPv_sys_tm(listJnlListEntity.get(nLoop).getSysTm());
			  jnlListInqListEntity.setPv_deal_sect(listJnlListEntity.get(nLoop).getDealSect());
			  jnlListInqListEntity.setPv_deal_type(listJnlListEntity.get(nLoop).getDealType());
			  jnlListInqListEntity.setPv_deal_mode(listJnlListEntity.get(nLoop).getDealMode());
			  jnlListInqListEntity.setPv_actl_sales_amt(listJnlListEntity.get(nLoop).getActlSalesAmt());
			  
			  if(jnlListInqDto.getPv_req_type().equals("2") == true)
			  {
				  jnlListInqListEntity.setPv_jnl_data_len(listJnlListEntity.get(nLoop).getJnlDataLen());
				  jnlListInqListEntity.setPv_jnl_data(listJnlListEntity.get(nLoop).getJnlData());  
			  }
			  else
			  {
				  jnlListInqListEntity.setPv_jnl_data_len("");
				  jnlListInqListEntity.setPv_jnl_data("");  
			  }
			  
			  jnlListInqResEntity.getPv_jnl_list().add(jnlListInqListEntity);
		  }
		  
		  jnlListInqResEntity.setPv_res_cd("0000");
		  jnlListInqResEntity.setErr_msg_ctt("정상");
		  return jnlListInqResEntity;	  
	  } catch (Exception e) {
			throw new BaseException(e);
	  }
  }
  
  /**
   *  부분정산 데이터 조회 (CP803)
   * @param partCalcDataInqDto
   * @return
   * @throws BaseException
   */
  public PartCalcDataInqResEntity partCalcDataInq(
		  PartCalcDataInqDto partCalcDataInqDto) throws BaseException {
	  try {
		  // 파라메타 엔티티
		  PartCalcDataInqParamEntity partCalcDataInqParamEntity = new PartCalcDataInqParamEntity();
		  partCalcDataInqParamEntity.setCoId(partCalcDataInqDto.getPv_co_id());
		  partCalcDataInqParamEntity.setShopId(partCalcDataInqDto.getPv_shop_id());
		  partCalcDataInqParamEntity.setSteId(partCalcDataInqDto.getPv_ste_id());
		  partCalcDataInqParamEntity.setSaleDt(partCalcDataInqDto.getPv_sale_dt());
		  partCalcDataInqParamEntity.setPosNo(partCalcDataInqDto.getPv_pos_no());
		  
		  // 응답 엔티티
		  PartCalcDataInqResEntity partCalcDataInqResEntity = new PartCalcDataInqResEntity();
		  partCalcDataInqResEntity.setErr_cd("S");
		  partCalcDataInqResEntity.setErr_msg_ctt("");
		  partCalcDataInqResEntity.setInstanceid(partCalcDataInqDto.getPv_instanceid());
		  partCalcDataInqResEntity.setPv_procid(partCalcDataInqDto.getPv_procid());
		  partCalcDataInqResEntity.setPv_req_dtm(partCalcDataInqDto.getPv_req_dtm());
		  partCalcDataInqResEntity.setPv_req_emp_id(partCalcDataInqDto.getPv_req_emp_id());
		  partCalcDataInqResEntity.setPv_co_id(partCalcDataInqDto.getPv_co_id());
		  partCalcDataInqResEntity.setPv_res_cd("");
		  
		  List<PartCalcDataInqEntity> listPartCalcDataInqEntity = null;
		  if(partCalcDataInqDto.getPv_inq_tp().compareTo("1") == 0)            // 사이트별 매출
		  {
			  listPartCalcDataInqEntity = adaterMapper.getPartCalcListSite(partCalcDataInqParamEntity);
		  }
		  else if(partCalcDataInqDto.getPv_inq_tp().compareTo("2") == 0)       // 점포별 매출
		  {
			  listPartCalcDataInqEntity = adaterMapper.getPartCalcListShop(partCalcDataInqParamEntity);
		  }
		  else                                                                 // 포스별 매출
		  {
			  listPartCalcDataInqEntity = adaterMapper.getPartCalcListPos(partCalcDataInqParamEntity);
		  }
		  
		  // 부분정산 데이터 값 체크
		  if(listPartCalcDataInqEntity == null || listPartCalcDataInqEntity.size() <= 0)
		  {
			  partCalcDataInqResEntity.setPv_res_cd("0001");
			  partCalcDataInqResEntity.setErr_msg_ctt("데이터 없음");
			  return partCalcDataInqResEntity;	  
		  }
		  
		  // 부분정산데이처 설정
		  for(int nLoop = 0 ; nLoop < listPartCalcDataInqEntity.size() ; nLoop++)
		  {
			  PartCaclDataInqInqEntity partCaclDataInqInqEntity = new PartCaclDataInqInqEntity();
			  
			  partCaclDataInqInqEntity.setPv_act_cd(listPartCalcDataInqEntity.get(nLoop).getActCd());
			  partCaclDataInqInqEntity.setPv_cnt(listPartCalcDataInqEntity.get(nLoop).getSalesCnt());
			  partCaclDataInqInqEntity.setPv_amt(listPartCalcDataInqEntity.get(nLoop).getSalesAmt());
			  
			  partCalcDataInqResEntity.getPv_part_calc_list().add(partCaclDataInqInqEntity);
		  }
		  
		  partCalcDataInqResEntity.setPv_res_cd("0000");
		  partCalcDataInqResEntity.setErr_msg_ctt("정상");
		  return partCalcDataInqResEntity;	  
	  } catch (Exception e) {
			throw new BaseException(e);
	  }
  }
  
  /**
   * 판매원정보조회 (CP804)
   * @param cashierInfoDto
   * @return
   * @throws BaseException
   */
  public CashierInfoInqResEntity cashierInfoInq(
		  CashierInfoInqDto cashierInfoInqDto) throws BaseException {
	  try {
		// 어뎁터 공통 엔티티
		  AdapterCommonEntity adpaterCommonEntity = new AdapterCommonEntity();
		  adpaterCommonEntity.setCoId(cashierInfoInqDto.getPv_co_id());
		  adpaterCommonEntity.setShopId(cashierInfoInqDto.getPv_shop_id());
		  adpaterCommonEntity.setSteId("");
		  adpaterCommonEntity.setSaleDt("");
		  adpaterCommonEntity.setPosNo("");
		  adpaterCommonEntity.setTranNo("");
		  adpaterCommonEntity.setCashierNo("");
		  
		  // 응답 엔티티
		  CashierInfoInqResEntity cashierInfoInqResEntity = new CashierInfoInqResEntity();
		  cashierInfoInqResEntity.setErr_cd("S");
		  cashierInfoInqResEntity.setErr_msg_ctt("");
		  cashierInfoInqResEntity.setInstanceid(cashierInfoInqDto.getPv_instanceid());
		  cashierInfoInqResEntity.setPv_procid(cashierInfoInqDto.getPv_procid());
		  cashierInfoInqResEntity.setPv_req_dtm(cashierInfoInqDto.getPv_req_dtm());
		  cashierInfoInqResEntity.setPv_req_emp_id(cashierInfoInqDto.getPv_req_emp_id());
		  
		  // 판매원정보조회
		  List<CashierInfoInqEntity> listCashierInfoInqEntity = adaterMapper.getFscpsCashierInfoList(adpaterCommonEntity);
		  
		  //  판매원정보 데이터 값 체크
		  if(listCashierInfoInqEntity == null || listCashierInfoInqEntity.size() <= 0)
		  {
			  cashierInfoInqResEntity.setErr_msg_ctt("데이터 없음");
			  return cashierInfoInqResEntity;	  
		  }
		  
		  //  판매원정보 데이처 설정
		  for(int nLoop = 0 ; nLoop < listCashierInfoInqEntity.size() ; nLoop++)
		  {
			  CashierInfoInqListInqEntity cashierInfoInqListInqEntity = new CashierInfoInqListInqEntity();
			  
			  cashierInfoInqListInqEntity.setPv_cashier_no(listCashierInfoInqEntity.get(nLoop).getCashierNo());
			  
			  // 판매원정보
			  String cashierInfo = listCashierInfoInqEntity.get(nLoop).getCashierInfo().trim();
			  if(cashierInfo == null)
			  {
				  cashierInfoInqListInqEntity.setPv_pd_info("");
			  }
			  else
			  {
				  cashierInfo = cashierInfo.trim();
				  
				  if(cashierInfo.equals("") == true)
				  {
					  cashierInfoInqListInqEntity.setPv_pd_info("");
				  }
				  else
				  {
					  cashierInfoInqListInqEntity.setPv_pd_info(UtilsDAmo.dAmoDesc(cashierInfo));
				  }
			  }
			  
			  cashierInfoInqResEntity.getPv_cashier_list().add(cashierInfoInqListInqEntity);
		  }
		  
		  cashierInfoInqResEntity.setErr_msg_ctt("정상");
		  return cashierInfoInqResEntity;	  
	  } catch (Exception e) {
			throw new BaseException(e);
	  }
  }
  
  
  /**
   * 주방장치 정보 조회 (CP805)
   * @param kvsKpsDeviceInfoInqDto
   * @return
   * @throws BaseException
   */
  public KvsKpsDeviceInfoInqResEntity kvskpsInfoInq(
		  KvsKpsDeviceInfoInqDto kvsKpsDeviceInfoInqDto) throws BaseException {
	  try {
		// 어뎁터 공통 엔티티
		  AdapterCommonEntity adpaterCommonEntity = new AdapterCommonEntity();
		  adpaterCommonEntity.setCoId(kvsKpsDeviceInfoInqDto.getPv_co_id());
		  adpaterCommonEntity.setShopId(kvsKpsDeviceInfoInqDto.getPv_shop_id());
		  adpaterCommonEntity.setSteId(kvsKpsDeviceInfoInqDto.getPv_ste_id());
		  adpaterCommonEntity.setSaleDt("");
		  adpaterCommonEntity.setPosNo("");
		  adpaterCommonEntity.setTranNo("");
		  adpaterCommonEntity.setCashierNo("");
		  
		  // 응답 엔티티
		  KvsKpsDeviceInfoInqResEntity kvsKpsDeviceInfoInqResEntity = new KvsKpsDeviceInfoInqResEntity();
		  kvsKpsDeviceInfoInqResEntity.setErr_cd("S");
		  kvsKpsDeviceInfoInqResEntity.setErr_msg_ctt("");
		  kvsKpsDeviceInfoInqResEntity.setInstanceid(kvsKpsDeviceInfoInqDto.getPv_instanceid());
		  kvsKpsDeviceInfoInqResEntity.setPv_procid(kvsKpsDeviceInfoInqDto.getPv_procid());
		  kvsKpsDeviceInfoInqResEntity.setPv_req_dtm(kvsKpsDeviceInfoInqDto.getPv_req_dtm());
		  kvsKpsDeviceInfoInqResEntity.setPv_req_emp_id(kvsKpsDeviceInfoInqDto.getPv_req_emp_id());
		  
		  // 주방장치 정보조회
		  List<KvsKpsDeviceInfoInqEntity> listKvsKpsDeviceInfoInqEntity = adaterMapper.getFscpsKvsKpsInfoList(adpaterCommonEntity);
		  
		  // 주방장치 정보 데이터 값 체크
		  if(listKvsKpsDeviceInfoInqEntity == null || listKvsKpsDeviceInfoInqEntity.size() <= 0)
		  {
			  kvsKpsDeviceInfoInqResEntity.setErr_msg_ctt("데이터 없음");
			  return kvsKpsDeviceInfoInqResEntity;	  
		  }
		  
		  //  판매원정보 데이처 설정
		  for(int nLoop = 0 ; nLoop < listKvsKpsDeviceInfoInqEntity.size() ; nLoop++)
		  {
			  KvsKpsDeviceInfoListInqEntity kvsKpsDeviceInfoListInqEntity = new KvsKpsDeviceInfoListInqEntity();
			  
			  kvsKpsDeviceInfoListInqEntity.setPv_dev_id(listKvsKpsDeviceInfoInqEntity.get(nLoop).getDeviceId());
			  
			  // 주방장치정보
			  String deviceDUpd = listKvsKpsDeviceInfoInqEntity.get(nLoop).getDeviceDUpd();
			  if(deviceDUpd == null)
			  {
				  kvsKpsDeviceInfoListInqEntity.setPv_km_d_upd("");
			  }
			  else
			  {
				  deviceDUpd = deviceDUpd.trim();
				  
				  if(deviceDUpd.equals("") == true)
				  {
					  kvsKpsDeviceInfoListInqEntity.setPv_km_d_upd("");
				  }
				  else
				  {
					  kvsKpsDeviceInfoListInqEntity.setPv_km_d_upd(UtilsDAmo.dAmoDesc(deviceDUpd));
				  }
			  }
			  
			  kvsKpsDeviceInfoInqResEntity.getPv_kdevice_list().add(kvsKpsDeviceInfoListInqEntity);
		  }
		  
		  kvsKpsDeviceInfoInqResEntity.setErr_msg_ctt("정상");
		  return kvsKpsDeviceInfoInqResEntity;	  
	  } catch (Exception e) {
			throw new BaseException(e);
	  }
  }
  
  /**
   * 트란전송 (CP900)
   * @param tranDataSendDto
   * @return
   * @throws BaseException
   */
  @Transactional
  public AdapterCommonResEntity tranDataSend(
		  TranDataSendDto tranDataSendDto) throws BaseException {
	  try {
		  // 트란 데이터 엔티티
		  AdapterCommonDataSendEntity adapterCommonDataSendEntity = new AdapterCommonDataSendEntity();
		  adapterCommonDataSendEntity.setCoId(tranDataSendDto.getPv_co_id());
		  adapterCommonDataSendEntity.setShopId(tranDataSendDto.getPv_shop_id());
		  adapterCommonDataSendEntity.setSteId(tranDataSendDto.getPv_ste_id());
		  adapterCommonDataSendEntity.setSaleDt(tranDataSendDto.getPv_sale_dt());
		  adapterCommonDataSendEntity.setPosNo(tranDataSendDto.getPv_pos_no());
		  adapterCommonDataSendEntity.setTranNo(tranDataSendDto.getPv_tran_no());
		  adapterCommonDataSendEntity.setSysDt(tranDataSendDto.getPv_sys_dt());
		  adapterCommonDataSendEntity.setSysTm(tranDataSendDto.getPv_sys_tm());
		  adapterCommonDataSendEntity.setDealSect(tranDataSendDto.getPv_deal_sect());
		  adapterCommonDataSendEntity.setDealType(tranDataSendDto.getPv_deal_type());
		  adapterCommonDataSendEntity.setDealMode(tranDataSendDto.getPv_deal_mode());
		  adapterCommonDataSendEntity.setActlSalesAmt("0");
		  adapterCommonDataSendEntity.setDataLen(tranDataSendDto.getPv_tran_data_len());
		  adapterCommonDataSendEntity.setDataValue(tranDataSendDto.getPv_tran_data());
		  adapterCommonDataSendEntity.setTrId(""); // kgss25 20250106c
		  
		  // 응답 엔티티
		  AdapterCommonResEntity adapterCommonResEntity = new AdapterCommonResEntity();
		  adapterCommonResEntity.setErr_cd("S");
		  adapterCommonResEntity.setErr_msg_ctt("");
		  adapterCommonResEntity.setInstanceid(tranDataSendDto.getPv_instanceid());
		  adapterCommonResEntity.setPv_procid(tranDataSendDto.getPv_procid());
		  adapterCommonResEntity.setPv_req_dtm(tranDataSendDto.getPv_req_dtm());
		  adapterCommonResEntity.setPv_req_emp_id(tranDataSendDto.getPv_req_emp_id());
		  adapterCommonResEntity.setPv_co_id(tranDataSendDto.getPv_co_id());
		  adapterCommonResEntity.setPv_res_cd("");
		  
		  // 트란 데이터 추가
		  try
		  {
			  if(adaterMapper.insertTranData(adapterCommonDataSendEntity) <= 0)
			  {
				  adapterCommonResEntity.setPv_res_cd("0700");
				  adapterCommonResEntity.setErr_msg_ctt("기존데이터 있음");
				  return adapterCommonResEntity;
			  }
		  }
		  catch(Exception e)
		  {
			  log.info("tranDataSend: {} ", e);	
			  
			  adapterCommonResEntity.setPv_res_cd("0700");
			  adapterCommonResEntity.setErr_msg_ctt("트란데이터 갱신 실패");
			  return adapterCommonResEntity;
		  }
		  
		  adapterCommonResEntity.setPv_res_cd("0000");
		  adapterCommonResEntity.setErr_msg_ctt("정상");
		  return adapterCommonResEntity;	  
	  } catch (Exception e) {
			throw new BaseException(e);
	  }
  }
  
  /**
   * 저널전송 (CP901)
   * @param jnlDataSendDto
   * @return
   * @throws BaseException
   */
  @Transactional
  public AdapterCommonResEntity jlnDataSend(
		  JnlDataSendDto jnlDataSendDto) throws BaseException {
	  try {
		// 저널데이터 엔티티
		  AdapterCommonDataSendEntity adapterCommonDataSendEntity = new AdapterCommonDataSendEntity();
		  adapterCommonDataSendEntity.setCoId(jnlDataSendDto.getPv_co_id());
		  adapterCommonDataSendEntity.setShopId(jnlDataSendDto.getPv_shop_id());
		  adapterCommonDataSendEntity.setSteId(jnlDataSendDto.getPv_ste_id());
		  adapterCommonDataSendEntity.setSaleDt(jnlDataSendDto.getPv_sale_dt());
		  adapterCommonDataSendEntity.setPosNo(jnlDataSendDto.getPv_pos_no());
		  adapterCommonDataSendEntity.setTranNo(jnlDataSendDto.getPv_tran_no());
		  adapterCommonDataSendEntity.setSysDt(jnlDataSendDto.getPv_sys_dt());
		  adapterCommonDataSendEntity.setSysTm(jnlDataSendDto.getPv_sys_tm());
		  adapterCommonDataSendEntity.setDealSect(jnlDataSendDto.getPv_deal_sect());
		  adapterCommonDataSendEntity.setDealType(jnlDataSendDto.getPv_deal_type());
		  adapterCommonDataSendEntity.setDealMode(jnlDataSendDto.getPv_deal_mode());
		  adapterCommonDataSendEntity.setActlSalesAmt(jnlDataSendDto.getPv_actl_sales_amt());
		  adapterCommonDataSendEntity.setDataLen(jnlDataSendDto.getPv_jnl_data_len());
		  adapterCommonDataSendEntity.setDataValue(jnlDataSendDto.getPv_jnl_data());
		  adapterCommonDataSendEntity.setTrId(""); // kgss25 20250106c

		  // 응답 엔티티
		  AdapterCommonResEntity adapterCommonResEntity = new AdapterCommonResEntity();
		  adapterCommonResEntity.setErr_cd("S");
		  adapterCommonResEntity.setErr_msg_ctt("");
		  adapterCommonResEntity.setInstanceid(jnlDataSendDto.getPv_instanceid());
		  adapterCommonResEntity.setPv_procid(jnlDataSendDto.getPv_procid());
		  adapterCommonResEntity.setPv_req_dtm(jnlDataSendDto.getPv_req_dtm());
		  adapterCommonResEntity.setPv_req_emp_id(jnlDataSendDto.getPv_req_emp_id());
		  adapterCommonResEntity.setPv_co_id(jnlDataSendDto.getPv_co_id());
		  adapterCommonResEntity.setPv_res_cd("");
		  
		  // 저널데이터 추가
		  try
		  {
			  if(adaterMapper.insertJnlData(adapterCommonDataSendEntity) <= 0)
			  {
				  adapterCommonResEntity.setPv_res_cd("0700");
				  adapterCommonResEntity.setErr_msg_ctt("기존데이터 있음");
				  return adapterCommonResEntity;
			  }
		  }
		  catch(Exception e)
		  {
			  log.info("jlnDataSend: {} ", e);	
			  
			  adapterCommonResEntity.setPv_res_cd("0700");
			  adapterCommonResEntity.setErr_msg_ctt("저널데이터 갱신 실패");
			  return adapterCommonResEntity;
		  }
		  
		  adapterCommonResEntity.setPv_res_cd("0000");
		  adapterCommonResEntity.setErr_msg_ctt("정상");
		  return adapterCommonResEntity;	  
	  } catch (Exception e) {
			throw new BaseException(e);
	  }
  }
  
  /**
   * kgss25 20241224a
   * 인천공항 트란전송 (CP902)
   * @param tranDataSendDto
   * @return
   * @throws BaseException
   */
  @Transactional
  public AdapterCommonResEntity incheonAirportTranDataSend(
		  TranDataSendDto tranDataSendDto) throws BaseException {
	  try {
		  // 인천공항 트란 데이터 엔티티
		  AdapterCommonDataSendEntity adapterCommonDataSendEntity = new AdapterCommonDataSendEntity();
		  adapterCommonDataSendEntity.setCoId(tranDataSendDto.getPv_co_id());
		  adapterCommonDataSendEntity.setShopId(tranDataSendDto.getPv_shop_id());
		  adapterCommonDataSendEntity.setSteId(tranDataSendDto.getPv_ste_id());
		  adapterCommonDataSendEntity.setSaleDt(tranDataSendDto.getPv_sale_dt());
		  adapterCommonDataSendEntity.setPosNo(tranDataSendDto.getPv_pos_no());
		  adapterCommonDataSendEntity.setTranNo(tranDataSendDto.getPv_tran_no());
		  adapterCommonDataSendEntity.setTrId(tranDataSendDto.getPv_tr_id()); // 전문ID 추가 kgss25 20240106a
		  adapterCommonDataSendEntity.setSysDt(tranDataSendDto.getPv_sys_dt());
		  adapterCommonDataSendEntity.setSysTm(tranDataSendDto.getPv_sys_tm());
		  adapterCommonDataSendEntity.setDealSect(tranDataSendDto.getPv_deal_sect());
		  adapterCommonDataSendEntity.setDealType(tranDataSendDto.getPv_deal_type());
		  adapterCommonDataSendEntity.setDealMode(tranDataSendDto.getPv_deal_mode());
		  adapterCommonDataSendEntity.setActlSalesAmt("0");
		  adapterCommonDataSendEntity.setDataLen(tranDataSendDto.getPv_tran_data_len());
		  adapterCommonDataSendEntity.setDataValue(tranDataSendDto.getPv_tran_data());
		  
		  // 응답 엔티티
		  AdapterCommonResEntity adapterCommonResEntity = new AdapterCommonResEntity();
		  adapterCommonResEntity.setErr_cd("S");
		  adapterCommonResEntity.setErr_msg_ctt("");
		  adapterCommonResEntity.setInstanceid(tranDataSendDto.getPv_instanceid());
		  adapterCommonResEntity.setPv_procid(tranDataSendDto.getPv_procid());
		  adapterCommonResEntity.setPv_req_dtm(tranDataSendDto.getPv_req_dtm());
		  adapterCommonResEntity.setPv_req_emp_id(tranDataSendDto.getPv_req_emp_id());
		  adapterCommonResEntity.setPv_co_id(tranDataSendDto.getPv_co_id());
		  adapterCommonResEntity.setPv_res_cd("");
		  
		  // 인천공항 트란 데이터 추가
		  try
		  {
			  if(adaterMapper.insertIncheonAirportTranData(adapterCommonDataSendEntity) <= 0)
			  {
				  adapterCommonResEntity.setPv_res_cd("0700");
				  adapterCommonResEntity.setErr_msg_ctt("기존데이터 있음");
				  return adapterCommonResEntity;
			  }
		  }
		  catch(Exception e)
		  {
			  log.info("incheonAirportTranDataSend: {} ", e);	
			  
			  adapterCommonResEntity.setPv_res_cd("0700");
			  adapterCommonResEntity.setErr_msg_ctt("인천공항 트란데이터 갱신 실패");
			  return adapterCommonResEntity;
		  }
		  
		  adapterCommonResEntity.setPv_res_cd("0000");
		  adapterCommonResEntity.setErr_msg_ctt("정상");
		  return adapterCommonResEntity;	  
	  } catch (Exception e) {
			throw new BaseException(e);
	  }
  }
  
  
  /**
   * 인천공항 트란호출 (CP903)
   * @param incheonAirportTranCallDto
   * @return
   * @throws BaseException
   */
  public IncheonAirportTranCallResEntity incheonAirportTranCall(
		  IncheonAirportTranCallDto incheonAirportTranCallDto) throws BaseException {
	  try {
		  // 어뎁터 공통 엔티티
		  AdapterCommonEntity adpaterCommonEntity = new AdapterCommonEntity();
		  adpaterCommonEntity.setCoId(incheonAirportTranCallDto.getPv_co_id());
		  adpaterCommonEntity.setShopId(incheonAirportTranCallDto.getPv_shop_id());
		  adpaterCommonEntity.setSteId(incheonAirportTranCallDto.getPv_ste_id());
		  adpaterCommonEntity.setSaleDt(incheonAirportTranCallDto.getPv_sale_dt());
		  adpaterCommonEntity.setPosNo(incheonAirportTranCallDto.getPv_pos_no());
		  adpaterCommonEntity.setTranNo(incheonAirportTranCallDto.getPv_tr_id());
		  adpaterCommonEntity.setCashierNo("");
		  adpaterCommonEntity.setSeqNo("");
		  
		  // 응답 엔티티
		  IncheonAirportTranCallResEntity incheonAirportTranCallResEntity = new IncheonAirportTranCallResEntity();
		  incheonAirportTranCallResEntity.setErr_cd("S");
		  incheonAirportTranCallResEntity.setErr_msg_ctt("");
		  incheonAirportTranCallResEntity.setInstanceid(incheonAirportTranCallDto.getPv_instanceid());
		  incheonAirportTranCallResEntity.setPv_procid(incheonAirportTranCallDto.getPv_procid());
		  incheonAirportTranCallResEntity.setPv_req_dtm(incheonAirportTranCallDto.getPv_req_dtm());
		  incheonAirportTranCallResEntity.setPv_req_emp_id(incheonAirportTranCallDto.getPv_req_emp_id());
		  incheonAirportTranCallResEntity.setPv_res_cd("");
		  incheonAirportTranCallResEntity.setPv_co_id(incheonAirportTranCallDto.getPv_co_id());		  
		  // 트란호출 조회
		  IncheonAirportTranCallEntity incheonAirportTranCallEntity = adaterMapper.getFscpsIncheonAirportTranLog(adpaterCommonEntity);
		  
		  if(incheonAirportTranCallEntity == null)
		  {
			  incheonAirportTranCallResEntity.setPv_res_cd("0010");
			  incheonAirportTranCallResEntity.setErr_msg_ctt("해당거래 없음");
			  
			  return incheonAirportTranCallResEntity;
		  }
		  
//		  if(tranCallEntity.getProcTp().compareTo("1") != 0)
//		  {
//			  incheonAirportTranCallResEntity.setPv_res_cd("0530");
//			  incheonAirportTranCallResEntity.setErr_msg_ctt("매출 미갱신");
//			  
//			  return tranCallResEntity;
//		  }
		  
//		  // 트란헤더 조회
//		  TranHeaderEntity tranHeaderEntity = adaterMapper.getFscpsTranHeader(adpaterCommonEntity);
//		  if(tranHeaderEntity == null)
//		  {
//			  tranCallResEntity.setPv_res_cd("0530");
//			  tranCallResEntity.setErr_msg_ctt("매출 미갱신");
//			  
//			  return tranCallResEntity;
//		  }
//		  
//		  // 반품여부체크
//		  if(tranHeaderEntity.getDealSect().compareTo("1") == 0)
//		  {
//			  tranCallResEntity.setPv_res_cd("0510");
//			  tranCallResEntity.setErr_msg_ctt("반품거래");
//			  return tranCallResEntity;
//		  }
//		
//		  // 원거래 반품여부 체크
//		  if(tranHeaderEntity.getRfndYn().compareTo("Y") == 0)
//		  {
//			  tranCallResEntity.setPv_res_cd("0500");
//			  tranCallResEntity.setErr_msg_ctt("기환불거래");
//			  return tranCallResEntity;
//		  }
		
		  incheonAirportTranCallResEntity.setPv_res_cd("0000");
		  incheonAirportTranCallResEntity.setErr_msg_ctt("정상");
		  incheonAirportTranCallResEntity.setPv_tr_id(incheonAirportTranCallEntity.getTrId());  // 전문ID
		  incheonAirportTranCallResEntity.setPv_sale_dt(incheonAirportTranCallEntity.getSaleDt());  // 영업일자
		  incheonAirportTranCallResEntity.setPv_tran_data(incheonAirportTranCallEntity.getTranData());
		  incheonAirportTranCallResEntity.setPv_tran_data_len(incheonAirportTranCallEntity.getTranDataLen());
		  return incheonAirportTranCallResEntity;
	  
	  } catch (Exception e) {
			throw new BaseException(e);
	  }
  }
}
