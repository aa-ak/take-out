package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;


import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
   

    OrderSubmitVO submit(OrderDTO orderDTO) throws Exception;

    void repeat(Long id);

    OrderVO orderDetail(Long id);

    void cancel(Long id);

    PageResult history(Integer status, Integer page, Integer pageSize);

    void reminder(Long id);

    PageResult getSearch(LocalDateTime beginTime, LocalDateTime endTime, Integer status, String number, String phone, Integer page, Integer pageSize);

    void delivery(Long id);

    void confirm(OrdersConfirmDTO OrdersConfirmDTO);

    void rejection(OrdersRejectionDTO ordersRejectionDTO);

    void complete(Long id);

    OrderVO orderDetailwithOrder(Long id);

    OrderStatisticsVO statistics();

    void Admincancel(OrdersCancelDTO ordersCancelDTO);

    OrderPaymentVO paymentSkip(OrdersPaymentDTO ordersPaymentDTO) ;

    void paySuccess(String outTradeNo);


//    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;
//    public void paySuccess(String outTradeNo);
}
