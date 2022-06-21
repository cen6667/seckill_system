package com.zyc.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespBean {
    private long code;
    private String message;
    private Object obj;

    /**
    * @description: 成功返回结果
    * @param:
    * @return:
    * @author zyc
    * @date: 2022/6/21 21:12
    */
    public static RespBean success(){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(),
                RespBeanEnum.SUCCESS.getMessage(), null);
    }
    /**
     * @description: 成功返回结果
     * @param:
     * @return:
     * @author zyc
     * @date: 2022/6/21 21:12
     */
    public static RespBean success(Object obj){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(),
                RespBeanEnum.SUCCESS.getMessage(), obj);
    }

    /**
    * @description: 失败返回结果
    * @param:
    * @return:
    * @author zyc
    * @date: 2022/6/21 21:14
    */
    public static RespBean error(RespBeanEnum respBeanEnum){
        return new RespBean(respBeanEnum.getCode(),
                respBeanEnum.getMessage(), null);
    }

    /**
     * @description: 失败返回结果
     * @param:
     * @return:
     * @author zyc
     * @date: 2022/6/21 21:14
     */
    public static RespBean error(RespBeanEnum respBeanEnum, Object obj){
        return new RespBean(respBeanEnum.getCode(),
                respBeanEnum.getMessage(), obj);
    }

}
