package com.kaihei.esportingplus.gamingteam.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.gamingteam.api.params.rpg.BossConfirmPaidSuccessParams;
import com.kaihei.esportingplus.gamingteam.api.params.rpg.RPGTeamCreateParams;
import com.kaihei.esportingplus.gamingteam.api.params.rpg.RPGTeamJoinParams;
import com.kaihei.esportingplus.gamingteam.api.params.rpg.RPGTeamEndParams;
import com.kaihei.esportingplus.gamingteam.api.params.TeamInfoBatchParams;
import com.kaihei.esportingplus.gamingteam.api.params.TeamQueryParams;
import com.kaihei.esportingplus.gamingteam.api.params.rpg.RPGTeamRolesParams;
import com.kaihei.esportingplus.gamingteam.api.params.UpdatePositionCountParams;
import com.kaihei.esportingplus.gamingteam.api.vo.BossInfoForOrderVO;
import com.kaihei.esportingplus.gamingteam.api.vo.GameTeamTotal;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGMemberInTeamVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGRedisTeamBaseVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGTeamCurrentInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamGameResultVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGTeamListVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamMemberCompaintAdminVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamSequenceUidVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGTeamStartOrderVO;
import com.kaihei.esportingplus.user.api.vo.QrCodeVo;
import com.kaihei.esportingplus.user.api.vo.UserGameAboardVo;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 基于 feign 实现远程车队服务接口调用
 * 1. esportingplus-gamingteam-service为服务名
 * 2. fallbackFactory指定断路器实现类
 * @author liangyi
 */
@FeignClient(name = "esportingplus-gamingteam-service",
        path = "/gamingteam/rpg", fallbackFactory = RPGTeamClientFallbackFactory.class)
public interface RPGTeamServiceClient {

    /**
     * 创建车队
     * @param token auth 认证的token
     * @param RPGTeamCreateParams 创建车队参数封装
     * @return
     */
    @PostMapping("/create")
    ResponsePacket<String> createGamingTeam(@RequestHeader(name = "Authorization") String token,
            @RequestBody RPGTeamCreateParams RPGTeamCreateParams);

    /**
     * 选择身份后进入角色选择页面--准备加入车队
     * @param token auth 认证的token
     * @param RPGTeamRolesParams
     * @return
     */
    @PostMapping("/roles")
    ResponsePacket<List<UserGameAboardVo>> getGamingTeamRoles(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody RPGTeamRolesParams RPGTeamRolesParams);

    /**
     * 加入车队
     * @param token auth 认证的token
     * @param teamJoinParams
     * @return
     */
    @PostMapping("/join")
    ResponsePacket<Void> joinGamingTeam(@RequestHeader(name = "Authorization") String token,
            @RequestBody RPGTeamJoinParams teamJoinParams);


    /**
     * 退出车队
     * @param token auth 认证的token
     * @param sequence 车队序列号
     * @return
     */
    @GetMapping("/quit/{sequence}")
    ResponsePacket<Void> quitGamingTeam(@RequestHeader(name = "Authorization") String token,
            @PathVariable("sequence") String sequence);

    /**
     * 踢出队员
     * @param token auth 认证的token
     * @param sequence 车队序列号
     * @param uid 被踢出队员的 uid
     * @return
     */
    @GetMapping("/kickOut/{sequence}/{uid}")
    ResponsePacket<Void> kickOutMember(@RequestHeader(name = "Authorization") String token,
            @PathVariable("sequence") String sequence, @PathVariable("uid") String uid);

    /**
     * 解散车队
     * @param token auth 认证的token
     * @param RPGTeamEndParams
     * @return
     */
    @PostMapping("/dismiss")
    ResponsePacket<Void> dismissGamingTeam(@RequestHeader(name = "Authorization") String token,
            @RequestBody RPGTeamEndParams RPGTeamEndParams);

    /**
     * 确认入团
     * @param token auth 认证的token
     * @param sequence 车队序列号
     * @return
     */
    @GetMapping("/confirmJoin/{sequence}")
    ResponsePacket<Void> confirmJoinGamingTeam(@RequestHeader(name = "Authorization") String token,
            @PathVariable("sequence") String sequence);

    /**
     * 立即开车
     * @param token auth 认证的token
     * @param sequence 车队序列号
     * @return
     */
    @GetMapping("/startTeam/{sequence}")
    ResponsePacket<Void> startGamingTeam(@RequestHeader(name = "Authorization") String token,
            @PathVariable("sequence") String sequence);

    /**
     * 结束车队
     * @param token auth 认证的token
     * @param RPGTeamEndParams
     * @return
     */
    @PostMapping("/endTeam/")
    ResponsePacket<Void> endGamingTeam(@RequestHeader(name = "Authorization") String token,
            @RequestBody RPGTeamEndParams RPGTeamEndParams);

