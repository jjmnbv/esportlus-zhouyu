package com.kaihei.esportingplus.resource.data.manager.impl;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.api.vo.BaseGameRaidVo;
import com.kaihei.esportingplus.api.vo.FrontSecondaryCareer;
import com.kaihei.esportingplus.api.vo.FrontTopCareer;
import com.kaihei.esportingplus.api.vo.RaidAndGameServerVo;
import com.kaihei.esportingplus.api.vo.RedisAwakeningCareer;
import com.kaihei.esportingplus.api.vo.RedisGame;
import com.kaihei.esportingplus.api.vo.RedisGameAcrossZone;
import com.kaihei.esportingplus.api.vo.RedisGameBigZone;
import com.kaihei.esportingplus.api.vo.RedisGameRaid;
import com.kaihei.esportingplus.api.vo.RedisGameSmallZone;
import com.kaihei.esportingplus.api.vo.RedisSmallZoneRefAcrossZone;
import com.kaihei.esportingplus.api.vo.RedisTopCareer;
import com.kaihei.esportingplus.api.vo.SimpleGameRaid;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.resource.data.manager.ResourceService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private RestTemplate restTemplate;
    protected static final CacheManager cacheManager = CacheManagerFactory.create();

    @Override
    public RedisSmallZoneRefAcrossZone getAcrossZoneFromSmallZoneCode(Integer gameCode,
            Integer smallZoneCode) {
        RedisSmallZoneRefAcrossZone zone = null;
        String result = cacheManager
                .get(String
                                .format(RedisKey.SMALL_ZONE_REF_ACROSS_ZONE_KEY, gameCode, smallZoneCode),
                        String.class);
        if (ObjectTools.isNotEmpty(result)) {
            zone = JacksonUtils.toBeanWithSnake(result, RedisSmallZoneRefAcrossZone.class);
        }
        return zone;
    }

    @Override
    public List<RedisGameBigZone> getBigOrSmallZoneByAcrossCode(Integer gameCode,
            Integer zoneAcrossCode) {
        //因为只有小区才能对应到跨区，而一个大区可能对应到多个跨区，所以必须通过跨区找到小区再从小区找到大区
        //1:通过跨区找到所有小区
        List<RedisGameSmallZone> smallZoneList = this
                .getSmallZoneByAcrossCode(gameCode, zoneAcrossCode);
        //遍历所有小区找到对应的大区,因为有多个小区会对应到一个大区上，所以定义一个Map集合,Key为大区code,value是大区小区
        Map<Integer, RedisGameBigZone> map = new HashMap<>();
        if (ObjectTools.isNotEmpty(smallZoneList)) {
            for (RedisGameSmallZone smallZone : smallZoneList) {
                RedisSmallZoneRefAcrossZone acrossZoneFromSmallZoneCode = this
                        .getAcrossZoneFromSmallZoneCode(gameCode, smallZone.getZoneSmallCode());
                if (acrossZoneFromSmallZoneCode != null) {
                    RedisGameBigZone bigZone = map
                            .get(acrossZoneFromSmallZoneCode.getZoneBigCode());
                    if (bigZone == null) {
                        bigZone = new RedisGameBigZone();
                        bigZone.setZoneBigCode(acrossZoneFromSmallZoneCode.getZoneBigCode());
                        bigZone.setZoneBigName(acrossZoneFromSmallZoneCode.getZoneBigName());
                        List<RedisGameSmallZone> smallZones = new ArrayList<>();
                        smallZones.add(smallZone);
                        bigZone.setZoneSmallList(smallZones);
                        map.put(acrossZoneFromSmallZoneCode.getZoneBigCode(),bigZone);
                    } else {
                        bigZone.getZoneSmallList().add(smallZone);
                    }

                }

            }
        }
        return map.values().stream().collect(Collectors.toList());
    }

    @Override
    public List<RedisGameSmallZone> getSmallZoneByAcrossCode(Integer gameCode,
            Integer zoneAcrossCode) {
        String result = cacheManager.get(String
                        .format(RedisKey.ACROSS_ZONE_REF_SMALL_ZONE_KEY, gameCode, zoneAcrossCode),
                String.class);
        List<RedisGameSmallZone> list = null;
        if (ObjectTools.isNotEmpty(result)) {
            list = (List<RedisGameSmallZone>) JacksonUtils
                    .toBeanCollectionWithSnake(result, List.class, RedisGameSmallZone.class);
        }
        return list;
    }

    @Override
    public List<RedisGameSmallZone> getSmallZoneByBigCode(Integer gameCode, Integer zoneBigCode) {
        String result = cacheManager
                .hget(String.format(RedisKey.BIG_ZONE_AND_SMALL_ZONE_KEY, gameCode),
                        zoneBigCode.toString(),
                        String.class);
        List<RedisGameSmallZone> list = new ArrayList<>();
        if (ObjectTools.isNotEmpty(result)) {
            RedisGameBigZone bigZone = JacksonUtils
                    .toBeanWithSnake(result, RedisGameBigZone.class);
            list = bigZone.getZoneSmallList();
        }
        return list;
    }

    @Override
    public RedisGameBigZone getBigAndSmallZoneByBigCode(Integer gameCode, Integer zoneBigCode) {
        String result = cacheManager
                .hget(String.format(RedisKey.BIG_ZONE_AND_SMALL_ZONE_KEY, gameCode),
                        zoneBigCode.toString(),
                        String.class);
        RedisGameBigZone bigZone = null;
        if (ObjectTools.isNotEmpty(result)) {
            bigZone = JacksonUtils
                    .toBeanWithSnake(result, RedisGameBigZone.class);
        }
        return bigZone;
    }

    @Override
    public List<RedisTopCareer> getCareerByGameCode(Integer gameCode) {
        Map<String, String> stringRedisTopCareerMap = cacheManager
                .hgetAll(String.format(RedisKey.GAME_CAREER_LIST_KEY, gameCode),
                        String.class);
        List<RedisTopCareer> list = null;
        if (ObjectTools.isNotEmpty(stringRedisTopCareerMap)) {
            list = new ArrayList<>();
            Set<Entry<String, String>> entries = stringRedisTopCareerMap.entrySet();
            for (Entry<String, String> entry : entries) {
                String value = entry.getValue();
                if (ObjectTools.isNotEmpty(value)) {
                    list.add(JacksonUtils.toBeanWithSnake(value, RedisTopCareer.class));
                }
            }
        }
        return list;
    }

    @Override
    public List<FrontTopCareer> getFrontTopCareer(Integer gameCode) {
        List<RedisTopCareer> redisTopCareers = this.getCareerByGameCode(gameCode);
        List<FrontTopCareer> list = new ArrayList<>();
        if (ObjectTools.isNotEmpty(redisTopCareers)) {
            for (RedisTopCareer redisTopCareer : redisTopCareers) {
                FrontTopCareer frontTopCareer = new FrontTopCareer();
                frontTopCareer.setCareerCode(redisTopCareer.getCareerCode());
                frontTopCareer.setCareerName(redisTopCareer.getCareerName());
                frontTopCareer
                        .setChildren(this.setChildrenCareer(redisTopCareer.getAwakeningCareer()));
                list.add(frontTopCareer);
            }
        }
        return list;
    }

    private List<FrontSecondaryCareer> setChildrenCareer(
            List<RedisAwakeningCareer> awakeningCareer) {
        List<FrontSecondaryCareer> children = new ArrayList<>();
        if (ObjectTools.isNotEmpty(awakeningCareer)) {
            for (RedisAwakeningCareer career : awakeningCareer) {
                FrontSecondaryCareer frontSecondaryCareer = new FrontSecondaryCareer();
                if (ObjectTools.isNotNull(career.getSecondAwakeningCode())) {
                    frontSecondaryCareer.setCareerCode(career.getSecondAwakeningCode());
                    frontSecondaryCareer.setCareerName(career.getSecondAwakeningName());
                    frontSecondaryCareer.setCareerIcon(career.getSecondAwakeningIcon());
                } else if (ObjectTools.isNotNull(career.getFirstAwakeningCode())) {
                    frontSecondaryCareer.setCareerCode(career.getFirstAwakeningCode());
                    frontSecondaryCareer.setCareerName(career.getFirstAwakeningName());
                    frontSecondaryCareer.setCareerIcon(null);
                } else {
                    frontSecondaryCareer.setCareerCode(career.getClassChangeCode());
                    frontSecondaryCareer.setCareerName(career.getClassChangeName());
                    frontSecondaryCareer.setCareerIcon(null);
                }
                children.add(frontSecondaryCareer);
            }
        }
        return children;
    }

    @Override
    public RedisTopCareer getCareerByGameCodeAndTopCareerCode(Integer gameCode,
            Integer careerCode) {
        String result = cacheManager
                .hget(String.format(RedisKey.GAME_CAREER_LIST_KEY, gameCode), careerCode.toString(),
                        String.class);
        RedisTopCareer career = null;
        if (ObjectTools.isNotEmpty(result)) {
            career = JacksonUtils.toBeanWithSnake(result, RedisTopCareer.class);
        }
        return career;
    }

    @Override
    public List<RedisGame> getGameList() {
        String result = cacheManager
                .get(RedisKey.GAME_LIST_KEY,
                        String.class);
        List<RedisGame> list = new ArrayList<>();
        if (ObjectTools.isNotEmpty(result)) {
            list = (ArrayList<RedisGame>) JacksonUtils
                    .toBeanCollectionWithSnake(result, ArrayList.class, RedisGame.class);
        }
        return list;
    }

    @Override
    public List<RedisGameRaid> getGameRaids(Integer gameCode) {
        Map<String, String> stringStringMap = cacheManager
                .hgetAll(String.format(RedisKey.GAME_RAID_KEY, gameCode), String.class);
        List<RedisGameRaid> list = null;
        if (ObjectTools.isNotEmpty(stringStringMap)) {
            list = new ArrayList<>();
            Set<Entry<String, String>> entries = stringStringMap.entrySet();
            for (Entry<String, String> entry : entries) {
                String value = entry.getValue();
                if (ObjectTools.isNotEmpty(value)) {
                    RedisGameRaid redisGameRaid = JacksonUtils
                            .toBeanWithSnake(value, RedisGameRaid.class);
                    if (redisGameRaid.getRaidAssistRate() != null) {
                        redisGameRaid.setRaidAssistRate(
                                redisGameRaid.getRaidAssistRate().setScale(2,
                                        BigDecimal.ROUND_HALF_UP));
                    }
                    if (redisGameRaid.getRaidDpsRate() != null) {
                        redisGameRaid
                                .setRaidDpsRate(redisGameRaid.getRaidDpsRate().setScale(2,
                                        BigDecimal.ROUND_HALF_UP));
                    }
                    list.add(redisGameRaid);
                }
            }
        }
        return list;
    }

    @Override
    public RedisGameRaid getSingleGameRaid(Integer gameCode, Integer raidCode) {
        String result = cacheManager
                .hget(String.format(RedisKey.GAME_RAID_KEY, gameCode), raidCode.toString(),
                        String.class);
        RedisGameRaid redisGameRaid = null;
        if (ObjectTools.isNotEmpty(result)) {
            redisGameRaid = JacksonUtils
                    .toBeanWithSnake(result, RedisGameRaid.class);
            if (redisGameRaid.getRaidAssistRate() != null) {
                redisGameRaid
                        .setRaidAssistRate(redisGameRaid.getRaidAssistRate().setScale(2,
                                BigDecimal.ROUND_HALF_UP));
            }
            if (redisGameRaid.getRaidDpsRate() != null) {
                redisGameRaid.setRaidDpsRate(redisGameRaid.getRaidDpsRate().setScale(2,
                        BigDecimal.ROUND_HALF_UP));
            }
        }
        return redisGameRaid;
    }


    @Override
    public List<BaseGameRaidVo> getCertGameRaids(Integer gameCode) {
        Map<String, String> stringStringMap = cacheManager
                .hgetAll(String.format(RedisKey.GAME_CERT_RAID_KEY, gameCode), String.class);
        List<BaseGameRaidVo> list = null;
        if (ObjectTools.isNotEmpty(stringStringMap)) {
            list = new ArrayList<>();
            Set<Entry<String, String>> entries = stringStringMap.entrySet();
            for (Entry<String, String> entry : entries) {
                String value = entry.getValue();
                if (ObjectTools.isNotEmpty(value)) {
                    BaseGameRaidVo baseGameRaidVo = JacksonUtils
                            .toBeanWithSnake(value, BaseGameRaidVo.class);
                    list.add(baseGameRaidVo);
                }
            }
        }
        return list;
    }


    @Override
    public BaseGameRaidVo getCertSingleGameRaid(Integer gameCode, Integer certRaidCode) {
        String result = cacheManager
                .hget(String.format(RedisKey.GAME_RAID_KEY, gameCode), certRaidCode.toString(),
                        String.class);
        BaseGameRaidVo baseGameRaidVo = null;
        if (ObjectTools.isNotEmpty(result)) {
            baseGameRaidVo = JacksonUtils
                    .toBeanWithSnake(result, BaseGameRaidVo.class);
        }
        return baseGameRaidVo;
    }


    @Override
    public List<RedisGameRaid> getGameRaidThroughCertCode(Integer gameCode, Integer certRaidCode) {
        String result = cacheManager
                .get(String.format(RedisKey.CERT_RAID_REF_TYPE_RAID_KEY, gameCode, certRaidCode),
                        String.class);
        List<RedisGameRaid> list = null;
        if (ObjectTools.isNotEmpty(result)) {
            list = (List<RedisGameRaid>) JacksonUtils
                    .toBeanCollectionWithSnake(result, List.class, RedisGameRaid.class);
        }
        return list;
    }


    @Override
    public List<RedisGameAcrossZone> getGameAcrossZone(Integer gameCode) {
        String result = cacheManager
                .get(String.format(RedisKey.GAME_ACROSS_ZONE_KEY, gameCode),
                        String.class);
        List<RedisGameAcrossZone> list = new ArrayList<>();
        if (ObjectTools.isNotEmpty(result)) {
            list = (ArrayList<RedisGameAcrossZone>) JacksonUtils
                    .toBeanCollectionWithSnake(result, ArrayList.class, RedisGameAcrossZone.class);
        }
        return list;
    }

    @Override
    public List<RedisGameBigZone> getAllBigZoneAndSmallZone(Integer gameCode) {
        Map<String, String> stringStringMap = cacheManager
                .hgetAll(String.format(RedisKey.BIG_ZONE_AND_SMALL_ZONE_KEY, gameCode),
                        String.class);
        List<RedisGameBigZone> list = new ArrayList<>();
        if (ObjectTools.isNotEmpty(stringStringMap)) {
            list = new ArrayList<>();
            Set<Entry<String, String>> entries = stringStringMap.entrySet();
            for (Entry<String, String> entry : entries) {
                String value = entry.getValue();
                if (ObjectTools.isNotEmpty(value)) {
                    list.add(JacksonUtils.toBeanWithSnake(value, RedisGameBigZone.class));
                }
            }
        }
        return list;
    }

    @Override
    public RaidAndGameServerVo getRaidAndServer(Integer gameCode) {
        List<RedisGameRaid> gameRaids = this.getGameRaids(gameCode);
        List<SimpleGameRaid> raids = new ArrayList<>();
        SimpleGameRaid allGameRaid = new SimpleGameRaid();
        allGameRaid.setRaidCode(0);
        allGameRaid.setRaidName("全部");
        raids.add(allGameRaid);
        if (ObjectTools.isNotEmpty(gameRaids)) {
            gameRaids.forEach(e -> raids.add(BeanMapper.map(e, SimpleGameRaid.class)));
        }
        List<RedisGameBigZone> servers = this.getAllBigZoneAndSmallZone(gameCode);
        RedisGameBigZone allBigZone = new RedisGameBigZone();
        allBigZone.setZoneBigCode(0);
        allBigZone.setZoneBigName("全部");
        servers.add(0, allBigZone);
        RaidAndGameServerVo vo = new RaidAndGameServerVo();
        vo.setRaids(raids);
        vo.setServers(servers);
        return vo;
    }

    @Override
    public String getSmallZoneName(Integer gameCode, Integer smallZoneCode) {
        return cacheManager
                .get(String.format(RedisKey.SMALL_ZONE_INFO_KEY, gameCode, smallZoneCode),
                        String.class);
    }

    @Override
    public String getBigZoneName(Integer gameCode, Integer bigZoneCode) {
        return cacheManager.get(String.format(RedisKey.BIG_ZONE_INFO_KEY, gameCode, bigZoneCode),
                String.class);
    }

    @Override
    public String getAcrossZoneName(Integer gameCode, Integer acrossZoneCode) {
        return cacheManager
                .get(String.format(RedisKey.ACROSS_ZONE_INFO_KEY, gameCode, acrossZoneCode),
                        String.class);
    }

    @Override
    public List<SimpleGameRaid> getGameRaidsForApp(Integer gameCode) {
        List<RedisGameRaid> gameRaids = this.getGameRaids(gameCode);
        List<SimpleGameRaid> raids = new ArrayList<>();
        if (ObjectTools.isNotEmpty(gameRaids)) {
            gameRaids.forEach(e -> raids.add(BeanMapper.map(e, SimpleGameRaid.class)));
        }
        return raids;
    }
}
