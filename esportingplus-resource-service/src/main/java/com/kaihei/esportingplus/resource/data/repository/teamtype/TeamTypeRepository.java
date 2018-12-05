package com.kaihei.esportingplus.resource.data.repository.teamtype;

import com.kaihei.esportingplus.api.vo.freeteam.TeamTypeSimpleVO;
import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.resource.domain.dto.TeamTypeIdNameGameIdDTO;
import com.kaihei.esportingplus.resource.domain.entity.freeteam.TeamType;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author liangyi
 */
public interface TeamTypeRepository extends CommonRepository<TeamType> {

    /**
     * 查询所有车队类型
     * @param status 状态, 1:有效, 0:无效, 为空表示查询所有
     * @param category 类别, 1:付费, 0:免费, 为空表示查询所有
     * @return
     */
    List<TeamTypeSimpleVO> selectTeamTypeList(
            @Param("status") Integer status,
            @Param("category") Integer category);

    /**
     * 查询与车队类型关联的所有游戏区数据字典 id
     * @param teamTypeId 车队类型 id
     * @return
     */
    List<Integer> selectGameZone(Integer teamTypeId);

    /**
     * 新增车队类型与游戏区关联
     * @param teamTypeId 免费车队类型 id
     * @param gameZoneIdList 游戏区 id 集合
     */
    void insertGameZone(@Param("teamTypeId") Integer teamTypeId,
            @Param("gameZoneIdList") List<Integer> gameZoneIdList);

    /**
     * 删除车队类型与游戏区的关联
     * @param teamTypeId 车队类型 id
     */
    void deleteGameZone(Integer teamTypeId);

    /**
     * 查询与车队类型关联的所有游戏段位数据字典id
     * @param teamTypeId 车队类型id
     * @return
     */
    List<Integer> selectGameDan(Integer teamTypeId);

    /**
     * 新增车队类型与游戏段位关联
     * @param teamTypeId 车队类型 id
     * @param gameDanIdList 游戏段位 id 集合
     */
    void insertGameDan(@Param("teamTypeId")Integer teamTypeId,
            @Param("gameDanIdList") List<Integer> gameDanIdList);

    /**
     * 删除车队类型与游戏段位的关联
     * @param teamTypeId 车队类型 id
     */
    void deleteGameDan(Integer teamTypeId);

    /**
     * 查询所有车队类型
     * @param status 状态, 1:有效, 0:无效, 为空表示查询所有
     * @param category 类别, 1:付费, 0:免费, 为空表示查询所有
     * @return
     */
    List<TeamType> selectByStatusAndCategory(
            @Param("status") Integer status,
            @Param("category") Integer category);

    /**
     * 根据 id 查询车队类型
     * @param teamTypeId
     * @return
     */
    TeamType selectByTeamTypeId(Integer teamTypeId);

    List<TeamTypeIdNameGameIdDTO> selectBySettlememtTypeId(
            @Param("settlementTypeId") Integer settlementTypeId);
}