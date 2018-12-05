package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;

public class RedisGameSmallZone implements Serializable{

  private static final long serialVersionUID = -7806564333669025091L;
  /**
   * 游戏小区code
   */
  private Integer zoneSmallCode;
  /**
   * 游戏小区名称
   */
  private String zoneSmallName;

  public Integer getZoneSmallCode() {
    return zoneSmallCode;
  }

  public void setZoneSmallCode(Integer zoneSmallCode) {
    this.zoneSmallCode = zoneSmallCode;
  }

  public String getZoneSmallName() {
    return zoneSmallName;
  }

  public void setZoneSmallName(String zoneSmallName) {
    this.zoneSmallName = zoneSmallName;
  }
}
