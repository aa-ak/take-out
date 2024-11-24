package com.sky.mapper;

import com.sky.entity.DishFlavor;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    void insertBatch(List<DishFlavor> flavors);

    void deletById(Long id);

    void alterById(Long id);

    void getById(Long id);
    @Select("select * from  dish_flavor WHERE id=#{id}")
    List<DishFlavor> getDishwithFlavorById(Long id);

    void insertByid(Long id);
}
