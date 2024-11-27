package com.sky.controller.user;

import com.sky.dto.OrderDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController("userOrderController")
@Api(tags = "订单管理")
@RequestMapping("user/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * @param orderDTO
     * @return
     */
    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> sumbit(@RequestBody OrderDTO orderDTO) throws Exception {
        log.info("用户下单{}",orderDTO);
        OrderSubmitVO orderSubmitVO=orderService.submit(orderDTO);
        return Result.success(orderSubmitVO);
    }
    @PostMapping("/repetition/{id}")
    @ApiOperation("再来一单")
    public Result repeat(@PathVariable Long id)
    {
        log.info("再来一单{}",id);
        orderService.repeat(id);
        return Result.success();
    }

    /**
     * 订单支付
     */
//    @PutMapping("payment")
//    @ApiOperation("支付")
//    public Result payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO)
//    {
//        log.info("支付{}",ordersPaymentDTO);
//        OrderPaymentVO ordersPaymentVo=orderService.payment(ordersPaymentDTO);
//        log.info("生成预支付订单",ordersPaymentVo);
//        return Result.success();
//    }



    @PutMapping("/payment")
    @ApiOperation("支付")
    public Result<OrderPaymentVO>payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO)
    {
        log.info("支付{}",ordersPaymentDTO);
        OrderPaymentVO ordersPaymentVo=orderService.paymentSkip(ordersPaymentDTO);
        log.info("生成预支付订单",ordersPaymentVo);
        return Result.success(ordersPaymentVo);

    }

    /**
     * 订单详情
     * @param id
     * @return
     */
    @GetMapping("/orderDetail/{id}")
    @ApiOperation("订单详情")
    public Result<OrderVO> orderDetail(@PathVariable Long id)
    {
        log.info("订单详情{}",id);
        OrderVO orderVO=orderService.orderDetail(id);
        return Result.success();
    }

    /**
     * 取消订单
     * @param id
     * @return
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation("取消订单")
    public Result cancel(@PathVariable Long id)
    {
        log.info("取消订单{}",id);
        orderService.cancel(id);
        return Result.success();
    }
    /**
     * 历史订单
     * @param status
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/historyOrders")
    @ApiOperation("历史订单")
    public Result<PageResult> history(Integer status,Integer page,Integer pageSize)
    {
        log.info("历史订单{}",status);
        PageResult pageResult=orderService.history(status,page,pageSize);
        return Result.success(pageResult);

    }

    @GetMapping("/reminder/{id}")
    @ApiOperation("催单")
    public Result reminder(@PathVariable Long id)
    {
        log.info("催单{}",id);
        orderService.reminder(id);
        return Result.success();
    }



}
