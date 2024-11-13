package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


import java.util.List;

@Mapper
public interface CatagoryMapper {
    void alter(Category category);

    Page<Category> getCaragory(CategoryPageQueryDTO categoryPageQueryDTO);



//    void enableDisanable(Category category);
   @Insert("insert into category(type, name, sort, status, create_time, update_time, create_user, update_user)values (#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void insertCategory(Category category);

    @Delete("delete from category where id=#{id}")
    void deleteById(Long id);

//    @Select("select * from category where type=#{type}")
     List<Category> queryType(Integer type);
}