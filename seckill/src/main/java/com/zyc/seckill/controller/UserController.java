package com.zyc.seckill.controller;


import com.zyc.seckill.pojo.User;
import com.zyc.seckill.rabbitmq.MQSender;
import com.zyc.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zyc
 * @since 2022-06-21
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    MQSender mqSender;
    /**
    * @description: 用户信息（测试使用）
    * @param:
    * @return:
    * @author zyc
    * @date: 2022/6/24 14:17
    */
    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(User user){
        return RespBean.success(user);
    }

    //测试发送消息
    @RequestMapping("/mq")
    @ResponseBody
    public void mq() {
        mqSender.send("hello");
    }

}
