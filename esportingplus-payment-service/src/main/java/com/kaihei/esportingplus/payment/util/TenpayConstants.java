package com.kaihei.esportingplus.payment.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName TenpayConstants
 * @Description 微信/QQ支付，常量信息
 * @Author xusisi
 * @Date 2018/11/20 下午9:24
 */
public class TenpayConstants {

    public static final Map<String, String> errorMap = new HashMap<>(2);
    public static final Map<String, String> successMap = new HashMap<>(2);

    static {
        errorMap.put("return_code", "FAIL");
        errorMap.put("return_msg", "微信通知参数异常");
        successMap.put("return_code", "SUCCESS");
        successMap.put("return_msg", "OK");
    }


    //回调通知类型
    /***
     * 支付回调 payment
     */
    public static final String NOTIFY_TYPE_PAYMENT = "payment";

    /**
     * 退款回调 refund
     */
    public static final String NOTIFY_TYPE_REFUND = "refund";

    //基础参数

    /***
     *  应用ID
     */
    public static final String APPID = "appid";

    /**
     * 商户号
     */
    public static final String MCH_ID = "mch_id";

    /**
     * 设备号
     */
    public static final String DEVICE_INFO = "device_info";

    /**
     * 随机字符串
     */
    public static final String NONCE_STR = "nonce_str";

    /**
     * 签名
     */
    public static final String SIGN = "sign";

    /**
     * 签名类型
     */
    public static final String SIGN_TYPE = "sign_type";

    /**
     * 商品描述
     */
    public static final String BODY = "body";

    /***
     * 商品详情
     */
    public static final String DETAIL = "detail";
    /**
     * 附加数据
     */
    public static final String ATTACH = "attach";

    // 支付相关
    /**
     * 商户订单号
     */
    public static final String OUT_TRADE_NO = "out_trade_no";
    /**
     * 货币类型
     */
    public static final String FEE_TYPE = "fee_type";

    /**
     * 订单金额
     */
    public static final String TOTAL_FEE = "total_fee";

    /**
     * 终端IP
     */
    public static final String SPBILL_CREATE_IP = "spbill_create_ip";

    /**
     * 交易起始时间
     */
    public static final String TIME_START = "time_start";

    /**
     * 交易结束时间
     */
    public static final String TIME_EXPIRE = "time_expire";

    /**
     * 订单优惠标记
     */
    public static final String GOODS_TAG = "goods_tag";

    /**
     * 通知地址
     */
    public static final String NOTIFY_URL = "notify_url";
    /**
     * 交易类型
     */
    public static final String TRADE_TYPE = "trade_type";

    /**
     * 指定支付方式
     */
    public static final String LIMIT_PAY = "limit_pay";

    /**
     * 场景信息
     */
    public static final String scene_info = "scene_info";

    /**
     * 预支付交易会话标识
     */
    public static final String prepay_id = "prepay_id";

    /**
     * 加密信息
     */
    public static final String REQ_INFO = "req_info";

    //微信/QQ返回参数

    /**
     * 返回状态码:通信标识
     */
    public static final String RETURN_CODE = "return_code";

    /**
     * 通信标识：SUCCESS 成功
     */
    public static final String RETURN_CODE_SUCCESS = "SUCCESS";

    /***
     * 通信标识：FAIL 失败
     */
    public static final String RETURN_CODE_FAIL = "FAIL";

    /**
     * 返回信息
     */
    public static final String RETURN_MSG = "return_msg";

    /**
     * 业务结果
     */
    public static final String RESULT_CODE = "result_code";

    /***
     * 业务结果:SUCCESS 成功
     */
    public static final String RESULT_CODE_SUCCESS = "SUCCESS";

    /**
     * 业务结果: FAIL 失败
     */
    public static final String RESULT_CODE_FAIL = "FAIL";

    /**
     * 错误代码
     */
    public static final String ERR_CODE = "err_code";

    //错误码
    /***
     * 订单已支付>>>订单已支付，不能发起关单>>>订单已支付，不能发起关单，请当作已支付的正常交易
     */
    public static final String ERR_CODE_ORDERPAID = "ORDERPAID";

    /***
     * 此交易订单号不存在>>>查询系统中不存在此交易订单号>>>该API只能查提交支付交易返回成功的订单，请商户检查需要查询的订单号是否正确
     */
    public static final String ERR_CODE_ORDERNOTEXIST = "ORDERNOTEXIST";

    /***
     * 系统错误>>>系统错误>>>系统异常，请重新调用该API
     */
    public static final String ERR_CODE_SYSTEMERROR = "SYSTEMERROR";
    /***
     * 订单已关闭>>>订单已关闭，无法重复关闭>>>订单已关闭，无需继续调用
     */
    public static final String ERR_CODE_ORDERCLOSED = "ORDERCLOSED";
    /**
     * 签名错误>>>参数签名结果不正确>>>请检查签名参数和方法是否都符合签名算法要求
     */
    public static final String ERR_CODE_SIGNERROR = "SIGNERROR";
    /***
     * 请使用post方法>>>未使用post传递参数>>>请检查请求参数是否通过post方法提交
     */
    public static final String ERR_CODE_REQUIRE_POST_METHOD = "REQUIRE_POST_METHOD";
    /***
     * XML格式错误>>>XML格式错误>>>请检查XML参数格式是否正确
     */
    public static final String ERR_CODE_XML_FORMAT_ERROR = "XML_FORMAT_ERROR";

