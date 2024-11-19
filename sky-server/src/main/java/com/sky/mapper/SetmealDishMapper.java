package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public interface SetmealDishMapper {


    void insertSteal(List<SetmealDish> setmealDishes);

    List<SetmealDish> getById(Long id);

    void deleteById(Long id);

    void alter(List<SetmealDish> dishes);
}
