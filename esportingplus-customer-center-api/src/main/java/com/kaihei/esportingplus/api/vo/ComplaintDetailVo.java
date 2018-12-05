package com.kaihei.esportingplus.api.vo;

import com.kaihei.esportingplus.api.enums.ComplainOrderTypeEnum;
import com.kaihei.esportingplus.common.tools.CollectionUtils;
import java.util.Date;
import java.util.List;

/**
 * @author 谢思勇
 */
public class ComplaintDetailVo {

  /**
   * 投诉单表主键ID
   */
  private Integer oid;

  /**
   * 投诉单号sequeue
   */
  private String complaintOid;
  /**
   * 投诉状态，0： 默认，1：投诉中，3：需二次审核，4：超时未申诉，5：暴鸡已申诉，10：投诉成立 11：投诉不成立
   */
  private Integer handlerStatus;

  /**
   * 被投诉车队标题
   */
  private String complainTitle;

  /**
   * 投诉类型，0：默认 值，1：技术不好，2：态度不好，3：服务不好，4：其他原因
   */
  private Integer reasonOption;

  /**
   * 投诉项显示名称
   */
  private String reason;

  /**
   * 被投诉订单类型、暂时固定输出值：DNF
   */
  private String orderType;

  /**
   * 业务（车队老板）订单状态
   */
  private String orderStatus;

  /**
   * 业务订单号,（如车队老板订单号）
   */
  private String premadeOrderOid;

  /**
   * 游戏Code和名称
   */
  private GameType gameType;

  /**
   * 车队对应的游戏副本名称
   */
  private String instanceZonesType;

  /**
   * 车队sequeue
   */
  private String premadeId;

  /**
   * 比赛结果
   */
  private String result;

  /**
   * 车队（老板）订单开始时间
   */
  private Date startTime;

  /**
   * 车队（老板）订单结束时间
   */
  private Date endTime;

  /**
   * 投诉内容
   */
  private String complainContent;
  /**
   * 投诉图片地址
   */
  private List<String> complainImg;

  /**
   * 投诉时间
   */
  private Date complainTime;

  /**
   * 被投诉人信息
   */
  private BeComplaintInfo beComplaintInfo;

  /**
   * 投诉人信息
   */
  private ComplaintInfo complaintInfo;

  /**
   * 车队类型
   */
  private String levelText = "";

  public String getLevelText() {
    return levelText;
  }

  public void setLevelText(String levelText) {
    this.levelText = levelText;
  }

  public Integer getOid() {
    return oid;
  }

  public void setOid(Integer oid) {
    this.oid = oid;
  }

  public String getComplaintOid() {
    return complaintOid;
  }

  public void setComplaintOid(String complaintOid) {
    this.complaintOid = complaintOid;
  }

  public Integer getHandlerStatus() {
    return handlerStatus;
  }

  public void setHandlerStatus(Integer handlerStatus) {
    this.handlerStatus = handlerStatus;
  }

  public String getComplainTitle() {
    return complainTitle;
  }

  public void setComplainTitle(String complainTitle) {
    this.complainTitle = complainTitle;
  }

  public Integer getReasonOption() {
    return reasonOption;
  }

  public void setReasonOption(Integer reasonOption) {
    this.reasonOption = reasonOption;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getOrderType() {
    return orderType;
  }

  public void setOrderType(Integer orderType) {
    CollectionUtils
            .find(ComplainOrderTypeEnum.values(), cote -> cote.getCode() == orderType.byteValue())
            .ifPresent(cote -> this.orderType = cote.getDesc());
  }

  public String getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(String orderStatus) {
    this.orderStatus = orderStatus;
  }

  public String getPremadeOrderOid() {
    return premadeOrderOid;
  }

  public void setPremadeOrderOid(String premadeOrderOid) {
    this.premadeOrderOid = premadeOrderOid;
  }

  public GameType getGameType() {
    return gameType;
  }

  public void setGameType(GameType gameType) {
    this.gameType = gameType;
  }

  public String getInstanceZonesType() {
    return instanceZonesType;
  }

  public void setInstanceZonesType(String instanceZonesType) {
    this.instanceZonesType = instanceZonesType;
  }

  public String getPremadeId() {
    return premadeId;
  }

  public void setPremadeId(String premadeId) {
    this.premadeId = premadeId;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public String getComplainContent() {
    return complainContent;
  }

  public void setComplainContent(String complainContent) {
    this.complainContent = complainContent;
  }

  public List<String> getComplainImg() {
    return complainImg;
  }

  public void setComplainImg(List<String> complainImg) {
    this.complainImg = complainImg;
  }

  public Date getComplainTime() {
    return complainTime;
  }

  public void setComplainTime(Date complainTime) {
    this.complainTime = complainTime;
  }

  public BeComplaintInfo getBeComplaintInfo() {
    return beComplaintInfo;
  }

  public void setBeComplaintInfo(BeComplaintInfo beComplaintInfo) {
    this.beComplaintInfo = beComplaintInfo;
  }

  public ComplaintInfo getComplaintInfo() {
    return complaintInfo;
  }

  public void setComplaintInfo(ComplaintInfo complaintInfo) {
    this.complaintInfo = complaintInfo;
  }
}
