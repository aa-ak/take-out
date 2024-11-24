package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CatagoryMapper;
import com.sky.mapper.CumboMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.CumboService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CumboServiceImpl implements CumboService {
    @Autowired
    private CumboMapper cumboMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
//    @Autowired
//    private CatagoryMapper catagoryMapper;
    @Override
    public PageResult getpage(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Setmeal setmeal = new Setmeal();
        Page<SetmealVO>result=cumboMapper.getPage(setmealPageQueryDTO);
        return new PageResult(result.getTotal(),result.getResult());
    }


    @Transactional//事务注解
    @Override
    public void save(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        cumboMapper.save(setmeal);
        Long id=setmeal.getId();
        //向steal_dish新增菜品
        List<SetmealDish> setmealDishes=setmealDTO.getSetmealDishes();
        if(setmealDishes.size()>0&&setmealDishes!=null)
        {
            setmealDishes.forEach(setmealDish ->
                    setmealDish.setSetmealId(id)

                    );
            setmealDishMapper.insertSteal(setmealDishes);
        }


    }

    @Override
    public void startAndstop(Long status,Long id) {
        cumboMapper.startAndstop(status,id);
    }

    @Override
    public SetmealVO getById(Long id) {
        Setmeal setmeal=new Setmeal();
        setmeal=cumboMapper.getById(id);
        SetmealVO setmealVO=new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        List<SetmealDish>dish=setmealDishMapper.getById(id);
        setmealVO.setSetmealDishes(dish);

        return setmealVO;
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            Setmeal setmeal = new Setmeal();
            setmeal=cumboMapper.getById(id);
            if(setmeal.getStatus()== StatusConstant.ENABLE)
            {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        for (Long id : ids) {

            cumboMapper.deleteById(id);
            List<SetmealDish>dish=setmealDishMapper.getById(id);
            if(dish.size()>0&&dish!=null)
            {

                setmealDishMapper.deleteById(id);
            }

        }
    }

    @Override
    public void alter(SetmealDTO setmealDTO) {

        Setmeal setmeal = new Setmeal();
        //套餐ID
        Long id=setmealDTO.getId();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        //先修改套餐表
        cumboMapper.alter(setmeal);
        //删除套餐和菜品的关联关系，操作setmeal_dish表，执行delete
        setmealDishMapper.deleteById(id);
        List<SetmealDish> dishes=setmealDTO.getSetmealDishes();
        dishes.forEach(setmealDish ->
        {
            setmealDish.setSetmealId(id);
        });

            setmealDishMapper.insertSteal(dishes);

        }

    @Override
    public List<Setmeal> getByCategoryId(Long categoryId) {
        List<Setmeal>setmeals=setmealDishMapper.getByCategoryId(categoryId);
        return setmeals;
    }

    @Override
    public List<DishItemVO> getSetmealById(Long id) {
        List<DishItemVO> setmealById = cumboMapper.getSetmealById(id);

        return setmealById;

        }






}

