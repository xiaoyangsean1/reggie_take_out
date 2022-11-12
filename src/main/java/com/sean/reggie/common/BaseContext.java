package com.sean.reggie.common;

/**
 * @小羊肖恩
 * @2022/11/03
 * @9:43
 * @Describe：基于ThreadLocal的封装工具类，用于保存和获取当前用户的id
 */

public class BaseContext {

    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置值
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }


    /**
     * 获取值
     * @return
     */
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
