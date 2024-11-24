package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CumboService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "套餐相关")
@RequestMapping("/admin/setmeal")
@Slf4j
public class CumboContoller {
    @Autowired
    private CumboService cumboService;

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public Result<PageResult> getPage( SetmealPageQueryDTO setmealPageQueryDTO)
    {
        log.info("套餐分页查询,{}",setmealPageQueryDTO);
        PageResult result=cumboService.getpage(setmealPageQueryDTO);
        return Result.success(result);
    }
    @CachePut(cacheNames = "setmeal",key = "#setmealDTO.categoryId")
    @ApiOperation("新增套餐")
    @PostMapping
    public Result save(@RequestBody SetmealDTO setmealDTO)
    {
        log.info("新增套餐");
        cumboService.save(setmealDTO);
        return Result.success();
    }
    @CacheEvict(cacheNames = "setmeal",allEntries = true)
    @ApiOperation("套餐起售、停售")
    @PostMapping("/status/{status}")
    public Result startAndstop(@PathVariable Long status,Long id)
    {
        log.info("套餐起售、停售,{},{}",status,id);
        cumboService.startAndstop(status,id);
        return Result.success();
    }
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询套餐")
    public Result<SetmealVO>getById(@PathVariable Long id)
    {
        log.info("根据ID查询套餐,{}",id);
        SetmealVO setmealVO=cumboService.getById(id);
        return Result.success(setmealVO);
    }
    @CacheEvict(cacheNames = "setmeal",allEntries = true)
    @ApiOperation("批量删除套餐")
    @DeleteMapping
    public Result deleteBatch(@RequestParam  List<Long> ids)
    {
        log.info("批量删除，{}",ids);
        cumboService.deleteBatch(ids);
        return Result.success();
    }
   @CacheEvict(cacheNames = "setmeal",allEntries = true)
   @PutMapping
   @ApiOperation("修改套餐")
   public Result alter(@RequestBody SetmealDTO setmealDTO)
   {
       log.info("修改套餐");
       cumboService.alter(setmealDTO);
       return  Result.success();
   }
}
