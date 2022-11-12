package com.sean.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sean.reggie.dto.SetmealDTo;
import com.sean.reggie.entity.Setmeal;

import java.util.List;

/**
 * @小羊肖恩
 * @2022/11/03
 * @15:11
 * @Describe：
 */
public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，同时需要保存套餐和菜品之间的关系
     * @param setmealDto
     */
    void saveWithDish(SetmealDTo setmealDto);

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     * @param ids
     */
    void removeWithDish(List<Long> ids);

}
