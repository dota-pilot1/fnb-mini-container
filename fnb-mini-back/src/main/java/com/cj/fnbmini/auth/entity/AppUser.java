package com.cj.fnbmini.auth.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppUser {
    private Long id;
    private String userId;
    private String password;
    private String username;
    private LocalDateTime createdAt;
}
