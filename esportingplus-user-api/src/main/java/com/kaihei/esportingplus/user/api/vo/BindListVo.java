package com.kaihei.esportingplus.user.api.vo;

/**
 * 获取用户绑定列表信息
 *
 * @author yangshidong
 * @date 2018/10/25
 * */
public class BindListVo {

    /**
     * 绑定的手机号
     */
    private String phone;

    /**
     * 是否绑定微信：bind unbind
     */
    private String wx;

    /**
     * 是否绑定qq：bind unbind
     */
    private String qq;

    /**
     * 是否绑定alipay
     */
    private String alipay;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }
}
