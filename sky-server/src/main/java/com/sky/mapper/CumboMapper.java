package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

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
}
