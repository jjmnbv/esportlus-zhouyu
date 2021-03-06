package com.kaihei.esportingplus.payment.api.params;

/**
 * @program: esportingplus
 * @description: 云账户提现到支付宝时传入的参数
 * @author: xusisi
 * @create: 2018-10-25 18:05
 **/
public class CloudAccountAlipayParams {

    /***
     * 商户订单号，由商户保持唯一性(必填)， 64个英文字符以内
     */
    private String orderId;

    /**
     * 商户代码(必填)
     */
    private String dealerId;

    /***
     * 代征主体(必填)
     */
    private String brokerId;

    /**
     * 姓名(必填)
     */
    private String realName;

    /**
     * 身份证(必填)
     */
    private String idCard;

    /**
     * 打款金额（单位为元, 必填）
     */
    private String pay;

    /**
     * 备注信息(选填)
     */
    private String notes;

    /**
     * 打款备注(选填，最大20个字符，⼀个汉字占2个字符，不允许特殊字符： ' " & | @ % * ( ) - : # ￥)
     */
    private String payRemark;

    /***
     * 回调地址(选填，最大长度200)
     */
    private String notifyUrl;

    /***
     * 校验支付宝账户姓名，可填 Check、 NoCheck
     */
    private String checkName;

    /**
     * 收款人支付宝账户(必填)
     */
    private String cardNo;
}
