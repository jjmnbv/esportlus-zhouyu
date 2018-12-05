package com.kaihei.esportingplus.resource.domain.service.freeteam;

import com.kaihei.esportingplus.api.vo.freeteam.ChickenpointTaskConfigVo;
import java.util.List;

/**
 * @author zhangfang
 */
public interface ChickenpointTaskConfigService {

    /**
     * 新增鸡分任务
     */
    void save(ChickenpointTaskConfigVo vo);

    /**
     * 修改鸡分任务
     */
    void update(ChickenpointTaskConfigVo vo);

    /**
     * 获取鸡分任务列表
     */
    List<ChickenpointTaskConfigVo> findChickpointTaskConfig();

    /**
     * 查询有效的积分任务列表
     */
    List<ChickenpointTaskConfigVo> findEfficientChickpointTaskConfig();

    /**
     * 删除鸡分任务
     */
    void delete(Long id);
}
