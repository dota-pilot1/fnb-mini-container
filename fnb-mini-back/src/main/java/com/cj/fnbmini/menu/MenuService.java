package com.cj.fnbmini.menu;

import com.cj.fnbmini.menu.dto.MenuDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 메뉴 서비스
 *
 * 실무: UserInfoService.selectMenuList()
 * - 실무는 depth별로 3번 쿼리 후 조립
 * - 여기선 한 번 조회 후 parent_id 기준으로 트리 조립 (더 심플)
 */
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuMapper menuMapper;

    /**
     * 전체 메뉴를 트리 구조로 반환
     * flat 리스트 → depth 3 기준 subMenus 재귀 조립
     */
    public List<MenuDto> getMenuTree() {
        // 전체 메뉴 flat 리스트 조회
        List<MenuDto> flatList = menuMapper.selectMenuList();

        // 플랫 리스트에서 최상위 메뉴 제거 후 parent_id 기준으로 그룹핑
        // 이후 최상위 메뉴 아이디를 기준으로 그룹핑된 리스트에서 자식 메뉴 조립하기 위한 준비
        Map<Long, List<MenuDto>> groupByParent = flatList.stream()
                .filter(m -> m.getParentId() != null)
                .collect(Collectors.groupingBy(MenuDto::getParentId));

        // depth 3 기준으로 최상위 메뉴만 필터링 후 재귀로 subMenus 조립
        return flatList.stream()
                .filter(m -> m.getDepth() == 3)
                .peek(m -> attachSubMenus(m, groupByParent))
                .collect(Collectors.toList());
    }

    /** 전체 flat 리스트 반환 (메뉴 관리 페이지용) */
    public List<MenuDto> getMenuList() {
        return menuMapper.selectMenuList();
    }

    /** 메뉴 저장 (신규/수정) */
    public void saveMenu(MenuDto menuDto) {
        if (menuDto.getId() == null) {
            menuMapper.insertMenu(menuDto);
        } else {
            menuMapper.updateMenu(menuDto);
        }
    }

    /** 메뉴 삭제 */
    public void deleteMenu(Long id) {
        menuMapper.deleteMenu(id);
    }

    /** 재귀로 subMenus 조립 */
    private void attachSubMenus(MenuDto parent, Map<Long, List<MenuDto>> groupByParent) {
        List<MenuDto> children = groupByParent.getOrDefault(parent.getId(), new ArrayList<>());
        parent.setSubMenus(children);
        children.forEach(child -> attachSubMenus(child, groupByParent));
    }
}
