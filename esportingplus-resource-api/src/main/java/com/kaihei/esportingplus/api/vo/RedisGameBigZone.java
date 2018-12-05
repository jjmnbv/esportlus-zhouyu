package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;
import java.util.List;

public class RedisGameBigZone implements Serializable {

    private static final long serialVersionUID = 2768108581530971626L;
    /**
     * 游戏大区code
     */
    private Integer zoneBigCode;
    /**
     * 游戏大区名称
     */
    private String zoneBigName;

    private List<RedisGameSmallZone> zoneSmallList;

    public Integer getZoneBigCode() {
        return zoneBigCode;
    }

    public void setZoneBigCode(Integer zoneBigCode) {
        this.zoneBigCode = zoneBigCode;
    }

    public String getZoneBigName() {
        return zoneBigName;
    }

    public void setZoneBigName(String zoneBigName) {
        this.zoneBigName = zoneBigName;
    }

    public List<RedisGameSmallZone> getZoneSmallList() {
        return zoneSmallList;
    }

    public void setZoneSmallList(List<RedisGameSmallZone> zoneSmallList) {
        this.zoneSmallList = zoneSmallList;
    }
}
