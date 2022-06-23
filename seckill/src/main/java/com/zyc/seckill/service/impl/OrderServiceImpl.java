package com.zyc.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zyc.seckill.pojo.Order;
import com.zyc.seckill.mapper.OrderMapper;
import com.zyc.seckill.pojo.SeckillGoods;
import com.zyc.seckill.pojo.SeckillOrder;
import com.zyc.seckill.pojo.User;
import com.zyc.seckill.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyc.seckill.service.ISeckillGoodsService;
import com.zyc.seckill.service.ISeckillOrderService;
import com.zyc.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    /**
    * @description: 秒杀减库存
    * @param:
    * @return:
    * @author zyc
    * @date: 2022/6/23 18:50
    */
    @Override
    public Order seckill(User user, GoodsVo goodsVo) {
        //减秒杀商品的库存
        QueryWrapper<SeckillGoods> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("goods_id", goodsVo.getId());
        SeckillGoods seckillGoods = seckillGoodsService.getOne(wrapper1);
        seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
        seckillGoodsService.updateById(seckillGoods);
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
        return order;
    }
}
