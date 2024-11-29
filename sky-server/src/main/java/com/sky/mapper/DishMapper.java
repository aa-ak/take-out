package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper {


    void edit(Dish dish);

    /*插入菜品数据

     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);


    Page<DishVO> getPage(DishPageQueryDTO dishPageQueryDTO);

    void deleteBatch(List<Long> ids);

    @Select("select * from dish where id=#{id};")
    Dish getById(Long id);

    void deleteById(Long id);
    @AutoFill(value =OperationType.UPDATE)
    void alterDish(Dish dish);


    void startAndstop(Long status,Long id);

    @Select("select * from dish where category_id=#{categoryId}")
    List<Dish> getByTypeId(Long categoryId);

    List<Dish> UsergetById(Category category);

    Integer getAllDish(Integer enable);
}
