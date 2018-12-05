package com.kaihei.esportingplus.core.domain.service.impl;

import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.core.api.params.UserTagParam;
import com.kaihei.esportingplus.core.config.MessageConfig;
import com.kaihei.esportingplus.core.data.manager.RonyunUserTokenCacheManager;
import com.kaihei.esportingplus.core.domain.service.RongYunService;
import com.kaihei.esportingplus.core.utils.RonYunUtils;
import io.rong.RongCloud;
import io.rong.models.Result;
import io.rong.models.group.GroupMember;
import io.rong.models.group.GroupModel;
import io.rong.models.response.BlackListResult;
import io.rong.models.response.TokenResult;
import io.rong.models.user.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static com.kaihei.esportingplus.core.constant.RongYunConstants.Limit.MAX_USERS_PER_TAG;
import static com.kaihei.esportingplus.core.constant.RongYunConstants.Result.SUCCESSCODE;

/**
 * @author liuyang
 * @Description: 融云服务实现类
 * @Title: RongYunServiceImpl
 */
@Service
public class RongYunServiceImpl implements RongYunService {

    private static final Logger logger = LoggerFactory.getLogger(RongYunServiceImpl.class);

    private static int SUCCESS_CODE = SUCCESSCODE;

    @Autowired
    private RongCloud rongCloud;

    @Autowired
    private MessageConfig messageConfig;

    @Autowired
    private RonyunUserTokenCacheManager userTokenCacheManager;

    @Override
    public String getToken(String uid, String userName, String thumbnail) {
        String imUserId = encodeIMUserId(uid);
        try {
            String token = userTokenCacheManager.getToken(uid);
            if (StringUtils.isNotEmpty(token)) {
                return token;
            }

            thumbnail = setImageUrl(thumbnail);
            TokenResult result = rongCloud.user.register(new UserModel(imUserId, userName, thumbnail));
            if (validateResult(result)) {
                String resultToken = result.getToken();
                userTokenCacheManager.saveToken(uid, resultToken);
                logger.debug("RongYun TOKEN SAVE CACHE:{}: the uid={},the userName={},the thumbnail={}",
                        imUserId, userName, thumbnail);
                return resultToken;
            }

            logger.error("RongYun Error:{}, getRongYunToken failed: the uid={},the userName={},the thumbnail={}",
                    result.msg, imUserId, userName, thumbnail);
        } catch (Exception e) {
            logger.error(
                    "RongYun Error:{}, getRongYunToken failed: the uid={},the userName={},the thumbnail={}",
                    e.getMessage(), imUserId, userName, thumbnail);
        }

        return null;
    }

    private String setImageUrl(String thumbnail) {
        if (StringUtils.isEmpty(thumbnail)) {
            thumbnail = messageConfig.getRonyun().getImgurl();
        }
        return thumbnail;
    }

    @Override
    public boolean updateUser(String uid, String userName, String thumbnail) {
        String imUserId = encodeIMUserId(uid);
        try {
            thumbnail = setImageUrl(thumbnail);
            Result result = rongCloud.user.update(new UserModel(imUserId, userName, thumbnail));
            return validateResult(result);
        } catch (Exception e) {
            logger.error(
                    "RongYun Error:{}, refresh user info failed: the uid={},the userName={},the thumbnail={}",
                    e.getMessage(), uid, userName, thumbnail);
            return false;
        }
    }

