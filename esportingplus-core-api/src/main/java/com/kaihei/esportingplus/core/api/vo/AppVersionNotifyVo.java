package com.kaihei.esportingplus.core.api.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * app版本升级弹窗vo
 *
 * @author yangshidong
 * @date 2018/12/3
 */
public class AppVersionNotifyVo implements Serializable {

    private static final long serialVersionUID = 5638249809440703430L;


    /**
     * 客户端系统类型
     */
    private Short clientType;

    /**
     * 版本号
     */
    private String version;


    /**
     * 是否强制更新
     */
    private Boolean isForcedUpdate;


    /**
     * 下载地址
     */
    private String url;


    /**
     * 创建时间
     */
    private Date createDatetime;


    /**
     * 更新说明
     */
    private String logDesc;

    /**
     * 当前服务器域名
     * */
    private String apiHost;

    public Short getClientType() {
        return clientType;
    }

    public void setClientType(Short clientType) {
        this.clientType = clientType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Boolean getIsForcedUpdate() {
        return isForcedUpdate;
    }

    public void setIsForcedUpdate(Boolean forcedUpdate) {
        isForcedUpdate = forcedUpdate;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getLogDesc() {
        return logDesc;
    }

    public void setLogDesc(String logDesc) {
        this.logDesc = logDesc;
    }

    public String getApiHost() {
        return apiHost;
    }

    public void setApiHost(String apiHost) {
        this.apiHost = apiHost;
    }
}
