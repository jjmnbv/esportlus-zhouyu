package com.kaihei.esportingplus.user.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.user.domain.entity.MembersUserPoint;

/**
 * 用户鸡分持久化类
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/9 14:38
 */
public interface MembersUserPointRepository extends CommonRepository<MembersUserPoint> {

    /**
     * 增加用户鸡分，若记录不存在则插入新记录
     *
     * @param membersUserPoint 用户鸡分对象
     */
    public void insertOrUpdate(MembersUserPoint membersUserPoint);

}
