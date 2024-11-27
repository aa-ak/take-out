package com.sky.dto;

import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderDTO implements Serializable {
    //地址
    private Long addressBookId;
    //总金额
    private BigDecimal amount;
    //配送状态
    private Integer deliveryStatus;//1 立即送出 0选择具体时间
    //预计送达时间
    private String estimatedDeliveryTime;
    //打包费
    private Integer packAmount;
    //付款方式
    private Integer payMethod;
    //备注
    private String remark;
    //餐具数量
    private Integer tablewareNumber;
    //餐具数量状态
    private Integer tablewareStatus;


}
