package com.kaihei.esportingplus.resource.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.resource.domain.entity.BannerConfig;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BannerConfigRepository extends CommonRepository<BannerConfig> {

    /**
     * 获取轮播的banner信息
     * @param currentDate
     * @param carouseCount
     * @return
     */
    List<BannerConfig> selectCarouselBannerConfig(@Param("userType") Integer userType,@Param("position")String position,@Param("currentDate") Date currentDate,@Param("carouseCount")Integer carouseCount);

    /**
     * 按时间倒叙（id倒叙）排列查询
     * @return
     */
    List<BannerConfig> selectBannerConfig(@Param("userType") Integer userType,@Param("position")String position);
}