package com.sky.controller.admin;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CatagoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.sky.dto.CategoryDTO;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/category")
@Api(tags="分类管理相关接口")
public class CatagoryController {
    @Autowired
    private CatagoryService catagoryService;

    @ApiOperation("修改分类")
    @PutMapping
    public Result alterType(@RequestBody CategoryDTO categoryDTO)
    {
        log.info("编辑分类信息");
        catagoryService.alter(categoryDTO);
        return Result.success();
    }
    @GetMapping("/page")
    @ApiOperation("分类分页查询")
    public Result<PageResult> getCatagory(CategoryPageQueryDTO categoryPageQueryDTO)
    {
         PageResult result=catagoryService.getCatagory(categoryPageQueryDTO);
         return Result.success(result);
    }
    @PostMapping("/status/{status}")
    @ApiOperation("启用，禁用分类")
    public Result EnableDisanable(@PathVariable Integer status,Long id)
    {
        catagoryService.enableDisanable(status,id);
        return Result.success();
    }
    @PostMapping
    @ApiOperation("新增分类")
    public Result InsertCategory(@RequestBody CategoryDTO categoryDTO)
    {
        catagoryService.inserCategory(categoryDTO);
        return Result.success();
    }
    @DeleteMapping
    @ApiOperation("根据ID删除分类")
    public Result deleteById(Long id)
    {

        catagoryService.delete(id);
        return Result.success();
    }
    @GetMapping("/list")
    public Result<List<Category>> getById(Integer type)
    {
        log.info("类型ID分类");
       List<Category> categories=catagoryService.queryType(type);
        return Result.success(categories);
    }

}
