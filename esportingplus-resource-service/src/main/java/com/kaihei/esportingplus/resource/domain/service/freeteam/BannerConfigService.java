package com.kaihei.esportingplus.resource.domain.service.freeteam;

import com.kaihei.esportingplus.api.params.freeteam.BannerSaveParams;
import com.kaihei.esportingplus.api.params.freeteam.BannerUpdateParams;
import com.kaihei.esportingplus.api.vo.freeteam.BannerDictConfigVo;
import com.kaihei.esportingplus.api.vo.freeteam.BannerConfigVo;
import com.kaihei.esportingplus.common.paging.PagingRequest;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import java.util.List;

/**
 * @author zhangfang
 */
public interface BannerConfigService  {

    /**
     * 获取banner位
     * @param pagingRequest
     * @return
     */
    PagingResponse<BannerConfigVo> findBannerConfig(Integer userType,String position,PagingRequest pagingRequest);

    /**
     * 获取轮播Banner
     * @return
     */
    public List<BannerConfigVo> findCarouselBannerConfig(Integer userType,String position);

    /**
     * 获取一个banner
     * @return
     */
    public BannerConfigVo findBannerConfigById(Integer bannerId);

    /**
     * 添加banner位
     * @param bannerSaveParams
     */
    public void saveBannerConfig(BannerSaveParams bannerSaveParams);
    /**
     * 修改banner位
     * @param bannerUpdateParams
     */
    public void updateBannerConfig(BannerUpdateParams bannerUpdateParams);

    /**
     * 删除banner配置
     * @param id
     */
    public void deleteBannerConfig(Integer id);

    /**
     * 获取banner字典配置
     * @return
     */
    public BannerDictConfigVo getBannerDictConfigVo();

    /**
     * 保存或者修改 banner字典配置
     * @param vo
     */
    public void updateBannerDictConfigVo(BannerDictConfigVo vo);
}
