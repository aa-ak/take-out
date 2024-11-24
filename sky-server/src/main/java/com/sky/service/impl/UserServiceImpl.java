package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.result.PageResult;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    /**
     *
     * @param userLoginDTO
     * @return
     */
    @Autowired
    private WeChatProperties weChatProperties;
    public  static  final  String wx_lOGIN="https://api.weixin.qq.com/sns/jscode2session";
    
    @Autowired 
    private UserMapper userMapper;
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) throws Exception {
        //获取openID
        CloseableHttpClient http = HttpClients.createDefault();
        HttpGet httpGet=new HttpGet(wx_lOGIN);
        String openid = getopenid(userLoginDTO.getCode());
        if (openid==null )
        {
            throw new Exception(MessageConstant.LOGIN_FAILED);
        }
        User user = userMapper.getOpenid(openid);
        if(user==null)
        {
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        //判断openID是否为空，如果为空表示登陆失败，抛出业务异常


        //判断当前用户是否为新用户

        //如果是新用户，自动完成注册

        //返回这个用户对象




        return user;
    }
    private String getopenid(String code)
    {
        Map<String, String> map=new HashMap<>();

        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String s = HttpClientUtil.doGet(wx_lOGIN, map);

        JSONObject jsonObject = JSON.parseObject(s);//获得了jsonoobject对象

        String openid = jsonObject.getString("openid");
        return openid;
    }
}
