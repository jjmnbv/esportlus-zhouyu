package com.kaihei.esportingplus.marketing.domian.service.impl;


import com.alibaba.fastjson.JSON;
import com.kaihei.esportingplus.api.feign.DictionaryClient;
import com.kaihei.esportingplus.api.feign.TaskConfigServiceClient;
import com.kaihei.esportingplus.api.vo.freeteam.ShareCopywriterConfigVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.marketing.api.enums.ShareEnum;
import com.kaihei.esportingplus.marketing.api.vo.ShareVo;
import com.kaihei.esportingplus.marketing.domian.service.MarketUserShareService;

import com.kaihei.esportingplus.user.api.feign.UserInfoServiceClient;
import com.kaihei.esportingplus.user.api.vo.MembersUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Auther: linruihe
 * @Date: 2018-10-12 14:40
 * @Description:
 */
@Component("MarketUserShareServiceImpl")
public class MarketUserShareServiceImpl implements MarketUserShareService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${user.free.share.icon}")
    private String shareIcon;

    @Value("user.free.share.url")
    private String shareUrl;

    @Autowired
    private TaskConfigServiceClient taskConfigServiceClient;

    @Autowired
    private DictionaryClient dictionaryClient;

    @Autowired
    private UserInfoServiceClient userInfoServiceClient;

    @Override
    public ShareVo getShareByType(String uid, String shareUid, String type) {
        ShareVo shareVo = new ShareVo();
        if(judgeType(type)){
            if(type.equals(ShareEnum.SHARE_FREE.getCode())){
                logger.info("cmd=sharePoint param | uid={} | shareUid={} ", uid,shareUid);
                ShareCopywriterConfigVO shareCopywriterConfigVO = getShare(type);
                //获取当天分享用户信息
                ResponsePacket<MembersUserVo> membersUserVoResponsePacket = userInfoServiceClient.getMembersUserByUid(uid);
                if (membersUserVoResponsePacket.responseSuccess()){
                    MembersUserVo membersUserVo = membersUserVoResponsePacket.getData();
                    String url = getUrl(membersUserVo,shareUid,shareCopywriterConfigVO.getRedirectUrl());
                    String title = shareCopywriterConfigVO.getShareTitle();
                    shareVo.setUrl(url);
                    shareVo.setTitle(title);
                    shareVo.setIcon(shareCopywriterConfigVO.getImgUrl());
                    shareVo.setContent(shareCopywriterConfigVO.getShareContent());
                }
            }
        }else{
            throw new BusinessException(BizExceptionEnum.USER_SHARE_NOT_ERROR);
        }
        logger.info("cmd=sharePoint.result | uid={} | shareUid={} | shareVo={}", uid,shareUid, JSON.toJSON(shareVo));
        return shareVo;
    }

    private boolean judgeType(String type){
        ShareEnum[] shareEnums = ShareEnum.values();
        for (ShareEnum shareEnum : shareEnums) {
            if(shareEnum.getCode().equals(type)){
                return true;
            }
        }
        return false;
    }

    private ShareCopywriterConfigVO getShare(String scene){
        ResponsePacket<List<ShareCopywriterConfigVO>> responsePacket =
                taskConfigServiceClient.findShareCopywriterConfigForBack(scene);
        if (responsePacket.responseSuccess()){
            List<ShareCopywriterConfigVO> shareCopywriterConfigVOList = responsePacket.getData();
            if (shareCopywriterConfigVOList != null && shareCopywriterConfigVOList.size() > 0){
                for (ShareCopywriterConfigVO shareCopywriterConfigVO : shareCopywriterConfigVOList) {
                    if(scene.equals(shareCopywriterConfigVO.getScene())){
                        return shareCopywriterConfigVO;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取分享的Url
     * @param membersUser
     * @param uid
     * @return
     */
    private String getUrl(MembersUserVo membersUser, String uid,String url){
       /*ResponsePacket<List<DictBaseVO<Object>>> dictBaseVOResponsePacket =
                dictionaryClient.findByCategoryCode("user_register", null);
        if (!dictBaseVOResponsePacket.responseSuccess()){
            //获取资源失败
        }
        List<DictBaseVO<Object>> dictBaseVOList = dictBaseVOResponsePacket.getData();*/
        //Map dictionaryValue = (Map) dictBaseVOResponsePacket.getData().getValue();
        //int reward_free_count = (int) dictionaryValue.get("reward_free_count");
        //使用多少天
        int day = (int) ((System.currentTimeMillis() - membersUser.getDateJoined().getTime()) / (1000*3600*24));
        StringBuilder sbd = new StringBuilder();
        sbd.append(url);
        sbd.append("uid=").append(uid);
        sbd.append("&username=").append(membersUser.getUsername());
        sbd.append("&reg_days=").append(day);
        sbd.append("&avatar=").append(membersUser.getThumbnail());
        /*sbd.append("&free_premade_times=").append(3);*/
        sbd.append("&share_key=").append(membersUser.getUid());
        return sbd.toString();
    }
}
