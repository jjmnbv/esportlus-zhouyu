package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;

/**
 * 第三方绑定上报神策消费对象
 *
 * @author yangshidong
 * @date 2018/10/29
 */
public class Auth3BindSATrackMessageVo extends ConsumeBasicVo implements Serializable {
    private static final long serialVersionUID = 2944694687832819388L;

    /**
     * uid
     */
    private String uid;

    /**
     * 绑定事件
     * 默认为 AccountBinding
     */
    private String eventName;

    /**
     * 绑定类型
     * 1-绑定第三方以及手机  2-解绑第三方账号  3-修改手机号
     */
    private String bindType;

    /**
     * 账号类型
     * 1-wx  2-qq  3-手机号
     */
    private String accountType;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getBindType() {
        return bindType;
    }

    public void setBindType(String bindType) {
        this.bindType = bindType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Auth3BindSATrackMessageVo(String uid, String eventName, String bindType, String accountType) {
        this.uid = uid;
        this.eventName = eventName;
        this.bindType = bindType;
        this.accountType = accountType;
    }

    public Auth3BindSATrackMessageVo() {
    }

}
