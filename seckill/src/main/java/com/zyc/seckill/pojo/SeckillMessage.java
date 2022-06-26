package com.zyc.seckill.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author zyc
 * @description: 秒杀信息
 */
@Data
@AllArgsConstructor
public class SeckillMessage {

    private User user;
    private Long goodsId;

}
