package com.kaihei.esportingplus.gamingteam.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGRedisTeamMemberVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamMemberCompaintAdminVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamSequenceUidVO;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamMemberRPG;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TeamMemberRPGRepository extends CommonRepository<TeamMemberRPG> {

    /**
     * 批量插入车队队员
     * @param memberList
     * @return 受影响的行数
     */
    int insertMemberList(List<RPGRedisTeamMemberVO> memberList);

    /**
     * 获取车队队员的简略信息
     * 提供给管理后台投诉详情使用
     * @param sequence 车队序列号
     * @return
     */
    List<TeamMemberCompaintAdminVO> selectBriefInfo(String sequence);

    /**
     * 根据车队序列号查询车队队员
     * @param teamSequence
     * @return
     */
    List<TeamMemberRPG> getMemberListByTeam(String teamSequence);

    List<TeamSequenceUidVO> selectBaojiTeamSequencesByUids(@Param("uids") List<String> uids);
}