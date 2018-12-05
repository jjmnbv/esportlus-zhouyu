package com.kaihei.esportingplus.resource.domain.entity.freeteam;

import com.kaihei.esportingplus.common.data.Castable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 车队类型
 * @author liangyi
 */
@Table(name = "team_type")
@Builder
@AllArgsConstructor
public class TeamType implements Castable {

    private static final long serialVersionUID = 1929363387152679883L;

    /**
     * 免费车队类型主键id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Integer teamTypeId;

    /**
     * 免费车队名称
     */
    private String name;

    /**
     * 分类, 0:免费, 1:付费
     */
    private Byte category;

    /**
     * 所属游戏id,来自数据字典
     */
    @Column(name = "game_id")
    private Integer gameId;

    /**
     * 玩法模式id,来自数据字典(上分/陪玩)
     */
    @Column(name = "play_mode_id")
    private Integer playModeId;

    /**
     * 可组建的身份,来自数据字典(暴鸡/暴娘)
     */
    @Column(name = "baoji_identity_id")
    private Integer baojiIdentityId;

    /**
     * 最大座位数
     */
    @Column(name = "max_position_count")
    private Integer maxPositionCount;

    /**
     * 排序,值越大越靠前
     */
    @Column(name = "order_index")
    private Integer orderIndex;

    /**
     * 状态, 0:失效,1:有效
     */
    private Byte status;

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

    @Transient
    private List<TeamSettlementMode> teamSettlementModeList;

    public TeamType() {
        super();
    }

    public Integer getTeamTypeId() {
        return teamTypeId;
    }

    public void setTeamTypeId(Integer teamTypeId) {
        this.teamTypeId = teamTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getCategory() {
        return category;
    }

    public void setCategory(Byte category) {
        this.category = category;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Integer getPlayModeId() {
        return playModeId;
    }

    public void setPlayModeId(Integer playModeId) {
        this.playModeId = playModeId;
    }

    public Integer getBaojiIdentityId() {
        return baojiIdentityId;
    }

    public void setBaojiIdentityId(Integer baojiIdentityId) {
        this.baojiIdentityId = baojiIdentityId;
    }

    public Integer getMaxPositionCount() {
        return maxPositionCount;
    }

    public void setMaxPositionCount(Integer maxPositionCount) {
        this.maxPositionCount = maxPositionCount;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
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

    public List<TeamSettlementMode> getTeamSettlementModeList() {
        return teamSettlementModeList;
    }

    public void setTeamSettlementModeList(
            List<TeamSettlementMode> teamSettlementModeList) {
        this.teamSettlementModeList = teamSettlementModeList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}