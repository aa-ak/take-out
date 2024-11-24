package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user/addressBook")
@Api(tags = "地址接口")
public class AddressController {

    @Autowired
    private AddressService addressService;

    /**
     *  获取地址
     * @return
     */
    @ApiOperation("获取地址")
    @GetMapping("/list")
    public Result<List<AddressBook>> getAddress()
    {
        log.info("获取地址");
        List<AddressBook> addressBook=addressService.getAddress();
        return Result.success(addressBook);
    }

    @GetMapping("/default")
    @ApiOperation("获取默认地址")
    public Result<AddressBook> getDefault()
    {
        log.info("获取默认地址");
        AddressBook addressBook=addressService.getDefaultAddress();
        return Result.success(addressBook);
    }

    /**
     * 新增地址
     * @param addressBook
     * @return
     */
    @PostMapping
    @ApiOperation("新增地址")
    public Result addAddress(@RequestBody AddressBook addressBook)
    {
        addressService.save(addressBook);
        return Result.success();
    }

    /**
     *  根据ID查询地址
     * @param id
     * @return
     */
   @GetMapping("/{id}")
    @ApiOperation("根据ID查询地址")
    public Result<AddressBook> getAddress(@PathVariable Long id)
    {
        AddressBook addressBook=addressService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * 删除地址
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除地址")
    public Result deleteAddress(@RequestParam Long id)
    {
        addressService.delete(id);
        return Result.success();
    }

    /**
     * 设置默认地址
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result setDefault(@RequestBody AddressBook addressBook)
    {
        addressService.setDefault(addressBook);
        return Result.success();
    }
    @PutMapping
    @ApiOperation("修改地址")
    public Result updateaddress(@RequestBody AddressBook addressBook)
    {
        addressService.update(addressBook);
        return Result.success();
    }

}
