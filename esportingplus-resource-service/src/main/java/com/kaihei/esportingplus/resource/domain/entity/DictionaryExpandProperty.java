package com.kaihei.esportingplus.resource.domain.entity;

import io.swagger.models.auth.In;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;

@Builder
@Table(name = "dictionary_expand_property")
public class DictionaryExpandProperty  implements DictEntity{
    /**
     * 主键 id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 数据字典 id
     */
    @Column(name = "dictionary_id")
    private Integer dictionaryId;

    /**
     * 扩展属性名称
     */
    private String name;

    /**
     * 扩展属性备注
     */
    private String remark;

    /**
     * 排序号
     */
    @Column(name = "order_index")
    private Integer orderIndex;

    /**
     * 是否启用, 1: 启用, 0: 禁用
     */
    private Integer status;

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

    public DictionaryExpandProperty(Integer id, Integer dictionaryId, String name, String remark, Integer orderIndex, Integer status, Date gmtCreate, Date gmtModified) {
        this.id = id;
        this.dictionaryId = dictionaryId;
        this.name = name;
        this.remark = remark;
        this.orderIndex = orderIndex;
        this.status = status;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public DictionaryExpandProperty() {
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
     * 获取数据字典 id
     *
     * @return dictionary_id - 数据字典 id
     */
    public Integer getDictionaryId() {
        return dictionaryId;
    }

    /**
     * 设置数据字典 id
     *
     * @param dictionaryId 数据字典 id
     */
    public void setDictionaryId(Integer dictionaryId) {
        this.dictionaryId = dictionaryId;
    }

    /**
     * 获取扩展属性名称
     *
     * @return name - 扩展属性名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置扩展属性名称
     *
     * @param name 扩展属性名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取扩展属性备注
     *
     * @return remark - 扩展属性备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置扩展属性备注
     *
     * @param remark 扩展属性备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 获取排序号
     *
     * @return order_index - 排序号
     */
    public Integer getOrderIndex() {
        return orderIndex;
    }

    /**
     * 设置排序号
     *
     * @param orderIndex 排序号
     */
    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    /**
     * 获取是否启用, 1: 启用, 0: 禁用
     *
     * @return status - 是否启用, 1: 启用, 0: 禁用
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置是否启用, 1: 启用, 0: 禁用
     *
     * @param status 是否启用, 1: 启用, 0: 禁用
     */
    public void setStatus(Integer status) {
        this.status = status;
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