package com.sean.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sean.reggie.common.CustomExpection;
import com.sean.reggie.entity.Category;
import com.sean.reggie.entity.Dish;
import com.sean.reggie.entity.Setmeal;
import com.sean.reggie.mapper.CategoryMapper;
import com.sean.reggie.service.CategotyService;
import com.sean.reggie.service.DishService;
import com.sean.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @小羊肖恩
 * @2022/11/03
 * @10:08
 * @Describe：
 */

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategotyService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id进行删除，并在删除之前进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        //查询当前分类是否关联了菜品，如果已经关联则抛出一个业务异常
        if (count1 > 0)
        {
            throw new CustomExpection("当前分类下已经关联了菜品，无法删除！");
        }
        //查询当前分类是否关联了套餐，如果已经关联则抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if(count2 > 0)
        {
            throw new CustomExpection("当前分类下已经关联了套餐，无法删除！");
        }


        //正常删除
        super.removeById(id);

    }
}
