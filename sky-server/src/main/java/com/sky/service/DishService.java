package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface DishService {

    void edit(Dish dish);
    void savewithFlavor(DishDTO dishDTO);

    PageResult getPage(DishPageQueryDTO dishPageQueryDTO);

    void deleteBatch(List<Long> ids);

    void alterDish(DishDTO dishDTO);

    DishVO getById(Long id);

    void startAndstop(Long status,Long id);

    Dish getByTypeId(Long categoryId);
}
