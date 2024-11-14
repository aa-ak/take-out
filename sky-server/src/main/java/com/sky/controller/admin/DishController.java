package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Api(tags = "菜品相关接口")
@RequestMapping("/admin/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @ApiOperation("修改套餐")
    @RequestMapping
    public Result edit(@RequestBody Dish dish)
    {
//        DishService.edit(dish);
        return Result.success();
    }
    @PutMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO)
    {
        log.info("新增菜品{}",dishDTO);
        dishService.savewithFlavor(dishDTO);
        return Result.success();





    }

}

