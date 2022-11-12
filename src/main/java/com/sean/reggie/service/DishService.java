package com.sean.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sean.reggie.dto.DishDTO;
import com.sean.reggie.entity.Dish;

/**
 * @小羊肖恩
 * @2022/11/03
 * @15:10
 * @Describe：
 */
public interface DishService extends IService<Dish> {

    /**
     * 新增菜品，同时插入菜品对应的口味数据，需要操作两张表：dish、dish_flavor
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);


    /**
     * 根据id查询菜品信息和口味信息
     * @param id
     * @return
     */
    DishDTO getByIdWithFlavor(Long id);

    /**
     * 更新菜品信息，同时更新口味信息
     * @param dishDTO
     */
    void updateWithFlavor(DishDTO dishDTO);
}
