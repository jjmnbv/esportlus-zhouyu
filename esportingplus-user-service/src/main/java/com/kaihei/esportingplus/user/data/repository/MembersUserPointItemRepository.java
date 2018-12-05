package com.kaihei.esportingplus.user.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.user.domain.entity.MembersUserPointItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 鸡分明细持久化类
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/9 16:12
 */
public interface MembersUserPointItemRepository extends CommonRepository<MembersUserPointItem> {

    /**
     * 根据用户ID查询明细列表
     *
     * @param userId 用户ID
     * @return List<MembersUserPointItem>
     */
    public List<MembersUserPointItem> selectByUserId(Integer userId);

    /**
     * 根据车队标识符ID查询车队获得的积分明细
     *
     * @param itemType 车队标识符
     * @param slug 车队标识符
     * @return List<MembersUserPointItem>
     */
    public MembersUserPointItem selectByTypeAndSlug(@Param("itemType") Integer itemType, @Param("slug") String slug);

    /**
     * 车队统计数据
     *
     * @param userId 车队标识符
     * @return Integer
     */
    public Integer selectCountByUserId(@Param("userId") Integer userId);
}
