package com.cj.freshway.fs.cps.system.systemmng.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
 * @author jangjaehyun
 * @description
 *
 *              <pre>
 * 프로그램관리 테이블 DTO
 *              </pre>
 */
@Data
@ToString(callSuper = true)
public class PgmMngDto {

  @Schema(description = "순번")
  private String rowNum;

  @Schema(description = "프로그램코드")
  private String progCd;

  @Schema(description = "프로그램명")
  private String progNm;

  @Schema(description = "프로그램레벨")
  private int progLvl;

  @Schema(description = "프로그램내부순번")
  private String progNo;

  @Schema(description = "프로그램ID")
  private String progUrl;

  @Schema(description = "프로그램URL")
  private String progUrlArgs;

  @Schema(description = "사용여부")
  private String useYn;

  private String userId;
  private String status;
}
