package com.zyc.seckill.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

/**
 * @author zyc
 * @description: MVC配置类
 */
@Configuration
//@EnableWebMvc //表示完全接管mvc，放弃默认配置，现在这个版本加上好像已经不影响了
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private UserArgumentResolver userArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userArgumentResolver);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }
    
    /**
    * @description: 实例化拦截器，防止RedisTemplate还没加载
    * @param:
    * @return:
    * @author zyc
    * @date: 2022/6/24 18:06
    */
    @Bean
    public LoginHandlerInterceptor loginInterceptor() {
        return new LoginHandlerInterceptor();
    }

    /**
    * @description: 页面拦截器
    * @param:
    * @return:
    * @author zyc
    * @date: 2022/6/24 17:47
    */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/goods/*","/order/*", "/seckill/*")
                .excludePathPatterns("/","/login/toLogin");
    }

    /**
    * @description: 页面跳转
    * @param:
    * @return:
    * @author zyc
    * @date: 2022/6/24 17:47
    */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("goodsList");
    }

}
