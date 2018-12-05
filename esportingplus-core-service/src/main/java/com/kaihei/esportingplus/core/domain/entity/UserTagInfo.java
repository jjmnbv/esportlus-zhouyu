package com.kaihei.esportingplus.core.domain.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @program: esportingplus
 * @description: 用户标签
 * @author: xusisi
 * @create: 2018-12-03 17:20
 **/
@Table(name = "user_tag_info")
public class UserTagInfo {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /***
     * 每个 tags 最大不能超过 40 个字节，标签中不能包含特殊字符。
     */
    @Column(name = "tag_name", nullable = false, length = 40, columnDefinition = "VARCHAR(40) COMMENT '用户标签' ")
    private String tagName;

    /***
     * 用户ID串，json格式的
     */
    @Column(name = "uids", nullable = false, length = 2048, columnDefinition = "varchar(2048) COMMENT '用户id集合' ")
    private String uids;

    /***
     * 该标签下的用户数量
     */
    @Column(name = "user_number", nullable = false, length = 10, columnDefinition = "int(10) COMMENT '该标签下的用户数量' ")
    private Integer userNumber;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create", nullable = false, columnDefinition = "timestamp  COMMENT '创建时间' ")
    private Date gmtCreate;

    /**
     * 数据操作员
     */
    @Column(name = "operator", nullable = false, columnDefinition = "varchar(64) COMMENT '数据操作员' ")
    private String operator;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified", columnDefinition = "timestamp  COMMENT '修改时间' ")
    private Date gmtModified;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getUids() {
        return uids;
    }

    public void setUids(String uids) {
        this.uids = uids;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(Integer userNumber) {
        this.userNumber = userNumber;
    }
}
