package com.sean.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sean.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;


/**
 * @小羊肖恩
 * @2022/10/30
 * @15:34
 * @Describe：
 */

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
