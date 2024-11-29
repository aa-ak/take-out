package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface UserMapper {

    Integer getUser(LocalDateTime dateTime1, LocalDateTime dateTime2);
    @Select("select * from user where openid=#{openid}")
    User getOpenid(String openid);

    @Select("select * from user where id=#{userId}")
    User getById(Long userId);


    void insert(User user);

    @Select("select count(distinct openid) from user")
    Integer getUser1();

    @Select("select count(distinct openid) from user where create_time between #{localDate1} and #{localDate2}")
    Integer getUserBydate(LocalDateTime localDate1, LocalDateTime localDate2);
}
