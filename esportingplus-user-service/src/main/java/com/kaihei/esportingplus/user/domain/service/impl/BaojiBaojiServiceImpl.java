package com.kaihei.esportingplus.user.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.user.api.enums.BaojiStatusType;
import com.kaihei.esportingplus.user.api.enums.BaoniangStatusEnum;
import com.kaihei.esportingplus.user.api.vo.PointDateVo;
import com.kaihei.esportingplus.user.api.vo.UserOrderCountVo;
import com.kaihei.esportingplus.user.api.vo.UserPointItemsVo;
import com.kaihei.esportingplus.user.data.pyrepository.BaojiBaojiRepository;
import com.kaihei.esportingplus.user.data.pyrepository.BaojiBaoniangRepository;
import com.kaihei.esportingplus.user.data.pyrepository.MembersUserRepository;
import com.kaihei.esportingplus.user.domain.entity.BaojiBaoJiTag;
import com.kaihei.esportingplus.user.domain.entity.BaojiBaoji;
import com.kaihei.esportingplus.user.domain.entity.BaojiBaoniang;
import com.kaihei.esportingplus.user.domain.entity.MembersUser;
import com.kaihei.esportingplus.user.domain.service.BaojiBaojiService;
import com.kaihei.esportingplus.user.domain.service.MembersOrderCountService;
import com.kaihei.esportingplus.user.domain.service.MembersUserPointService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author linruihe
 * @Title: BaojiBaojiServiceImpl
 * @Description: 暴鸡服务
 * @date 2018/11/15 15:31
 */
@Service
public class BaojiBaojiServiceImpl implements BaojiBaojiService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private BaojiBaoniangRepository baojiBaoniangRepository;

    @Autowired
    private BaojiBaojiRepository baojiBaojiRepository;

    @Autowired
    private MembersUserRepository membersUserRepository;

    @Autowired
    private MembersUserPointService membersUserPointService;

    @Autowired
    private MembersOrderCountService membersOrderCountService;

    /**
     * 判断是否为暴娘
     * @return
     */
    @Override
    public boolean isBaoniang(String uid){
        boolean result = false;
        List<BaojiBaoniang> baojiBaoniangList = baojiBaoniangRepository.selectBaoniangInfoByUid(uid);
        //存在暴娘记录，并且暴娘的状态为正常状态
        if(baojiBaoniangList != null && baojiBaoniangList.size() > 0){
            int code = BaoniangStatusEnum.BN_NORMAL.getCode();
            for (BaojiBaoniang baojiBaoniang: baojiBaoniangList) {
                if(baojiBaoniang.getStatus() == code){
                    result = true;
                    break;
                }
            }
        }
        logger.info("cmd=isBaoniang | uid={} | baojiBaoniangList={} | result={}", uid, JSON.toJSON(baojiBaoniangList),result);
        return result;
    }

    /**
     * 判断是否为暴鸡
     * @return
     */
    @Override
    public boolean isBaoji(int userId,String uid){
        //查询baoji_baoji表，状态为正常的暴鸡
        List<BaojiBaoji> baojiBaojiList = baojiBaojiRepository.selectBaojiByUidStatus(
                uid, BaojiStatusType.BAOJI_NORMAL.getCode().shortValue());
        logger.info("cmd=isBaoji | uid={} | userId={} | baojiBaojiList={} ", uid, userId, JSON.toJSON(baojiBaojiList));
        if(baojiBaojiList != null && baojiBaojiList.size() > 0){
            return true;
        }
        return false;
    }

    /**
     * 0：表示不存在用户信息
     * 1：表示玩家，非暴鸡或暴娘
     * 2：表示是暴鸡，非暴娘
     * 3：表示是暴娘，非暴鸡
     * 4：表示即是暴鸡，又是暴娘
     * @param uid
     * @return
     */
    @Override
    public int getIdentityByUid(String uid) {
        if(StringUtils.isEmpty(uid)){
            throw new BusinessException(BizExceptionEnum.PARAM_ENTRY_ERROR);
        }
        int result = 0;
        List<MembersUser> membersUserList =  membersUserRepository.selectUserInfoIdByUid(uid);
        if(membersUserList != null && membersUserList.size() > 0){
            result = 1;
        }
        if(result > 0){
            MembersUser membersUser = membersUserList.get(0);
            boolean isBn = isBaoniang(uid);
            boolean isBj = isBaoji(membersUser.getId(),uid);
            if(isBn && isBj){
                result = 4;
            }else{
                if(isBn){
                    result = 3;
                }else if(isBj){
                    result = 2;
                }
            }
        }
        return result;
    }

    /**
     * 获取暴鸡中心—数据
     * @param uid
     * @return
     */
    @Override
    public PointDateVo getUserPointDate(String uid) {
        PointDateVo pointDateVo = new PointDateVo();
        //获取当日鸡分值、总的鸡分值、当前接单数
        UserPointItemsVo userPointItemsVo = membersUserPointService.getUserAccumulatePoint(uid);
        int todayPoint = 0;
        int pointTotal = 0;
        int todayOrder = 0;
        if(userPointItemsVo != null){
            todayPoint = userPointItemsVo.getTodayAccPointAmount();
            pointTotal = userPointItemsVo.getPointAmount();
        }
        UserOrderCountVo membersOrderCount = membersOrderCountService.getUserTodayTotalData(uid);
        if(membersOrderCount != null){
            todayOrder = membersOrderCount.getTodayAccept();
        }
        pointDateVo.setTodayPoint(todayPoint);
        pointDateVo.setPointTotal(pointTotal);
        pointDateVo.setTodayOrder(todayOrder);
        return pointDateVo;
    }

    /**
     * 获取暴鸡最大等级的等级信息
     * @param uid
     * @return
     */
    @Override
    public BaojiBaoJiTag getBaoJiMaxLevel(String uid) {
        short status = BaojiStatusType.BAOJI_NORMAL.getCode().shortValue();
        BaojiBaoJiTag baojiBaoJiTag = baojiBaojiRepository.getBaoJiMaxLevel(uid,status);
        return baojiBaoJiTag;
    }

    /**
     * 根据游戏Code查询该游戏的暴鸡等级
     * @param uid
     * @param gameCode
     * @return
     */
    @Override
    public BaojiBaoJiTag getBaoJiLevelByGame(String uid, Integer gameCode) {
        short status = BaojiStatusType.BAOJI_NORMAL.getCode().shortValue();
        BaojiBaoJiTag baojiBaoJiTag = baojiBaojiRepository.getBaoJiLevelByGame(uid,gameCode,status);
        return baojiBaoJiTag;
    }
}
