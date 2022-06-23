package com.zyc.seckill.controller;

import com.zyc.seckill.pojo.User;
import com.zyc.seckill.service.IGoodsService;
import com.zyc.seckill.service.IUserService;
import com.zyc.seckill.vo.GoodsVo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author zyc
 * @description: 商品
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IGoodsService goodsService;

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
        model.addAttribute("goodsList", goodsService.findGoodsVo());
        return "goodsList";
    }

    /**
     * @description: 商品详情页
     * @param:
     * @return:
     * @author zyc
     * @date: 2022/6/22 18:31
     */
    @RequestMapping("/toDetail/{goodsId}")
    public String toDetail(Model model, User user, @PathVariable Long goodsId){
        model.addAttribute("user", user);
        //获取秒杀时间
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        // 秒杀状态
        int secKillStatus = 0;
        // 秒杀倒计时
        int remainSeconds = 0;
        if(nowDate.before(startDate)){
            // 秒杀还未开始
            remainSeconds = (int) ((startDate.getTime() - nowDate.getTime())/1000);
        } else if(nowDate.after(endDate)){
            // 秒杀结束
            secKillStatus = 2;
            remainSeconds = -1;
        }else {
            // 秒杀进行中
            secKillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("secKillStatus", secKillStatus);
        model.addAttribute("goods", goodsVo);
        return "goodsDetail";
    }
}
