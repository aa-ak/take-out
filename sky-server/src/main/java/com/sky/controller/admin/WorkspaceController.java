package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@Slf4j
@Api(tags = "工作台")
@RequestMapping("/admin/workspace")
public class WorkspaceController {
    @Autowired
    private WorkspaceService workspaceService;

    @GetMapping("/overviewOrders")
    @ApiOperation("订单概览")
    public Result<OrderOverViewVO>getOrderOverView(){
        return Result.success(workspaceService.getOrderOverView());
    }

    /**
     * 菜品概览
     * @return
     */
    @GetMapping("/overviewDishes")
    @ApiOperation("菜品概览")
    public Result<DishOverViewVO>getDishOverView(){
        return Result.success(workspaceService.getDishOverView());
    }

    /**
     * 套餐概览
     * @return
     */
    @GetMapping("/overviewSetmeals")
    @ApiOperation("套餐概览")
    public Result<SetmealOverViewVO>getSetMeal()
    {
        return Result.success(workspaceService.getSetmealOverView());
    }

    /**
     * 营业数据
     * @return
     */
    @GetMapping("/businessData")
    @ApiOperation("营业数据")
    public Result<BusinessDataVO>getBusinessData(){
        LocalDate date=LocalDate.now();
        LocalDateTime dateTime1 =LocalDateTime.of(date, LocalTime.MIN) ;
        LocalDateTime dateTime2=LocalDateTime.of(date, LocalTime.MAX);
        return Result.success(workspaceService.getBusinessData(dateTime1,dateTime2));
    }
}
