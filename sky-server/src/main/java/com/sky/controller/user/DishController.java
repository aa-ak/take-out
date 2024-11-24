package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.CatagoryService;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "c端分类接口")
@Slf4j
@RequestMapping("user/dish")
@RestController("userDishController")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;
    @ApiOperation("根据ID分类查询")
    @GetMapping("/list")
    public Result<List<DishVO>> getCategory(Long categoryId)
    {

//        构造red is中的key,规则 dish_分类ID
        String key="dish_"+categoryId;

        //查询redis中是否存在菜品数据
       List<DishVO> list= (List<DishVO>) redisTemplate.opsForValue().get(key);
        //如果存在 直接返回 无需查询数据库

         //修改操作时，需要清理缓存数据
        //新增菜品
        //删除菜品
         //起售停售后缓存数据也要清理掉
        if(list!=null&&list.size()>0)
        {
            return Result.success(list);
        }
        //如果不存在 查询数据库，将查询到的数据放入redis中

        List<DishVO> dishVO=dishService.UsergetById(categoryId);
        redisTemplate.opsForValue().set(key,dishVO);
        return Result.success(dishVO);
    }
}
