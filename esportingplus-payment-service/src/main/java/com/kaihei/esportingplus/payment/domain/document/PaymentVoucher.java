package com.kaihei.esportingplus.payment.domain.document;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * 支付凭证文档对象
 *
 * @author haycco
 */
@Document(collection = "payment_vouchers")
@TypeAlias("payment_voucher")
public class PaymentVoucher extends AbstractVoucher {

    private static final long serialVersionUID = -6237699625823188957L;

    /**
     * 第三方撤销请求参数
     */
    private String cancelRequestParams;
    /**
     * 第三方撤销请求地址
     */
    private String cancelRequestUrl;
    /**
     * 第三方撤销请求方法
     */
    private String cancelRequestMethod;
    /**
     * 第三方撤销请求时间戳
     */
    private LocalDateTime cancelTimestamp;
    /**
     * 第三方完成撤销的时间戳
     */
    private LocalDateTime cancelCompletedTime;

    /**
     * 第三方撤销接口返回元数据
     */
    private Object cancelMetadata;

    /**
     * 第三方关闭请求参数
     */
    private String closeRequestParams;
    /**
     * 第三方关闭请求地址
     */
    private String closeRequestUrl;
    /**
     * 第三方关闭请求方法
     */
    private String closeRequestMethod;
    /**
     * 第三方关闭请求时间戳
     */
    private LocalDateTime closeTimestamp;
    /**
     * 第三方完成关闭的时间戳
     */
    private LocalDateTime closeCompletedTime;

    /**
     * 第三方关闭接口返回元数据
     */
    private Object closeMetadata;

    public String getCancelRequestParams() {
        return cancelRequestParams;
    }

    public void setCancelRequestParams(String cancelRequestParams) {
        this.cancelRequestParams = cancelRequestParams;
    }

    public String getCancelRequestUrl() {
        return cancelRequestUrl;
    }

    public void setCancelRequestUrl(String cancelRequestUrl) {
        this.cancelRequestUrl = cancelRequestUrl;
    }

    public String getCancelRequestMethod() {
        return cancelRequestMethod;
    }

    public void setCancelRequestMethod(String cancelRequestMethod) {
        this.cancelRequestMethod = cancelRequestMethod;
    }

    public LocalDateTime getCancelTimestamp() {
        return cancelTimestamp;
    }

    public void setCancelTimestamp(LocalDateTime cancelTimestamp) {
        this.cancelTimestamp = cancelTimestamp;
    }

    public LocalDateTime getCancelCompletedTime() {
        return cancelCompletedTime;
    }

    public void setCancelCompletedTime(LocalDateTime cancelCompletedTime) {
        this.cancelCompletedTime = cancelCompletedTime;
    }

    public Object getCancelMetadata() {
        return cancelMetadata;
    }

    public void setCancelMetadata(Object cancelMetadata) {
        this.cancelMetadata = cancelMetadata;
    }

    public String getCloseRequestParams() {
        return closeRequestParams;
    }

    public void setCloseRequestParams(String closeRequestParams) {
        this.closeRequestParams = closeRequestParams;
    }

    public String getCloseRequestUrl() {
        return closeRequestUrl;
    }

    public void setCloseRequestUrl(String closeRequestUrl) {
        this.closeRequestUrl = closeRequestUrl;
    }

    public String getCloseRequestMethod() {
        return closeRequestMethod;
    }

    public void setCloseRequestMethod(String closeRequestMethod) {
        this.closeRequestMethod = closeRequestMethod;
    }

    public LocalDateTime getCloseTimestamp() {
        return closeTimestamp;
    }

    public void setCloseTimestamp(LocalDateTime closeTimestamp) {
        this.closeTimestamp = closeTimestamp;
    }

    public LocalDateTime getCloseCompletedTime() {
        return closeCompletedTime;
    }

    public void setCloseCompletedTime(LocalDateTime closeCompletedTime) {
        this.closeCompletedTime = closeCompletedTime;
    }

    public Object getCloseMetadata() {
        return closeMetadata;
    }

    public void setCloseMetadata(Object closeMetadata) {
        this.closeMetadata = closeMetadata;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
