package com.kaihei.esportingplus.api.vo.freeteam;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 免费车队类型数据字典 VO
 * @author liangyi
 */
public class FreeTeamTypeDictVO implements Serializable {

    private static final long serialVersionUID = 5147000450019910653L;

    /**
     * 免费车队类型主键 id
     */
    private Integer freeTeamTypeId;

    /**
     * 免费车队类型名称 = 游戏名称 + 副标题
     */
    private String name;

    /**
     * 排序权重, 值越小越靠前
     */
    private Integer orderIndex;

    /**
     * 状态, 0:失效,1:有效
     */
    private Integer status;

    public FreeTeamTypeDictVO() {
    }

    public Integer getFreeTeamTypeId() {
        return freeTeamTypeId;
    }

    public void setFreeTeamTypeId(Integer freeTeamTypeId) {
        this.freeTeamTypeId = freeTeamTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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