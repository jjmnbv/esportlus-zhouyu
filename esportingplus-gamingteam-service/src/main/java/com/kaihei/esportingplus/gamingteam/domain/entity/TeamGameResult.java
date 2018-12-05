package com.kaihei.esportingplus.gamingteam.domain.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 车队比赛结果
 * @author liangyi
 */
@Data
@Builder
@AllArgsConstructor
public class TeamGameResult implements Serializable {

    private static final long serialVersionUID = -4278540790498020576L;

    /**
     * PVP车队比赛结果主键ID
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 车队ID
     */
    @Column(name = "team_id")
    private Long teamId;

    /**
     * 比赛结果序号, 表示第几局/第几个小时
     */
    @Column(name = "result_sequence")
    private Integer resultSequence;

    /**
     * 比赛结果 {@link com.kaihei.esportingplus.common.enums.GameResultEnum}
     */
    @Column(name = "game_result")
    private Integer gameResult;

    /**
     * 比赛结果截图
     */
    @Column(name = "screenshot")
    private String screenshot;

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

    public TeamGameResult() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Integer getResultSequence() {
        return resultSequence;
    }

    public void setResultSequence(Integer resultSequence) {
        this.resultSequence = resultSequence;
    }

    public Integer getGameResult() {
        return gameResult;
    }

    public void setGameResult(Integer gameResult) {
        this.gameResult = gameResult;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
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