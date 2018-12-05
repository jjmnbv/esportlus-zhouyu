package com.kaihei.esportingplus.core.domain.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.core.api.params.UserTagInfoParam;
import com.kaihei.esportingplus.core.api.params.UserTagParam;
import com.kaihei.esportingplus.core.api.vo.PageInfo;
import com.kaihei.esportingplus.core.data.repository.UserTagInfoRepository;
import com.kaihei.esportingplus.core.domain.entity.UserTagInfo;
import com.kaihei.esportingplus.core.domain.service.RongYunService;
import com.kaihei.esportingplus.core.domain.service.UserTagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: esportingplus
 * @description: 用户Tag
 * @author: xusisi
 * @create: 2018-12-03 17:31
 **/
@Service
public class UserTagServiceImpl implements UserTagService {

    private static Logger logger = LoggerFactory.getLogger(UserTagServiceImpl.class);

    @Autowired
    private RongYunService rongYunService;

    @Autowired
    private UserTagInfoRepository userTagInfoRepository;

    private static Pattern p = Pattern.compile("\\s*|\t|\r|\n");

    @Override
    public Boolean insertOrUpdateUserTagInfo(UserTagInfoParam userTagInfoParam) {

        String filePath = userTagInfoParam.getFilePath();
        //读取文件中的uid
        List<String> userIds = null;
        try {

            String result = new String(Files.readAllBytes(Paths.get(filePath)));

            if (StringUtils.isEmpty(result)) {
                logger.error("exception : {} ", BizExceptionEnum.PUSH_MESSAGE_FILE_USERID_IS_EMPTY.getErrMsg());
                throw new BusinessException(BizExceptionEnum.PUSH_MESSAGE_FILE_USERID_IS_EMPTY);
            }
            Matcher m = p.matcher(result);
            result = m.replaceAll("");
            String[] list = result.split(",");
            Set<String> set = new HashSet<>(Arrays.asList(list));
            userIds = new ArrayList<>(set);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String tagName = userTagInfoParam.getTagName();
        //判断tag是否存在，不能重名
        UserTagInfo info = userTagInfoRepository.getTagInfoByTagName(tagName);
        Integer tagId = userTagInfoParam.getTagId();
        if (tagId == null) {
            //新增，判断是否有tag重名
            if (info != null) {
                System.out.println("tag重名了");
            }
            info = new UserTagInfo();
            info.setUids(FastJsonUtils.toJson(userIds));
            info.setUserNumber(userIds.size());
            info.setTagName(tagName);
            info.setOperator(userTagInfoParam.getOperator());
            userTagInfoRepository.insertTagInfo(info);

        } else {
            //修改时，判断是否有重名的tag
            if (info != null && !info.getId().equals(tagId)) {
                System.out.println("tag重名了");
            }
            info = new UserTagInfo();
            info.setId(tagId);
            info.setTagName(tagName);
            info.setUids(FastJsonUtils.toJson(userIds));
            info.setUserNumber(userIds.size());
            info.setOperator(userTagInfoParam.getOperator());
            userTagInfoRepository.updateUserTagInfo(info);
        }

        UserTagParam userTagParam = new UserTagParam();
        List<String> tags = new ArrayList<>();
        tags.add(tagName);
        userTagParam.setUserIds(userIds);
        userTagParam.setTags(tags);
        return rongYunService.setTag(userTagParam);

    }

    @Override
    public PageInfo<UserTagInfo> getTagsList(Integer currentPage, Integer pageSize) {
        PageInfo pageInfo = new PageInfo();
        Page<UserTagInfo> page = PageHelper
                .startPage(currentPage, pageSize)
                .doSelectPage(() -> userTagInfoRepository.getTagsList());
        pageInfo.setDataList(page.getResult());
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    @Override
    public Boolean checkTagNameIsExist(String tagName) {
        UserTagInfo userTagInfo = userTagInfoRepository.getTagInfoByTagName(tagName);
        if (userTagInfo != null) {
            return true;
        }
        return false;
    }

    @Override
    public UserTagInfo selectById(Integer tagId) {
        return userTagInfoRepository.selectById(tagId);
    }

    public static void main(String[] args) {
        String filePath = "C:\\Users\\admin\\Desktop\\订单类型.txt";
        try {
            String result = new String(Files.readAllBytes(Paths.get(filePath)));
            String[] list = result.split(",");
            Set<String> set = new HashSet<>(Arrays.asList(list));
            System.out.println(set);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}