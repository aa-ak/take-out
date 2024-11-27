package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j

public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    @Scheduled(cron = "0 * * * * ?")//每分钟触发一次
    public void processTimeOut() {
      log.info("处理超时订单:{}", LocalDateTime.now());

      LocalDateTime time=LocalDateTime.now().plusMinutes(-15);
      List<Orders> ordersList=orderMapper.getProceeTimeOut(Orders.PENDING_PAYMENT, time);
      if(ordersList!=null&&ordersList.size()>0)
      {
          ordersList.forEach(
                  orders -> {
                        log.info("订单超时:{}",orders.getId());
                      orders.setStatus(Orders.CANCELLED);
                      orders.setCancelReason("超时未支付");
                      orders.setCancelTime(LocalDateTime.now());
                      orderMapper.update(orders);
                  }
          );
      }
      //status=0 and orderTime<now - 15min

    }

    /**
     * 处理派送中订单
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder()
    {
        log.info("处理派送中订单:{}", LocalDateTime.now());
        LocalDateTime time=LocalDateTime.now().plusMinutes(-60);
       List<Orders> orders= orderMapper.getProceeTimeOut(Orders.DELIVERY_IN_PROGRESS,time);
       if(orders.size()>0)
       {
           orders.forEach(
                   order->{
                       order.setStatus(Orders.COMPLETED);
                       orderMapper.update(order);
                   }
           );
       }


    }

}
