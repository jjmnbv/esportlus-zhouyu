package com.kaihei.esportingplus.resource.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

@Data
@Table(name = "dictionary")
public class Dictionary implements DictEntity {
    /**
     * 主键 id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Integer dictId;

    /**
     * 父级 id
     */
    @JsonIgnore
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 数据字典分类id
     */
    @JsonIgnore
    @Column(name = "category_id")
    private Integer categoryId;

    /**
     * 数据字典名称
     */
    private String name;

    /**
     * 数据字典code
     */
    private String code;

    /**
     * 有效状态, 0:禁用，1：启用, 默认值
     */
    private Byte status;

    /**
     * 排序号
     */
    @JsonIgnore
    @Column(name = "order_index")
    private Integer orderIndex;

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
    private Dictionary parentDictionary;
    @Transient
    @JsonIgnore
    private List<Dictionary> childDictionary;
    @Transient
    @JsonIgnore
    private DictionaryCategory dictionaryCategory;
    @Transient
    @JsonIgnore
    private List<DictionaryExpand> expandProperties;

    /**
     * 数据字典值
     */
    private String value;

    public Dictionary(Integer id, Integer parentId, Integer categoryId, String name, String code,
            Byte status, Integer orderIndex, Date gmtCreate, Date gmtModified, String value) {
        this.dictId = id;
        this.parentId = parentId;
        this.categoryId = categoryId;
        this.name = name;
        this.code = code;
        this.status = status;
        this.orderIndex = orderIndex;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
        this.value = value;
    }

    public Dictionary() {
        super();
    }

    /**
     * 获取主键 id
     *
     * @return id - 主键 id
     */
    @JsonIgnore
    @Override
    public Integer getId() {
        return dictId;
    }

    /**
     * 设置主键 id
     *
     * @param id 主键 id
     */
    @Override
    public void setId(Integer id) {
        this.dictId = id;
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
     * 获取数据字典分类id
     *
     * @return category_id - 数据字典分类id
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * 设置数据字典分类id
     *
     * @param categoryId 数据字典分类id
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 获取数据字典名称
     *
     * @return name - 数据字典名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置数据字典名称
     *
     * @param name 数据字典名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取数据字典code
     *
     * @return code - 数据字典code
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置数据字典code
     *
     * @param code 数据字典code
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 获取有效状态, 0:禁用，1：启用, 默认值
     *
     * @return status - 有效状态, 0:禁用，1：启用, 默认值
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置有效状态, 0:禁用，1：启用, 默认值
     *
     * @param status 有效状态, 0:禁用，1：启用, 默认值
     */
    public void setStatus(Byte status) {
        this.status = status;
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

    /**
     * 获取数据字典值
     *
     * @return value - 数据字典值
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置数据字典值
     *
     * @param value 数据字典值
     */
    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}