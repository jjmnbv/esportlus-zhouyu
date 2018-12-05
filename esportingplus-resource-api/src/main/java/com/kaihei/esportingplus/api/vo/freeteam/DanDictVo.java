package com.kaihei.esportingplus.api.vo.freeteam;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 段位类
 */
public class DanDictVo implements Serializable {

    private static final long serialVersionUID = -7424069601228189308L;
    /**
     * 数据字典Id
     */
    private Integer dictId;
    /**
     * 段位名称
     */
    private String name;
    /**
     * 段位图标
     */
    private String url;
    /**
     * 段位码
     */
    private String code;
    /**
     * 段位阶层
     */
    private Integer social;
    /**
     * 排序号
     */
    private Integer orderIndex;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSocial() {
        return social;
    }

    public void setSocial(Integer social) {
        this.social = social;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
