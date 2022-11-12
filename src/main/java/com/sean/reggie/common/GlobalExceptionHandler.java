package com.sean.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @小羊肖恩
 * @2022/10/31
 * @19:52
 * @Describe：
 */

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody //将结果封装为json格式
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 异常处理方法
     * @param ex
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        return R.error("失败了");
    }

    /**
     * 异常处理方法
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomExpection.class)
    public R<String> exceptionHandler(CustomExpection ex){
        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }

}
