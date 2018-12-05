package com.kaihei.esportingplus.core.domain.service;

import com.kaihei.esportingplus.core.api.params.UserTagInfoParam;
import com.kaihei.esportingplus.core.api.vo.PageInfo;
import com.kaihei.esportingplus.core.domain.entity.UserTagInfo;

public interface UserTagService {

    public Boolean insertOrUpdateUserTagInfo(UserTagInfoParam userTagInfoParam);

    public PageInfo<UserTagInfo> getTagsList(Integer page, Integer size);

    public Boolean checkTagNameIsExist(String tagName);

    public UserTagInfo selectById(Integer tagId);

}
