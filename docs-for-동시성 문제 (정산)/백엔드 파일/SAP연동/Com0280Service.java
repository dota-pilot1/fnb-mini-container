package com.cj.freshway.fs.cps.common.interfacesap;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cj.freshway.fs.common.base.BaseException;
import com.cj.freshway.fs.cps.closing.closingmng.ClosingAdjMapper;
import com.cj.freshway.fs.cps.closing.closingmng.dto.ClosingAdjDto;
import com.cj.freshway.fs.cps.closing.closingmng.dto.ShopGdohRegDto;
import com.cj.freshway.fs.cps.closing.closingmng.entity.ShopGdohRegEntity;
import com.cj.freshway.fs.cps.closing.closingmng.exception.ShopGdohRegException;
import com.cj.freshway.fs.cps.common.interfacesap.dto.GdohResultDto;
import com.cj.freshway.fs.cps.common.interfacesap.dto.SapStatusDto;
import com.cj.freshway.fs.cps.common.interfacesap.dto.SearchDto;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author doojin.lee
 * @description
 *
 *              <pre>
 *
 *              </pre>
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class Com0280Service {

  @Autowired
  Environment message;

  /*
   * !!중요!! Spring AOP(Proxy) self invocation 시 transaction 이슈가 있음으로 반드시 self injection 형태로 invoke
   * 한다.
   */
  @Autowired
  Com0280Service self;

  @Autowired
  SearchGdohMapper searchGdohMapper;

  @Autowired
  ClosingAdjMapper closingAdjMapper;

  /**
   *
   * @author doojin.lee
   * @param SearchDto
   * @return
   * @throws BaseException
   * @description
   *
   *              <pre>
   * 전송용 시재 List 조회
   *              </pre>
   */
  public List<GdohResultDto> selectGdohList(SearchDto dto) throws BaseException {
	try {
		List<GdohResultDto> response = searchGdohMapper.selectGdohList(dto);

	    return response;
	} catch (Exception e) {
		throw new BaseException(e);
	}
  }

  /**
   *
   * @author doojin.lee
   * @param SearchDto
   * @return
   * @throws BaseException
   * @description
   *
   *              <pre>
   * 삭제 전송용 시재 List 조회
   *              </pre>
   */
  public List<GdohResultDto> selectGdohDelList(SearchDto dto) throws BaseException {
	try {
		List<GdohResultDto> response = searchGdohMapper.selectGdohDelList(dto);

	    return response;
	} catch (Exception e) {
		throw new BaseException(e);
	}
  }

  /**
   *
   * @author doojin.lee
   * @param SearchDto
   * @return
   * @throws BaseException
   * @description
   *
   *              <pre>
   * 취소 전송용 시재 List 조회
   *              </pre>
   */
  public List<GdohResultDto> selectGdohCnclList(SearchDto dto) throws BaseException {
	try {
		List<GdohResultDto> response = searchGdohMapper.selectGdohCnclList(dto);

	    return response;
	} catch (Exception e) {
		throw new BaseException(e);
	}
  }

  /**
   *
   * @author doojin.lee
   * @param SearchDto
   * @return
   * @throws BaseException
   * @description
   *
   *              <pre>
   * 시재 전송 결과 저장
   *              </pre>
   */
  @Transactional
  public void updateGdohStatus(SapStatusDto sapStatusDto) throws BaseException {
	try {
		searchGdohMapper.updateGdohStatus(sapStatusDto);

	} catch (Exception e) {
		throw new BaseException(e);
	}
  }

  @Transactional
  public void insertTCBOM002(SapStatusDto dto) throws BaseException {
	try {
		searchGdohMapper.insertTCBOM002(dto);

	} catch (Exception e) {
		throw new BaseException(e);
	}
  }

  @Transactional
  public void deleteTCBOM002(SapStatusDto dto) throws BaseException {
	try {
		searchGdohMapper.deleteTCBOM002(dto);

	} catch (Exception e) {
		throw new BaseException(e);
	}
  }

  @Transactional
  public void updateSuccess(SapStatusDto dto) throws BaseException {
	try {
		searchGdohMapper.updateSuccess(dto);

	} catch (Exception e) {
		throw new BaseException(e);
	}
  }

  @Transactional
  public void updateFail(SapStatusDto dto) throws BaseException {
	try {
		searchGdohMapper.updateFail(dto);

	} catch (Exception e) {
		throw new BaseException(e);
	}
  }

  /**
   *
   * @author doojin.lee
   * @param SapStatusDto
   * @return
   * @throws BaseException
   * @description
   *
   *              <pre>
   * 통합결제 일마감 저장
   *              </pre>
   */
  @Transactional
  public void updateClosingGdoh(ClosingAdjDto dto) throws BaseException {
    try {
      closingAdjMapper.insertClosingAdjReg(dto);
    } catch (Exception e) {
      throw new BaseException(e);
    }
  }

  /**
   *
   * @author doojin.lee
   * @param SapStatusDto
   * @return
   * @throws BaseException
   * @description
   *
   *              <pre>
   * I-Fresh 일마감 저장
   *              </pre>
   */
  @Transactional
  public void gdohBalAplyProcedure(ClosingAdjDto dto) throws BaseException {
	try {
		searchGdohMapper.gdohBalAplyProcedure(dto);

	} catch (Exception e) {
		throw new BaseException(e);
	}
  }

    /**
     *
     * @author junho.song
     * @param SearchDto
     * @return
     * @throws BaseException
     * @description
     *
     *              <pre>
     * 시재 전표코드 채번
     *              </pre>
     */
    public String getTcbom002No(SearchDto dto) throws BaseException {
        try {
            String slipNo = searchGdohMapper.getTcbom002No(dto);

            return slipNo;
        } catch (Exception e) {
            throw new BaseException(e);
        }
    }

}
