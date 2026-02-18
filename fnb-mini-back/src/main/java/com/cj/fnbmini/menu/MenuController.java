package com.cj.fnbmini.menu;

import com.cj.fnbmini.common.dto.ApiResponse;
import com.cj.fnbmini.menu.dto.MenuDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    /**
     * 메뉴 트리 반환 (Header/LeftMenu 렌더링용)
     * depth 3 기준 subMenus 재귀 구조
     */
    @GetMapping("/tree")
    public ApiResponse<List<MenuDto>> tree() {
        return ApiResponse.ok(menuService.getMenuTree());
    }

    /**
     * 메뉴 flat 목록 (메뉴 관리 페이지용)
     */
    @GetMapping("/list")
    public ApiResponse<List<MenuDto>> list() {
        return ApiResponse.ok(menuService.getMenuList());
    }

    /**
     * 메뉴 저장 (신규/수정)
     */
    @PostMapping("/save")
    public ApiResponse<Void> save(@RequestBody MenuDto menuDto) {
        menuService.saveMenu(menuDto);
        return ApiResponse.ok(null, "저장되었습니다.");
    }

    /**
     * 메뉴 삭제
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return ApiResponse.ok(null, "삭제되었습니다.");
    }
}