    /**
     * 获取车队列表
     * @param token
     * @param gameCode
     * @param teamQueryParams
     * @return
     */
    @PostMapping("/{game_code}/list")
    ResponsePacket<PagingResponse<RPGTeamListVO>> getTeamList(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("game_code") Integer gameCode,
            @RequestBody TeamQueryParams teamQueryParams);

    /**
     * 获取车队总数
     * @param token
     * @param gameCode
     * @param teamQueryParams
     * @return
     */
    @PostMapping("/{game_code}/total")
    ResponsePacket<GameTeamTotal> getTeamTotal(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("game_code") Integer gameCode,
            @RequestBody(required = false) TeamQueryParams teamQueryParams);

    @GetMapping("/qrcode/{game_code}/{sequence}")
    ResponsePacket<QrCodeVo> getTeamQrCode(@RequestHeader(name = "Authorization") String token,
            @PathVariable("game_code") Integer gameCode,
            @PathVariable("sequence") String sequence);

    @PostMapping("/positioncount/update")
    ResponsePacket<Void> updateTeamPositioncount(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody UpdatePositionCountParams positionCountParams);

    /**
     * 获取车队基本信息
     * @param token
     * @param sequence
     * @return
     */
    @GetMapping("/baseInfo/{sequence}")
    ResponsePacket<RPGRedisTeamBaseVO> getTeamBaseInfo(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("sequence") String sequence);

    /**
     * 当前队员获取车队实时数据(包含车队队员的详细数据)
     * @param token auth 认证的token
     * @param sequence
     * @return
     */
    @GetMapping("/currentTeamInfo/{sequence}")
    ResponsePacket<RPGTeamCurrentInfoVO> getTeamCurrentInfo(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("sequence") String sequence);

    /**
     * 提供给老板创建订单
     * @param sequence 车队序列号
     * @param uid 老板的 uid
     * @return
     */
    @GetMapping("/bossInfo/{sequence}/{uid}")
    ResponsePacket<BossInfoForOrderVO> getBossInfoForOrder(@PathVariable("sequence") String sequence,
            @PathVariable("uid") String uid);

    /**
     * 提供给立即开车后批量创建暴鸡订单
     * @param sequence 车队序列号
     * @return
     */
    @GetMapping("/baojiInfo/{sequence}")
    ResponsePacket<RPGTeamStartOrderVO> getBaojiInfoForOrder(
            @PathVariable("sequence") String sequence);


    /**
     * 获取用户所在的车队信息
     * @param token auth 认证的 token
     * @param gameCode 游戏 code
     * @return
     */
    @GetMapping("/memberInTeam/{game_code}")
    ResponsePacket<RPGMemberInTeamVO> getMemberInTeam(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("game_code") Integer gameCode);


    /**
     * 老板获取支付倒计时
     * @param token 认证的 token
     * @param sequence 车队序列号
     * @return
     */
    @GetMapping("/bossPayCountdown/{sequence}")
    ResponsePacket<Integer> getBossPayCountdown(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("sequence") String sequence);

    /**
     * 获取车队的比赛结果
     * @param sequence
     * @return
     */
    @GetMapping("/gameResult/{sequence}")
    ResponsePacket<TeamGameResultVO> getGamingTeamGameResult(@PathVariable("sequence") String sequence);

    /**
     * 获取车队队员的简略信息
     * 提供给管理后台投诉详情页使用
     * @param sequence
     * @return
     */
    @GetMapping("/memberBriefInfo/{sequence}")
    ResponsePacket<List<TeamMemberCompaintAdminVO>> getGamingTeamMemberBriefInfo(@PathVariable("sequence") String sequence);

    /**
     * 根据车队序列号批量查询车队信息
     * 提供给管理后台使用
     * @param teamInfoBatchParams
     * @return
     */
    @PostMapping("/teamBatchInfo")
    ResponsePacket<List<TeamInfoVO>> getBatchGamingTeamInfo(
            @RequestBody TeamInfoBatchParams teamInfoBatchParams);

    /**
     * 根据uid查找用户（暴鸡）所有已完成的车队唯一Id [去重]
     */
    @PostMapping("teamSequences")
    ResponsePacket<List<TeamSequenceUidVO>> getBaojiTeamSequencesByUids(
            @RequestParam("uids") List<String> uids);

    /**
     * 老板支付成功后, 前端调用该接口
     * @param token
     * @param bossConfirmPaidSuccessParams
     * @return
     */
    @PostMapping("/bossConfirmPaidSuccess")
    ResponsePacket<Void> bossConfirmPaidSuccess(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody BossConfirmPaidSuccessParams bossConfirmPaidSuccessParams);
}
