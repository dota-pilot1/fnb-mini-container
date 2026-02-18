package com.cj.freshway.fs.cps.closing.closingmng;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.cj.freshway.fs.common.base.BaseException;
import com.cj.freshway.fs.common.base.GridResponse;
import com.cj.freshway.fs.cps.closing.closingmng.dto.GdohRegCnclAprvDto;
import com.cj.freshway.fs.cps.closing.closingmng.entity.GdohRegCnclAprvEntity;
import com.cj.freshway.fs.cps.closing.closingmng.exception.GdohRegCnclAprvException;
import com.cj.freshway.fs.cps.common.interfacesap.SI_COM0280_FS_ClientSynchronousModule;
import com.cj.freshway.fs.cps.common.interfacesap.dto.SearchDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class GdohRegCnclAprvService {
  @Autowired
  GdohRegCnclAprvService self;

  @Autowired
  GdohRegCnclAprvMapper gdohRegCnclAprvMapper;
  
  @Autowired
  SI_COM0280_FS_ClientSynchronousModule module;

	/**
	 * @author Sungsik.Jung
	 * @param GdohRegCnclAprvDto
	 * @return
	 * @throws GdohRegCnclAprvException
	 * @description
	 *
	 * <pre>
	 * 시재등록 상세내역 목록 조회
	 * </pre>
	*/
	public GridResponse<Integer, Integer, Object> selectGdohRegCnclAprvList(GdohRegCnclAprvDto dto) throws GdohRegCnclAprvException {
		try {
			int count = 0;
			List<GdohRegCnclAprvEntity> list = gdohRegCnclAprvMapper.selectGdohRegCnclAprvList(dto);
	
			if (!ObjectUtils.isEmpty(list)) {
				count = list.size();
			}
	
			GridResponse<Integer, Integer, Object> response = new GridResponse<>();
			response.setLastRow(count);
			response.setLastPage(0);
			response.setData(list);
			
			return response;
		} catch (Exception e) {
			log.info(e.getMessage());
			throw new GdohRegCnclAprvException(e);
	    }
	}
	
	/**
	* <pre>
	* 시재 승인/반려
	* </pre>
	* 
	* @author Sungsik.Jung
	* @return List<CrnrGrupMng>
	* @throws BaseException 
	* 
	*/
	@Transactional
	public void updateGdohRegCnclAprvList(GdohRegCnclAprvEntity gdohRegCnclAprvEntity) throws BaseException {
		try {
			List<GdohRegCnclAprvEntity> reqAprvList = gdohRegCnclAprvEntity.getGdohAprvList();
			
			// 레코드별 처리
			for (GdohRegCnclAprvEntity  reqAprv : reqAprvList) {
				if ("2".equals(reqAprv.getAckSttsCd())) {
					gdohRegCnclAprvMapper.updateGdohRegAprvList(reqAprv);
				} else if ("3".equals(reqAprv.getAckSttsCd())) {
					gdohRegCnclAprvMapper.updateGdohCnclAprvList(reqAprv);
				}
				
				if ("2".equals(reqAprv.getAckSttsCd())) {
					// SAP I/F
				    SearchDto inParams = new SearchDto();
				    inParams.setCoId(reqAprv.getCoId());
				    inParams.setShopId(reqAprv.getShopId());
				    inParams.setSteId(reqAprv.getSteId());
				    inParams.setSalesDt(reqAprv.getOccrDt());
				    inParams.setUserId(reqAprv.getRegrId());
				    inParams.setGdohSlipNo(reqAprv.getGdohSlipNo());
				    inParams.setDelYN("N");				    
				    inParams.setCancelYN("N");				    
				    if ("6".equals(reqAprv.getSapSndSttsCd())) {
				    	inParams.setCancelYN("Y");
				    } else {
				    	inParams.setCancelYN("N");
				    }
				    inParams.setCrtTypeCd("M");
				    module.moduleCallEAI(inParams);
				}
			}
			
		} catch (GdohRegCnclAprvException e) {
			log.info(e.getMessage());
			throw new GdohRegCnclAprvException(e);
		}
	}
}

