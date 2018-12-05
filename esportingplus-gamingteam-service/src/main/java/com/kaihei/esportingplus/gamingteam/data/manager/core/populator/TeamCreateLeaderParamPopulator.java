package com.kaihei.esportingplus.gamingteam.data.manager.core.populator;

import com.alibaba.fastjson.JSONObject;
import com.kaihei.esportingplus.api.feign.DictionaryClient;
import com.kaihei.esportingplus.api.vo.DictBaseVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.CollectionUtils;
import com.kaihei.esportingplus.gamingteam.api.enums.PVPTeamMemberStatusEnum;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.CreateTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.UnnecessaryPopulator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author 谢思勇
 *
 * 组建车队时
 *
 * 对TeamGame类型的数据，进行参数填充
 *
 */
@Component
@OnScene(includes = CreateTeamScene.class)
public class TeamCreateLeaderParamPopulator extends UnnecessaryPopulator<TeamGame> {

    @Autowired
    @Qualifier("restTemplateExtrnal")
    private RestTemplate restTemplate;

    @Value("${python.identity-uri}")
    private String pythonUri;

    @Autowired
    private PVPContextHolder<TeamGame, TeamMember> pvpContextHolder;

    @Autowired
    private DictionaryClient dictionaryClient;

    @Override
    public void doPopulate() {
        //填充TG
        populateTeamGame();

        //填充队长的参数
        populateTeamMember();
    }

    /**
     * {@link TeamGame#setGameName(String)}
     *
     * {@link TeamGame#setGameZoneName(String)} (String)}
     */
    private void populateTeamGame() {
        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();
        TeamGame teamGame = context.getTeamGame();

        //游戏名
        Optional.of(teamGame).filter(it -> Objects.isNull(it.getGameName()))
                .map(TeamGame::getGameId)
                .map(this::converte).ifPresent(teamGame::setGameName);

        //区名
        Optional.ofNullable(teamGame.getGameZoneId())
                .map(this::converte).ifPresent(teamGame::setGameZoneName);
    }

    /**
     * {@link TeamMember#setGameDanName(String)}
     *
     * {@link TeamMember#setStatus(Byte)}
     *
     * {@link TeamMember#setBaojiLevel(Integer)}
     *
     * {@link TeamMember#setUserIdentity(Byte)}
     */
    @SuppressWarnings("unchecked")
    private void populateTeamMember() {
        PVPContext<TeamGame, TeamMember> context = pvpContextHolder.getContext();
        TeamGame teamGame = context.getTeamGame();
        TeamMember leader = context.getJoin();
        //段位名
        Optional.ofNullable(leader.getGameDanId())
                .map(this::converte).ifPresent(leader::setGameDanName);

        Optional.of(leader.getUserIdentity())
                .map(UserIdentityEnum::of)
                .filter(ui -> !UserIdentityEnum.BOSS.equals(ui))
                .ifPresent(ui -> {

                    JSONObject params = new JSONObject(2)
                            .fluentPut("uid", leader.getUid())
                            .fluentPut("identity", ui.getPythonCode());

                    log.info("params -> {}", params.toJSONString());

                    Map<String, Object> responseData =
                            (Map<String, Object>) restTemplate
                                    .postForObject(pythonUri, params,
                                            ResponsePacket.class)
                                    .getData();
                    log.info("responseData->{}", responseData);
                    List<Map<String, Integer>> certInfos = (List<Map<String, Integer>>) responseData
                            .get("cert_info");
                    DictBaseVO<Object> gameDict = dictionaryClient
                            .findById(teamGame.getGameId())
                            .getData();
                    int gamCode = Integer.parseInt(gameDict.getCode());

                    Map<String, Integer> certInfo = CollectionUtils
                            .find(certInfos,
                                    it -> it.get("game_code").equals(gamCode))
                            .orElse(null);
                    if (certInfo == null) {
                        throw new BusinessException(
                                BizExceptionEnum.USER_CERT_INFO_NOT_EXIT);
                    }
                    Integer baojeLevelCode = certInfo.get("baoji_level");

                    leader.setBaojiLevel(baojeLevelCode);

                    leader.setUserIdentity(((byte) (UserIdentityEnum.LEADER.getCode())));
                    leader.setGmtModified(new Date());
                    leader.setStatus(((byte) (PVPTeamMemberStatusEnum.PREPARE_READY.getCode())));
                });

    }

}
