package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.catalina.LifecycleState;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShopMapper {
    void deleteShopping(ShoppingCartDTO shoppingCartDTO);

    /**
     * 动态条件查询
     * @param shoppingCart
     * @return
     */
       List<ShoppingCart> list(ShoppingCart shoppingCart);

       @Update("update shopping_cart set number=#{number} where id=#{id}")
       void addNumber(ShoppingCart shoppingCart1);

    /**
     * 添加购物车
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart(name, image, user_id, dish_id, setmeal_id, dish_flavor, amount, create_time) values (#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{amount},#{createTime})")
    void insert(ShoppingCart shoppingCart);


    List<ShoppingCart> getCart(ShoppingCart shoppingCart);

    /**
     * 清空购物车
     * @param userId
     */
    @Delete("delete from shopping_cart where user_id=#{userId}")
    void deleteAll(Long userId);

    @Select("select * from shopping_cart where user_id=#{currentId}")
    List<ShoppingCart> getByShopId(Long currentId);
}
