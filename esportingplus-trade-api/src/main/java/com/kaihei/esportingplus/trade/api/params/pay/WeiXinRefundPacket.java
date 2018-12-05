package com.kaihei.esportingplus.trade.api.params.pay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/*/**
 *@Description: 微信退款结果数据包
 *
 *@author  Orochi-Yzh
 *@dateTime  2018/8/10 17:28
*/
@ApiModel(value = "微信退款结果数据包", description = "微信退款结果数据包")
public class WeiXinRefundPacket implements Serializable {

    @ApiModelProperty(value = "业务结果:SUCCESS/FAIL")
    private String result_code;
    @ApiModelProperty(value = "返回状态码:SUCCESS/FAIL")
    private String return_code;
    @ApiModelProperty(value = "返回信息，如非空，为错误原因 签名失败 参数格式校验错误")
    private String return_msg;
    @ApiModelProperty(value = "小程序ID")
    private String appid;
    @ApiModelProperty(value = "商户号")
    private String mch_id;
    @ApiModelProperty(value = "随机字符串")
    private String nonce_str;
    @ApiModelProperty(value = "签名")
    private String sign;
    @ApiModelProperty(value = "微信订单号")
    private String transaction_id;
    @ApiModelProperty(value = "商户订单号")
    private String out_trade_no;
    @ApiModelProperty(value = "微信退款单号")
    private String refund_id;
    @ApiModelProperty(value = "商户退款单号")
    private String out_refund_no;
    @ApiModelProperty(value = "退款渠道")
    private String refund_channel;
    @ApiModelProperty(value = "订单金额")
    private int total_fee;
    @ApiModelProperty(value = "申请退款金额")
    private int refund_fee;
    @ApiModelProperty(value = "代金券退款金额")
    private int coupon_refund_fee;
    @ApiModelProperty(value = "代金券使用数量")
    private int coupon_refund_count;
    @ApiModelProperty(value = "现金支付金额")
    private int cash_fee;
    @ApiModelProperty(value = "现金退款金额")
    private int cash_refund_fee;

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getRefund_id() {
        return refund_id;
    }

    public void setRefund_id(String refund_id) {
        this.refund_id = refund_id;
    }

    public String getOut_refund_no() {
        return out_refund_no;
    }

    public void setOut_refund_no(String out_refund_no) {
        this.out_refund_no = out_refund_no;
    }

    public String getRefund_channel() {
        return refund_channel;
    }

    public void setRefund_channel(String refund_channel) {
        this.refund_channel = refund_channel;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public int getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(int refund_fee) {
        this.refund_fee = refund_fee;
    }

    public int getCoupon_refund_fee() {
        return coupon_refund_fee;
    }

    public void setCoupon_refund_fee(int coupon_refund_fee) {
        this.coupon_refund_fee = coupon_refund_fee;
    }

    public int getCoupon_refund_count() {
        return coupon_refund_count;
    }

    public void setCoupon_refund_count(int coupon_refund_count) {
        this.coupon_refund_count = coupon_refund_count;
    }

    public int getCash_fee() {
        return cash_fee;
    }

    public void setCash_fee(int cash_fee) {
        this.cash_fee = cash_fee;
    }

    public int getCash_refund_fee() {
        return cash_refund_fee;
    }

    public void setCash_refund_fee(int cash_refund_fee) {
        this.cash_refund_fee = cash_refund_fee;
    }

    @Override
    public String toString() {
        return "WeiXinRefundPacket{" +
                "result_code='" + result_code + '\'' +
                ", return_code='" + return_code + '\'' +
                ", return_msg='" + return_msg + '\'' +
                ", appid='" + appid + '\'' +
                ", mch_id='" + mch_id + '\'' +
                ", nonce_str='" + nonce_str + '\'' +
                ", sign='" + sign + '\'' +
                ", transaction_id='" + transaction_id + '\'' +
                ", out_trade_no='" + out_trade_no + '\'' +
                ", refund_id='" + refund_id + '\'' +
                ", out_refund_no='" + out_refund_no + '\'' +
                ", refund_channel='" + refund_channel + '\'' +
                ", total_fee=" + total_fee +
                ", refund_fee=" + refund_fee +
                ", coupon_refund_fee=" + coupon_refund_fee +
                ", coupon_refund_count=" + coupon_refund_count +
                ", cash_fee=" + cash_fee +
                ", cash_refund_fee=" + cash_refund_fee +
                '}';
    }
}
