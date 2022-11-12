package com.sean.reggie.config;

import com.sean.reggie.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

/**
 * @小羊肖恩
 * @2022/10/30
 * @14:17
 * @Describe：
 */

@Slf4j
@Configuration
public class WebMVCConfig extends WebMvcConfigurationSupport {

    /**
     * 设置静态资源映射
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始静态资源映射！");
        registry.addResourceHandler("/backend/**").addResourceLocations("classPath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classPath:/front/");
    }

    /**
     * 扩展MVC框架的消息转换器
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        log.info("扩展消息转换器");

        //创建消息转换器
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Jackson将Java对象转换成json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到mvc框架的转换器集合中
        //要添加一个索引使得我们的消息转换器优先被执行，索引从0开始
        converters.add(0, messageConverter);
    }
}
