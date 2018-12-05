package com.kaihei.esportingplus.auth.server.domain.entity;

import javax.persistence.*;

@Table(name = "auth_resources")
public class AuthRecources {
    /**
     * uid
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private String id;

    /**
     * 接口路径
     */
    private String url;

    public AuthRecources(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public AuthRecources() {
        super();
    }

    /**
     * 获取uid
     *
     * @return id - uid
     */
    public String getId() {
        return id;
    }

    /**
     * 设置uid
     *
     * @param id uid
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取接口路径
     *
     * @return url - 接口路径
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置接口路径
     *
     * @param url 接口路径
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }
}