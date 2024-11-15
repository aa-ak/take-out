package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {


    void edit(Dish dish);

    /*插入菜品数据

     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);


}
