package com.kaihei.esportingplus.core.domain.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "app_version_changelog")
public class AppVersionChangelog {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 客户端系统类型
     */
    @Column(name = "client_type")
    private Short clientType;

    /**
     * 版本号
     */
    private String version;

    /**
     * 最低版本号(小于则强更)
     */
    @Column(name = "require_version")
    private String requireVersion;

    /**
     * 是否强制更新
     */
    @Column(name = "is_forced_update")
    private Boolean isForcedUpdate;

    /**
     * 是否推荐更新
     */
    @Column(name = "is_recommended_update")
    private Boolean isRecommendedUpdate;

    /**
     * 是否启用
     */
    @Column(name = "is_enabled")
    private Boolean isEnabled;

    /**
     * 是否删除
     */
    @Column(name = "is_deleted")
    private Boolean isDeleted;

    /**
     * 下载地址
     */
    private String url;

    /**
     * 包名 kaihei:开黑 liudu:六度
     */
    @Column(name = "package_name")
    private String packageName;

    /**
     * 创建时间
     */
    @Column(name = "create_datetime")
    private Date createDatetime;

    /**
     * 更新时间
     */
    @Column(name = "update_datetime")
    private Date updateDatetime;

    /**
     * 删除时间
     */
    @Column(name = "delete_datetime")
    private Date deleteDatetime;

    /**
     * 更新说明
     */
    @Column(name = "log_desc")
    private String logDesc;

    public AppVersionChangelog(Integer id, Short clientType, String version, String requireVersion, Boolean isForcedUpdate, Boolean isRecommendedUpdate, Boolean isEnabled, Boolean isDeleted, String url, String packageName, Date createDatetime, Date updateDatetime, Date deleteDatetime, String logDesc) {
        this.id = id;
        this.clientType = clientType;
        this.version = version;
        this.requireVersion = requireVersion;
        this.isForcedUpdate = isForcedUpdate;
        this.isRecommendedUpdate = isRecommendedUpdate;
        this.isEnabled = isEnabled;
        this.isDeleted = isDeleted;
        this.url = url;
        this.packageName = packageName;
        this.createDatetime = createDatetime;
        this.updateDatetime = updateDatetime;
        this.deleteDatetime = deleteDatetime;
        this.logDesc = logDesc;
    }

    public AppVersionChangelog() {
        super();
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取客户端系统类型
     *
     * @return client_type - 客户端系统类型
     */
    public Short getClientType() {
        return clientType;
    }

    /**
     * 设置客户端系统类型
     *
     * @param clientType 客户端系统类型
     */
    public void setClientType(Short clientType) {
        this.clientType = clientType;
    }

    /**
     * 获取版本号
     *
     * @return version - 版本号
     */
    public String getVersion() {
        return version;
    }

    /**
     * 设置版本号
     *
     * @param version 版本号
     */
    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
    }

    /**
     * 获取最低版本号(小于则强更)
     *
     * @return require_version - 最低版本号(小于则强更)
     */
    public String getRequireVersion() {
        return requireVersion;
    }

    /**
     * 设置最低版本号(小于则强更)
     *
     * @param requireVersion 最低版本号(小于则强更)
     */
    public void setRequireVersion(String requireVersion) {
        this.requireVersion = requireVersion == null ? null : requireVersion.trim();
    }

    /**
     * 获取是否强制更新
     *
     * @return is_forced_update - 是否强制更新
     */
    public Boolean getIsForcedUpdate() {
        return isForcedUpdate;
    }

    /**
     * 设置是否强制更新
     *
     * @param isForcedUpdate 是否强制更新
     */
    public void setIsForcedUpdate(Boolean isForcedUpdate) {
        this.isForcedUpdate = isForcedUpdate;
    }

    /**
     * 获取是否推荐更新
     *
     * @return is_recommended_update - 是否推荐更新
     */
    public Boolean getIsRecommendedUpdate() {
        return isRecommendedUpdate;
    }

    /**
     * 设置是否推荐更新
     *
     * @param isRecommendedUpdate 是否推荐更新
     */
    public void setIsRecommendedUpdate(Boolean isRecommendedUpdate) {
        this.isRecommendedUpdate = isRecommendedUpdate;
    }

    /**
     * 获取是否启用
     *
     * @return is_enabled - 是否启用
     */
    public Boolean getIsEnabled() {
        return isEnabled;
    }

    /**
     * 设置是否启用
     *
     * @param isEnabled 是否启用
     */
    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
     * 获取是否删除
     *
     * @return is_deleted - 是否删除
     */
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置是否删除
     *
     * @param isDeleted 是否删除
     */
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 获取下载地址
     *
     * @return url - 下载地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置下载地址
     *
     * @param url 下载地址
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * 获取包名 kaihei:开黑 liudu:六度
     *
     * @return package_name - 包名 kaihei:开黑 liudu:六度
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * 设置包名 kaihei:开黑 liudu:六度
     *
     * @param packageName 包名 kaihei:开黑 liudu:六度
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName == null ? null : packageName.trim();
    }

    /**
     * 获取创建时间
     *
     * @return create_datetime - 创建时间
     */
    public Date getCreateDatetime() {
        return createDatetime;
    }

    /**
     * 设置创建时间
     *
     * @param createDatetime 创建时间
     */
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    /**
     * 获取更新时间
     *
     * @return update_datetime - 更新时间
     */
    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    /**
     * 设置更新时间
     *
     * @param updateDatetime 更新时间
     */
    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    /**
     * 获取删除时间
     *
     * @return delete_datetime - 删除时间
     */
    public Date getDeleteDatetime() {
        return deleteDatetime;
    }

    /**
     * 设置删除时间
     *
     * @param deleteDatetime 删除时间
     */
    public void setDeleteDatetime(Date deleteDatetime) {
        this.deleteDatetime = deleteDatetime;
    }

    /**
     * 获取更新说明
     *
     * @return log_desc - 更新说明
     */
    public String getLogDesc() {
        return logDesc;
    }

    /**
     * 设置更新说明
     *
     * @param logDesc 更新说明
     */
    public void setLogDesc(String logDesc) {
        this.logDesc = logDesc == null ? null : logDesc.trim();
    }
}