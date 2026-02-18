package com.cj.fnbmini.menu;

import com.cj.fnbmini.menu.dto.MenuDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {

    /** 전체 메뉴 flat 조회 (use_yn='Y', sort_order 정렬) */
    List<MenuDto> selectMenuList();

    /** 단건 조회 */
    MenuDto selectMenuById(Long id);

    /** 메뉴 등록 */
    void insertMenu(MenuDto menuDto);

    /** 메뉴 수정 */
    void updateMenu(MenuDto menuDto);

    /** 메뉴 삭제 */
    void deleteMenu(Long id);
}
