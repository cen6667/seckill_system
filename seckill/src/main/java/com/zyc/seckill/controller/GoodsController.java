package com.zyc.seckill.controller;

import com.zyc.seckill.pojo.User;
import com.zyc.seckill.service.IGoodsService;
import com.zyc.seckill.service.IUserService;
import com.zyc.seckill.vo.GoodsVo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author zyc
 * @description: 商品
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThymeleafViewResolver viewResolver;

    /**
    * @description: 商品列表页
    * @param:
    * @return:
    * @author zyc
    * @date: 2022/6/22 18:31
    */
    @RequestMapping(value = "/toList", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, User user, HttpServletRequest request, HttpServletResponse response){
        // 从Redis里获取页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());
//        return "goodsList";
        // 如果为空，手动渲染，存入Redis
        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(),model.asMap());
        String newHtml = viewResolver.getTemplateEngine().process("goodsList", webContext);
        // 添加失效时间一分钟
        if(!StringUtils.isEmpty(newHtml)) {
            valueOperations.set("goodsList", newHtml, 60, TimeUnit.SECONDS);
        }
        return newHtml;
    }

    /**
     * @description: 商品详情页
     * @param:
     * @return:
     * @author zyc
     * @date: 2022/6/22 18:31
     */
    @RequestMapping(value = "/toDetail/{goodsId}", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail(Model model, User user, @PathVariable Long goodsId, HttpServletRequest request, HttpServletResponse response){
        // 从Redis里获取页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsDetail:" + goodsId);
        if(!StringUtils.isEmpty(html)){
            return html;
        }

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
        // 如果为空，手动渲染，存入Redis
        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(),model.asMap());
        String newHtml = viewResolver.getTemplateEngine().process("goodsDetail", webContext);
        // 添加失效时间一分钟
        if(!StringUtils.isEmpty(newHtml)) {
            valueOperations.set("goodsDetail", newHtml, 60, TimeUnit.SECONDS);
        }
        return newHtml;
    }
}
