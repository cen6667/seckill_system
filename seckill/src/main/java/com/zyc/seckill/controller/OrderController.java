package com.zyc.seckill.controller;


import com.zyc.seckill.pojo.User;
import com.zyc.seckill.service.IOrderService;
import com.zyc.seckill.vo.OrderDetailVo;
import com.zyc.seckill.vo.RespBean;
import com.zyc.seckill.vo.RespBeanEnum;
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
 * @since 2022-06-23
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    //订单信息
    @RequestMapping("/detail")
    @ResponseBody
    public RespBean detail(User user, Long orderId) {
        if(user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        OrderDetailVo detail = orderService.getDetail(orderId);

        return RespBean.success(detail);

    }
}
