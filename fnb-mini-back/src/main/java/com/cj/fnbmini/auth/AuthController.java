package com.cj.fnbmini.auth;

import com.cj.fnbmini.auth.dto.LoginReqDto;
import com.cj.fnbmini.auth.dto.LoginResDto;
import com.cj.fnbmini.auth.dto.SignupReqDto;
import com.cj.fnbmini.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResDto> login(@RequestBody LoginReqDto req) {
        return ApiResponse.ok(authService.login(req));
    }

    @PostMapping("/signup")
    public ApiResponse<Void> signup(@RequestBody SignupReqDto req) {
        authService.signup(req);
        return ApiResponse.ok(null, "회원가입이 완료되었습니다.");
    }
}
