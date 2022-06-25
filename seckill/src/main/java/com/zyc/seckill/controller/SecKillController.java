package com.zyc.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zyc.seckill.pojo.Order;
import com.zyc.seckill.pojo.SeckillOrder;
import com.zyc.seckill.pojo.User;
import com.zyc.seckill.service.IGoodsService;
import com.zyc.seckill.service.IOrderService;
import com.zyc.seckill.service.ISeckillOrderService;
import com.zyc.seckill.vo.GoodsVo;
import com.zyc.seckill.vo.RespBean;
import com.zyc.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zyc
 * @description: 秒杀
 */
@Controller
@RequestMapping("/seckill")
public class SecKillController {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
    * @description: 秒杀
    * @param: 
    * @return: 
    * @author zyc
    * @date: 2022/6/23 18:40
    */
    @RequestMapping("/doSeckill2")
    public String doSeckill2(Model model, User user, Long goodsId) {
        if(user == null) {
            return "login";
        }
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goodsVo);
        //判断库存
        if(goodsVo.getStockCount() <= 0) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "secKillFail";
        }
        //判断当前用户是否已经秒杀过
        QueryWrapper<SeckillOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", user.getId());
        wrapper.eq("goods_id", goodsId);
        SeckillOrder seckillOrder = seckillOrderService.getOne(wrapper);
        if(seckillOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
            return "secKillFail";
        }
        //下订单
        Order order = orderService.seckill(user, goodsVo);
        model.addAttribute("order", order);
        return "orderDetail";

    }

    @RequestMapping("/doSeckill")
    @ResponseBody
    public RespBean doSeckill(Model model, User user, Long goodsId) {
        if(user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        // 我已经做了登录拦截
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goodsVo);
        //判断库存
        if(goodsVo.getStockCount() <= 0) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //判断当前用户是否已经秒杀过
//        QueryWrapper<SeckillOrder> wrapper = new QueryWrapper<>();
//        wrapper.eq("user_id", user.getId());
//        wrapper.eq("goods_id", goodsId);
//        SeckillOrder seckillOrder = seckillOrderService.getOne(wrapper);
        SeckillOrder seckillOrder =
                (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsVo.getId());
        if(seckillOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        //下订单
        Order order = orderService.seckill(user, goodsVo);
        model.addAttribute("order", order);
        return RespBean.success(order);

    }
    
}
