package com.sean.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sean.reggie.entity.AddressBook;
import com.sean.reggie.mapper.AddressBookMapper;
import com.sean.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @小羊肖恩
 * @2022/11/06
 * @15:14
 * @Describe：
 */

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
