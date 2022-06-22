package com.zyc.seckill.controller;

import com.zyc.seckill.pojo.User;
import com.zyc.seckill.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author zyc
 * @description: 商品
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IUserService userService;

    /**
    * @description: 商品列表页
    * @param:
    * @return:
    * @author zyc
    * @date: 2022/6/22 18:31
    */
    @RequestMapping("/toList")
    public String toList(Model model, User user){
        //已经使用MVC配置类解决登陆验证
//        if(StringUtils.isEmpty(ticket)){
//            return "login";
//        }
//        // 拿到session里面的user对象
////        User user = (User) session.getAttribute(ticket);
//        User user = userService.getUserByCookie(ticket, request, response);
        if(null == user){
            return "login";
        }
        model.addAttribute("user", user);
        return "goodsList";
    }

    /**
     * @description: 商品详情页
     * @param:
     * @return:
     * @author zyc
     * @date: 2022/6/22 18:31
     */
    @RequestMapping("/toDetail")
    public String toDetail(Model model, User user){
        model.addAttribute("user", user);
        return "goodsList";
    }
}
