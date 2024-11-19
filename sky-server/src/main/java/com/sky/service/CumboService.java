package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface CumboService {
    PageResult getpage(SetmealPageQueryDTO setmealPageQueryDTO);

    void save(SetmealDTO setmealDTO);

    void startAndstop(Long status,Long id);

    SetmealVO getById(Long id);

    void deleteBatch(List<Long> ids);

    void alter(SetmealDTO setmealDTO);
}
