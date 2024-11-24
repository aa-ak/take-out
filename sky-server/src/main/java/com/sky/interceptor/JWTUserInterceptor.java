package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JWTUserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handle)
    {

        System.out.println("当前线程的ID"+Thread.currentThread().getId());
        if(!(handle instanceof HandlerMethod))
        {
            return  true;
        }
        String token = request.getHeader(jwtProperties.getUserTokenName());
        try {
            log.info("jwt校验",token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            BaseContext.setCurrentId(userId);
            log.info("当前userID: ",userId);
            return  true;
        }catch (Exception e)
        {
            response.setStatus(401);
            return false;
        }
    }

}
