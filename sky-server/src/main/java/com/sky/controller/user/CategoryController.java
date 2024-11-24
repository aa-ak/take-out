package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.entity.DishFlavor;
import com.sky.result.Result;
import com.sky.service.CatagoryService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userCategoryController")
@Api(tags = "C端分类接口")
@Slf4j
@RequestMapping("/user/category")
public class CategoryController {
    @Autowired
    private CatagoryService catagoryService;

    @ApiOperation("条件查询")
    @GetMapping("/list")
    public Result<List<Category>> getBytype(Integer type)
    {

        List<Category> categories=catagoryService.getByType(type);
        return Result.success(categories);
    }
}