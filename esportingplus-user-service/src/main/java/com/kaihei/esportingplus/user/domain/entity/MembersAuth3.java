package com.kaihei.esportingplus.user.domain.entity;

import javax.persistence.*;

@Table(name = "members_auth3")
public class MembersAuth3 {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 第三方登陆平台代号：微信-WX，QQ-QQ，新浪微博-WB，支付宝-ZFB 
     */
    private String platform;

    /**
     * 第三方登陆平台标识：
     */
    private String identifier;

    /**
     * 第三方登陆平台凭证
     */
    private String credential;

    /**
     * 用户user_id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 唯一凭证(微信专用)
     */
    private String unionid;

    /**
     * 第三方登陆平台名称：默认-kaihei
     */
    @Column(name = "package_name")
    private String packageName;

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
     * 获取第三方登陆平台代号：微信-WX，QQ-QQ，新浪微博-WB，支付宝-ZFB
     *
     * @return platform - 第三方登陆平台代号：微信-WX，QQ-QQ，新浪微博-WB，支付宝-ZFB
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * 设置第三方登陆平台代号：微信-WX，QQ-QQ，新浪微博-WB，支付宝-ZFB
     *
     * @param platform 第三方登陆平台代号：微信-WX，QQ-QQ，新浪微博-WB，支付宝-ZFB
     */
    public void setPlatform(String platform) {
        this.platform = platform == null ? null : platform.trim();
    }

    /**
     * 获取第三方登陆平台标识：
     *
     * @return identifier - 第三方登陆平台标识：
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * 设置第三方登陆平台标识：
     *
     * @param identifier 第三方登陆平台标识：
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier == null ? null : identifier.trim();
    }

    /**
     * 获取第三方登陆平台凭证
     *
     * @return credential - 第三方登陆平台凭证
     */
    public String getCredential() {
        return credential;
    }

    /**
     * 设置第三方登陆平台凭证
     *
     * @param credential 第三方登陆平台凭证
     */
    public void setCredential(String credential) {
        this.credential = credential == null ? null : credential.trim();
    }

    /**
     * 获取用户user_id
     *
     * @return user_id - 用户user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户user_id
     *
     * @param userId 用户user_id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取唯一凭证(微信专用)
     *
     * @return unionid - 唯一凭证(微信专用)
     */
    public String getUnionid() {
        return unionid;
    }

    /**
     * 设置唯一凭证(微信专用)
     *
     * @param unionid 唯一凭证(微信专用)
     */
    public void setUnionid(String unionid) {
        this.unionid = unionid == null ? null : unionid.trim();
    }

    /**
     * 获取第三方登陆平台名称：默认-kaihei
     *
     * @return package_name - 第三方登陆平台名称：默认-kaihei
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * 设置第三方登陆平台名称：默认-kaihei
     *
     * @param packageName 第三方登陆平台名称：默认-kaihei
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName == null ? null : packageName.trim();
    }
}