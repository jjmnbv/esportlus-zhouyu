package com.kaihei.esportingplus.gamingteam.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.gamingteam.api.params.PVPFreeTeamsForBackupParams;
import com.kaihei.esportingplus.gamingteam.api.params.UpdatePositionCountParams;
import com.kaihei.esportingplus.gamingteam.api.params.pvp.PVPTeamEndParams;
import com.kaihei.esportingplus.gamingteam.api.params.pvp.PVPTeamFreeCreateParams;
import com.kaihei.esportingplus.gamingteam.api.params.pvp.PVPTeamFreeJoinParams;
import com.kaihei.esportingplus.gamingteam.api.vo.PVPFreeTeamBackupVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamGameResultVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamSequenceVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeMemberInTeamVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeTeamBaseVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeTeamCurrentInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeTeamCurrentMatchingInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeTeamMemberInfoVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * <pre>
 *     基于 feign 实现远程车队服务接口调用
 *     1. esportingplus-gamingteam-service为服务名
 *     2. fallbackFactory指定断路器实现类
 * </pre>
 *
 * @author liangyi
 */
@FeignClient(name = "esportingplus-gamingteam-service",
        path = "/gamingteam/pvp/free", fallbackFactory = PVPFreeTeamClientFallbackFactory.class)
public interface PVPFreeTeamServiceClient {

    /**
     * 创建 pvp 免费车队
     * @param token
     * @param pvpTeamFreeCreateParams
     * @return
     */
    @PostMapping
    ResponsePacket<TeamSequenceVO> createTeam(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody PVPTeamFreeCreateParams pvpTeamFreeCreateParams);


    /**
     * 获取 pvp 免费车队基本信息
     * @param token
     * @param sequence
     * @return
     */
    @GetMapping("/baseInfo/{sequence}")
    ResponsePacket<PVPFreeTeamBaseVO> getTeamBaseInfo(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("sequence") String sequence);

    /**
     * 获取 pvp 免费车队实时数据
     * @param token
     * @param sequence
     * @return
     */
    @GetMapping("/currentTeamInfo/{sequence}")
    ResponsePacket<PVPFreeTeamCurrentInfoVO> getTeamCurrentInfo(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("sequence") String sequence);

    @ApiOperation("加入车队")
    @PostMapping("/join")
    ResponsePacket<?> joinGamingTeam(@RequestHeader(name = "Authorization") String token,
            @RequestBody PVPTeamFreeJoinParams teamJoinParams);

    @ApiOperation(value = "退出车队")
    @DeleteMapping("/quit/{sequence}")
    ResponsePacket<?> quitGamingTeam(@RequestHeader(name = "Authorization") String token,
            @PathVariable("sequence") String sequence);

    /**
     * 踢出队员
     *
     * @param token auth 认证的token
     * @param sequence 车队序列号
     * @param uid 被踢出队员的 uid
     */
    @ApiOperation(value = "踢出队员")
    @DeleteMapping("/kickOut/{sequence}/{uid}")
    ResponsePacket<?> kickOutMember(@RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "车队序列号", required = true) @PathVariable("sequence") String sequence,
            @ApiParam(value = "被踢出队员的uid", required = true) @PathVariable("uid") String uid);

    /**
     * 立即开车
     *
     * @param token auth 认证的token
     * @param sequence 车队序列号
     */
    @GetMapping("/startTeam/{sequence}")
    ResponsePacket<?> startGamingTeam(@RequestHeader(name = "Authorization") String token,
            @PathVariable("sequence") String sequence);

    @PostMapping("/positioncount/update")
    ResponsePacket<Void> updateTeamPositioncount(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody UpdatePositionCountParams positionCountParams);

    /**
     * 确认准备
     *
     * @param token auth 认证的token
     * @param sequence 车队序列号
     */
    @GetMapping("/confirmReady/{sequence}")
    ResponsePacket<Void> confirmReadyGamingTeam(@RequestHeader(name = "Authorization") String token,
            @PathVariable("sequence") String sequence);

    /**
     * 结束车队
     *
     * @param token auth 认证的token
     */
    @PostMapping("endTeam")
    ResponsePacket<?> endGamingTeam(@RequestHeader(name = "Authorization") String token,
            @RequestBody PVPTeamEndParams pvpTeamEndParams);

    /**
     * 解散车队
     *
     * @param token auth 认证的token
     */
    @DeleteMapping("/dismiss/team/{teamSequence}")
    ResponsePacket<Void> dismissGamingTeam(@RequestHeader(name = "Authorization") String token,
            @PathVariable("teamSequence") String teamSequence);

    /**
     * 获取车队比赛结果
     *
     * @param token auth 认证的 token
     * @param teamSequence 车队序列号
     * @return
     */
    @GetMapping("/gameResult/{teamSequence}")
    ResponsePacket<List<TeamGameResultVO>> getTeamGameResult(
            @RequestHeader(name = "Authorization", required = false) String token,
            @PathVariable("teamSequence") String teamSequence);

    /**
     * 获取车队及车队队员信息
     *
     * @param token auth 认证的 token
     * @param teamSequence 车队序列号
     * @return
     */
    @GetMapping("/teamMemberInfo/{teamSequence}")
    ResponsePacket<PVPFreeTeamMemberInfoVO> getTeamMemberInfo(
            @RequestHeader(name = "Authorization", required = false) String token,
            @PathVariable("teamSequence") String teamSequence);

    /**
     * 取消准备
     */
    @GetMapping("/cancelReady/{sequence}")
    ResponsePacket<Void> cancelReadyGamingTeam(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "车队序列号", required = true)
            @PathVariable("sequence") String sequence);

    /**
     * 获取老板的匹配信息
     *
     * @param token auth 认证的 token
     * @return
     */
    @GetMapping("/currentMatchingInfo")
    ResponsePacket<PVPFreeTeamCurrentMatchingInfoVO> getCurrentMatchingInfo(
            @RequestHeader(name = "Authorization", required = false) String token);

    /**
     * 后管系统获取车队信息
     * @param params
     * @return
     */
    @PostMapping("/backup/list")
    ResponsePacket<PagingResponse<PVPFreeTeamBackupVO>> getPvpFreeTeamBackup(
            @RequestBody PVPFreeTeamsForBackupParams params);

    /**
     * 获取用户在某个车队的信息
     *
     * @param token auth 认证的 token
     * @return
     */
    @GetMapping("/memberInTeam")
    ResponsePacket<PVPFreeMemberInTeamVO> getMemberInTeam(
            @RequestHeader(name = "Authorization") String token);
}
