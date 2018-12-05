package com.kaihei.esportingplus.gamingteam.api.vo.rpg;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 车队基本信息 VO
 * @author liangyi
 */
public class RPGRedisTeamVO extends RPGRedisTeamBaseVO implements Serializable{

    private static final long serialVersionUID = 6595179928419779148L;

    /** 车队id */
    private Long id;

    /** 车队原始位置数 */
    private Integer originalPositionCount;

    /** 车队当前的实际位置数 */
    private Integer actuallyPositionCount;

    /** 车队状态，0：准备中 1：已发车(进行中) 2：已解散 3：已结束 */
    private Integer status;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    public RPGRedisTeamVO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOriginalPositionCount() {
        return originalPositionCount;
    }

    public void setOriginalPositionCount(Integer originalPositionCount) {
        this.originalPositionCount = originalPositionCount;
    }

    public Integer getActuallyPositionCount() {
        return actuallyPositionCount;
    }

    public void setActuallyPositionCount(Integer actuallyPositionCount) {
        this.actuallyPositionCount = actuallyPositionCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}