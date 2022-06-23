package com.zyc.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyc.seckill.pojo.Goods;
import com.zyc.seckill.vo.GoodsVo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zyc
 * @since 2022-06-23
 */
@Component
public interface GoodsMapper extends BaseMapper<Goods> {
    /**
    * @description: 获取商品列表
    * @param:
    * @return:
    * @author zyc
    * @date: 2022/6/23 14:49
    */
    List<GoodsVo> findGoodsVo();

    /**
    * @description: 获取商品详情
    * @param:
    * @return:
    * @author zyc
    * @date: 2022/6/23 15:38
    */
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