    /**
     * 错误代码描述
     */
    public static final String ERR_CODE_DES = "err_code_des";

    /***
     * 用户标识
     */
    public static final String OPENID = "openid";

    /***
     * 是否关注公众账号
     */
    public static final String IS_SUBSCRIBE = "is_subscribe";
    /***
     * 付款银行
     */
    public static final String BANK_TYPE = "bank_type";
    /***
     * 现金支付金额
     */
    public static final String CASH_FEE = "cash_fee";
    /***
     * 现金支付货币类型
     */
    public static final String CASH_FEE_TYPE = "cash_fee_type";
    /***
     * 用支付完成时间
     */
    public static final String TIME_END = "time_end";
    /***
     * 交易状态
     */
    public static final String TRADE_STATE = "trade_state";

    /***
     * 用户标识预支付交易会话标识
     */
    public static final String PREPAY_ID = "prepay_id";

    /***
     * QQ退款需要商户号id和密码
     * 操作员号
     */
    public static final String OP_USER_ID = "op_user_id";

    /***
     * QQ退款需要商户号id和密码
     * 操作员密码，就是商户号的登录密码MD5后
     */
    public static final String OP_USER_PASSWD = "op_user_passwd";
    /**
     * 微信订单号
     */
    public static final String TRANSACTION_ID = "transaction_id";

    /**
     * 微信退款单号
     */
    public static final String REFUND_ID = "refund_id";

    /**
     * 商户退款单号
     */
    public static final String OUT_REFUND_NO = "out_refund_no";

    /**
     * 应结订单金额
     */
    public static final String SETTLEMENT_TOTAL_FEE = "settlement_total_fee";

    /**
     * 申请退款金额
     */
    public static final String REFUND_FEE = "refund_fee";

    /**
     * 退款金额
     */
    public static final String SETTLEMENT_REFUND_FEE = "settlement_refund_fee";

    /**
     * 退款状态
     */
    public static final String REFUND_STATUS = "refund_status";

    /***
     * SUCCESS-退款成功
     */
    public static final String REFUND_STATUS_SUCCESS = "SUCCESS";
    /**
     * CHANGE-退款异常
     */
    public static final String REFUND_STATUS_CHANGE = "CHANGE";
    /***
     * REFUNDCLOSE—退款关闭
     */
    public static final String REFUND_STATUS_REFUNDCLOSE = "REFUNDCLOSE";

    /***
     * 退款成功时间
     */
    public static final String SUCCESS_TIME = "success_time";

    /**
     * 退款入账账户
     */
    public static final String REFUND_RECV_ACCOUT = "refund_recv_accout";

    /**
     * 退款资金来源
     */
    public static final String REFUND_ACCOUNT = "refund_account";

    /**
     * 退款发起来源
     */
    public static final String REFUND_REQUEST_SOURCE = "refund_request_source";

    /***
     * 退款成功时间
     */
    public static final String REFUND_SUCCESS_TIME_$N = "refund_success_time_$n";

    //交易状态码
    /***
     *  SUCCESS—支付成功
     */
    public static final String TRADE_STATE_SUCCESS = "SUCCESS";
    /**
     * REFUND—转入退款
     */
    public static final String TRADE_STATE_REFUND = "REFUND";
    /**
     * NOTPAY—未支付
     */
    public static final String TRADE_STATE_NOTPAY = "NOTPAY";
    /**
     * CLOSED—已关闭
     */
    public static final String TRADE_STATE_CLOSED = "CLOSED";
    /**
     * REVOKED—已撤销（刷卡支付）
     */
    public static final String TRADE_STATE_REVOKED = "REVOKED";
    /***
     * USERPAYING--用户支付中
     */
    public static final String TRADE_STATE_USERPAYING = "USERPAYING";
    /***
     * PAYERROR--支付失败(其他原因，如银行返回失败)
     */
    public static final String TRADE_STATE_PAYERROR = "PAYERROR";

    //货币类型：默认人民币CNY
    public static final String CURRENCY_TYPE = "CNY";

    /***
     * 交易类型trade_type
     * JSAPI--JSAPI支付（或小程序支付）、NATIVE--Native支付、APP--app支付，MWEB--H5支付，不同trade_type决定了调起支付的方式，
     * MICROPAY--付款码支付，付款码支付有单独的支付接口，所以接口不需要上传，该字段在对账单中会出现
     */
    //小程序或者公众号(微信内打开的H5页面)
    public static final String TENPAY_TRADE_TYPE_JSAPI = "JSAPI";

    //原生扫码支付
    public static final String TENPAY_TRADE_TYPE_NATIVE = "NATIVE";

    //付款码支付
    public static final String TENPAY_TRADE_TYPE_MICROPAY = "MICROPAY";

    //H5支付(微信外的浏览器打开的H5)
    public static final String TENPAY_TRADE_TYPE_MWEB = "MWEB";

    //APP支付
    public static final String TENPAY_TRADE_TYPE_APP = "APP";

}
