package com.zyc.seckill.service;

import com.zyc.seckill.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zyc.seckill.pojo.User;
import com.zyc.seckill.vo.GoodsVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyc
 * @since 2022-06-23
 */
public interface IOrderService extends IService<Order> {

    Order seckill(User user, GoodsVo goodsVo);
}
