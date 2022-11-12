package com.sean.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sean.reggie.entity.OrderDetail;
import com.sean.reggie.mapper.OrderDetailMapper;
import com.sean.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @小羊肖恩
 * @2022/11/06
 * @19:30
 * @Describe：
 */

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
