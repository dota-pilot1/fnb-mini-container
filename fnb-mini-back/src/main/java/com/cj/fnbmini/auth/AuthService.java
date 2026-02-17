package com.cj.fnbmini.auth;

import com.cj.fnbmini.auth.dto.LoginReqDto;
import com.cj.fnbmini.auth.dto.LoginResDto;
import com.cj.fnbmini.auth.dto.SignupReqDto;
import com.cj.fnbmini.auth.entity.AppUser;
import com.cj.fnbmini.common.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthMapper authMapper;
    private final JwtUtil jwtUtil;

    public LoginResDto login(LoginReqDto req) {
        AppUser user = authMapper.findByUserId(req.getUserId());
        if (user == null || !user.getPassword().equals(req.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.generateToken(user.getUserId(), user.getUsername());

        return LoginResDto.builder()
                .accessToken(token)
                .userInfo(Map.of(
                        "userId", user.getUserId(),
                        "username", user.getUsername()
                ))
                .build();
    }

    public void signup(SignupReqDto req) {
        if (authMapper.countByUserId(req.getUserId()) > 0) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        AppUser user = new AppUser();
        user.setUserId(req.getUserId());
        user.setPassword(req.getPassword());
        user.setUsername(req.getUsername());
        authMapper.insertUser(user);
    }
}
