package com.kaihei.esportingplus.resource.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "dictionary_category")
public class DictionaryCategory  implements DictEntity{
    /**
     * 主键 id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Integer dictionaryCategoryId;

    /**
     * 父级 id
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 数据字典分类名称
     */
    private String name;

    /**
     * 数据字典分类code
     */
    private String code;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否启用 0: 禁用, 1: 启用
     */
    private Byte status;

    /**
     * 创建时间
     */
    @JsonIgnore
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @JsonIgnore
    @Column(name = "gmt_modified")
    private Date gmtModified;

    @Transient
    @JsonIgnore
    private DictionaryCategory parentDictionaryCategory;
    
    @Transient
    private List<DictionaryCategory> childrenDictionaryCategorys;

    public List<DictionaryCategory> getChildrenDictionaryCategorys() {
        return childrenDictionaryCategorys;
    }

    public void setChildrenDictionaryCategorys(
            List<DictionaryCategory> childrenDictionaryCategorys) {
        this.childrenDictionaryCategorys = childrenDictionaryCategorys;
    }

    public DictionaryCategory getParentDictionaryCategory() {
        return parentDictionaryCategory;
    }

    public void setParentDictionaryCategory(
            DictionaryCategory parentDictionaryCategory) {
        this.parentDictionaryCategory = parentDictionaryCategory;
    }

    public DictionaryCategory(Integer id, Integer parentId, String name, String code, String remark, Byte status, Date gmtCreate, Date gmtModified) {
        this.dictionaryCategoryId = id;
        this.parentId = parentId;
        this.name = name;
        this.code = code;
        this.remark = remark;
        this.status = status;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public DictionaryCategory() {
        super();
    }

    /**
     * 获取主键 id
     *
     * @return id - 主键 id
     */
    @Override
    public Integer getId() {
        return dictionaryCategoryId;
    }

    /**
     * 设置主键 id
     *
     * @param id 主键 id
     */
    @Override
    public void setId(Integer id) {
        this.dictionaryCategoryId = id;
    }

    /**
     * 获取父级 id
     *
     * @return parent_id - 父级 id
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * 设置父级 id
     *
     * @param parentId 父级 id
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取数据字典分类名称
     *
     * @return name - 数据字典分类名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置数据字典分类名称
     *
     * @param name 数据字典分类名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取数据字典分类code
     *
     * @return code - 数据字典分类code
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置数据字典分类code
     *
     * @param code 数据字典分类code
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 获取是否启用 0: 禁用, 1: 启用
     *
     * @return status - 是否启用 0: 禁用, 1: 启用
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置是否启用 0: 禁用, 1: 启用
     *
     * @param status 是否启用 0: 禁用, 1: 启用
     */
    public void setStatus(Byte status) {
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