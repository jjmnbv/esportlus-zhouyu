package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 游戏 VO
 * @author liangyi
 */
@Builder
@AllArgsConstructor
public class GameDictVO implements Serializable {

    private static final long serialVersionUID = 4733029823254453063L;

    /**
     * 数据字典Id
     */
    private Integer dictId;

    /**
     * 游戏名称
     */
    private String name;

    /**
     * 游戏图标
     */
    private String url;

    /**
     * 游戏 code
     */
    private String code;

    /**
     * 排序号
     */
    private Integer orderIndex;

    public GameDictVO() {
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
