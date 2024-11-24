package com.sky.controller.user;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CumboService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userCumboContoller")
@Api(tags = "套餐相关")
@RequestMapping("/user/setmeal")
@Slf4j
public class CumboContoller {
    @Autowired
    private CumboService cumboService;

    @GetMapping("/list")
    @ApiOperation("根据分类ID查询包含的菜品")
    public Result<List<Setmeal>> getByid(Long categoryId)
    {
        List<Setmeal>setmeals=cumboService.getByCategoryId(categoryId);
        return Result.success(setmeals);
    }
    @ApiOperation("根据套餐ID查询包含的菜品")
    @GetMapping("/dish/{id}")
    public Result<List<DishItemVO>> getById(@PathVariable Long id)
    {
       List<DishItemVO> setmeals=cumboService.getSetmealById(id);
       return Result.success(setmeals);
    }

}