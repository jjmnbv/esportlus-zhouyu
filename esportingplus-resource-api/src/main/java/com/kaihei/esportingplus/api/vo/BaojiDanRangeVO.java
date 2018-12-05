package com.kaihei.esportingplus.api.vo;

import com.kaihei.esportingplus.api.vo.freeteam.DanDictVo;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 暴鸡接单范围 VO
 * @author liangyi
 */
@Builder
@AllArgsConstructor
public class BaojiDanRangeVO implements Serializable {

    private static final long serialVersionUID = 6704177040360937478L;

    /**
     * 暴鸡接单段位范围主键id
     */
    private Integer baojiDanRangeId;

    /**
     * 所属游戏id
     */
    private GameDictVO game;

    /**
     * 暴鸡等级 {@link com.kaihei.esportingplus.common.enums.BaojiLevelEnum}
     */
    private Integer baojiLevel;

    /**
     * 段位下限
     */
    private DanDictVo lowerDan;

    /**
     * 段位上限
     */
    private DanDictVo upperDan;


    public BaojiDanRangeVO() {
    }

    public Integer getBaojiDanRangeId() {
        return baojiDanRangeId;
    }

    public void setBaojiDanRangeId(Integer baojiDanRangeId) {
        this.baojiDanRangeId = baojiDanRangeId;
    }

    public GameDictVO getGame() {
        return game;
    }

    public void setGame(GameDictVO game) {
        this.game = game;
    }

    public Integer getBaojiLevel() {
        return baojiLevel;
    }

    public void setBaojiLevel(Integer baojiLevel) {
        this.baojiLevel = baojiLevel;
    }

    public DanDictVo getLowerDan() {
        return lowerDan;
    }

    public void setLowerDan(DanDictVo lowerDan) {
        this.lowerDan = lowerDan;
    }

    public DanDictVo getUpperDan() {
        return upperDan;
    }

    public void setUpperDan(DanDictVo upperDan) {
        this.upperDan = upperDan;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}