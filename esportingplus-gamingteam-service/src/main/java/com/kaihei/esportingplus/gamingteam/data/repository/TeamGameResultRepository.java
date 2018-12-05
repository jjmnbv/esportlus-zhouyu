package com.kaihei.esportingplus.gamingteam.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamGameResultVO;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGameResult;
import java.util.List;

public interface TeamGameResultRepository extends CommonRepository<TeamGameResult> {

    /**
     * 根据车队序列号查询比赛结果
     * @param teamSequence 车队序列号
     * @return
     */
    List<TeamGameResultVO> selectByTeamSequence(String teamSequence);

}