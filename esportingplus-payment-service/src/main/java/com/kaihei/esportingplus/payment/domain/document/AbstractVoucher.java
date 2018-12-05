package com.kaihei.esportingplus.payment.domain.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 凭证文档对象抽象类
 *
 * @author haycco
 */
public abstract class AbstractVoucher implements Serializable {

    private static final long serialVersionUID = -4840517008004177106L;

    @Id
    private String id;
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 支付订单号(来源表：extrnal_payment_order)
     */
    @Indexed(unique = true)
    private String orderId;
    /**
     * 调用第三方请求参数
     */
    private String requestParams;
    /**
     * 调用第三方请求地址
     */
    private String requestUrl;
    /**
     * 调用第三方请求方法
     */
    private String requestMethod;
    /**
     * 调用第三方请求时间戳
     */
    private LocalDateTime timestamp;
    /**
     * 第三方完成支付的时间戳
     */
    private LocalDateTime completedTime;
    /**
     * 凭证发起客户端IP
     */
    private String clientIp;
    /**
     * 第三方返回元数据
     */
    private Object metadata;
    /**
     * 其他信息
     */
    private String message;
    /**
     * 订单状态
     */
    private String state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDateTime getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(LocalDateTime completedTime) {
        this.completedTime = completedTime;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
