package com.kaihei.esportingplus.trade.api.params.pay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/*/**
 *@Description: 微信确认支付结果数据包
 *
 *@author  Orochi-Yzh
 *@dateTime  2018/8/10 17:28
*/
@Data
@ApiModel(value = "微信确认支付结果数据包", description = "微信确认支付结果数据包")
public class WeiXinPayConfirmPacket implements Serializable {

    private static final long serialVersionUID = 8666022733220697934L;

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
    @ApiModelProperty(value = "订单金额")
    private int total_fee;
    @ApiModelProperty(value = "货币种类")
    private String fee_type;
    @ApiModelProperty(value = "现金支付金额")
    private int cash_fee;
    @ApiModelProperty(value = "微信支付订单号")
    private String transaction_id;
    @ApiModelProperty(value = "商户订单号")
    private String out_trade_no;
    @ApiModelProperty(value = "支付完成时间")
    private String time_end;
    @ApiModelProperty(value = "交易状态：SUCCESS/FAIL")
    private String trade_state;
    @ApiModelProperty(value = "交易状态描述：支付成功、失败")
    private String trade_state_desc;
    @ApiModelProperty(value = "商家数据包")
    private Attach attach;

    public WeiXinPayConfirmPacket() {
    }

    /**
     *@Description: 商家数据包
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/8/10 17:28
    */
    @ApiModel(value = "商家数据包", description = "商家数据包")
    public class Attach {

        private int order_type;

        public Attach() {
        }

        public int getOrder_type() {
            return order_type;
        }

        public void setOrder_type(int order_type) {
            this.order_type = order_type;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
