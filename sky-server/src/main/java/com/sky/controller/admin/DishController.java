package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@Api(tags = "菜品相关接口")
@RequestMapping("/admin/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;


    @ApiOperation("修改套餐")
    @RequestMapping
    public Result edit(@RequestBody Dish dish)
    {
//        DishService.edit(dish);
        return Result.success();
    }
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO)
    {
        log.info("新增菜品{}",dishDTO);
        dishService.savewithFlavor(dishDTO);

        String key="dish_"+dishDTO.getCategoryId();
        redisTemplate.delete(key);
        return Result.success();

        //清理缓存数据



    }
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> getPage(DishPageQueryDTO dishPageQueryDTO)
    {
        log.info("菜品分页查询");
        PageResult result=dishService.getPage(dishPageQueryDTO);
        return Result.success(result);
    }
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result deleteBatch(@RequestParam List<Long> ids)
    {
           log.info("菜品批量删除");
           dishService.deleteBatch(ids);
           //将所有菜品缓存全删掉
           Set keys = redisTemplate.keys("dish_*");
           redisTemplate.delete(keys);
           return Result.success();
    }
    @PutMapping
    @ApiOperation("修改菜品")
    public Result alterDish(@RequestBody DishDTO dishDTO)
    {
        log.info("修改菜品");
        dishService.alterDish(dishDTO);

        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        return Result.success();
    }
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询菜品")
    public Result<DishVO> getById(@PathVariable Long id)
    {
        DishVO dishVO=dishService.getById(id);
        return Result.success(dishVO);
    }
    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售、停售")
    public Result startAndstop(@PathVariable Long status,Long id)
    {
        log.info("菜品起售，停售;{},{}",status,id);
        dishService.startAndstop(status,id);
//        Set keys = redisTemplate.keys("dish_*");
//        redisTemplate.delete(keys);

        cleanCache("dish_*");
        return Result.success();
    }
    @GetMapping("/list")
    @ApiOperation("根据分类ID查询菜品")
    public Result<List<DishVO>>getByTypeId(Long categoryId)
    {
        log.info("根据分类ID查询菜品");
         List<DishVO> dish=dishService.UsergetById(categoryId);
         return Result.success(dish);
    }

    private void cleanCache(String pattern)
    {
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}

