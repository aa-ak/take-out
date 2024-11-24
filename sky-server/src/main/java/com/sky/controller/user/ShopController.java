package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.result.Result;
import com.sky.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@RequestMapping("user")
@Slf4j
@Api(tags = "店铺相关接口")
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


}
