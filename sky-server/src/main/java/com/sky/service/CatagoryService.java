package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.DishFlavor;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.DishVO;

import java.util.List;

public interface CatagoryService {


    void alter(CategoryDTO categoryDTO);

    PageResult getCatagory(CategoryPageQueryDTO categoryPageQueryDTO);

    void enableDisanable(Integer status, Long id);

    void inserCategory(CategoryDTO categoryDTO);

    void delete(Long id);

    List<Category> queryType(Integer type);

    List<Category> getByType(Integer type);


//    List<Category> getByType(Integer type);
}
