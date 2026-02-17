package com.cj.freshway.fs.cps.closing.closingmng;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import com.cj.freshway.fs.common.base.BaseException;
import com.cj.freshway.fs.common.base.GridResponse;
import com.cj.freshway.fs.cps.closing.closingmng.dto.CrdtCardAckGdohRegDto;
import com.cj.freshway.fs.cps.closing.closingmng.entity.CrdtCardAckGdohRegEntity;
import com.cj.freshway.fs.cps.closing.closingmng.exception.CrdtCardAckGdohRegException;
import com.cj.freshway.fs.cps.common.interfacesap.SI_COM0280_FS_ClientSynchronousModule;
import com.cj.freshway.fs.cps.common.interfacesap.dto.SearchDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class CrdtCardAckGdohRegService {

  @Autowired
  CrdtCardAckGdohRegService self;

  @Autowired
  CrdtCardAckGdohRegMapper cardAckGdohRegMapper;

  @Autowired
  SI_COM0280_FS_ClientSynchronousModule module;

  /**
   * @author jangjaehyun
   * @param CrdtCardAckGdohRegDto
   * @return
   * @throws CrdtCardAckGdohRegException
   * @description
   *
   *              <pre>
   * 신용카드 승인정보 조회
   *              </pre>
   */
  public GridResponse<Integer, Integer, Object> crdtCardAckGdohRegLst(CrdtCardAckGdohRegDto dto)
      throws CrdtCardAckGdohRegException {
    Integer count = 0;

    try {
      List<CrdtCardAckGdohRegEntity> list = cardAckGdohRegMapper.crdtCardAckGdohRegLst(dto);

      if (!ObjectUtils.isEmpty(list)) {
        count = list.size();
      }

      GridResponse<Integer, Integer, Object> response = new GridResponse<>();
      response.setLastRow(count);
      response.setLastPage(0);
      response.setData(list);
      return response;
    } catch (Exception e) {
      throw new CrdtCardAckGdohRegException(e);
    }
  }

  /**
   * @author jangjaehyun
   * @param CrdtCardAckGdohRegDto
   * @return
   * @throws CrdtCardAckGdohRegException
   * @description
   *
   *              <pre>
   * 신용카드 고객 조회
   *              </pre>
   */
  public GridResponse<Integer, Integer, Object> crdtCardAckCustLst(CrdtCardAckGdohRegDto dto)
      throws CrdtCardAckGdohRegException {
    Integer count = 0;

    try {
      List<CrdtCardAckGdohRegEntity> list = cardAckGdohRegMapper.crdtCardAckCustLst(dto);

      if (!ObjectUtils.isEmpty(list)) {
        count = list.size();
      }

      GridResponse<Integer, Integer, Object> response = new GridResponse<>();
      response.setLastRow(count);
      response.setLastPage(0);
      response.setData(list);
      return response;
    } catch (Exception e) {
      throw new CrdtCardAckGdohRegException(e);
    }
  }

  /**
   * @author jangjaehyun
   * @param CrdtCardAckGdohRegDto
   * @return
   * @throws CrdtCardAckGdohRegException
   * @description
   *
   *              <pre>
   * 신용카드 전기코드 조회
   *              </pre>
   */
  public GridResponse<Integer, Integer, Object> crdtCardAckWaccLst(CrdtCardAckGdohRegDto dto)
      throws CrdtCardAckGdohRegException {
    Integer count = 0;

    try {
      List<CrdtCardAckGdohRegEntity> list = cardAckGdohRegMapper.crdtCardAckWaccLst(dto);

      if (!ObjectUtils.isEmpty(list)) {
        count = list.size();
      }

      GridResponse<Integer, Integer, Object> response = new GridResponse<>();
      response.setLastRow(count);
      response.setLastPage(0);
      response.setData(list);
      return response;
    } catch (Exception e) {
      throw new CrdtCardAckGdohRegException(e);
    }
  }

  /**
   * @author jangjaehyun
   * @param CrdtCardAckGdohRegDto
   * @return
   * @throws CrdtCardAckGdohRegException
   * @description
   *
   *              <pre>
   * 신용카드 단말기 조회
   *              </pre>
   */
  public GridResponse<Integer, Integer, Object> crdtCardAckVanLst(CrdtCardAckGdohRegDto dto)
      throws CrdtCardAckGdohRegException {
    Integer count = 0;

    try {
      List<CrdtCardAckGdohRegEntity> list = cardAckGdohRegMapper.crdtCardAckVanLst(dto);

      if (!ObjectUtils.isEmpty(list)) {
        count = list.size();
      }

      GridResponse<Integer, Integer, Object> response = new GridResponse<>();
      response.setLastRow(count);
      response.setLastPage(0);
      response.setData(list);
      return response;
    } catch (Exception e) {
      throw new CrdtCardAckGdohRegException(e);
    }
  }

  /**
   * 신용카드 승인내역 리스트 저장
   *
   * @param List<CrdtCardAckGdohRegDto>
   * @throws BaseException
   * @returnsss
   */
  @Transactional
  // public void insertCrdtCardAckLst(List<CrdtCardAckGdohRegDto> dtoLst)
  // throws CrdtCardAckGdohRegException {
  // for (CrdtCardAckGdohRegDto dto : dtoLst) {
  // self.insertCrdtCardAck(dto);
  // }
  // }
  public void insertCrdtCardAckLst(CrdtCardAckGdohRegDto dto) throws BaseException {
    dto.setSalesGdohWaccCdNullChk("Y");
    List<CrdtCardAckGdohRegEntity> list = cardAckGdohRegMapper.crdtCardAckGdohRegLst(dto);

    if (!ObjectUtils.isEmpty(list)) {
      throw new CrdtCardAckGdohRegException("ALREADY_ISSUE", "고객, 전기코드가 모두 지정되어야 전송 가능합니다.");
    } else {
      self.insertCrdtCardAck(dto);
    }
  }

  /**
   * 신용카드 승인내역 저장
   *
   * @param List<CrdtCardAckGdohRegDto>
   * @throws BaseException
   * @returnsss
   */
  @Transactional
  public void insertCrdtCardAck(CrdtCardAckGdohRegDto dto) throws BaseException {

    // SAP 취소 I/F
    SearchDto inParams = new SearchDto();
    inParams.setCoId(dto.getCoId());
    inParams.setShopId(dto.getShopId());
    inParams.setSteId(dto.getSteId());
    inParams.setSalesDt(dto.getCardAckDt());
    inParams.setUserId(dto.getUserId());
    inParams.setDelYN("Y");
    inParams.setCancelYN("N");
    inParams.setCrtTypeCd("C");
    module.moduleCallEAI(inParams);
    // List<Map<String, String>> rtnData = module.moduleCallEAI(inParams);

    // SAP I/F
    cardAckGdohRegMapper.insertCrdtCardAck(dto);

    // SearchDto inParams = new SearchDto();
    inParams.setCoId(dto.getCoId());
    inParams.setShopId(dto.getShopId());
    inParams.setSteId(dto.getSteId());
    inParams.setSalesDt(dto.getCardAckDt());
    inParams.setUserId(dto.getUserId());
    inParams.setDelYN("N");
    inParams.setCancelYN("N");
    inParams.setCrtTypeCd("C");
    module.moduleCallEAI(inParams);
  }

  /**
   * 신용카드 고객사 시재내역 리스트 저장
   *
   * @param List<CrdtCardAckGdohRegDto>
   * @returnsss
   */
  @Transactional
  public void insertCrdtCustWaccLst(List<CrdtCardAckGdohRegDto> dtoLst)
      throws CrdtCardAckGdohRegException {
    for (CrdtCardAckGdohRegDto dto : dtoLst) {
      if (dto.getCustId().length() < 5) {
        dto.setSteId(dto.getCustId());
        dto.setCustId(null);
      } else {
        dto.setSteId(null);
      }
      self.insertCrdtCustWacc(dto);
    }
  }

  /**
   * 신용카드 고객사 시재내역 저장
   *
   * @param List<CrdtCardAckGdohRegDto>
   * @returnsss
   */
  @Transactional
  public void insertCrdtCustWacc(CrdtCardAckGdohRegDto dto) throws CrdtCardAckGdohRegException {
    cardAckGdohRegMapper.insertCrdtCustWacc(dto);
  }
}

