package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.CumboMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class WorkSpaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private CumboMapper  cumboMapper;
    @Autowired
    private UserMapper  userMapper;

    /**
     * @return
     */
    @Override
    public OrderOverViewVO getOrderOverView() {

        Integer cancelOrders = orderMapper.getAllOrders(Orders.CANCELLED);
        Integer completedOrders = orderMapper.getAllOrders(Orders.COMPLETED);
        Integer deliveredOrders = orderMapper.getAllOrders(Orders.DELIVERY_IN_PROGRESS);
        Integer waitingOrders = orderMapper.getAllOrders(Orders.TO_BE_CONFIRMED);
        Integer allOrders = orderMapper.getAllOrders(null);

        return OrderOverViewVO.builder()
                .cancelledOrders(cancelOrders)
                .completedOrders(completedOrders)
                .deliveredOrders(deliveredOrders)
                .waitingOrders(waitingOrders)
                .allOrders(allOrders)
                .build();

    }

    /**
     * @return
     */
    @Override
    public DishOverViewVO getDishOverView() {


        Integer enable = dishMapper.getAllDish(StatusConstant.ENABLE);
        Integer disanable = dishMapper.getAllDish(StatusConstant.DISABLE);
        return DishOverViewVO.builder()
                .discontinued(disanable)
                .sold(enable)
                .build();
    }

    /**
     * @return
     */
    @Override
    public SetmealOverViewVO getSetmealOverView() {

        Integer enable = cumboMapper.getAllCumbo(StatusConstant.ENABLE);
        Integer disanable = cumboMapper.getAllCumbo(StatusConstant.DISABLE);
        return SetmealOverViewVO.builder()
                .discontinued(disanable)
                .sold(enable)
                .build();
    }

    /**
     * @return
     */
    @Override
    public BusinessDataVO getBusinessData(LocalDateTime dateTime1,LocalDateTime dateTime2) {

        Integer validOrderCount=orderMapper.getValidOrderCount(dateTime1,dateTime2,Orders.COMPLETED);
        Integer OrderCount=orderMapper.getValidOrderCount(dateTime1,dateTime2,null);
        Integer user=userMapper.getUserBydate(dateTime1,dateTime2);
        Double rate=0.0;
        if(OrderCount!=0){
            rate=validOrderCount.doubleValue()/OrderCount;
        }
        Double price=orderMapper.getAmount(dateTime1,dateTime2,Orders.COMPLETED);
        if(price==null)
        {
            price=0.0;
        }

        Double avePrice=0.0;
        if(validOrderCount!=0){
            avePrice=price/validOrderCount;
        }
        return BusinessDataVO.builder()
                .validOrderCount(validOrderCount)
                .newUsers(user)
                .orderCompletionRate(rate)
                .turnover(price)
                .unitPrice(avePrice)
                .build();
    }
}