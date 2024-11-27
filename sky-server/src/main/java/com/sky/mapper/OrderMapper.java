package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrderDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {

    /**
     *
     * @param orders
     */
   void submit(Orders orders);

    void insertdetail(OrderDetail orderDetail);

    Orders getOrders(Long id);

    List<OrderDetail> getDetail(Long id);
    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

 /**
  *
  * @param id
  */
 @Delete("delete from orders where id = #{id}")
    void cancel(Long id);

 /**
  * 删除订单明细
  * @param id
  */
    @Delete("delete from order_detail where order_id = #{id}")
    void cancelDetail(Long id);

    /**
     *
     * @param status
     * @return
     */
     List<OrderVO> getOrdersByStatus(Integer status);

    /**
     * 搜索
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders>getSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 订单详情
     * @param
     * @return
     */
//    List<OrderVO> getDetailwithOrder(Long id);

    Integer getStatusOfOrder();


    List<Orders>getProceeTimeOut(Integer status, LocalDateTime orderTime);

//    @Select("select * from orders where status = #{status}")
//    List<Orders>getProceeDelivery(Integer status);
}
