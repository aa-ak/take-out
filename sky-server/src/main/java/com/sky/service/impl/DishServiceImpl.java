package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Override
    public void edit(Dish dish) {
       dishMapper.edit(dish);

    }

    @Override
    @Transactional//事务注解
    public void savewithFlavor(DishDTO dishDTO) {
       //向菜品表插入数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.insert(dish);
        //获取insert语句生成的主键值
        Long id = dish.getId();
        //向口味表插入n条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!=null&&flavors.size()>0)
        {
            flavors.forEach(dishFlavor ->
            {
                dishFlavor.setDishId(id);
            });
            dishFlavorMapper.insertBatch(flavors);
        }



    }



    @Override
    public PageResult getPage(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO>page=dishMapper.getPage(dishPageQueryDTO);
        Long total=page.getTotal();
        List<DishVO>records=page.getResult();

        return new PageResult(total,records);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        //当前菜品是否能够删除--是否存在售卖的菜品
        //判断当前菜品是否被套餐关联
        //可以删除，删除菜品数据
        //菜品有口味数据，删除口味数据
        for (Long id : ids) {
           Dish dish=dishMapper.getById(id);
           Integer status = dish.getStatus();
           if (status== StatusConstant.ENABLE)
           {
               throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
           }
        }

           List<Long> count=setmealMapper.getById(ids);
           if(count.size()>0&&count!=null)
           {
               throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
           }

        for (Long id : ids) {

            List<DishFlavor>sum=dishFlavorMapper.getDishwithFlavorById(id);
            if(sum.size()>0&&sum!=null)
            {
                dishFlavorMapper.deletById(id);

            }

        }



        dishMapper.deleteBatch(ids);

    }

    @Override
    public void alterDish(DishDTO dishDTO) {

        Dish dish1 = new Dish();
        Long id=dishDTO.getId();
        BeanUtils.copyProperties(dishDTO,dish1);
        dishMapper.alterDish(dish1);
        List<DishFlavor> flavors=dishDTO.getFlavors();
        if(flavors!=null &&flavors.size()>0)
        {
//            DishFlavor dishFlavor=new DishFlavor();
//            BeanUtils.copyProperties(dishDTO.getFlavors(),dishFlavor);
//            dishFlavor.setDishId(id);
            dishFlavorMapper.alterById(id);
        }



    }

    @Override
    public DishVO getById(Long id) {
       Dish dish=dishMapper.getById(id);
       List<DishFlavor>flavors= dishFlavorMapper.getDishwithFlavorById(id);
       DishVO dishVO=new DishVO();
       BeanUtils.copyProperties(dish,dishVO);
       dishVO.setFlavors(flavors);
       return dishVO;

    }



    @Override
    public void startAndstop(Long status,Long id) {

        dishMapper.startAndstop(status,id);
    }

    @Override
    public Dish getByTypeId(Long categoryId) {
        Dish dish=dishMapper.getByTypeId(categoryId);
        return dish;
    }


}
