package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AddressMapper {
    /**
     * 获取地址
     * @param id
     * @return
     */

    List<AddressBook> getAddress(Long id);

    /**
     * 获取默认地址
     * @return
     */
    @Select("select * from address_book where is_default=1")
    AddressBook getDefault();

    void save(AddressBook addressBook);

    /**
     * 根据ID查询地址
     * @param id
     * @return
     */
    AddressBook getById(Long id);

    /**
     * 删除地址
     * @param id
     */
    @Delete("DELETE FROM address_book WHERE id=#{id}")
    void delete(Long id);

    /**
     * 设置默认地址
     * @param addressBook
     */
    @Update("update address_book set is_default=#{isDefault} where id=#{id}")
    void setDefault(AddressBook addressBook);

    void update(AddressBook addressBook);

    void updateIsDefault(AddressBook book);
}
