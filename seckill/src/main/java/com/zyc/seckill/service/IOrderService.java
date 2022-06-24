package com.zyc.seckill.service;

import com.zyc.seckill.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zyc.seckill.pojo.User;
import com.zyc.seckill.vo.GoodsVo;
import com.zyc.seckill.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyc
 * @since 2022-06-23
 */
public interface IOrderService extends IService<Order> {
    /**
    * @description: 秒杀
    * @param: 
    * @return: 
    * @author zyc
    * @date: 2022/6/24 0:00
    */
    Order seckill(User user, GoodsVo goodsVo);
    /**
    * @description: 订单详情
    * @param:
    * @return:
    * @author zyc
    * @date: 2022/6/25 0:00
    */
    OrderDetailVo getDetail(Long orderId);
}
