package com.sean.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sean.reggie.common.CustomExpection;
import com.sean.reggie.dto.SetmealDTo;
import com.sean.reggie.entity.Setmeal;
import com.sean.reggie.entity.SetmealDish;
import com.sean.reggie.mapper.SetmealMapper;
import com.sean.reggie.service.SetmealDishService;
import com.sean.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.ws.ServiceMode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @小羊肖恩
 * @2022/11/03
 * @15:12
 * @Describe：
 */

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐，同时需要保存套餐和菜品之间的关系
     * @param setmealDto
     */
    @Transactional
    @Override
    public void saveWithDish(SetmealDTo setmealDto) {

        //保存套餐的基本信息，操作setmeal表执行insert操作
        save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());

            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品的关联信息，操作setmeal_dish表执行insert操作
        setmealDishService.saveBatch(setmealDishes);

    }

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     * @param ids
     */
    @Transactional
    @Override
    public void removeWithDish(List<Long> ids) {

        //查询套餐状态，确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(queryWrapper);

        //如果不能则抛出一个业务异常
        if(count > 0)   throw new CustomExpection("套餐正在售卖中，无法删除！");

        //如果可以删除，则先删除套餐表中的数据setmeal
        this.removeByIds(ids);

        //删除关系表中的数据setmeal_dish
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(lambdaQueryWrapper);

    }


}





































