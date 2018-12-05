package com.kaihei.esportingplus.resource.domain.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;

@Builder
@Table(name = "dictionary_expand_property_value")
public class DictionaryExpandPropertyValue implements DictEntity {

    /**
     * 主键 id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 数据字典扩展属性 id
     */
    @Column(name = "property_id")
    private Integer propertyId;

    /**
     * 数据字典扩展属性值
     */
    @Column(name = "property_value")
    private String propertyValue;

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

    public DictionaryExpandPropertyValue(Integer id, Integer propertyId, String propertyValue, Date gmtCreate, Date gmtModified) {
        this.id = id;
        this.propertyId = propertyId;
        this.propertyValue = propertyValue;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public DictionaryExpandPropertyValue() {
        super();
    }

    /**
     * 获取主键 id
     *
     * @return id - 主键 id
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键 id
     *
     * @param id 主键 id
     */
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取数据字典扩展属性 id
     *
     * @return property_id - 数据字典扩展属性 id
     */
    public Integer getPropertyId() {
        return propertyId;
    }

    /**
     * 设置数据字典扩展属性 id
     *
     * @param propertyId 数据字典扩展属性 id
     */
    public void setPropertyId(Integer propertyId) {
        this.propertyId = propertyId;
    }

    /**
     * 获取数据字典扩展属性值
     *
     * @return property_value - 数据字典扩展属性值
     */
    public String getPropertyValue() {
        return propertyValue;
    }

    /**
     * 设置数据字典扩展属性值
     *
     * @param propertyValue 数据字典扩展属性值
     */
    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue == null ? null : propertyValue.trim();
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