package com.kaihei.esportingplus.gamingteam.domain.service;

import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.gamingteam.api.params.rpg.BossConfirmPaidSuccessParams;
import com.kaihei.esportingplus.gamingteam.api.params.rpg.RPGTeamCreateParams;
import com.kaihei.esportingplus.gamingteam.api.params.rpg.RPGTeamJoinParams;
import com.kaihei.esportingplus.gamingteam.api.params.rpg.RPGTeamEndParams;
import com.kaihei.esportingplus.gamingteam.api.params.TeamQueryParams;
import com.kaihei.esportingplus.gamingteam.api.params.rpg.RPGTeamRolesParams;
import com.kaihei.esportingplus.gamingteam.api.vo.BossInfoForOrderVO;
import com.kaihei.esportingplus.gamingteam.api.vo.GameTeamTotal;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGMemberInTeamVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGRedisTeamBaseVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGTeamStartOrderVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGTeamCurrentInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamGameResultVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGTeamListVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamMemberCompaintAdminVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamSequenceUidVO;
import com.kaihei.esportingplus.user.api.vo.UserGameAboardVo;
import java.util.List;

public interface RPGTeamService extends TeamService {

    /**
     * 创建车队
     * @param RPGTeamCreateParams
     * @return
     */
    String createTeam(RPGTeamCreateParams RPGTeamCreateParams);

    /**
     * 加入车队前置--获取角色列表
     * @param RPGTeamRolesParams
     * @return
     */
    List<UserGameAboardVo> getTeamRoles(RPGTeamRolesParams RPGTeamRolesParams);

    /**
     * 加入车队
     * @param teamJoinParams
     */
    void joinTeam(RPGTeamJoinParams teamJoinParams);

    /**
     * 确认入团
     * @param sequence
     */
    void confirmJoinTeam(String sequence);

    /**
     * 解散车队
     * @param RPGTeamEndParams
     */
    void dismissTeam(RPGTeamEndParams RPGTeamEndParams);

    /**
     * 结束车队
     * @param RPGTeamEndParams
     */
    void endTeam(RPGTeamEndParams RPGTeamEndParams);

    /**
     * 查询车队列表
     */
    PagingResponse<RPGTeamListVO> findTeamList(Integer gameCode, TeamQueryParams teamQueryParams);

    /**
     * 查询符合条件的车队数
     * @param gameCode
     * @param teamQueryParams
     * @return
     */
    GameTeamTotal findTeamTotal(Integer gameCode, TeamQueryParams teamQueryParams);

    /**
     * 生成车队二维码
     * @param gameCode
     * @param sequence
     * @param uid
     * @return
     */
    String getSmallProgramTeamQrCode(Integer gameCode, String sequence,String uid);

    /**
     * 获取车队基本信息
     * @param sequence
     * @return
     */
    RPGRedisTeamBaseVO getTeamBaseInfo(String sequence);

    /**
     * 获取车队详细信息(包含车队队员信息)
     * @param sequence
     * @return
     */
    RPGTeamCurrentInfoVO getTeamCurrentInfo(String sequence);

    /**
     * 提供给老板创建订单时使用
     * 包含了车队的基本信息和老板的基本信息
     * @param sequence 车队序列号
     * @param uid 老板的 uid
     * @return
     */
    BossInfoForOrderVO getBossInfoForOrder(String sequence, String uid);

    /**
     * 提供给立即开车后批量创建暴鸡订单
     * 包含了车队的基本信息和暴鸡的基本信息
     * @param sequence 车队序列号
     * @return
     */
    RPGTeamStartOrderVO getBaojiInfoForOrder(String sequence);

    /**
     * 获取车队队员所在的车队信息
     * @param gameCode 游戏 code
     * @return
     */
    RPGMemberInTeamVO getMemberInTeam(Integer gameCode);

    /**
     * 获取车队比赛结果
     * @param sequence
     * @return
     */
    TeamGameResultVO getTeamGameResult(String sequence);

    /**
     * 获取车队队员的简略信息, 提供给管理后台投诉单查询车队详情时使用
     * @param sequence
     * @return
     */
    List<TeamMemberCompaintAdminVO> getTeamMemberBriefInfo(String sequence);

    /**
     * 根据车队序列号批量查询车队信息
     * @param sequenceList
     * @return
     */
    List<TeamInfoVO> getBatchTeamInfo(List<String> sequenceList);

    /**
     * 根据uid查找用户（暴鸡）所有已完成的车队唯一Id
     */
    List<TeamSequenceUidVO> getBaojiTeamSequencesByUids(List<String> uids);

    /**
     * 老板确认支付成功后, 前端调用(后台判断是否需要退款)
     * @param bossConfirmPaidSuccessParams
     */
    void bossConfirmPaidSuccess(BossConfirmPaidSuccessParams bossConfirmPaidSuccessParams);
}
