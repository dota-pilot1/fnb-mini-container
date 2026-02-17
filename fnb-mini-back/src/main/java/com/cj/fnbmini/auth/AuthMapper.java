package com.cj.fnbmini.auth;

import com.cj.fnbmini.auth.entity.AppUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {

    AppUser findByUserId(String userId);

    int insertUser(AppUser user);

    int countByUserId(String userId);
}
