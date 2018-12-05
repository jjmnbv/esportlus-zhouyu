package com.kaihei.esportingplus.trade.api.params.pay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/*/**
 *@Description: 微信支付结果数据包
 *
 *@author  Orochi-Yzh
 *@dateTime  2018/8/10 17:28
*/
@ApiModel(value = "微信支付结果数据包", description = "微信支付结果数据包")
public class WeiXinPayPacket implements Serializable {

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
    @ApiModelProperty(value = "用户在商户appid下的唯一标识")
    private String openid;
    @ApiModelProperty(value = "是否关注公众账号")
    private String is_subscribe;
    @ApiModelProperty(value = "交易类型")
    private String trade_type;
    @ApiModelProperty(value = "付款银行")
    private String bank_type;
    @ApiModelProperty(value = "微信支付订单号")
    private String transaction_id;
    @ApiModelProperty(value = "订单金额")
    private int total_fee;
    @ApiModelProperty(value = "货币种类")
    private String fee_type;
    @ApiModelProperty(value = "现金支付金额")
    private int cash_fee;
    @ApiModelProperty(value = "商户订单号")
    private String out_trade_no;
    @ApiModelProperty(value = "支付完成时间")
    private String time_end;
    @ApiModelProperty(value = "商家数据包")
    private Attach attach;


    /**
     *@Description: 商家数据包
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/8/10 17:28
     */
    @ApiModel(value = "商家数据包", description = "商家数据包")
    public class Attach {

        private int order_type;

        public int getOrder_type() {
            return order_type;
        }

        public void setOrder_type(int order_type) {
            this.order_type = order_type;
        }
    }

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

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getIs_subscribe() {
        return is_subscribe;
    }

    public void setIs_subscribe(String is_subscribe) {
        this.is_subscribe = is_subscribe;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getBank_type() {
        return bank_type;
    }

    public void setBank_type(String bank_type) {
        this.bank_type = bank_type;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public int getCash_fee() {
        return cash_fee;
    }

    public void setCash_fee(int cash_fee) {
        this.cash_fee = cash_fee;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public Attach getAttach() {
        return attach;
    }

    public void setAttach(Attach attach) {
        this.attach = attach;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    @Override
    public String toString() {
        return "WeiXinPayPacket{" +
                "result_code='" + result_code + '\'' +
                ", return_code='" + return_code + '\'' +
                ", return_msg='" + return_msg + '\'' +
                ", appid='" + appid + '\'' +
                ", mch_id='" + mch_id + '\'' +
                ", nonce_str='" + nonce_str + '\'' +
                ", sign='" + sign + '\'' +
                ", openid='" + openid + '\'' +
                ", is_subscribe='" + is_subscribe + '\'' +
                ", trade_type='" + trade_type + '\'' +
                ", bank_type='" + bank_type + '\'' +
                ", transaction_id='" + transaction_id + '\'' +
                ", total_fee=" + total_fee +
                ", fee_type='" + fee_type + '\'' +
                ", cash_fee=" + cash_fee +
                ", out_trade_no='" + out_trade_no + '\'' +
                ", time_end='" + time_end + '\'' +
                ", attach=" + attach +
                '}';
    }
}
