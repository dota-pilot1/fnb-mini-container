package com.cj.freshway.fs.cps.system.systemmng;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cj.freshway.fs.common.base.BaseException;
import com.cj.freshway.fs.common.base.GridResponse;
import com.cj.freshway.fs.cps.common.base.FscpsBaseController;
import com.cj.freshway.fs.cps.system.systemmng.dto.PgmMngDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 프로그램관리 Controller
 * </pre>
 *
 * @author jangjaehyun
 */
@Slf4j
@RestController
@RequestMapping("/api/system/systemmng") // //api/base/vanmstmng/v1.0/selectVanMstMngLst
@Tag(name = "cps/system/systemmng", description = "cps/system/systemmng")
public class PgmMngController extends FscpsBaseController {
  @Autowired
  PgmMngService pgmMngService;

  /**
   * <pre>
   * 프로그램관리 리스트
   * </pre>
   *
   * @author jangjaehyun
   * @param PgmMngDto
   * @return
   * @throws BaseException
   */
  @PostMapping("v1.0/pgmMngLst")
  public GridResponse<Integer, Integer, Object> selectPgmMngLst(@RequestBody PgmMngDto dto)
      throws BaseException {
    log.info("selectPgmMngLst : {}", dto);
    return pgmMngService.selectPgmMngLst(dto);
  }

  /**
   * <pre>
   * 프로그램관리 리스트 저장
   * </pre>
   *
   * @author jangjaehyun
   * @param PgmMngDto
   * @return
   * @throws BaseException
   */
  @PostMapping("v1.0/insertPgmMngLst")
  public void insertPgmMngLst(@RequestBody List<PgmMngDto> pgmMngDtoLst) throws BaseException {
    pgmMngService.insertPgmMngLst(pgmMngDtoLst);
  }

}
