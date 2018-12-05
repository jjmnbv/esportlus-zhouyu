package com.kaihei.esportingplus.gamingteam.data.manager.core.context;

import com.kaihei.esportingplus.common.data.JsonSerializable;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGameResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 缓存聚合类
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PVPContext<TG extends TeamGame, TM extends TeamMember> implements JsonSerializable {

    private Team team;

    private TG teamGame;

    private List<TM> teamMemberList;

    private List<TeamGameResult> teamGameResults;

    //-------------------------------------以下数据通过通过数据前处理得到---------------------------------------------//

    private TM leader;

    private TM me;

    private Map<String, TM> uidTeamMemberMap;

    //-------------------------------------以下数据通过通过数据后处理得到---------------------------------------------//

    private List<TM> restores = new ArrayList<>();

    private TM join;

    private List<TM> leave;
}
