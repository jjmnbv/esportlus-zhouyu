package com.kaihei.esportingplus.resource.domain.service.freeteam;

import com.kaihei.esportingplus.api.params.freeteam.BannerSaveParams;
import com.kaihei.esportingplus.api.params.freeteam.BannerUpdateParams;
import com.kaihei.esportingplus.api.params.freeteam.ResourceStateSaveParams;
import com.kaihei.esportingplus.api.params.freeteam.ResourceStateUpdateParams;
import com.kaihei.esportingplus.api.vo.freeteam.BannerConfigVo;
import com.kaihei.esportingplus.api.vo.freeteam.BannerDictConfigVo;
import com.kaihei.esportingplus.api.vo.freeteam.ResourceStateConfigVo;
import com.kaihei.esportingplus.common.paging.PagingRequest;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import java.util.List;

/**
 * 资源位管理
 * @author zhangfang
 */
public interface ResourceStateConfigService {

    /**
     * 获取资源位
     * @param pagingRequest
     * @return
     */
    List<ResourceStateConfigVo> findResourceStateConfig(Integer userType, String position);


    /**
     * 获取一个资源位
     * @return
     */
    public ResourceStateConfigVo findResourceStateConfigById(Integer resourceId);

    /**
     * 添加资源位
     * @param resourceStateSaveParams
     */
    public void saveResourceStateConfig(ResourceStateSaveParams resourceStateSaveParams);
    /**
     * 修改资源位
     * @param resourceStateUpdateParams
     */
    public void updateResourceStateConfig(ResourceStateUpdateParams resourceStateUpdateParams);

    /**
     * 删除资源位
     * @param id
     */
    public void deleteResourceStateConfig(Integer id);
}
