package com.kaihei.esportingplus.resource.domain.entity.freeteam;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 创建车队暴鸡白名单
 * @author liangyi
 */
@Table(name = "team_user_white_list")
public class TeamUserWhiteList implements Serializable {

    private static final long serialVersionUID = 4028923858844183016L;

    /**
     * 创建车队暴鸡白名单主键 id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Integer teamUserWhiteListId;

    /**
     * 用户 uid
     */
    private String uid;

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

    public TeamUserWhiteList() {
        super();
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