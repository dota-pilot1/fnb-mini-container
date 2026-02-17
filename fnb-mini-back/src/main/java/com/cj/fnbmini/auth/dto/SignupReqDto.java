package com.cj.fnbmini.auth.dto;

import lombok.Data;

@Data
public class SignupReqDto {
    private String userId;
    private String password;
    private String username;
}
