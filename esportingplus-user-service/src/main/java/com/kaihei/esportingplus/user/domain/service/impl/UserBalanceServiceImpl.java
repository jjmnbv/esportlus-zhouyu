package com.kaihei.esportingplus.user.domain.service.impl;

import com.kaihei.esportingplus.api.feign.DictionaryClient;
import com.kaihei.esportingplus.api.vo.DictBaseVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.user.api.enums.AuthorityType;
import com.kaihei.esportingplus.user.api.enums.UserSwitchType;
import com.kaihei.esportingplus.user.api.vo.UserBalanceResutVo;
import com.kaihei.esportingplus.user.constant.UserRedisKey;
import com.kaihei.esportingplus.user.data.pyrepository.UserBaoJiGameStudioRelationRepository;
import com.kaihei.esportingplus.user.data.pyrepository.UserBaoJiRepository;
import com.kaihei.esportingplus.user.data.pyrepository.UserBaoNiangRepository;
import com.kaihei.esportingplus.user.domain.entity.UserBaoJi;
import com.kaihei.esportingplus.user.domain.entity.UserBaoJiGameStudioRelation;
import com.kaihei.esportingplus.user.domain.entity.UserBaoNiang;
import com.kaihei.esportingplus.user.domain.service.UserBalanceService;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author zl.zhao
 * @description:
 * @date: 2018/10/22 17:40
 */
@Service
public class UserBalanceServiceImpl implements UserBalanceService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserBaoJiRepository userBaoJiRepository;
    @Autowired
    private UserBaoNiangRepository userBaoNiangRepository;
    @Autowired
    private DictionaryClient dictionaryClient;
    @Autowired
    private UserBaoJiGameStudioRelationRepository userBaoJiGameStudioRelationRepository;


    @Override
    public UserBalanceResutVo getExchangeAuthority(String uid) {
        if (uid == null) {
            logger.info("cmd=UserBalanceServiceImpl.getExchangeAuthority | msg=传入参数有误 | uid={}", uid);
            return null;
        }

        //查询是否暴鸡/暴娘
        String identity = getIdentity(uid);

        //获取兑换控制配置
        Map<String, Integer> config = getCategoryCode();

        //根据身份匹配配置信息
        if(checkExchangeAuthority(identity, config)){
            return new UserBalanceResutVo(Boolean.TRUE);
        }else{
            return new UserBalanceResutVo(Boolean.FALSE);
        }
    }

    /**
     * 查询用户身份
     * @param uid
     * @return
     */
    private String getIdentity(String uid) {
        UserBaoJi baoJi = new UserBaoJi();
        baoJi.setUid(uid);
        int baoJiCount = userBaoJiRepository.selectCount(baoJi);
        String identity = UserSwitchType.ORDINARY_USER.getCode();
        //暴鸡
        if(baoJiCount > 0){
            int baoJiGameStudioCount = getBaoJiGameStudioCount(uid);

            if(baoJiGameStudioCount > 0){
                //工作室暴鸡
                identity = UserSwitchType.GZSBAOJI.getCode();
            }else{
                identity = UserSwitchType.BAOJI.getCode();
            }
        }else {
            UserBaoNiang baoNiang = new UserBaoNiang();
            baoNiang.setUid(uid);
            int baoNiangCount = userBaoNiangRepository.selectCount(baoNiang);
            //暴娘
            if (baoNiangCount > 0) {
                int baoJiGameStudioCount = getBaoJiGameStudioCount(uid);

                if(baoJiGameStudioCount > 0){
                    //工作室暴娘
                    identity = UserSwitchType.GZSBAONIANG.getCode();
                }else{
                    identity = UserSwitchType.BAONIANG.getCode();
                }
            }
        }
        return identity;
    }

    /**
     * 查询是否工作室暴鸡/暴娘
     * @return
     */
    private int getBaoJiGameStudioCount(String uid) {
        UserBaoJiGameStudioRelation gameStudioRelation = new UserBaoJiGameStudioRelation();
        gameStudioRelation.setUid(uid);
        return userBaoJiGameStudioRelationRepository.selectCount(gameStudioRelation);
    }

    /**
     * 匹配兑换控制配置
     * @return
     */
    private boolean checkExchangeAuthority(String identity, Map<String, Integer> config){
        Integer flag = config.get(identity);

        //根据配置信息判断权限
        if (AuthorityType.EXCHANGE_TRUE.getCode().equals(flag)) {
            return true;
        }

        return false;
    }

    /**
     * 获取兑换控制配置
     * @return
     */
    private Map<String, Integer> getCategoryCode(){
        ResponsePacket<DictBaseVO<Object>> result = dictionaryClient
                .findByCodeAndCategoryCode(
                UserRedisKey.EXCHANGE_AUTHORITY_DICTIONARY_CATEGORY, UserRedisKey.EXCHANGE_AUTHORITY_DICTIONARY,null);
        DictBaseVO<Object> dictBaseVO = result.getData();
        Map<String, Integer> config = (Map<String, Integer>) dictBaseVO.getValue();
        return config;
    }
}
