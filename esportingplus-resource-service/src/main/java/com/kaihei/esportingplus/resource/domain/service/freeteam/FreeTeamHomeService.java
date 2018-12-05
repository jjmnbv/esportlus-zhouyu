package com.kaihei.esportingplus.resource.domain.service.freeteam;

import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamAdvertiseHomeVo;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamHomeVo;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamScrollTemplateHomeVo;
import java.util.List;

/**
 * 免费车队首页配置
 * @author Administrator
 */
public interface FreeTeamHomeService {

    /**
     * 获取首页宣传图列表
     * @return
     */
    public List<FreeTeamAdvertiseHomeVo> findHomeAdvertiseList();
    /**
     * 新增首页宣传图
     */
    public void saveFreeTeamAdvertiseHome(FreeTeamAdvertiseHomeVo freeTeamAdvertiseHomeVo);

    /**
     * 修改首页宣传图
     */
    public void updateFreeTeamAdvertiseHome(FreeTeamAdvertiseHomeVo freeTeamAdvertiseHomeVo);

    /**
     * 保存免费车队滚动模板
     */
    public void saveFreeTeamScrollTemplate(
            FreeTeamScrollTemplateHomeVo freeTeamScrollTemplateHomeVo);

    /**
     * 修改免费车队首页滚动模板
     */
    public void updateFreeTeamScrollTemplate(
            FreeTeamScrollTemplateHomeVo freeTeamScrollTemplateHomeVo);

    /**
     * 删除滚动模板
     * @param id
     * @return
     */
    public void deleteFreeTeamScrollTemplate(Integer id);

    /**
     * 获取宣传图
     * @return
     */
    public FreeTeamAdvertiseHomeVo findAdvertise(Integer machineType);


}
