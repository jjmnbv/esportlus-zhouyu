package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 客户端版本信息 VO, 客户端用
 * @author liangyi
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class APPVersionConfigVO implements Serializable {

    private static final long serialVersionUID = 2718363720849617193L;

    /**
     * 版本号
     */
    private String version;

    /**
     * 更新内容
     */
    private String updateDesc;

    /**
     * 下载链接
     */
    private String downloadUrl;

    /**
     * 是否强制更新
     */
    private Boolean forceUpdate = false;

    /**
     * 是否提醒更新
     */
    private Boolean remindUpdate = false;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 接口地址
     */
    private String apiHost;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUpdateDesc() {
        return updateDesc;
    }

    public void setUpdateDesc(String updateDesc) {
        this.updateDesc = updateDesc;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Boolean getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(Boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public Boolean getRemindUpdate() {
        return remindUpdate;
    }

    public void setRemindUpdate(Boolean remindUpdate) {
        this.remindUpdate = remindUpdate;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getApiHost() {
        return apiHost;
    }

    public void setApiHost(String apiHost) {
        this.apiHost = apiHost;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}