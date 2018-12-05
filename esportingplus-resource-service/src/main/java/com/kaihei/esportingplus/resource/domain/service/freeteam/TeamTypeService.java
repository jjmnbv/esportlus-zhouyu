package com.kaihei.esportingplus.resource.domain.service.freeteam;

import com.kaihei.esportingplus.api.params.freeteam.TeamTypeAppQueryParams;
import com.kaihei.esportingplus.api.params.freeteam.TeamTypeBackendQueryParams;
import com.kaihei.esportingplus.api.params.freeteam.TeamTypeParams;
import com.kaihei.esportingplus.api.vo.freeteam.TeamTypeDetailVO;
import com.kaihei.esportingplus.api.vo.freeteam.TeamTypeSimpleVO;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import java.util.List;

/**
 * @author liangyi
 */
public interface TeamTypeService {

    /**
     * 根据 id 查询免费车队类型
     * @param freeTeamTypeId
     * @return
     */
    TeamTypeDetailVO getTypeById(Integer freeTeamTypeId);

    /**
     * 查询免费车队类型详细
     * @param freeTeamTypeAppQueryParams
     * @return
     */
    TeamTypeDetailVO getTypeDetail(TeamTypeAppQueryParams freeTeamTypeAppQueryParams);

    /**
     * 新增免费车队类型
     * @param teamTypeParams
     */
    void addTeamType(TeamTypeParams teamTypeParams);

    /**
     * 修改免费车队类型
     * @param teamTypeParams
     */
    void updateTeamType(TeamTypeParams teamTypeParams);

    /**
     * 根据 id 删除免费车队类型
     * @param freeTeamTypeId
     */
    void deleteTeamType(Integer freeTeamTypeId);

    /**
     *  根据暴鸡身份和上车行为查询所有有效的免费车队类型
     * @param freeTeamTypeAppQueryParams
     * @return
     */
    List<TeamTypeSimpleVO> getSimpleByBaojiIdentity(
            TeamTypeAppQueryParams freeTeamTypeAppQueryParams);

    /**
     * 分页查询免费车队类型
     * @param teamTypeBackendQueryParams
     * @return
     */
    PagingResponse<TeamTypeSimpleVO> getAllByPage(
            TeamTypeBackendQueryParams teamTypeBackendQueryParams);

    /**
     * 初始化免费车队类型缓存
     */
    void initFreeTeamTypeCache();
}