    @Override
    public boolean blockUser(List<String> uids, int minute) {
        try {
            for (String uid : uids) {
                String imUserId = encodeIMUserId(uid);
                UserModel user = new UserModel().setId(imUserId).setMinute(minute);
                Result result = rongCloud.user.block.add(user);
                if (!validateResult(result)) {
                    logger.error("RongYun Error:{}, blockUser failed: the uid={},the minute={}",
                            result.getMsg(), uid, minute);
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            logger.error("RongYun Error:{}, blockUser failed: the uids={},the minute={}",
                    e.getMessage(), uids, minute);
        }

        return false;
    }

    @Override
    public boolean unBlockUser(List<String> uids) {
        try {
            for (String uid : uids) {
                String imUserId = encodeIMUserId(uid);
                Result result = rongCloud.user.block.remove(imUserId);
                if (!validateResult(result)) {
                    logger.error("RongYun Error:{}, unBlockUser failed: the uid={}", result.getMsg(), uid);
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            logger.error("RongYun Error:{}, unBlockUser failed: the uids={}", e.getMessage(), uids);
        }

        return false;
    }

    @Override
    public boolean addUserToBlacklist(String uid, List<String> blackUserId) {
        try {
            String imUserId = encodeIMUserId(uid);
            UserModel user = new UserModel().setId(imUserId);
            UserModel[] blackList = new UserModel[blackUserId.size()];
            for (int i = 0; i < blackUserId.size(); i++) {
                String imBlackUserId = encodeIMUserId(blackUserId.get(i));
                blackList[i] = new UserModel().setId(imBlackUserId);
            }

            user.setBlacklist(blackList);
            Result result = rongCloud.user.blackList.add(user);
            return validateResult(result);
        } catch (Exception e) {
            logger.error(
                    "RongYun Error:{}, addUserToBlacklist failed: the uid={},the blackUserid={}",
                    e.getMessage(), uid, blackUserId);
        }

        return false;
    }

    @Override
    public boolean removeUserFromBlacklist(String uid, List<String> blackUserIds) {
        try {
            String imUserId = encodeIMUserId(uid);
            UserModel user = new UserModel().setId(imUserId);
            UserModel[] blackList = new UserModel[blackUserIds.size()];
            for (int i = 0; i < blackUserIds.size(); i++) {
                String imBlackUserId = encodeIMUserId(blackUserIds.get(i));
                blackList[i] = new UserModel().setId(imBlackUserId);
            }
            user.setBlacklist(blackList);
            Result result = rongCloud.user.blackList.remove(user);
            return validateResult(result);
        } catch (Exception e) {
            logger.error(
                    "RongYun Error:{}, removeBlackListResult failed: the uid={},the blackUserids={}",
                    e.getMessage(), uid, blackUserIds);
        }
        return false;
    }

    @Override
    public String[] queryUsersBlacklist(String uid) {
        String imUserId = encodeIMUserId(uid);
        String[] blackUsers = new String[]{};
        try {
            UserModel user = new UserModel().setId(imUserId);
            BlackListResult result = rongCloud.user.blackList.getList(user);
            UserModel[] users = result.getUsers();
            blackUsers = new String[users.length];
            for (int i = 0; i < users.length; i++) {
                blackUsers[i] = users[i].getId();
            }
        } catch (Exception e) {
            logger.error("RongYun Error:{}, queryUsersBlacklist failed: the uid={}", e.getMessage(),
                    uid);
        }

        return blackUsers;
    }

    @Override
    public boolean joinGroup(List<String> members, String groupId, String groupName) {
        try {
            GroupModel groupModel = new GroupModel().setId(groupId).setName(groupName);
            GroupMember[] groupMembers = new GroupMember[members.size()];
            for (int i = 0; i < members.size(); i++) {
                groupMembers[i] = new GroupMember().setId(encodeIMUserId(members.get(i)));
            }

            groupModel.setMembers(groupMembers);
            Result result = rongCloud.group.join(groupModel);
            return validateResult(result);
        } catch (Exception e) {
            logger.error(
                    "RongYun Error:{}, joinGroup failed: the members={},the groupId={},the groupName={}",
                    e.getMessage(), members, groupId, groupName);
        }

        return false;
    }

    @Override
    public boolean leaveGroup(List<String> members, String groupId) {
        try {
            GroupModel groupModel = new GroupModel().setId(groupId);
            GroupMember[] groupMembers = new GroupMember[members.size()];
            for (int i = 0; i < members.size(); i++) {
                groupMembers[i] = new GroupMember().setId(encodeIMUserId(members.get(i)));
            }

            groupModel.setMembers(groupMembers);
            Result result = rongCloud.group.quit(groupModel);
            return validateResult(result);
        } catch (Exception e) {
            logger.error("RongYun Error:{}, leaveGroup failed: the members={},the groupId={}",
                    e.getMessage(), members, groupId);
        }

        return false;
    }

    @Override
    public boolean dismissGroup(String uid, String groupId) {
        try {
            GroupModel groupModel = new GroupModel().setId(groupId);
            GroupMember[] groupMembers = new GroupMember[1];
            GroupMember groupMember = new GroupMember();
            groupMember.setId(encodeIMUserId(uid)).setGroupId(groupId);
            groupMembers[0] = groupMember;
            groupModel.setMembers(groupMembers);
            Result result = rongCloud.group.dismiss(groupModel);
            return validateResult(result);
        } catch (Exception e) {
            logger.error("RongYun Error:{}, dismissGroup failed: the uid={},the groupId={}",
                    e.getMessage(), uid, groupId);
        }

        return false;
    }

    @Override
    public boolean setTag(UserTagParam userTagParam) {
        List<String> userids = userTagParam.getUserIds();
        while (userids.size() > MAX_USERS_PER_TAG) {
            //分批发送
            List<String> subUserids = userids.subList(0, MAX_USERS_PER_TAG);
            UserTagParam param = new UserTagParam();
            param.setTags(userTagParam.getTags());
            param.setUserIds(RonYunUtils.encodeIMUser(subUserids));
            if (!sendTag(param)) {
                return false;
            }
            userids.subList(0, MAX_USERS_PER_TAG).clear();
        }

        if (userids.size() > 0) {
            UserTagParam param = new UserTagParam();
            param.setTags(userTagParam.getTags());
            param.setUserIds(userids);
            if (!sendTag(param)) {
                return false;
            }
        }

        return true;
    }

    private boolean sendTag(UserTagParam param) {
        RestTemplate template = new RestTemplate();
        HttpEntity entity = new HttpEntity(FastJsonUtils.toJson(param), RonYunUtils.getHeader());
        String url = messageConfig.getRonyun().getUrl() + "/user/tag/batch/set.json";
        ResponseEntity<String> response = template.postForEntity(url, entity, String.class);
        return response.getStatusCode() == HttpStatus.OK
                && SUCCESSCODE == Integer.parseInt(FastJsonUtils.fromJson(response.getBody(), Map.class).get("code").toString());
    }

    private boolean validateResult(Result result) {
        return result.code.intValue() == SUCCESS_CODE;
    }

    private String encodeIMUserId(String s) {
        return RonYunUtils.encodeIMUser(s);
    }
}
