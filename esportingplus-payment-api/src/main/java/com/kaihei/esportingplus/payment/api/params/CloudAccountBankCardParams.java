package com.kaihei.esportingplus.payment.api.params;

/**
 * @program: esportingplus
 * @description: 云账户提现到银行卡的传入参数
 * @author: xusisi
 * @create: 2018-10-25 18:04
 **/
public class CloudAccountBankCardParams {

    /***
     * 商户订单号，由商户保持唯⼀性(必填)， 64个英文字符以内
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

    /***
     * 银行开户姓名(必填)
     */
    private String realName;

    /**
     * 银行开户身份证号(必填)
     */
    private String idCard;

    /***
     * 打款金额(单位为元, 必填)
     */
    private String pay;

    /***选填***/

    /***
     * 备注信息(选填)
     */
    private String notes;

    /***
     * 打款备注(选填，最⼤20个字符，⼀个汉字占2个字符，不允许特殊字符： ' " & | @ % * ( ) - : # ￥)
     */
    private String payRemark;

    /***
     * 回调地址(选填，最大长度200)
     */
    private String notifyUrl;

    /***
     * 银行开户卡号(必填)
     */
    private String cardNo;

    /***
     * 用户或联系人手机号(选填)
     */
    private String phoneNo;

    /***差异值***/

    /***
     * 收款人id(选填)
     */
    private String anchorId;

}
