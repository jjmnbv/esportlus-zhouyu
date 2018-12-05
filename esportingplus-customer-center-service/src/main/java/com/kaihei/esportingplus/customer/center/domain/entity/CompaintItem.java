package com.kaihei.esportingplus.customer.center.domain.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "compaint_item")
public class CompaintItem {
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
     * 投诉类型，0：默认 值，1：技术不好，2：态度不好，3：服务不好，4：其他原因
     */
    private Byte type;

    /**
     * 投诉内容
     */
    private String content;

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

    public CompaintItem(Long id, Long compaintId, Byte type, String content, Date gmtCreate, Date gmtModified) {
        this.id = id;
        this.compaintId = compaintId;
        this.type = type;
        this.content = content;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public CompaintItem() {
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
     * 获取投诉类型，0：默认 值，1：技术不好，2：态度不好，3：服务不好，4：其他原因
     *
     * @return type - 投诉类型，0：默认 值，1：技术不好，2：态度不好，3：服务不好，4：其他原因
     */
    public Byte getType() {
        return type;
    }

    /**
     * 设置投诉类型，0：默认 值，1：技术不好，2：态度不好，3：服务不好，4：其他原因
     *
     * @param type 投诉类型，0：默认 值，1：技术不好，2：态度不好，3：服务不好，4：其他原因
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * 获取投诉内容
     *
     * @return content - 投诉内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置投诉内容
     *
     * @param content 投诉内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
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