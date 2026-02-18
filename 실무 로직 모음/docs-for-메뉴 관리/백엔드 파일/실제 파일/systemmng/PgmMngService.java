package com.cj.freshway.fs.cps.system.systemmng;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import com.cj.freshway.fs.common.base.GridResponse;
import com.cj.freshway.fs.cps.system.systemmng.dto.PgmMngDto;
import com.cj.freshway.fs.cps.system.systemmng.exception.PgmMngException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class PgmMngService {

  @Autowired
  PgmMngService self;

  @Autowired
  PgmMngMapper pgmMngMapper;


  /**
   * @author jangjaehyun
   * @param PgmMngDto
   * @return
   * @throws PgmMngException
   * @description
   *
   *              <pre>
   * 프로그램관리 목록 조회
   *              </pre>
   */
  public GridResponse<Integer, Integer, Object> selectPgmMngLst(PgmMngDto dto)
      throws PgmMngException {
    Integer count = 0;

    try {
      List<PgmMngDto> list = pgmMngMapper.selectPgmMngLst(dto);

      if (!ObjectUtils.isEmpty(list)) {
        count = list.size();
      }

      GridResponse<Integer, Integer, Object> response = new GridResponse<>();
      response.setLastRow(count);
      response.setLastPage(0);
      response.setData(list);
      return response;
    } catch (Exception e) {
      throw new PgmMngException(e);
    }
  }

  /**
   * 프로그램관리 저장
   *
   * @param PgmMngDto
   * @returnsss
   */
  @Transactional
  public void insertPgmMngLst(List<PgmMngDto> pgmMngDtoLst) throws PgmMngException {
    for (PgmMngDto dto : pgmMngDtoLst) {
      if (dto.getStatus().equals("N")) {
        self.insertPgmMng(dto);
      } else {
        self.updatePgmMng(dto);
      }
    }
  }

  /**
   * 프로그램관리 저장
   *
   * @param PgmMngDto
   * @returnsss
   */
  @Transactional
  public void insertPgmMng(PgmMngDto pgmMngDto) throws PgmMngException {
    pgmMngMapper.insertPgmMng(pgmMngDto);
    pgmMngMapper.insertPgmMngLangKo(pgmMngDto);
    pgmMngMapper.insertPgmMngLangEn(pgmMngDto);
  }

  /**
   * 프로그램관리 수정
   *
   * @param PgmMngDto
   * @returnsss
   */
  @Transactional
  public void updatePgmMng(PgmMngDto pgmMngDto) throws PgmMngException {
    pgmMngMapper.updatePgmMng(pgmMngDto);
    pgmMngMapper.updatePgmMngLangKo(pgmMngDto);
    pgmMngMapper.updatePgmMngLangEn(pgmMngDto);
  }
}

