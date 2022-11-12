package com.sean.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sean.reggie.entity.User;
import com.sean.reggie.mapper.UserMapper;
import com.sean.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @小羊肖恩
 * @2022/11/05
 * @18:38
 * @Describe：
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
