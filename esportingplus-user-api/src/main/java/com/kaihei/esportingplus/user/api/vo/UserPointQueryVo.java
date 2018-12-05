package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;

/**
 * 查询用户鸡分响应信息
 *
 * @author keriezhang
 * @date: 2018/10/9 11:45
 * @version: 1.0
 */
public class UserPointQueryVo implements Serializable {

    private static final long serialVersionUID = 6811056139852304512L;

    /**
     * 用户鸡分值
     */
    private Integer pointAmount;

    public Integer getPointAmount() {
        return pointAmount;
    }

    public void setPointAmount(Integer pointAmount) {
        this.pointAmount = pointAmount;
    }
}
