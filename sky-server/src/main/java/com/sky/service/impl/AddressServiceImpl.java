package com.sky.service.impl;


import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressMapper;
import com.sky.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

   @Autowired
   private AddressMapper addressMapper;

    /**
     *  获取地址
     * @return
     */
    @Override
    public List<AddressBook> getAddress() {
        Long id= BaseContext.getCurrentId();
        List<AddressBook> addressBook=addressMapper.getAddress(id);
        return addressBook;
    }

    /**
     * 获取默认地址
     * @return
     */
    @Override
    public AddressBook getDefaultAddress() {
        AddressBook addressBook=addressMapper.getDefault();
        return null;
    }

    /**
     *  新增地址
     * @param addressBook
     */
    @Override
    public void save(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(StatusConstant.DISABLE);
        addressMapper.save(addressBook);

    }

    /**
     * 根据ID查询地址
     * @param id
     * @return
     */
    @Override
    public AddressBook getById(Long id) {
        AddressBook addressBook=addressMapper.getById(id);
        return addressBook;
    }

    /**
     * 删除地址
     * @param id
     */
    @Override
    public void delete(Long id) {
        addressMapper.delete(id);
    }

    /**
     *  设置默认地址
     * @param addressBook
     */
    @Override
    public void setDefault(AddressBook addressBook) {
//       List<AddressBook>addressBooks= addressMapper.getAddress(BaseContext.getCurrentId());
//        for (AddressBook book : addressBooks) {
//            book.setIsDefault(0);
//            addressMapper.updateIsDefault(book);
//        }
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        addressMapper.updateIsDefault(addressBook);
//        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(1);
        addressMapper.setDefault(addressBook);
    }

    @Override
    public void update(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressMapper.update(addressBook);
    }
}
