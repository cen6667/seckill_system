package com.zyc.seckill.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zyc.seckill.pojo.Order;
import com.zyc.seckill.pojo.SeckillMessage;
import com.zyc.seckill.pojo.SeckillOrder;
import com.zyc.seckill.pojo.User;
import com.zyc.seckill.rabbitmq.MQSender;
import com.zyc.seckill.service.IGoodsService;
import com.zyc.seckill.service.IOrderService;
import com.zyc.seckill.service.ISeckillOrderService;
import com.zyc.seckill.vo.GoodsVo;
import com.zyc.seckill.vo.RespBean;
import com.zyc.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zyc
 * @description: 秒杀
 */
@Controller
@RequestMapping("/seckill")
public class SecKillController implements InitializingBean {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MQSender mqSender;
    // 内存标记
    private Map<Long, Boolean> stockEmptyMap = new HashMap<>();
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
        /*model.addAttribute("user", user);
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
        return RespBean.success(order);*/

        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 判断是否重复抢购
        SeckillOrder seckillOrder =
                (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if(seckillOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        // 通过内存标记减少Redis操作
        if(stockEmptyMap.get(goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        // 预减库存
        Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
        if(stock<0){
            // 无库存
            stockEmptyMap.put(goodsId, true);
            valueOperations.increment("seckillGoods:" + goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        // 下订单，发送给RabbitMQ
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqSender.sendSeckillMessage(JSON.toJSONString(seckillMessage));
        return RespBean.success(0);
    }

    /**
    * @description: 初始化执行方法
    * @param:
    * @return:
    * @author zyc
    * @date: 2022/6/26 17:50
    */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodsVo();
        if(list.size() == 0) {
            return ;
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        for(GoodsVo goodsVo : list) {
            valueOperations.set("seckillGoods:" + goodsVo.getId(), goodsVo.getStockCount());
            // 有库存
            stockEmptyMap.put(goodsVo.getId(), false);
        }
    }

    /**
    * @description: 前端轮询秒杀结果
    * @param:
    * @return:
    * @author zyc
    * @date: 2022/6/26 18:22
    */
    @RequestMapping("/result")
    @ResponseBody
    public RespBean getResult(User user, Long goodsId) {
        if(user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        // 获取秒杀结果
//        Long orderId = seckillOrderService.getResult(user, goodsId);
//        System.out.println(orderId);
//        return RespBean.success(orderId);
        String key = "order" + user.getId() + ":" + goodsId;
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Long orderId = -1L;
        boolean o = valueOperations.get(key) != null;
        System.out.println("是否"+o);
        if(valueOperations.get(key) != null) {
            SeckillOrder seckillOrder = (SeckillOrder) valueOperations.get(key);
            orderId = seckillOrder.getOrderId();
        } else if((int)valueOperations.get("seckillGoods:" + goodsId) <= 0) {
            orderId = -1L;
        } else {
            orderId = 0L;
        }
        return RespBean.success(orderId);
    }

}
