package com.kaihei.esportingplus.customer.center.domain.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "compaint_item_picture")
public class CompaintItemPicture {
    /**
     * 投诉ID
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 所属投诉单ID
     */
    @Column(name = "compaint_id")
    private Long compaintId;

    /**
     * 凭证图片网络地址
     */
    private String url;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    private Date gmtModified;

    public CompaintItemPicture(Long id, Long compaintId, String url, Date gmtCreate, Date gmtModified) {
        this.id = id;
        this.compaintId = compaintId;
        this.url = url;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public CompaintItemPicture() {
        super();
    }

    /**
     * 获取投诉ID
     *
     * @return id - 投诉ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置投诉ID
     *
     * @param id 投诉ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取所属投诉单ID
     *
     * @return compaint_id - 所属投诉单ID
     */
    public Long getCompaintId() {
        return compaintId;
    }

    /**
     * 设置所属投诉单ID
     *
     * @param compaintId 所属投诉单ID
     */
    public void setCompaintId(Long compaintId) {
        this.compaintId = compaintId;
    }

    /**
     * 获取凭证图片网络地址
     *
     * @return url - 凭证图片网络地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置凭证图片网络地址
     *
     * @param url 凭证图片网络地址
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * 获取创建时间
     *
     * @return gmt_create - 创建时间
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * 设置创建时间
     *
     * @param gmtCreate 创建时间
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * 获取修改时间
     *
     * @return gmt_modified - 修改时间
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * 设置修改时间
     *
     * @param gmtModified 修改时间
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}