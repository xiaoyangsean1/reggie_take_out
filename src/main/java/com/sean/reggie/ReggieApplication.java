package com.sean.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @小羊肖恩
 * @2022/10/30
 * @14:05
 * @Describe：
 */

@EnableTransactionManagement
@ServletComponentScan
@Slf4j
@SpringBootApplication
public class ReggieApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class, args);
        log.info("项目启动成功了！");
    }


}
