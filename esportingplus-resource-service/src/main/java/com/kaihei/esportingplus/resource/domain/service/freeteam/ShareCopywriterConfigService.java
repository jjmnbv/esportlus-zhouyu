package com.kaihei.esportingplus.resource.domain.service.freeteam;

import com.kaihei.esportingplus.api.params.freeteam.ShareCopywriterConfigParams;
import com.kaihei.esportingplus.api.vo.freeteam.ShareCopywriterConfigVO;
import java.util.List;

/**
 * @author zhangfang
 */
public interface ShareCopywriterConfigService {

    /**
     * 根据场景查找所有的第三方分享配置
     * @return
     */
    public List<ShareCopywriterConfigVO> findShareCopywriterConfig(String scene);

    /**
     * 新增第三方分享任务
     * @param params
     */
    public void saveShareCopywriterConfig(ShareCopywriterConfigParams params);
    /**
     * 修改第三方分享任务
     * @param params
     */
    public void updateShareCopywriterConfig(ShareCopywriterConfigParams params);


    public ShareCopywriterConfigVO findById(Integer shareId);
}
