package com.kaihei.esportingplus.resource.domain.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 暴鸡接单范围配置
 * @author liangyi
 */
@Table(name = "baoji_dan_range")
public class BaojiDanRange implements Serializable {

    private static final long serialVersionUID = 9205087438402268440L;

    /**
     * 暴鸡接单段位范围主键id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Integer baojiDanRangeId;

    /**
     * 所属游戏id
     */
    @Column(name = "game_id")
    private Integer gameId;

    /**
     * 暴鸡等级id,来自数据字典(普通/优选/超级/暴娘)
     */
    @Column(name = "baoji_level")
    private Integer baojiLevel;

    /**
     * 段位下限id,来自数据字典游戏段位
     */
    @Column(name = "lower_dan_id")
    private Integer lowerDanId;

    /**
     * 段位上限id,来自数据字典游戏段位
     */
    @Column(name = "upper_dan_id")
    private Integer upperDanId;

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

    public BaojiDanRange() {
        super();
    }

    public Integer getBaojiDanRangeId() {
        return baojiDanRangeId;
    }

    public void setBaojiDanRangeId(Integer baojiDanRangeId) {
        this.baojiDanRangeId = baojiDanRangeId;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Integer getBaojiLevel() {
        return baojiLevel;
    }

    public void setBaojiLevel(Integer baojiLevel) {
        this.baojiLevel = baojiLevel;
    }

    public Integer getLowerDanId() {
        return lowerDanId;
    }

    public void setLowerDanId(Integer lowerDanId) {
        this.lowerDanId = lowerDanId;
    }

    public Integer getUpperDanId() {
        return upperDanId;
    }

    public void setUpperDanId(Integer upperDanId) {
        this.upperDanId = upperDanId;
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