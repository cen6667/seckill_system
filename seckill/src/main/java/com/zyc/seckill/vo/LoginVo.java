package com.zyc.seckill.vo;

import com.zyc.seckill.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
* @description: 登录传参
* @author zyc
* @date: 2022/6/22 15:35
*/
@Data
public class LoginVo {
    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    @Length(min = 32)
    private String password;
}
