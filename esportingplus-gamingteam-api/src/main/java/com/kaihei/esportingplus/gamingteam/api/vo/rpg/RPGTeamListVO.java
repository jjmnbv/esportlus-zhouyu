package com.kaihei.esportingplus.gamingteam.api.vo.rpg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * RPG 车队列表
 * @author zhangfang
 */
public class RPGTeamListVO implements Serializable {

    private static final long serialVersionUID = -9068122725154770861L;

    /**
     * 车队序列号
     */
    private String sequence;

    /**
     * 车队房间编号，1000-9999 ，号码随机赋予不重复
     */
    private Integer roomNum;

    /**
     * 房间标题,20个字符以内
     */
    private String title;

    /**
     * 攻坚队名称，20 字符以内
     */
    private String assaultName;

    /**
     * 车队状态，0：准备中 1：已发车 2：已解散 3：已结束
     */
    private String statusDesc;

    private Integer status;

    /**
     * 副本名称
     */
    private String raidName;

    /**
     * 游戏跨区code，如：跨二区的code
     */
    private Integer zoneAcrossCode;

    /**
     * 游戏跨区名称，如：跨二区
     */
    private String zoneAcrossName;

    /**
     * 车队折扣价 单位分
     */
    private Integer price;

    /**
     * 车队位置数
     */
    private Integer originalPositionCount;

    /**
     * 当前设置的最大队员人数
     */
    private Integer actuallyPositionCount;

    private Integer currentPositionCount;

    private List<RPGTeamMemberVO> members = new ArrayList<>();

    public RPGTeamListVO() {
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public Integer getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(Integer roomNum) {
        this.roomNum = roomNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAssaultName() {
        return assaultName;
    }

    public void setAssaultName(String assaultName) {
        this.assaultName = assaultName;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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

    public Integer getCurrentPositionCount() {
        return currentPositionCount;
    }

    public void setCurrentPositionCount(Integer currentPositionCount) {
        this.currentPositionCount = currentPositionCount;
    }

    public List<RPGTeamMemberVO> getMembers() {
        return members;
    }

    public void setMembers(List<RPGTeamMemberVO> members) {
        this.members = members;
    }

    public void addMembers(RPGTeamMemberVO memberVo) {
        members.add(memberVo);
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
