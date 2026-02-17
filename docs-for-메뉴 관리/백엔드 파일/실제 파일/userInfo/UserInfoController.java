package com.cj.freshway.fs.biz.common.userInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cj.freshway.fs.biz.common.userInfo.dto.AuthDto;
import com.cj.freshway.fs.biz.common.userInfo.dto.MenuDto;
import com.cj.freshway.fs.biz.common.userInfo.dto.ShopSearchDto;
import com.cj.freshway.fs.biz.common.userInfo.dto.UserInfoDto;
import com.cj.freshway.fs.biz.common.userInfo.entity.NotificationHist;
import com.cj.freshway.fs.biz.common.userInfo.exception.UserInfoException;
import com.cj.freshway.fs.common.base.GridResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 사용자 정보(메뉴, 권한, 정보) 컨트롤러
 * </pre>
 *
 * @author MunYoungJun <yj.moon82@kt.com>
 */
@Slf4j
@RestController
@RequestMapping("api/useInfo")
@Tag(name = "useInfo", description = "useInfo API")
public class UserInfoController {

  @Autowired
  UserInfoService userInfoService;

  /**
   * <pre>
   * 사용자 정보 조회(사용자 정보, 메뉴 조회 ,권한 조회)
   * </pre>
   *
   * @param HttpServletRequest req
   * @return Map<String, Object>
   * @throws UserInfoException UserInfoException
   * @author MunYoungJun <yj.moon82@kt.com>
   */
  @PostMapping("v1.0")
  public Map<String, Object> userInfo(HttpServletRequest req) throws UserInfoException {


    Map<String, Object> returnMap = new HashMap<String, Object>();

    // 토큰 정보 추출
    String userId = userInfoService.tokenUserId(req);

    log.info("authentication 검증 추출: {}", userId);


    // 사용자 정보 세팅
    UserInfoDto userInfo = userInfoService.selectUserInfo(userId);

    // 메뉴 정보 세팅
    List<MenuDto> menuList = userInfoService.selectMenuList(userId);

    returnMap.put("menuList", menuList);

    // 프로그램 권한 조회
    List<AuthDto> progAuthList = userInfoService.selectAuthList(userId);

    returnMap.put("progAuthList", progAuthList);

    // 점포 조회
    userInfo.setShopList(userInfoService.selectShopList(userId));
    // 점포 조회
    userInfo.setSiteList(userInfoService.selectSiteList(userId));

    returnMap.put("userInfo", userInfo);

    return returnMap;
  }



  /**
   * <pre>
   * 점포 조회(사용자 전환)
   * </pre>
   *
   * @param request HttpServletRequest
   * @return List<PopupMng>
   * @throws PopupException popupException
   * @author MunYoungJun <yj.moon82@kt.com>
   */
  @PostMapping("v1.0/shopAllList")
  public GridResponse<Integer, Integer, Object> selectShopAllList(HttpServletRequest request,
      @RequestBody ShopSearchDto shopSearchDto) throws UserInfoException {
    return userInfoService.selectShopAllList(shopSearchDto);
  }

  /**
   * <pre>
   * 알림 이력 조회
   * </pre>
   *
   * @param request HttpServletRequest
   * @return List<NotificationHist>
   * @throws UserInfoException userInfoException
   * @author MunYoungJun <yj.moon82@kt.com>
   */
  @GetMapping("v1.0/notificationHistList")
  public List<NotificationHist> selectNotificationHistList(HttpServletRequest request,
      NotificationHist notificationHist) throws UserInfoException {
    return userInfoService.selectNotificationHistList(request, notificationHist);
  }

  /**
   * <pre>
   * 알림 이력 등록
   * </pre>
   *
   * @param request HttpServletRequest
   * @return seq
   * @throws UserInfoException userInfoException
   * @author MunYoungJun <yj.moon82@kt.com>
   */
  @PostMapping("v1.0/insertNotificationHist")
  public long insertNotificationHist(HttpServletRequest request,
      @RequestBody NotificationHist notificationHist) throws UserInfoException {

    long seq = userInfoService.insertNotificationHist(request, notificationHist);

    return seq;
  }

  /**
   * <pre>
   * 알림 이력 수정
   * </pre>
   *
   * @param request HttpServletRequest
   * @return void
   * @throws UserInfoException userInfoException
   * @author MunYoungJun <yj.moon82@kt.com>
   */
  @PostMapping("v1.0/updateNotificationHist")
  public void updatePopupMng(HttpServletRequest request,
      @RequestBody NotificationHist notificationHist) throws UserInfoException {

    userInfoService.updateNotificationHist(request, notificationHist);

  }
}
