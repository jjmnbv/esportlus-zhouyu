package com.kaihei.esportingplus.resource.domain.dto;

import com.kaihei.esportingplus.common.data.Castable;
import lombok.Data;

@Data
public class TeamTypeIdNameGameIdDTO implements Castable {

    private Integer freeTeamTypeId;

    private String name;

    private Integer gameId;
}
