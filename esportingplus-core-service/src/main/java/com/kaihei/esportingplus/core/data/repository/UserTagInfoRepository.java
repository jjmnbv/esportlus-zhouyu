package com.kaihei.esportingplus.core.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.core.domain.entity.UserTagInfo;

import java.util.List;

public interface UserTagInfoRepository extends CommonRepository<UserTagInfo> {

    public Integer insertTagInfo(UserTagInfo userTagInfo);

    public List<UserTagInfo> getTagsList();

    public UserTagInfo getTagInfoByTagName(String tag);

    public UserTagInfo selectById(Integer id);

    public void updateUserTagInfo(UserTagInfo userTagInfo);

}
