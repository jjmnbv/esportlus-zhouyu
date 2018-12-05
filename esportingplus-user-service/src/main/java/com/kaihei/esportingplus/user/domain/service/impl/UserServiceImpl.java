package com.kaihei.esportingplus.user.domain.service.impl;

import com.kaihei.esportingplus.api.feign.DictionaryClient;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.config.QiNiuConfig;
import com.kaihei.esportingplus.common.constant.CommonConstants;
import com.kaihei.esportingplus.common.constant.UploadFolderConstants;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.file.FileUploadService;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.user.config.UserProperties;
import com.kaihei.esportingplus.user.domain.service.UserService;
import com.kaihei.esportingplus.user.event.AvatarUpdateEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    @Resource(name = "restTemplateExtrnal")
    private RestTemplate restTemplateExtrnal;
    @Autowired
    private UserProperties userProperties;
    @Autowired
    private QiNiuConfig qiNiuConfig;
    @Autowired
    private FileUploadService qiNiuFileUploadService;
    @Autowired
    private Environment env;
    @Autowired
    private DictionaryClient dictionaryClient;

    @Override
    public List<UserSessionContext> getUserInfosByIds(List<String> uids) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, uids);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        Map<String, List<String>> requestParams = new HashMap<>();
        requestParams.put("uid_list", uids);
        HttpEntity<String> formEntity = new HttpEntity<String>(JacksonUtils.toJson(requestParams),
                headers);
        String result = restTemplateExtrnal.postForObject(
                userProperties.getUserInfoDomain() + userProperties.getUserInfoBatchQueryUrl(),
                formEntity, String.class);
        ResponsePacket responsePacket = JacksonUtils.toBean(result, ResponsePacket.class);
        if (responsePacket.responseSuccess() && ObjectTools.isNotEmpty(responsePacket.getData())) {
            return (List<UserSessionContext>) JacksonUtils
                    .toBeanCollectionWithSnake(JacksonUtils.toJson(responsePacket.getData()),
                            List.class, UserSessionContext.class);
        }
        return new ArrayList<>();

    }

    @Override
    public UserSessionContext getUserInfoByUid(String uid) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, uid);
        String result = restTemplateExtrnal.getForObject(
                userProperties.getUserInfoDomain() + StringUtils
                        .formatTemplate(userProperties.getUserInfoSingleQueryUrl(), uid),
                String.class);
        ResponsePacket responsePacket = JacksonUtils.toBean(result, ResponsePacket.class);
        if (responsePacket.responseSuccess() && ObjectTools.isNotEmpty(responsePacket.getData())) {
            return JacksonUtils
                    .toBeanWithSnake(JacksonUtils.toJson(responsePacket.getData()),
                            UserSessionContext.class);
        }
        return null;
    }

    public void postNotifyUpdateAvatar(String uid, String avatar) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, uid, avatar);
        AvatarUpdateEvent event =new AvatarUpdateEvent();
        event.setUid(uid);
        event.setAvatar(avatar);
        EventBus.post(event);
    }

    @Override
    public String getAvatarLinkAndNotifyUpdateAvatar(String uid) {
        UserSessionContext info = getUserInfoByUid(uid);
        ValidateAssert.hasNotNull(BizExceptionEnum.USER_NOT_EXIST, info);
        return changeAndUpdateAvatar(uid, info.getAvatar());
    }

    @Override
    public String getAvatarLink(String uid) {
        UserSessionContext info = getUserInfoByUid(uid);
        ValidateAssert.hasNotNull(BizExceptionEnum.USER_NOT_EXIST, info);
        String avatarLink = null;
        if (ObjectTools.isNotEmpty(info.getAvatar())) {
            avatarLink = getSupportAvatarLink(uid, info.getAvatar());
        }
        return avatarLink;
    }

    @Override
    public String changeAndUpdateAvatar(String uid, String avatar) {
        String avatarLink = getSupportAvatarLink(uid, avatar);
        //通知修改用户头像
        if (ObjectTools.isNotEmpty(avatarLink) && !avatarLink.equals(avatar)) {
            postNotifyUpdateAvatar(uid, avatarLink);
        }
        return avatarLink;
    }

    private String getSupportAvatarLink(String uid, String avatar) {
        //判断是否是七牛的地址
        String avatarLink = null;
        if(ObjectTools.isEmpty(avatar)){
            return avatar;
        }
        if (!avatar.matches(userProperties.getExcludeAvatarRule())) {
            //如果不是七牛的地址，则下载图片上传到七牛
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
                HttpEntity<String> entity = new HttpEntity<String>(headers);
                ResponseEntity<byte[]> response = restTemplateExtrnal.exchange(avatar,
                        HttpMethod.GET, entity, byte[].class);
                ValidateAssert.isTrue(response.getStatusCode().is2xxSuccessful(),
                        BizExceptionEnum.FILE_DOWNLOAD_FAIL);
                //下载成功
                if (response.getStatusCode().is2xxSuccessful()) {
                    byte[] body = response.getBody();
                    InputStream is = new ByteArrayInputStream(body);
                    String avarServerFileName = qiNiuFileUploadService
                            .upload(is, env.getProperty(CommonConstants.PROJECT_ENV),
                                    UploadFolderConstants.USER_AVATAR_FLODER, uid, true);
                    //获取链接
                    avatarLink = qiNiuFileUploadService
                            .getUploadFileLink(avarServerFileName);

                } else {
                    throw new BusinessException(BizExceptionEnum.FILE_DOWNLOAD_FAIL);
                }
            } catch (Exception e) {
                //如果下载失败，直接返回null
                LOGGER.warn("user avatar dowanload or upload fail，the avatar link is:{}", avatar);
                avatarLink = null;
            }


        } else {
            avatarLink = avatar;
        }
        return avatarLink;
    }
}
