package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CumboMapper {
   

    @AutoFill(value = OperationType.INSERT)
    void save(Setmeal setmeal);

    Page<SetmealVO> getPage(SetmealPageQueryDTO setmealPageQueryDTO);


    void startAndstop(Long status,Long id);

    Setmeal getById(Long id);

    void  deleteById(Long id);

    @AutoFill(value = OperationType.UPDATE)
    void alter(Setmeal setmeal);

    List<DishItemVO> getSetmealById(Long id);

    Integer getCopies(Long id);

    Integer getAllCumbo(Integer enable);
}
