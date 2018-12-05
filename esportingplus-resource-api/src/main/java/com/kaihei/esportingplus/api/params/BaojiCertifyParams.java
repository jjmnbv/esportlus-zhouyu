package com.kaihei.esportingplus.api.params;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 暴鸡认证查询参数 -- 调用 python 端接口使用
 * @author liangyi
 */
public class BaojiCertifyParams implements Serializable {

    private static final long serialVersionUID = -8054784232879432889L;

    /**
     * 用户 uid
     */
    private String uid;

    /**
     * 暴鸡身份 baoji: 暴鸡, bn: 暴娘
     */
    private String identity;

    public BaojiCertifyParams() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public static class Builder {

        private BaojiCertifyParams baojiCertifyParams = new BaojiCertifyParams();

        public Builder uid(String uid) {
            baojiCertifyParams.uid = uid;
            return this;
        }

        public Builder identity(String identity) {
            baojiCertifyParams.identity = identity;
            return this;
        }

        public BaojiCertifyParams build() {
            return baojiCertifyParams;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}