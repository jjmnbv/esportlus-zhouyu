package com.kaihei.esportingplus.resource.domain.service.freeteam;

import com.kaihei.esportingplus.api.params.freeteam.FreeTeamTypeAppQueryParams;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamTypeDetailVO;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamTypeSimpleVO;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamTypeVO;
import java.util.List;

/**
 * @author liangyi
 */
public interface FreeTeamTypeService {

    /**
     * 根据 id 查询免费车队类型
     * @param freeTeamTypeId
     * @return
     */
    FreeTeamTypeDetailVO getByFreeTeamTypeId(Integer freeTeamTypeId);

    /**
     * 查询免费车队类型详细
     * @param freeTeamTypeAppQueryParams
     * @return
     */
    FreeTeamTypeDetailVO getFreeTeamTypeDetail(FreeTeamTypeAppQueryParams freeTeamTypeAppQueryParams);

    /**
     * 新增免费车队类型
     * @param freeTeamTypeVO
     */
    void addFreeTeamType(FreeTeamTypeVO freeTeamTypeVO);

    /**
     * 修改免费车队类型
     * @param freeTeamTypeVO
     */
    void updateFreeTeamType(FreeTeamTypeVO freeTeamTypeVO);

    /**
     * 根据 id 删除免费车队类型
     * @param freeTeamTypeId
     */
    void deleteFreeTeamType(Integer freeTeamTypeId);

    /**
     *  根据暴鸡身份和上车行为查询所有有效的免费车队类型
     * @param freeTeamTypeAppQueryParams
     * @return
     */
    List<FreeTeamTypeSimpleVO> getSimpleByBaojiIdentity(
            FreeTeamTypeAppQueryParams freeTeamTypeAppQueryParams);

    /**
     * 查询所有免费车队类型
     * @return
     */
    List<FreeTeamTypeSimpleVO> getAll();

    /**
     * 查询所有上架的免费车队类型
     * @return
     */
    List<FreeTeamTypeDetailVO> getAllEnableFreeTeamType();

}
