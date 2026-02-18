package com.cj.fnbmini.menu.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 메뉴 응답 DTO
 *
 * 실무: MenuDto.java (biz/common/userInfo/)
 * - 동일한 재귀 구조 (subMenus)
 * - 실무는 depth를 prog_lvl-3 으로 변환(0,1,2) 하지만 여기선 3,4,5 그대로 사용
 */
@Getter
@Setter
public class MenuDto {
    private Long id;
    private Long parentId;
    private int depth;
    private String menuName;
    private String componentName;   // depth 5만 값 있음
    private int sortOrder;
    private String useYn;
    private List<MenuDto> subMenus; // 하위 메뉴 재귀 구조
}
