package com.kaihei.esportingplus.resource.data.repository.teamtype;

import com.kaihei.esportingplus.api.vo.freeteam.TeamUserWhiteListVO;
import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.resource.domain.entity.freeteam.TeamUserWhiteList;
import java.util.List;

/**
 * @author liangyi
 */
public interface TeamUserWhiteListRepository extends CommonRepository<TeamUserWhiteList> {

    /**
     * 查询所有用户 uid 白名单
     * @return
     */
    List<TeamUserWhiteListVO> selectAllUserWhiteList();

    /**
     * 批量添加用户 uid 白名单
     * @param uidList
     * @return
     */
    Integer addBatchUserWhiteList(List<String> uidList);

    /**
     * 根据 uid 删除对应的用户白名单记录
     * @param uid
     */
    void deleteUserWhiteListByUid(String uid);

    /**
     * 根据 uid 查询单个用户白名单记录
     * @param uid
     * @return
     */
    TeamUserWhiteList selectUserWhiteListByUid(String uid);
}