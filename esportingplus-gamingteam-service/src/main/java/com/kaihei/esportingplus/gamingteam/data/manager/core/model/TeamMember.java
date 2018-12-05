package com.kaihei.esportingplus.gamingteam.data.manager.core.model;

import com.kaihei.esportingplus.common.data.Castable;
import java.util.Date;
import java.util.List;

public interface TeamMember extends Castable {

    Long getId();

    void setId(Long id);

    String getUid();

    String getUsername();

    Long getTeamId();

    void setTeamId(Long teamId);

    void setGmtModified(Date gmtModified);

    Date getGmtModified();

    Integer getGameDanId();

    void setGameDanId(Integer gameDanId);

    String getGameDanName();

    void setGameDanName(String gameDanName);

    Byte getUserIdentity();

    void setUserIdentity(Byte userIdentity);

    void setBaojiLevel(Integer baojiLevel);

    Integer getBaojiLevel();

    Byte getStatus();

    String getChickenId();

    void setChickenId(String chickenId);

    void setStatus(Byte status);

    Date getJoinTime();

    void setJoinTime(Date joinTime);

    Date getGmtCreate();

    void setGmtCreate(Date gmtCreate);

    void setCouponsIds(List<Long> couponsIds);
}
