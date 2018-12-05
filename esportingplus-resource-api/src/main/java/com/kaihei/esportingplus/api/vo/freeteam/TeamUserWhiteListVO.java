package com.kaihei.esportingplus.api.vo.freeteam;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 免费车队白名单用户 VO
 * @author liangyi
 */
@Builder
@AllArgsConstructor
public class TeamUserWhiteListVO implements Serializable {

    private static final long serialVersionUID = -5802590763264237297L;

    /**
     * 用户白名单主键id
     */
    private Integer teamUserWhiteListId;

    /**
     * 用户uid
     */
    private String uid;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    public TeamUserWhiteListVO() {
    }

    public Integer getTeamUserWhiteListId() {
        return teamUserWhiteListId;
    }

    public void setTeamUserWhiteListId(Integer teamUserWhiteListId) {
        this.teamUserWhiteListId = teamUserWhiteListId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
