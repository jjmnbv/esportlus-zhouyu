package com.kaihei.esportingplus.core.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.core.api.vo.AppVersionChangelogVo;
import com.kaihei.esportingplus.core.domain.entity.AppVersionChangelog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppVersionChangelogRepository extends CommonRepository<AppVersionChangelog> {
    /**
     * 判断版本日志记录是否被删除
     *
     * @param logId
     * @return boolean
     */
    boolean judgeIsDeleted(@Param("logId") int logId);

    /**
     * 更新版本日志记录状态
     *
     * @param updateType 1:启用 2:停用 3:删除
     * @param logId
     * @return 更新条数
     */
    int updateVersionLogStatus(@Param("updateType") int updateType, @Param("logId") int logId);

    /**
     * 查询版本更新日志信息
     *
     * @return List<AppVersionChangelogVo>
     * */
    List<AppVersionChangelogVo> selectChangelogVoList();

    /**
     * 根据客户端类型获取更新日志信息
     *
     * @param clientType 客户端类型(1:安卓 2:ios)
     * @return AppVersionChangelog
     * */
    AppVersionChangelog queryChangeLogByClientType(@Param("clientType")short clientType);
}