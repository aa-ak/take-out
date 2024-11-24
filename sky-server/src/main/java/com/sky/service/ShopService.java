package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShopService {
    void deleteShopping(ShoppingCartDTO shoppingCartDTO);

    void addCart(ShoppingCartDTO shoppingCartDTO);


    List<ShoppingCart>getCart();

    void cleanCart();
}
