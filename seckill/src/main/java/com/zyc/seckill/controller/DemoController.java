package com.zyc.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/demo")
public class DemoController {

    /**
    * @description: 测试页面跳转
    * @param:
    * @return:
    * @author zyc
    * @date: 2022/6/21 16:03
    */
    @RequestMapping("/hello")
    public String hello(Model model){
        model.addAttribute("name","zyc");
        return "hello";
    }
}
