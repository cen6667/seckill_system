package com.zyc.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyc.seckill.pojo.Goods;
import com.zyc.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyc
 * @since 2022-06-23
 */
public interface IGoodsService extends IService<Goods> {
    /**
    * @description: 获取商品列表
    * @param:
    * @return:
    * @author zyc
    * @date: 2022/6/23 14:47
    */
    List<GoodsVo> findGoodsVo();

    /**
    * @description: 获取商品详情
    * @param:
    * @return:
    * @author zyc
    * @date: 2022/6/23 15:35
    */
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
