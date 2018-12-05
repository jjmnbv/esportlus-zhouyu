package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;

/**
 * @Auther: chen.junyong
 * @Date: 2018-09-21 14:58
 * @Description: 创建银行账号消息对象
 */
public class CreateBankMessageVo extends ConsumeBasicVo implements Serializable {

    private static final long serialVersionUID = -4509188478894188696L;

    private Integer userId;

    private String uid;

    public CreateBankMessageVo() {
    }

    public CreateBankMessageVo(Integer userId, String uid) {
        this.userId = userId;
        this.uid = uid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
