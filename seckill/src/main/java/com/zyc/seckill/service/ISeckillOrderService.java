package com.zyc.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyc.seckill.pojo.SeckillOrder;
import com.zyc.seckill.pojo.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyc
 * @since 2022-06-23
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {
    Long getResult(User tUser, Long goodsId);
}
