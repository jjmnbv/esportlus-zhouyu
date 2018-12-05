package com.kaihei.esportingplus.gamingteam.api.vo.rpg;

import com.kaihei.esportingplus.gamingteam.api.vo.TeamStartOrderBaseVO;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 批量创建暴鸡订单时需要的 VO 对象
 * @author liangyi
 */
public class RPGTeamStartOrderVO extends TeamStartOrderBaseVO {

    private static final long serialVersionUID = 2588485884118612868L;


    /** 副本 code */
    private Integer raidCode;

    /** 副本名称 */
    private String raidName;

    /** 车队跨区 code */
    private Integer zoneAcrossCode;

    /** 车队跨区名称 */
    private String zoneAcrossName;

    /** 暴鸡队员信息列表 */
    List<RPGBaojiInfoVO> baojiInfoList;

    /** 当前车队所有老板的 uid 集合 */
    List<String> bossUidList;

    public RPGTeamStartOrderVO() {
    }


    public Integer getRaidCode() {
        return raidCode;
    }

    public void setRaidCode(Integer raidCode) {
        this.raidCode = raidCode;
    }

    public String getRaidName() {
        return raidName;
    }

    public void setRaidName(String raidName) {
        this.raidName = raidName;
    }

    public Integer getZoneAcrossCode() {
        return zoneAcrossCode;
    }

    public void setZoneAcrossCode(Integer zoneAcrossCode) {
        this.zoneAcrossCode = zoneAcrossCode;
    }

    public String getZoneAcrossName() {
        return zoneAcrossName;
    }

    public void setZoneAcrossName(String zoneAcrossName) {
        this.zoneAcrossName = zoneAcrossName;
    }

    public List<RPGBaojiInfoVO> getBaojiInfoList() {
        return baojiInfoList;
    }

    public void setBaojiInfoList(
            List<RPGBaojiInfoVO> baojiInfoList) {
        this.baojiInfoList = baojiInfoList;
    }

    public List<String> getBossUidList() {
        return bossUidList;
    }

    public void setBossUidList(List<String> bossUidList) {
        this.bossUidList = bossUidList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
