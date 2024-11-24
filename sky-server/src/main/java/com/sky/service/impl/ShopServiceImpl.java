package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.CumboMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.ShopMapper;
import com.sky.service.ShopService;
import com.sky.vo.DishItemVO;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private CumboMapper cumboMapper;
    @Override
    public void deleteShopping(ShoppingCartDTO shoppingCartDTO) {
        shopMapper.deleteShopping(shoppingCartDTO);

    }

    @Override
    public void addCart(ShoppingCartDTO shoppingCartDTO) {
        //判断当前加入购物车中的商品是否已经存在了
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shopMapper.list(shoppingCart);
        //如果已经存在了，数量+1
        if(list!=null&&list.size()>0)
        {
            ShoppingCart shoppingCart1 = list.get(0);
            shoppingCart1.setNumber(shoppingCart1.getNumber()+1);//update shopping_Cart
            shopMapper.addNumber(shoppingCart1);

        }
        else{
            //判断是菜品还是套餐
            Long dishId = shoppingCartDTO.getDishId();
            Long setmealId = shoppingCartDTO.getSetmealId();
            if(dishId!=null)
            {
                Dish dish = dishMapper.getById(dishId);

                shoppingCart.setImage(dish.getImage());
                shoppingCart.setName(dish.getName());
                shoppingCart.setAmount(dish.getPrice());

                
            }
            else{
               Setmeal setmeal = cumboMapper.getById(setmealId);
               shoppingCart.setName(setmeal.getName());
               shoppingCart.setImage(setmeal.getImage());
               shoppingCart.setAmount(setmeal.getPrice());



            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shopMapper.insert(shoppingCart);
        }
        //如果不存在，需要加入一条购物车数据


    }

    /**
     * 查看购物车
     */
    @Override
    public List<ShoppingCart>getCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart>shoppingCarts= shopMapper.getCart(shoppingCart);
        return shoppingCarts;
    }

    /**
     *  清空购物车
     */
    @Override
    public void cleanCart() {
        Long id=BaseContext.getCurrentId();
        shopMapper.deleteAll(id);
    }
}
