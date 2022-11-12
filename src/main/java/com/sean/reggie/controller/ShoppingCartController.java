package com.sean.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sean.reggie.common.BaseContext;
import com.sean.reggie.common.R;
import com.sean.reggie.entity.ShoppingCart;
import com.sean.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @小羊肖恩
 * @2022/11/06
 * @15:51
 * @Describe：
 */

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("购物车详情：{}", shoppingCart);

        //指定用户id
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);


        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);

        if(dishId != null){
            //添加的是菜品
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        }else {
            //添加的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        //查询当前菜品或者套餐是否在购物车中
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);

        if(one != null){
            //如果已经存在，就在原购物车项数量上+1
            one.setNumber(one.getNumber() + 1);
            shoppingCartService.updateById(one);
        }else {
            //如果不存在，则添加到购物车中，数量默认就是1
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);

            one = shoppingCart;
        }

        return R.success(one);
    }


    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){

        //获取用户id
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(currentId != null, ShoppingCart::getUserId, currentId);
        //queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        //执行
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);


        return R.success(list);
    }


    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean(){

        //获取id
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        shoppingCartService.remove(queryWrapper);


        return R.success("清空成功！");
    }
}
