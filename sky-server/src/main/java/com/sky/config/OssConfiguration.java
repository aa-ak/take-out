package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Slf4j
/*
配置类，用于创建alioss对象
 */
public class OssConfiguration {

//    @ConditionalOnBean
    @Bean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties)
    {
        log.info("创建阿里云文件上传工具类对象:{}",aliOssProperties);
         return new AliOssUtil(aliOssProperties.getEndpoint(),
                 aliOssProperties.getAccessKeyId(),
                 aliOssProperties.getAccessKeySecret(),
                 aliOssProperties.getBucketName());
    }
}
