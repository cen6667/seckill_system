package com.zyc.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyc.seckill.exception.GlobalException;
import com.zyc.seckill.mapper.UserMapper;
import com.zyc.seckill.pojo.User;
import com.zyc.seckill.service.IUserService;
import com.zyc.seckill.utils.CookieUtil;
import com.zyc.seckill.utils.MD5Util;
import com.zyc.seckill.utils.UUIDUtil;
import com.zyc.seckill.utils.ValidatorUtil;
import com.zyc.seckill.vo.LoginVo;
import com.zyc.seckill.vo.RespBean;
import com.zyc.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyc
 * @since 2022-06-21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;

    /**
    * @description: 登录
    * @param:
    * @return:
    * @author zyc
    * @date: 2022/6/22 14:46
    */
    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        //已经使用validator实现，这里注释掉
//        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
//        }
//        // 校验手机号
//        if(!ValidatorUtil.isMobile(mobile)){
//            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
//        }
        //根据id获取用户
        User user = userMapper.selectById(mobile);
        if(null == user){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        //校验密码
        if(!MD5Util.formPassToDBPass(password, user.getSalt()).equals(user.getPassword())){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        // 生成cookie
        String ticket = UUIDUtil.uuid();
        request.getSession().setAttribute(ticket, user);
        CookieUtil.setCookie(request, response, "userTicket", ticket);
        return RespBean.success();
    }
}
