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
    @Autowired
    private AccessLimitInterceptor accessLimitInterceptor;

//     使用拦截器也可以加载
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userArgumentResolver);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
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
        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("")
                .excludePathPatterns("/","/login/toLogin")
                .excludePathPatterns(    //添加不拦截路径
                        "/**/*.html",                //html静态资源
                        "/**/*.js",                  //js静态资源
                        "/**/*.css"                  //css静态资源
                );
        registry.addInterceptor(accessLimitInterceptor);
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
