package com.sean.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sean.reggie.common.R;
import com.sean.reggie.entity.User;
import com.sean.reggie.service.UserService;
import com.sean.reggie.utils.SMSUtils;
import com.sean.reggie.utils.ValidateCodeUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.lang.ref.PhantomReference;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @小羊肖恩
 * @2022/11/05
 * @18:40
 * @Describe：
 */

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 发送手机短信验证码
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){

        //获取手机号
        String phone = user.getPhone();

        if(StringUtils.isNotEmpty(phone)){
            //生成随机的4位验证码
            String code = String.valueOf(ValidateCodeUtils.generateValidateCode(4));
            log.info("code：{}", code);

            //调用阿里云提供的短信服务API完成发送短信
            //SMSUtils.sendMessage("瑞吉外卖", , phone, code);

            //需要将生成的验证码保存到Session中
            //session.setAttribute(phone, code);

            //将验证码缓存到redis中，并设置有效时长为5分钟
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);

            return R.success("发送成功！");

        }

        return R.error("发送失败！");
    }

    /**
     * 移动端登录
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map user, HttpSession session){

        log.info("phone；{}，code：{}", user.get("phone"), user.get("code"));

        //获取手机号
        String phone = user.get("phone").toString();
        //获取验证码
        String code = user.get("code").toString();
        //从session中获取保存的验证码
        //String validateCode = session.getAttribute(phone).toString();

        //从redis中取出验证码
        String validateCode = (String) redisTemplate.opsForValue().get(phone);

        //进行验证码的比对（页面提交的验证码和session中保存的验证码进行比对）
        if(validateCode != null && validateCode.equals(code)){
            //如果能够比对成功，说明登录成功
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User one = userService.getOne(queryWrapper);


            //判断当前手机号对应的用户是否为新用户，如果是新用户就自动注册
            if(one == null){
                one = new User();
                one.setPhone(phone);
                one.setStatus(1);
                userService.save(one);
            }
            session.setAttribute("user", one.getId());

            //如果用户登录成功，则删除redis中缓存的验证码

            redisTemplate.delete(phone);
            return R.success(one);

        }
        return R.error("登录失败！");
    }
}
