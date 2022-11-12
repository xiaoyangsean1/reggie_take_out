package com.sean.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sean.reggie.entity.Category;

/**
 * @小羊肖恩
 * @2022/11/03
 * @10:07
 * @Describe：
 */

public interface CategotyService extends IService<Category> {

    void remove(Long id);
}
