package com.sean.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sean.reggie.entity.Orders;

/**
 * @小羊肖恩
 * @2022/11/06
 * @19:28
 * @Describe：
 */
public interface OrderService extends IService<Orders> {

    //用户下单
    void submit(Orders orders);
}
