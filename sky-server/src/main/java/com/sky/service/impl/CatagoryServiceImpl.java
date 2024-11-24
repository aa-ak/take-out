package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.DishFlavor;
import com.sky.mapper.CatagoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.CatagoryService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.List;

@Service
public class CatagoryServiceImpl implements CatagoryService {

    @Autowired
    private CatagoryMapper catagoryMapper;
  @Autowired
  private DishMapper dishMapper;
    @Override
    public void alter(CategoryDTO categoryDTO) {
        Category category=new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateUser(BaseContext.getCurrentId());
        catagoryMapper.alter(category);


    }

    @Override
    public PageResult getCatagory(CategoryPageQueryDTO  categoryPageQueryDTO) {
        //使用分页查询插件

        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        Page<Category>page=catagoryMapper.getCaragory(categoryPageQueryDTO);
        long total=page.getTotal();

        return new PageResult(total,page.getResult());
    }



    @Override
    public void enableDisanable(Integer status, Long id) {
        Category category= Category.builder()
                        .id(id)
                        .status(status)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                        .build();
        catagoryMapper.alter(category);

    }

    @Override
    public void inserCategory(CategoryDTO categoryDTO) {
        Category category=new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());
        category.setStatus(StatusConstant.DISABLE);
        catagoryMapper.insertCategory(category);

    }

    @Override
    public void delete(Long id) {
        catagoryMapper.deleteById(id);

    }

    @Override
    public List<Category> queryType(Integer type) {
        return catagoryMapper.queryType(type);
    }

    @Override
    public List<Category> getByType(Integer type) {
        Category category = new Category();
        category.setType(type);
        category.setStatus(StatusConstant.ENABLE);
        List<Category>categories=catagoryMapper.userGetByType(category);
       return categories;
    }


}
