package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userShopController")
@RequestMapping("user")
@Slf4j
@Api(tags = "购物车相关接口")
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ShopService shopService;

    @GetMapping("/shop/status")
    @ApiOperation("查询营业状态")
    public Result<Integer> getStatus()
    {
        Integer status= (Integer) redisTemplate.opsForValue().get("SHOP_STATUS");
        log.info("查询营业状态,{}",status==1?"营业中":"打烊中");
        return Result.success(status);
    }
    @PostMapping("/shoppingCart/sub")
    @ApiOperation("删除购物车中一个商品")
    public Result deleteShopping(@RequestBody ShoppingCartDTO shoppingCartDTO)
    {
        shopService.deleteShopping(shoppingCartDTO);
        return Result.success();
    }
    @PostMapping("/shoppingCart/add")
    @ApiOperation("添加购物车")
    public Result AddCart(@RequestBody ShoppingCartDTO shoppingCartDTO)
    {
        log.info("添加购物车");
        shopService.addCart(shoppingCartDTO);
        return Result.success();
    }
    @GetMapping("/shoppingCart/list")
    @ApiOperation("查看购物车")
    public Result<List<ShoppingCart>> getCart()
    {
        List<ShoppingCart>shoppingCarts=shopService.getCart();
        return Result.success(shoppingCarts);
    }
    @DeleteMapping("/shoppingCart/clean")
    @ApiOperation("清空购物车")
    public Result cleanCart()
    {
        shopService.cleanCart();
        return Result.success();
    }


}
