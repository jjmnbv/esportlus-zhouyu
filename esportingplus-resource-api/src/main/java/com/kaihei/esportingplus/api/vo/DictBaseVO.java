package com.kaihei.esportingplus.api.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 数据字典基本属性 vo
 * @author liangyi
 */
public class DictBaseVO<T> implements Serializable {

    private static final long serialVersionUID = -4269787550891840279L;

    /**
     * 主键 id
     */
    private Integer dictId;

    /**
     * 数据字典名称
     */
    private String name;

    /**
     * 数据字典code
     */
    private String code;

    /**
     * 数据字典value
     */
    private T value;

    /**
     * 状态 {@link com.kaihei.esportingplus.common.enums.StatusEnum}
     */
    @JsonIgnore
    private Integer status;

    /**
     * 排序权重
     */
    private Integer orderIndex;

    public DictBaseVO() {
    }

    public Integer getDictId() {
        return dictId;
    }

    public void setDictId(Integer dictId) {
        this.dictId = dictId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}