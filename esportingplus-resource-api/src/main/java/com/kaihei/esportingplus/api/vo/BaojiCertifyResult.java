package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 暴鸡认证查询返回结果 -- 调用 python 端接口使用
 * @author liangyi
 */
public class BaojiCertifyResult implements Serializable {

    private static final long serialVersionUID = -436134214357972470L;
    /**
     * 暴鸡认证信息
     */
    private List<BaojiCertInfo> certInfo;

    public List<BaojiCertInfo> getCertInfo() {
        return certInfo;
    }

    public void setCertInfo(
            List<BaojiCertInfo> certInfo) {
        this.certInfo = certInfo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}