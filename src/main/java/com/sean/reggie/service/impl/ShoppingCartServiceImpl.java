package com.sean.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sean.reggie.entity.ShoppingCart;
import com.sean.reggie.mapper.ShoppingCartMapper;
import com.sean.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @小羊肖恩
 * @2022/11/06
 * @17:21
 * @Describe：
 */

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
