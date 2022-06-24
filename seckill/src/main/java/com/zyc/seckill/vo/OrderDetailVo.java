package com.zyc.seckill.vo;

import com.zyc.seckill.pojo.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailVo {

    private Order order;
    private GoodsVo goodsVo;
}
