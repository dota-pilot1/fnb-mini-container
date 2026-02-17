package com.cj.fnbmini.auth.dto;

import lombok.Data;

@Data
public class LoginReqDto {
    private String userId;
    private String password;
}
