package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

public interface UserService {
    static void wxLogin() {
    }

    /*
        微信用户登陆
         */
    User wxLogin(UserLoginDTO userLoginDTO) throws Exception;
}