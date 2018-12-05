package com.kaihei.esportingplus.gamingteam.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.gamingteam.api.params.PVPFreeTeamsForBackupParams;
import com.kaihei.esportingplus.gamingteam.api.params.TeamDataBaseQueryParams;
import com.kaihei.esportingplus.gamingteam.api.vo.PVPFreeTeamBackupVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamGameResultVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGRedisTeamVO;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author liangyi
 */
public interface TeamRepository extends CommonRepository<Team> {


    void setNames();

    /**
     * 根据序列号查询车队信息
     * @param sequence
     * @return
     */
    RPGRedisTeamVO selectBySequence(String sequence);

    /**
     * 根据车队主键 id 查询车队信息
     * @param id
     * @return
     */
    RPGRedisTeamVO selectByTeamId(Long id);

    /**
     * 根据 sequence 更新车队数据
     * @param sequence 车队序列号
     * @param status 车队状态
     * @return
     */
    int updateTeamStatus(
            @Param(value = "sequence") String sequence,
            @Param(value = "status") Integer status);

    /**
     * 查询可以加入的车队
     * @param params
     * @return
     */
    List<Team> selectHomePageTeamList(@Param("params") TeamDataBaseQueryParams params);
    /**
     * 查询可以加入的数量
     * @param params
     * @return
     */
    Long selectHomePageTeamTotal(@Param("params") TeamDataBaseQueryParams params);

    /**
     * 查询车队比赛结果
     * @param sequence 车队序列号
     * @return
     */
    TeamGameResultVO selectGameResultBySequence(String sequence);

    /**
     * 根据车队序列号批量查询车队信息
     * @param sequenceList
     * @return
     */
    List<TeamInfoVO> selectTeamInfoBySequence(List<String> sequenceList);

    /**
     * 根据队长身份查询后管系统车队
     * @param code
     * @param params
     * @return
     */
    List<PVPFreeTeamBackupVO> selectPVPFreeTeamsForBackup(@Param("leaderCode") int code, @Param("params") PVPFreeTeamsForBackupParams params);
}