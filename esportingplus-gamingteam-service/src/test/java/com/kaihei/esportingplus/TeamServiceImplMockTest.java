package com.kaihei.esportingplus;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.algorithm.SnowFlake;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.enums.BaojiLevelEnum;
import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.api.enums.TeamMemberStatusEnum;
import com.kaihei.esportingplus.gamingteam.api.enums.TeamStatusEnum;
import com.kaihei.esportingplus.gamingteam.api.params.UpdatePositionCountParams;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGRedisTeamMemberVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGRedisTeamVO;
import com.kaihei.esportingplus.gamingteam.data.manager.rpg.RPGTeamCacheManager;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
public class TeamServiceImplMockTest extends AbstractGamingTeamMockTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamServiceImplMockTest.class);

    @MockBean
    @Autowired
    private RPGTeamCacheManager rpgTeamCacheManager;
    @Autowired
    private SnowFlake snowFlake;
    @Test
    public void test001UpdateTeamPositioncount(){

        try {
            UserSessionContext userSessionContext = initUserSessionContext();
            RPGRedisTeamVO RPGRedisTeamVO = preMadeTeamVo();
            String teamMemberKey = String.format(RedisKey.TEAM_MEMBER_PREFIX, RPGRedisTeamVO.getId());
            Mockito.when(rpgTeamCacheManager.queryTeamInfoBySequence(Mockito.anyString())).thenReturn(
                    RPGRedisTeamVO);
            RPGRedisTeamMemberVO memberVO = initLeaderTeamMemberVo(RPGRedisTeamVO, userSessionContext);

            Mockito.when(cacheManager.hget(teamMemberKey, userSessionContext.getUid(), RPGRedisTeamMemberVO.class)).thenReturn(memberVO);
            Map<String, RPGRedisTeamMemberVO> teamMemberMap = initAllTeamMember(RPGRedisTeamVO,userSessionContext);
            Mockito.when(cacheManager.hgetAll(teamMemberKey, RPGRedisTeamMemberVO.class)).thenReturn(teamMemberMap);
            Mockito.doNothing().when(cacheManager).set(Mockito.anyString(),Mockito.anyString());
            PowerMockito.mockStatic(EventBus.class);
           // PowerMockito.doNothing().when(EventBus.class);
            UpdatePositionCountParams params = new UpdatePositionCountParams();
            params.setSequence(RPGRedisTeamVO.getSequence());
            params.setNumber(new Random().nextInt(18)+2);
            MvcResult result = mockMvc
                    .perform(post("/gamingteam/positioncount/update").characterEncoding("UTF-8").contentType(
                            MediaType.APPLICATION_JSON).header("Authorization", "123456")
                            .content(JacksonUtils.toJsonWithSnake(params)))
                    .andExpect(status().isOk()).andReturn();
            String str = result.getResponse().getContentAsString();
            LOGGER.info("result: {}", new Object[]{str});
            ResponsePacket packet = JacksonUtils.toBeanWithSnake(str, ResponsePacket.class);
            Assert.assertTrue(null != packet && packet.responseSuccess());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

    }

    private Map<String,RPGRedisTeamMemberVO> initAllTeamMember(RPGRedisTeamVO RPGRedisTeamVO,UserSessionContext userSessionContext) {
        Map<String, RPGRedisTeamMemberVO> teamMemberMap = new HashMap<>();
        teamMemberMap.put(userSessionContext.getUid(),initLeaderTeamMemberVo(RPGRedisTeamVO,userSessionContext));
        teamMemberMap.put("test002",initTeamMemberVo("test002","测试用户2",UserIdentityEnum.BOSS.getCode(),
                RPGRedisTeamVO.getId(),BaojiLevelEnum.BOSS.getCode(),"1","剑神",1001L,"老板1",null,null));
        teamMemberMap.put("test003",initTeamMemberVo("test003","测试用户3",UserIdentityEnum.BOSS.getCode(),
                RPGRedisTeamVO.getId(),BaojiLevelEnum.BOSS.getCode(),"1","剑神",1002L,"老板2",null,null));
        teamMemberMap.put("test003",initTeamMemberVo("test004","测试用户4",UserIdentityEnum.BOSS.getCode(),
                RPGRedisTeamVO.getId(),BaojiLevelEnum.BOSS.getCode(),"1","剑神",1003L,"老板3",null,null));
        return  teamMemberMap;

    }

    private RPGRedisTeamMemberVO initLeaderTeamMemberVo(RPGRedisTeamVO RPGRedisTeamVO,UserSessionContext userSessionContext){
        return initTeamMemberVo(userSessionContext.getUid(),userSessionContext.getUsername(),UserIdentityEnum.LEADER.getCode(),
                RPGRedisTeamVO.getId(),BaojiLevelEnum.SUPER.getCode(),"1","剑神",1000L,"喜欢的少年是你",1,"C位");
    }
    private RPGRedisTeamMemberVO initTeamMemberVo(String uid,String userName,Integer userIdentity,Long teamId,Integer baojiLevel,String careerCode,String careerName,Long roleId,String roleName,Integer raidLocationCode,String raidLocationName){
        RPGRedisTeamMemberVO vo = new RPGRedisTeamMemberVO();
        vo.setGmtCreate(new Date());
        vo.setGmtModified(new Date());
        vo.setTeamId(teamId);
        vo.setAvatar("http:www.baidu.com");
        vo.setBaojiLevel(baojiLevel);
        vo.setCareerCode(careerCode);
        vo.setCareerName(careerName);
        vo.setGameRoleId(roleId);
        vo.setGameRoleName(roleName);
        vo.setJoinTime(new Date());
        vo.setRaidLocationCode(raidLocationCode);
        vo.setRaidLocationName(raidLocationName);
        vo.setStatus(TeamMemberStatusEnum.PREPARE_JOIN_TEAM.getCode());
        vo.setUid(uid);
        vo.setUsername(userName);
        vo.setUserIdentity(userIdentity);
        vo.setZoneSmallCode(10);
        vo.setZoneSmallName("东北一区");
        return vo;
    }
    private UserSessionContext initUserSessionContext(){
        UserSessionContext userSessionContext = new UserSessionContext();
        userSessionContext.setUid("test001");
        userSessionContext.setUsername("测试用户1");
        PowerMockito.mockStatic(UserSessionContext.class);
        PowerMockito.when(UserSessionContext.getUser()).thenReturn(userSessionContext);
        return userSessionContext;
    }
    private RPGRedisTeamVO preMadeTeamVo(){
        RPGRedisTeamVO RPGRedisTeamVO = new RPGRedisTeamVO();
        RPGRedisTeamVO.setActuallyPositionCount(new Random().nextInt(18)+2);
        RPGRedisTeamVO.setGmtCreate(new Date());
        RPGRedisTeamVO.setGmtModified(new Date());
        RPGRedisTeamVO.setId(Math.abs(new Random().nextLong()));
        RPGRedisTeamVO.setOriginalPositionCount(20);
        RPGRedisTeamVO.setStatus(TeamStatusEnum.PREPARING.getCode());
        RPGRedisTeamVO.setAssaultName("单元测试车队"+ RPGRedisTeamVO.getId());
        RPGRedisTeamVO.setChannel("2");
        RPGRedisTeamVO.setDiscountFee(200);
        RPGRedisTeamVO.setGameCode(88);
        RPGRedisTeamVO.setGameName("DNF");
        RPGRedisTeamVO.setOriginalFee(300);
        RPGRedisTeamVO.setPaymentTimeout(300);
        RPGRedisTeamVO.setRaidCode(1);
        RPGRedisTeamVO.setRaidName("安图恩团队");
        RPGRedisTeamVO.setTitle("开车啦开车啦");
        RPGRedisTeamVO.setSequence(snowFlake.nextId()+"");
        RPGRedisTeamVO.setRoomNum(55);
        RPGRedisTeamVO.setZoneAcrossCode(6);
        RPGRedisTeamVO.setZoneAcrossName("跨6");
        return RPGRedisTeamVO;
    }



}
