package com.kaihei.esportingplus.gamingteam.domain.entity;

import com.kaihei.esportingplus.common.data.Castable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Builder
@AllArgsConstructor
@ToString
@Data
@Table(name = "team")
public class Team implements Castable {

    /**
     * 车队唯一标识符
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 车队序列号,通过分布式id生成器生成
     */
    private String sequence;

    /**
     * 车队房间编号，1000-9999 ，号码随机赋予不重复
     */
    @Column(name = "room_num")
    private Integer roomNum;

    /**
     * 房间标题,20个字符以内
     */
    private String title;

    /**
     * 车队类型, -1: 未知; 0:免费; 1:付费;
     */
    @Column(name = "team_type")
    private Byte teamType;

    /**
     * 玩法模式, 0:上分; 1:陪玩;
     */
    @Column(name = "play_mode")
    private Byte playMode;

    /**
     * 结算类型, 1:局; 2:小时
     */
    @Column(name = "settlement_type")
    private Byte settlementType;

    /**
     * 结算数量(保留1位有效数字)
     */
    @Column(name = "settlement_number")
    private BigDecimal settlementNumber;

    /**
     * 车队原始位置数
     */
    @Column(name = "original_position_count")
    private Integer originalPositionCount;

    /**
     * 车队实际位置数
     */
    @Column(name = "actually_position_count")
    private Integer actuallyPositionCount;

    /**
     * 车队状态，0：准备中 1：已发车 2：已解散 3：已结束
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
    private String groupId;

    public Team(Long id, String sequence, Integer roomNum, String title, Byte teamType, Byte playMode, Byte settlementType, BigDecimal settlementNumber, Integer originalPositionCount, Integer actuallyPositionCount, Byte status, Date gmtCreate, Date gmtModified) {
        this.id = id;
        this.sequence = sequence;
        this.roomNum = roomNum;
        this.title = title;
        this.teamType = teamType;
        this.playMode = playMode;
        this.settlementType = settlementType;
        this.settlementNumber = settlementNumber;
        this.originalPositionCount = originalPositionCount;
        this.actuallyPositionCount = actuallyPositionCount;
        this.status = status;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    /**
     * 车队游戏信息
     */
    private TeamGameRPG teamGameRPG;

    public Team() {
        super();
    }

    /**
     * 获取车队唯一标识符
     *
     * @return id - 车队唯一标识符
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置车队唯一标识符
     *
     * @param id 车队唯一标识符
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取车队序列号,通过分布式id生成器生成
     *
     * @return sequence - 车队序列号,通过分布式id生成器生成
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * 设置车队序列号,通过分布式id生成器生成
     *
     * @param sequence 车队序列号,通过分布式id生成器生成
     */
    public void setSequence(String sequence) {
        this.sequence = sequence == null ? null : sequence.trim();
    }

    /**
     * 获取车队房间编号，1000-9999 ，号码随机赋予不重复
     *
     * @return room_num - 车队房间编号，1000-9999 ，号码随机赋予不重复
     */
    public Integer getRoomNum() {
        return roomNum;
    }

    /**
     * 设置车队房间编号，1000-9999 ，号码随机赋予不重复
     *
     * @param roomNum 车队房间编号，1000-9999 ，号码随机赋予不重复
     */
    public void setRoomNum(Integer roomNum) {
        this.roomNum = roomNum;
    }

    /**
     * 获取房间标题,20个字符以内
     *
     * @return title - 房间标题,20个字符以内
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置房间标题,20个字符以内
     *
     * @param title 房间标题,20个字符以内
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取车队类型, -1: 未知; 0:免费; 1:付费;
     *
     * @return team_type - 车队类型, -1: 未知; 0:免费; 1:付费;
     */
    public Byte getTeamType() {
        return teamType;
    }

    /**
     * 设置车队类型, -1: 未知; 0:免费; 1:付费;
     *
     * @param teamType 车队类型, -1: 未知; 0:免费; 1:付费;
     */
    public void setTeamType(Byte teamType) {
        this.teamType = teamType;
    }

    /**
     * 获取玩法模式, 0:上分; 1:陪玩;
     *
     * @return play_mode - 玩法模式, 0:上分; 1:陪玩;
     */
    public Byte getPlayMode() {
        return playMode;
    }

    /**
     * 设置玩法模式, 0:上分; 1:陪玩;
     *
     * @param playMode 玩法模式, 0:上分; 1:陪玩;
     */
    public void setPlayMode(Byte playMode) {
        this.playMode = playMode;
    }

    /**
     * 获取结算类型, 1:局; 2:小时
     *
     * @return settlement_type - 结算类型, 1:局; 2:小时
     */
    public Byte getSettlementType() {
        return settlementType;
    }

    /**
     * 设置结算类型, 1:局; 2:小时
     *
     * @param settlementType 结算类型, 1:局; 2:小时
     */
    public void setSettlementType(Byte settlementType) {
        this.settlementType = settlementType;
    }

    /**
     * 获取结算数量(保留1位有效数字)
     *
     * @return settlement_number - 结算数量(保留1位有效数字)
     */
    public BigDecimal getSettlementNumber() {
        return settlementNumber;
    }

    /**
     * 设置结算数量(保留1位有效数字)
     *
     * @param settlementNumber 结算数量(保留1位有效数字)
     */
    public void setSettlementNumber(BigDecimal settlementNumber) {
        this.settlementNumber = settlementNumber;
    }

    /**
     * 获取车队原始位置数
     *
     * @return original_position_count - 车队原始位置数
     */
    public Integer getOriginalPositionCount() {
        return originalPositionCount;
    }

    /**
     * 设置车队原始位置数
     *
     * @param originalPositionCount 车队原始位置数
     */
    public void setOriginalPositionCount(Integer originalPositionCount) {
        this.originalPositionCount = originalPositionCount;
    }

    /**
     * 获取车队实际位置数
     *
     * @return actually_position_count - 车队实际位置数
     */
    public Integer getActuallyPositionCount() {
        return actuallyPositionCount;
    }

    /**
     * 设置车队实际位置数
     *
     * @param actuallyPositionCount 车队实际位置数
     */
    public void setActuallyPositionCount(Integer actuallyPositionCount) {
        this.actuallyPositionCount = actuallyPositionCount;
    }

    /**
     * 获取车队状态，0：准备中 1：已发车 2：已解散 3：已结束
     *
     * @return status - 车队状态，0：准备中 1：已发车 2：已解散 3：已结束
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置车队状态，0：准备中 1：已发车 2：已解散 3：已结束
     *
     * @param status 车队状态，0：准备中 1：已发车 2：已解散 3：已结束
     */
    public void setStatus(Byte status) {
        this.status = status;
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

    public TeamGameRPG getTeamGameRPG() {
        return teamGameRPG;
    }

    public void setTeamGameRPG(TeamGameRPG teamGameRPG) {
        this.teamGameRPG = teamGameRPG;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}