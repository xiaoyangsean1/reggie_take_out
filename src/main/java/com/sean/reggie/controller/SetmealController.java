package com.sean.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sean.reggie.common.R;
import com.sean.reggie.dto.SetmealDTo;
import com.sean.reggie.entity.Category;
import com.sean.reggie.entity.Setmeal;
import com.sean.reggie.service.CategotyService;
import com.sean.reggie.service.SetmealDishService;
import com.sean.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @小羊肖恩
 * @2022/11/05
 * @10:04
 * @Describe：套餐管理
 */

@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategotyService categoryService;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDTo setmealDto){
        log.info("套餐信息：{}", setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("新增成功！");
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize, String name){
        //分页构造器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDTo> setDTOPage = new Page<>();


        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //根据name进行like（模糊）查询
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, queryWrapper);

        //因为泛型不一样所以"records"属性不需要拷贝
        BeanUtils.copyProperties(pageInfo, setDTOPage, "records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDTo> list = records.stream().map((item) -> {
            SetmealDTo setmealDto = new SetmealDTo();
            BeanUtils.copyProperties(item, setmealDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }

            return setmealDto;
        }).collect(Collectors.toList());

        setDTOPage.setRecords(list);

        return R.success(setDTOPage);
    }


    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable("status") Integer status, @RequestParam Long ids){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getId, ids);
        Setmeal setmeal = setmealService.getOne(queryWrapper);
        setmeal.setStatus(status);

        setmealService.updateById(setmeal);

        return R.success("修改成功！");
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){

        log.info("ids：{}", ids);

        setmealService.removeWithDish(ids);

        return R.success("删除成功！");
    }

    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(Setmeal::getStatus, setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> setmealList = setmealService.list(queryWrapper);


        return R.success(setmealList);
    }
}




















































