package com.cj.fnbmini.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class LoginResDto {
    private String accessToken;
    private Map<String, Object> userInfo;
}
