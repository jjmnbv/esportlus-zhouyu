package com.kaihei.esportingplus.gamingteam.data.manager.core.model;

import com.kaihei.esportingplus.common.data.Castable;
import java.util.Date;

public interface TeamGame extends Castable {

    Long getId();

    void setId(Long id);

    Integer getGameId();

    void setGameId(Integer gameId);

    String getGameName();

    void setGameName(String gameName);

    void setTeamId(Long id);

    Date getGmtModified();

    void setGmtModified(Date date);

    void setGmtCreate(Date gmtCreate);

    Integer getGameZoneId();

    void setGameZoneId(Integer gameZoneId);

    String getGameZoneName();

    void setGameZoneName(String gameZoneName);

    Integer getLowerDanId();

    void setLowerDanName(String lowerDanName);

    Integer getUpperDanId();

    void setUpperDanName(String upperDanName);
}
