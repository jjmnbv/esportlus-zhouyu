package com.kaihei.esportingplus.payment.api;

import org.apache.http.client.HttpClient;

/**
 * @Author: zhouyu
 * @Date: 2018/10/26 16:20
 * @Description: 支付类常量
 */
public class PayConstants {

    public static final String DOMAIN_API = "api.mch.weixin.qq.com";
    public static final String DOMAIN_API2 = "api2.mch.weixin.qq.com";
    public static final String DOMAIN_APIHK = "apihk.mch.weixin.qq.com";
    public static final String DOMAIN_APIUS = "apius.mch.weixin.qq.com";



    //网络IO时间
    public static final int READ_TIME_OUT = 6000;
    public static final int CONNECT_TIME_OUT = 7000;




    public static final String FAIL = "FAIL";
    public static final String CHANGE = "CHANGE";
    public static final String REFUNDCLOSE = "REFUNDCLOSE";




    public static final String HMACSHA256 = "HMAC-SHA256";
    public static final String MD5 = "MD5";

    public static final String FIELD_SIGN = "sign";
    public static final String FIELD_SIGN_TYPE = "sign_type";

    public static final String WXPAYSDK_VERSION = "WXPaySDK/3.0.9";
    public static final String USER_AGENT = WXPAYSDK_VERSION +
            " (" + System.getProperty("os.arch") + " " + System.getProperty("os.name") + " "
            + System.getProperty("os.version") +
            ") Java/" + System.getProperty("java.version") + " HttpClient/" + HttpClient.class
            .getPackage().getImplementationVersion();

    /**
     * QQ下单
     */
    public static final String QQ_UNIFIED_ORDER_SUFEIX = "https://qpay.qq.com/cgi-bin/pay/qpay_unified_order.cgi";

    /***
     * QQ查询
     */
    public static final String QQ_ORDER_QUERY_SUFEIX = "https://qpay.qq.com/cgi-bin/pay/qpay_order_query.cgi";

    /**
     * QQ关闭订单
     */
    public static final String QQ_ORDER_CLOSE_SUFIX = "https://qpay.qq.com/cgi-bin/pay/qpay_close_order.cgi";

    /**
     * QQ退款
     */
    public static final String QQ_ORDER_REFUND_SUFIX = "https://api.qpay.qq.com/cgi-bin/pay/qpay_refund.cgi";

    /**
     * QQ退款查询
     */
    public static final String QQ_REFUND_QUERY_SUFIX = "https://qpay.qq.com/cgi-bin/pay/qpay_refund_query.cgi";

    /***
     * QQ商户id(1418215101)的登录密码，QQ退款需要
     */
    public static final String QQ_MCH_PASSWORD = "403653";
    /**
     * 微信沙箱
     */
    /**
     * 获取沙箱秘钥
     */
    public static final String SANDBOX_SIGN_KEY_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey";
    /***
     * 下单沙箱
     */
    public static final String SANDBOX_UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/unifiedorder";
    /**
     * 订单查询沙箱
     */
    public static final String SANDBOX_ORDER_QUERY_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/orderquery";
    /**
     * 关闭订单沙箱
     */
    public static final String SANDBOX_CLOSE_ORDER_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/closeorder";
    /**
     * 退款沙箱
     */
    public static final String SANDBOX_REFUND_URL = "https://api.mch.weixin.qq.com/sandboxnew/secapi/pay/refund";
    /**
     * 退款查询沙箱
     */
    public static final String SANDBOX_REFUND_QUERY_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/refundquery";

    public static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    public static final String ORDER_QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
    public static final String CLOSE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
    public static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    public static final String REFUND_QUERY_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
}
