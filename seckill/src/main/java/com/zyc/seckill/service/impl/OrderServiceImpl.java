package com.zyc.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zyc.seckill.pojo.Order;
import com.zyc.seckill.mapper.OrderMapper;
import com.zyc.seckill.pojo.SeckillGoods;
import com.zyc.seckill.pojo.SeckillOrder;
import com.zyc.seckill.pojo.User;
import com.zyc.seckill.service.IGoodsService;
import com.zyc.seckill.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyc.seckill.service.ISeckillGoodsService;
import com.zyc.seckill.service.ISeckillOrderService;
import com.zyc.seckill.utils.MD5Util;
import com.zyc.seckill.utils.UUIDUtil;
import com.zyc.seckill.vo.GoodsVo;
import com.zyc.seckill.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyc
 * @since 2022-06-23
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private ISeckillGoodsService seckillGoodsService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
    * @description: 秒杀减库存
    * @param:
    * @return:
    * @author zyc
    * @date: 2022/6/23 18:50
    */
    @Transactional
    @Override
    public Order seckill(User user, GoodsVo goodsVo) {
        //减秒杀商品的库存
        QueryWrapper<SeckillGoods> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("goods_id", goodsVo.getId());
        SeckillGoods seckillGoods = seckillGoodsService.getOne(wrapper1);
        // mybatis
        seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
        boolean seckillGoodsResult = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>()
                .setSql("stock_count="+"stock_count-1")
                .eq("id", seckillGoods.getGoodsId())
                .gt("stock_count", 0));//大于零判断

        ValueOperations valueOperations = redisTemplate.opsForValue();
        if (seckillGoods.getStockCount() < 1) {
            //判断是否还有库存
            valueOperations.set("isStockEmpty:" + goodsVo.getId(), "0");
            return null;
        }
        //生成订单
        Order order = new Order();
        order.setCreateDate(new Date());
        order.setGoodsCount(1);
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsId(goodsVo.getId());
        order.setGoodsPrice(goodsVo.getSeckillPrice());
        order.setUserId(user.getId());
        order.setDeliveryAddrId(0L);
        order.setOrderChannel(1);
        order.setStatus(0);
        orderService.save(order);

        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goodsVo.getId());
        seckillOrder.setUserId(user.getId());
        seckillOrderService.save(seckillOrder);

        redisTemplate.opsForValue().set("order:" + user.getId() + ":" + goodsVo.getId(), seckillOrder);
        return order;
    }
    /**
    * @description: 订单详情
    * @param:
    * @return:
    * @author zyc
    * @date: 2022/6/25 0:00
    */
    @Override
    public OrderDetailVo getDetail(Long orderId) {
        Order order = baseMapper.selectById(orderId);
        Long goodsId = order.getGoodsId();
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        OrderDetailVo res = new OrderDetailVo();
        res.setOrder(order);
        res.setGoodsVo(goodsVo);
        return res;
    }

    /**
    * @description: 获取秒杀地址
    * @param:
    * @return:
    * @author zyc
    * @date: 2022/6/27 17:21
    */
    @Override
    public String createPath(User user, Long goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisTemplate.opsForValue().set("seckillPath:" + user.getId() + ":" + goodsId, str, 60, TimeUnit.SECONDS);
        return str;
    }

    /**
     * @description: 校验秒杀接口地址
     * @param:
     * @return:
     * @author zyc
     * @date: 2022/6/27 17:26
     */
    @Override
    public Boolean checkPath(User user, String path, Long goodsId) {
        String key = "seckillPath:" + user.getId() + ":" + goodsId;
        String rightPath = (String) redisTemplate.opsForValue().get(key);
        if(rightPath == null || !rightPath.equals(path)) {
            return false;
        }
        return true;
    }
}
