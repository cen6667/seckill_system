package com.zyc.seckill.service.impl;

import com.zyc.seckill.pojo.Goods;
import com.zyc.seckill.mapper.GoodsMapper;
import com.zyc.seckill.service.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyc.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyc
 * @since 2022-06-23
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    /**
    * @description: 获取商品列表
    * @param:
    * @return:
    * @author zyc
    * @date: 2022/6/23 14:48
    */
    @Override
    public List<GoodsVo> findGoodsVo() {
        return goodsMapper.findGoodsVo();
    }
}
