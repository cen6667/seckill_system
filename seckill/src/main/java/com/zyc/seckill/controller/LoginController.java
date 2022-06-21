package com.zyc.seckill.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {
    /**
    * @description: 跳转登陆页面
    * @param:
    * @return:
    * @author zyc
    * @date: 2022/6/21 17:56
    */
    @RequestMapping("/toLogin")
    public String toLogin(){

    }
}
