package com.sean.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sean.reggie.common.R;
import com.sean.reggie.dto.DishDTO;
import com.sean.reggie.entity.Category;
import com.sean.reggie.entity.Dish;
import com.sean.reggie.entity.DishFlavor;
import com.sean.reggie.service.CategotyService;
import com.sean.reggie.service.DishFlavorService;
import com.sean.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @小羊肖恩
 * @2022/11/03
 * @20:33
 * @Describe：菜品管理
 */

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategotyService categotyService;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDTO dishDTO){
        dishService.saveWithFlavor(dishDTO);
        return R.success("新增成功！");
    }


    /**
     * 菜品信息分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

        //分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDTO> dishDTOPage = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.like(name != null, Dish::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //进行分页查询
        dishService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDTOPage, "records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDTO> list = records.stream().map((item) -> {
            DishDTO dishDTO = new DishDTO();

            BeanUtils.copyProperties(item, dishDTO);

            //获取分类id
            Long categoryId = item.getCategoryId();

            //根据id查询分类名称

            Category category = categotyService.getById(categoryId);
            if(category != null){

                String categoryName = category.getName();
                dishDTO.setCategoryName(categoryName);
            }

            return dishDTO;

        }).collect(Collectors.toList());

        dishDTOPage.setRecords(list);

        return R.success(dishDTOPage);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDTO> get(@PathVariable Long id){

        DishDTO dishDTO = dishService.getByIdWithFlavor(id);

        return R.success(dishDTO);
    }


    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDTO dishDTO){

        dishService.updateWithFlavor(dishDTO);

        return R.success("修改成功");
    }

    /**
     * 修改菜品的状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable("status") Integer status, @RequestParam Long ids){

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getId, ids);
        Dish dish = dishService.getOne(queryWrapper);
        dish.setStatus(status);
        dishService.updateById(dish);


        return R.success("修改成功！");
    }


//    /**
//     * 根据分类id查询相应菜品
//     * @param dish
//     * @return
//     */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//
//        //条件构造器
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        //添加查询条件
//        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
//        queryWrapper.eq(Dish::getStatus, 1);
//        //添加排序条件
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        //执行
//        List<Dish> list = dishService.list(queryWrapper);
//
//        return R.success(list);
//    }


    /**
     * 根据分类id查询相应菜品
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDTO>> list(Dish dish){

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        //执行
        List<Dish> list = dishService.list(queryWrapper);

        List<DishDTO> dishDTOList = list.stream().map((item) -> {
            DishDTO dishDTO = new DishDTO();

            BeanUtils.copyProperties(item, dishDTO);

            //获取分类id
            Long categoryId = item.getCategoryId();

            //根据id查询分类名称

            Category category = categotyService.getById(categoryId);
            if(category != null){

                String categoryName = category.getName();
                dishDTO.setCategoryName(categoryName);
            }

            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);

            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDTO.setFlavors(dishFlavorList);

            return dishDTO;

        }).collect(Collectors.toList());


        return R.success(dishDTOList);
    }
}
