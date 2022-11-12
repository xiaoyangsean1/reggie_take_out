package com.sean.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sean.reggie.entity.Employee;
import com.sean.reggie.mapper.EmployeeMapper;
import com.sean.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @小羊肖恩
 * @2022/10/30
 * @15:35
 * @Describe：
 */

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
