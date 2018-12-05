package com.kaihei.esportingplus.core.api.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询更新记录列表返回对象
 *
 * @author yangshidong
 * @date 2018/12/1
 */
public class AppVersionChangelogVo implements Serializable {

    private static final long serialVersionUID = -1163492141080839904L;
    private int id;

    /**
     * 客户端系统类型
     */
    private Short clientType;

    /**
     * 版本号
     */
    private String version;

    /**
     * 最低版本号(小于则强更)
     */
    private String requireVersion;

    /**
     * 是否强制更新
     */
    private Boolean isForcedUpdate;

    /**
     * 是否推荐更新
     */
    private Boolean isRecommendedUpdate;

    /**
     * 是否可用
     */
    private Boolean isEnabled;


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

    public String getRequireVersion() {
        return requireVersion;
    }

    public void setRequireVersion(String requireVersion) {
        this.requireVersion = requireVersion;
    }

    public Boolean getIsForcedUpdate() {
        return isForcedUpdate;
    }

    public void setIsForcedUpdate(Boolean forcedUpdate) {
        isForcedUpdate = forcedUpdate;
    }

    public Boolean getIsRecommendedUpdate() {
        return isRecommendedUpdate;
    }

    public void setIsRecommendedUpdate(Boolean recommendedUpdate) {
        isRecommendedUpdate = recommendedUpdate;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean enabled) {
        isEnabled = enabled;
    }
}