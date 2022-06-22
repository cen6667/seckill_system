package com.zyc.seckill.controller;

import com.zyc.seckill.service.IUserService;
import com.zyc.seckill.vo.LoginVo;
import com.zyc.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    private IUserService userService;

    /**
    * @description: 跳转登陆页面
    * @param:
    * @return:
    * @author zyc
    * @date: 2022/6/21 17:56
    */
    @RequestMapping(value = "/toLogin", method = RequestMethod.GET)
    public String toLogin() {
        return "login";
    }

    /**
    * @description: 登录功能
    * @param:
    * @return:
    * @author zyc
    * @date: 2022/6/22 14:37
    */
    @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doLogin(@Valid LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        // @Valid校验
        log.info("{}", loginVo);
        return userService.doLogin(loginVo);
    }
}
