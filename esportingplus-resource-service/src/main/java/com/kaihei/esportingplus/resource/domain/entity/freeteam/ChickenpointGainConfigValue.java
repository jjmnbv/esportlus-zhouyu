package com.kaihei.esportingplus.resource.domain.entity.freeteam;

import com.kaihei.esportingplus.common.data.Castable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.ToString;

@Builder
@Table(name = "chickenpoint_gain_config_value")
@ToString
public class ChickenpointGainConfigValue implements Castable {

    /**
     * 主键 id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 免费车队类型Id
     */
    @Column(name = "free_team_type_id")
    private Integer freeTeamTypeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChickenpointGainConfigValue)) {
            return false;
        }
        ChickenpointGainConfigValue that = (ChickenpointGainConfigValue) o;
        return freeTeamTypeId.equals(that.freeTeamTypeId) &&
                gameDanId.equals(that.gameDanId) &&
                gameResultId.equals(that.gameResultId) &&
                baojiIdentityId.equals(that.baojiIdentityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(freeTeamTypeId, gameDanId, gameResultId, baojiIdentityId);
    }

    /**
     * 游戏段位Id
     */
    @Column(name = "game_dan_id")
    private Integer gameDanId;

    /**
     * 游戏结果Id
     */
    @Column(name = "game_result_id")
    private Integer gameResultId;

    /**
     * 暴鸡身份Id
     */
    @Column(name = "baoji_identity_id")
    private Integer baojiIdentityId;

    /**
     * 配置值
     */
    private Integer value;

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

    public ChickenpointGainConfigValue(Integer id, Integer freeTeamTypeId, Integer gameDanId, Integer gameResultId, Integer baojiIdentityId, Integer value, Date gmtCreate, Date gmtModified) {
        this.id = id;
        this.freeTeamTypeId = freeTeamTypeId;
        this.gameDanId = gameDanId;
        this.gameResultId = gameResultId;
        this.baojiIdentityId = baojiIdentityId;
        this.value = value;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public ChickenpointGainConfigValue() {
        super();
    }

    /**
     * 获取主键 id
     *
     * @return id - 主键 id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键 id
     *
     * @param id 主键 id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取免费车队类型Id
     *
     * @return free_team_type_id - 免费车队类型Id
     */
    public Integer getFreeTeamTypeId() {
        return freeTeamTypeId;
    }

    /**
     * 设置免费车队类型Id
     *
     * @param freeTeamTypeId 免费车队类型Id
     */
    public void setFreeTeamTypeId(Integer freeTeamTypeId) {
        this.freeTeamTypeId = freeTeamTypeId;
    }

    /**
     * 获取游戏段位Id
     *
     * @return game_dan_id - 游戏段位Id
     */
    public Integer getGameDanId() {
        return gameDanId;
    }

    /**
     * 设置游戏段位Id
     *
     * @param gameDanId 游戏段位Id
     */
    public void setGameDanId(Integer gameDanId) {
        this.gameDanId = gameDanId;
    }

    /**
     * 获取游戏结果Id
     *
     * @return game_result_id - 游戏结果Id
     */
    public Integer getGameResultId() {
        return gameResultId;
    }

    /**
     * 设置游戏结果Id
     *
     * @param gameResultId 游戏结果Id
     */
    public void setGameResultId(Integer gameResultId) {
        this.gameResultId = gameResultId;
    }

    /**
     * 获取暴鸡身份Id
     *
     * @return baoji_identity_id - 暴鸡身份Id
     */
    public Integer getBaojiIdentityId() {
        return baojiIdentityId;
    }

    /**
     * 设置暴鸡身份Id
     *
     * @param baojiIdentityId 暴鸡身份Id
     */
    public void setBaojiIdentityId(Integer baojiIdentityId) {
        this.baojiIdentityId = baojiIdentityId;
    }

    /**
     * 获取配置值
     *
     * @return value - 配置值
     */
    public Integer getValue() {
        return value;
    }

    /**
     * 设置配置值
     *
     * @param value 配置值
     */
    public void setValue(Integer value) {
        this.value = value;
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
}