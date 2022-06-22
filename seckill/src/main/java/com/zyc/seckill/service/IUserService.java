package com.zyc.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyc.seckill.pojo.User;
import com.zyc.seckill.vo.LoginVo;
import com.zyc.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyc
 * @since 2022-06-21
 */
public interface IUserService extends IService<User> {

    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    /**
    * @description: 根据cookie获取user
    * @param:
    * @return:
    * @author zyc
    * @date: 2022/6/22 18:14
    */
    User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response);

}
