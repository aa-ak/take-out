package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShopMapper {
    void deleteShopping(ShoppingCartDTO shoppingCartDTO);
}
