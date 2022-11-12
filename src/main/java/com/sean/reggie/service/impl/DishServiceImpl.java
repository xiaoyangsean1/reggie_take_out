package com.sean.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sean.reggie.common.R;
import com.sean.reggie.dto.DishDTO;
import com.sean.reggie.entity.Dish;
import com.sean.reggie.entity.DishFlavor;
import com.sean.reggie.mapper.DishMapper;
import com.sean.reggie.service.DishFlavorService;
import com.sean.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @小羊肖恩
 * @2022/11/03
 * @15:11
 * @Describe：
 */

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品，同时保存对应的口味数据
     * @param dishDTO
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {

        //保存菜品的基本信息到菜品表dish
        this.save(dishDTO);

        //菜品id
        Long dishId = dishDTO.getId();

        //菜品口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        for (DishFlavor flavor : flavors)   flavor.setDishId(dishId);

        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);

    }

    /**
     * 根据id查询菜品信息和口味信息
     * @param id
     * @return
     */
    @Override
    public DishDTO getByIdWithFlavor(Long id) {

        //查询菜品基本信息，从dish表中查询
        Dish dish = getById(id);
        DishDTO dishDTO = new DishDTO();

        BeanUtils.copyProperties(dish, dishDTO);


        //查询当前菜品对应的口味信息，从dish_flavor表中查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);

        dishDTO.setFlavors(flavors);



        return dishDTO;
    }

    @Transactional
    @Override
    public void updateWithFlavor(DishDTO dishDTO) {

        //更新dish表的基本信息
        updateById(dishDTO);

        //清理当前菜品对应口味数据---dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDTO.getId());
        dishFlavorService.remove(queryWrapper);


        //添加当前菜品对应口味数据---dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDTO.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDTO.getId());
            return item;

        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);

    }

}










































