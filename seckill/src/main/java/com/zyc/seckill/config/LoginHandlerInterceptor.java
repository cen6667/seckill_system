package com.zyc.seckill.config;

import com.zyc.seckill.pojo.User;
import com.zyc.seckill.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @description: 登录拦截器
* @author zyc
* @date: 2022/6/24 17:49
*/
@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 登录成功之后，应该有ticket信息
        // 获取cookie里面的ticket
        String ticket = CookieUtil.getCookieValue(request, "userTicket");

        User user = (User) redisTemplate.opsForValue().get("user:"+ticket);
        //说明没有登录
        if (user == null){
            //说明没有登录
            response.sendRedirect("/login/toLogin");
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

