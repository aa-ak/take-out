package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShopMapper;
import com.sky.mapper.UserMapper;
import com.sky.properties.Address;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.utils.AddressUtil;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.jsonwebtoken.JwtBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private Address address;
    private static  final Integer MAX_DISTANCE=5000;
    @Autowired
    private AddressUtil addressUtil;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;
    @Autowired
    private UserMapper userMapper;
    @Transactional
    @Override
    public OrderSubmitVO submit(OrderDTO orderDTO) throws Exception {
        Long userid=BaseContext.getCurrentId();
        //各种业务异常 地址为空，购物车数据为空

        Long addressBookId=orderDTO.getAddressBookId();
        AddressBook addressBook = addressMapper.getById(addressBookId);
        StringBuilder sb = new StringBuilder();
        if(addressBook==null){
            //抛出异常
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);

        }
        sb.append(addressBook.getCityName()).append(addressBook.getDistrictName()).append(addressBook.getDetail());

        //超出配送距离
        Boolean distance = distance(addressBook);
        if(!distance)
        {
            throw new OrderBusinessException("超出配送范围");
        }

        List<ShoppingCart> shoppingCart=shopMapper.getByShopId(BaseContext.getCurrentId());
        if(shoppingCart==null)
        {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        //插入订单
        Orders orders = new Orders();
        BeanUtils.copyProperties(orderDTO, orders);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setUserId(userid);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setNumber(System.currentTimeMillis() + "");
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setAddress(sb.toString());
        orderMapper.submit(orders);

        //订单明细插入n条数据
        List<ShoppingCart> shoppinglist = shopMapper.getByShopId(userid);
        BigDecimal amount=new BigDecimal(0);
        for (ShoppingCart cart : shoppinglist) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderMapper.insertdetail(orderDetail);
            amount= amount.add(cart.getAmount());

        }
        //清空当前购物车数据
        shopMapper.deleteAll(userid);
        //封装vo
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderTime(orders.getOrderTime())
                .orderAmount(amount)
                .orderNumber(orders.getNumber())
                .build();

        return orderSubmitVO;
    }

    @Override
    public void repeat(Long id) {
        Orders orders=orderMapper.getOrders(id);
        //加入购物车
        List<OrderDetail> detail1 = orderMapper.getDetail(id);
        for (OrderDetail orderDetail : detail1) {
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(orderDetail, shoppingCart);
            shoppingCart.setUserId(BaseContext.getCurrentId());
            shopMapper.insert(shoppingCart);
        }
//        //插入订单
//        Orders orders1 = new Orders();
//        BeanUtils.copyProperties(orders, orders1);
//        orders1.setOrderTime(LocalDateTime.now());
//        orders1.setNumber(System.currentTimeMillis() + "");
//        orderMapper.submit(orders1);
//        //加入订单明细
//        List<OrderDetail> orderDetail=orderMapper.getDetail(id);
//        for (OrderDetail detail : orderDetail) {
//            OrderDetail orderDetail1 = new OrderDetail();
//            BeanUtils.copyProperties(detail, orderDetail1);
//            orderDetail1.setOrderId(orders1.getId());
//        }


    }

    /**
     * 订单详情
     * @param id
     * @return
     */
    @Override
    public OrderVO orderDetail(Long id) {
        Orders orders=orderMapper.getOrders(id);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        List<OrderDetail>orderDetails=orderMapper.getDetail(id);
        orderVO.setOrderDetailList(orderDetails);

        return orderVO;
    }

    @Override
    public void cancel(Long id) {
        //业务异常状况，订单状态为已完成和已取消和派送中不可以取消
        Orders orders = orderMapper.getOrders(id);
        if(orders.getStatus()==Orders.COMPLETED||orders.getStatus()==Orders.CANCELLED||orders.getStatus()==Orders.DELIVERY_IN_PROGRESS){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
//        else{
//            //从订单表中删除订单
//            //订单明细删除订单
//            orderMapper.cancel(id);
//            orderMapper.cancelDetail(id);
//        }
        else {
            Orders build = orders.builder()
                    .id(id)
                    .status(Orders.CANCELLED)
                    .build();
        }

    }

    @Override
    public PageResult history(Integer status, Integer page, Integer pageSize) {

        PageHelper.startPage(page, pageSize);
        List<OrderVO> ordersByStatus = orderMapper.getOrdersByStatus(status);
        Page<OrderVO> result = new Page<>();
        for (OrderVO byStatus : ordersByStatus) {
            byStatus.setOrderDetailList(orderMapper.getDetail(byStatus.getId()));
            result.add(byStatus);
        }


        return new PageResult(result.getTotal(), result.getResult());
    }

    @Override
    public void reminder(Long id) {


    }

    @Override
    public PageResult getSearch(LocalDateTime beginTime, LocalDateTime endTime, Integer status, String number, String phone, Integer page, Integer pageSize) {
        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setPage(page);
        ordersPageQueryDTO.setPageSize(pageSize);
        ordersPageQueryDTO.setBeginTime(beginTime);
        ordersPageQueryDTO.setEndTime(endTime);
        ordersPageQueryDTO.setNumber(number);
        ordersPageQueryDTO.setPhone(phone);
        ordersPageQueryDTO.setStatus(status);
        PageHelper.startPage(page, pageSize);


        Page<Orders>orders=orderMapper.getSearch(ordersPageQueryDTO);
        List<OrderVO> orderList = getOrderVoList(orders);

        return new PageResult(orders.getTotal(),orderList);
        }

    private List<OrderVO> getOrderVoList(Page<Orders> orders) {
        List<Orders> orderList = orders.getResult();
        List<OrderVO> orderVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orderList)) {
            for (Orders orders1 : orderList) {
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders1, orderVO);
                orderVO.setOrderDishes(getOrderDishesStr(orders1));
                orderVOS.add(orderVO);
            }
        }
        return orderVOS;
    }
    private String getOrderDishesStr(Orders orders)
    {
        List<OrderDetail> detail = orderMapper.getDetail(orders.getId());
        List<String> collect = detail.stream().map(x -> {
                    String orderDish = x.getName() + "*" + x.getNumber() + ";";
                    return orderDish;
                }
        ).collect(Collectors.toList());

        return String.join("", collect);
    }

    @Override
    public void delivery(Long id) {
        Orders orders = orderMapper.getOrders(id);
        if(orders.getStatus()!=Orders.CONFIRMED){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        else{
            Orders orders1 = Orders.builder()
                    .id(id)
                    .status(Orders.DELIVERY_IN_PROGRESS)
                    .build();
            orderMapper.update(orders1);
        }
    }

    @Override
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        Long id=ordersConfirmDTO.getId();
        Orders orders=orderMapper.getOrders(id);
        if(orders.getStatus()!=Orders.TO_BE_CONFIRMED)
        {
            throw  new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        else{
            Orders orders1=Orders.builder()
                    .id(id)
                    .status(Orders.CONFIRMED)
                    .build();
            orderMapper.update(orders1);
        }
    }

    /**
     * @param ordersRejectionDTO
     */
    @Override
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) {
        Orders orders=orderMapper.getOrders(ordersRejectionDTO.getId());
        if(orders.getStatus()==Orders.DELIVERY_IN_PROGRESS||orders.getStatus()==Orders.COMPLETED)
        {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        else
        {
            Orders orders1=Orders.builder()
                    .id(ordersRejectionDTO.getId())
                    .status(Orders.CANCELLED)
                    .rejectionReason(ordersRejectionDTO.getRejectionReason())
                    .build();
            orderMapper.update(orders1);
        }

    }

    /**
     * @param id
     */
    @Override
    public void complete(Long id) {
        Orders orders = orderMapper.getOrders(id);
        if(orders.getStatus()!=Orders.DELIVERY_IN_PROGRESS)
        {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        else{
             Orders orders1=Orders.builder()
                     .id(id)
                     .status(Orders.COMPLETED)
                     .build();
                orderMapper.update(orders1);
        }

    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public OrderVO orderDetailwithOrder(Long id) {

        Orders orders = orderMapper.getOrders(id);
        OrderVO orderVO1 = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO1);
        List<OrderDetail>details=orderMapper.getDetail(id);
        orderVO1.setOrderDetailList(details);
        return orderVO1;
    }

    /**
     * @return
     */
    @Override
    public OrderStatisticsVO statistics() {

        Integer toBeanConfirmed=orderMapper.getStatusOfOrder();
        Integer confirmed=orderMapper.getStatusOfOrder();
        Integer deliveryInProgress=orderMapper.getStatusOfOrder();
        OrderStatisticsVO orderStatisticsVO = OrderStatisticsVO.builder()
                .toBeConfirmed(toBeanConfirmed)
                .confirmed(confirmed)
                .deliveryInProgress(deliveryInProgress)
                .build();




        return orderStatisticsVO;
    }

    /**
     * @param
     */
    @Override
    public void Admincancel(OrdersCancelDTO ordersCancelDTO) {
        //业务异常状况，订单状态为已完成和已取消和派送中不可以取消
        Orders orders = orderMapper.getOrders(ordersCancelDTO.getId());
        if(orders.getStatus()==Orders.COMPLETED||orders.getStatus()==Orders.CANCELLED||orders.getStatus()==Orders.DELIVERY_IN_PROGRESS){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
//        else{
//            //从订单表中删除订单
//            //订单明细删除订单
//            orderMapper.cancel(ordersRejectionDTO.getId());
//            orderMapper.cancelDetail(ordersRejectionDTO.getId());
//
//        }
        else {
            Orders build = Orders.builder()
                    .id(ordersCancelDTO.getId())
                    .status(Orders.CANCELLED)
                    .cancelReason(ordersCancelDTO.getCancelReason())
                    .build();
            orderMapper.update(build);

        }
    }

    /**
     * @param ordersPaymentDTO
     * @return
     */
    @Override
    public OrderPaymentVO paymentSkip(OrdersPaymentDTO ordersPaymentDTO)  {

        OrderPaymentVO orderPaymentVO = OrderPaymentVO.builder()
                .nonceStr("1670380960")
                .paySign("qwuqwjsjs")
                .packageStr("prepay_id=wx07104240042328")
                .timeStamp("4235123123123")
                .signType("RSA")
                .build();

        paySuccess(ordersPaymentDTO.getOrderNumber());
        return orderPaymentVO;
    }
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }
   private Boolean distance(AddressBook addressBook) throws Exception {


       Map<String, Double> userAddress = addressUtil.getAddress(addressBook.getCityName() + addressBook.getDistrictName() + addressBook.getDetail());
       System.out.println(addressBook.getCityName() + addressBook.getDistrictName() + addressBook.getDetail());
       //校验用户的地址信息 获取店铺的经纬度
       Map<String, Double> shopAddress = addressUtil.getAddress(address.getAddress());
       //用户当前地址的经纬度  == 用户的城市名称 + 区名称 + 详细地址

       //店铺和用户的经纬度

       Double userLng = userAddress.get("lng");
       Double userLat = userAddress.get("lat");
       Double shopLng = shopAddress.get("lng");
       Double shopLat = shopAddress.get("lat");
       //经纬度之差
       Double diffLng = shopLng - userLng;
       Double diffLat = shopLat - userLat;
       //每经度单位米
       double jl_jd = 102834.74258026089786013677476285;
       //每纬度单位米
       double jl_wd = 111712.69150641055729984301412873;
       double b = Math.abs(diffLat * jl_jd);
       double a = Math.abs(diffLng * jl_wd);
       Double distance = Math.sqrt((a * a + b * b));

       //判断距离是否超过5km
       if (distance > MAX_DISTANCE) {
          return false;
       }
       return true;

   }




















//    @Override
//    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
//        /**
//         * 订单支付
//         *
//         * @param ordersPaymentDTO
//         * @return
//         */
//            // 当前登录用户id
//            Long userId = BaseContext.getCurrentId();
//            User user = userMapper.getById(userId);
//
//            //调用微信支付接口，生成预支付交易单
//            JSONObject jsonObject = weChatPayUtil.pay(
//                    ordersPaymentDTO.getOrderNumber(), //商户订单号
//                    new BigDecimal(0.01), //支付金额，单位 元
//                    "苍穹外卖订单", //商品描述
//                    user.getOpenid() //微信用户的openid
//            );
//
//            if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
//                throw new OrderBusinessException("该订单已支付");
//            }
//
//            OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
//            vo.setPackageStr(jsonObject.getString("package"));
//
//            return vo;
//        }
//
//    /**
//     * 支付成功，修改订单状态
//     *
//     * @param outTradeNo
//     */
//    public void paySuccess(String outTradeNo) {
//
//        // 根据订单号查询订单
//        Orders ordersDB = orderMapper.getByNumber(outTradeNo);
//
//        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
//        Orders orders = Orders.builder()
//                .id(ordersDB.getId())
//                .status(Orders.TO_BE_CONFIRMED)
//                .payStatus(Orders.PAID)
//                .checkoutTime(LocalDateTime.now())
//                .build();
//
//        orderMapper.update(orders);
//    }

}



