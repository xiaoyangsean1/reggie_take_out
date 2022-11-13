package com.sean.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.sean.reggie.common.BaseContext;
import com.sean.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @小羊肖恩
 * @2022/10/31
 * @13:38
 * @Describe：检查用户是否已经登录
 */

@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        /**
         * 1.获取本次请求的URI
         * 2.判断本次请求是否需要处理
         * 3.如果不需要处理，则直接放行
         * 4.判断登录状态，如果已经登录，则直接放行
         * 5.如果未登录则返回未登录结果
         */

        //1.
        String requestURI = request.getRequestURI();
        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs"
        };

        //2.
        boolean check = check(urls, requestURI);

        //3.
        if(check){
            filterChain.doFilter(request, response);
            return;
        }

        //4.1
        Long empId = (Long) request.getSession().getAttribute("employee");
        if(empId != null){

            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request, response);
            return;
        }

        //4.2
        Long userId = (Long) request.getSession().getAttribute("user");
        if(userId != null){

            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request, response);
            return;
        }

        //5. ，通过输出流的方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;



    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) return true;
        }
        return false;
    }
}
