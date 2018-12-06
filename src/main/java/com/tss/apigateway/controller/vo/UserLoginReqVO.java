package com.tss.apigateway.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author: MQG
 * @date: 2018/12/6
 */
@ApiModel("用户登录请求参数")
public class UserLoginReqVO {
   
    @ApiModelProperty(value = "账号", example = "admin")
    private String userAcc;

    @ApiModelProperty(value = "密码", example = "admin")
    private String password;

    @ApiModelProperty(value = "登录角色 1管理员 2实验员 3教师 4学生", example = "1")
    private int type;

    public String getUserAcc() {
        return userAcc;
    }

    public void setUserAcc(String userAcc) {
        this.userAcc = userAcc;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
