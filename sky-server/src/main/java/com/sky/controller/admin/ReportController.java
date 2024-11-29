package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.time.LocalDate;

@RestController
@Api(tags = "数据统计")
@RequestMapping("/admin/report")
@Slf4j

public class ReportController {
    @Autowired
    private ReportService reportService;

    /**
     * 营业额统计
     * @param
     * @param
     * @return
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额统计")
    public Result<TurnoverReportVO> turnoverReport( @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin, @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end) {

      log.info("营业额统计:{},{}",begin,end);
       return Result.success( reportService.getTurnOverStart(begin,end));
    }

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/userStatistics")
    @ApiOperation("用户统计")
    public Result<UserReportVO> userReportVO(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin, @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end) {
        log.info("用户统计:{},{}",begin,end);
        return Result.success( reportService.getUserReport(begin,end));
    }
    @GetMapping("/ordersStatistics")
    @ApiOperation("订单统计")
    public Result<OrderReportVO> orderReportVO(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin, @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end) {
        log.info("订单统计:{},{}",begin,end);
        return Result.success( reportService.getOrderReport(begin,end));
    }
    @GetMapping("/top10")
    @ApiOperation("热销商品10")
    public Result<SalesTop10ReportVO> top10(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin, @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end) {
        log.info("热销商品10:{},{}",begin,end);
        return Result.success( reportService.getSalesTop10(begin,end));
    }

    /**
     * 导出Excel表格
     * @param response
     */
    @GetMapping("/export")
    @ApiOperation("导出Excel表格")
    public void export(HttpServletResponse response) throws FileNotFoundException {

        log.info("导出Excel表格");
        reportService.exportBusinessData(response);
    }

}
