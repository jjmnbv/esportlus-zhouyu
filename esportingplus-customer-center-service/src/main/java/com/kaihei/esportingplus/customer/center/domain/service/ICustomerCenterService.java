package com.kaihei.esportingplus.customer.center.domain.service;

import com.kaihei.esportingplus.api.params.ComplainCreateParam;
import com.kaihei.esportingplus.api.params.ComplaintQueryParam;
import com.kaihei.esportingplus.api.params.StudioComplainQueryParams;
import com.kaihei.esportingplus.api.vo.ComplaintDetailVo;
import com.kaihei.esportingplus.api.vo.ComplaintListVo;
import com.kaihei.esportingplus.api.vo.StudioComplaintListVo;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import java.util.List;

/**
 * @author admin
 */
public interface ICustomerCenterService {

    /**
     * 创建投诉单
     */
    void createComplaint(ComplainCreateParam complainCreateParam);

    /**
     * 分页查询投诉单列表
     */
    PagingResponse<ComplaintListVo> listComplaint(ComplaintQueryParam complaintQueryParam);

    /**
     * 分页查询投诉单列表（工作室）
     */
    PagingResponse<StudioComplaintListVo> studiolist(
            StudioComplainQueryParams studioComplainQueryParams);

    /**
     * 获取投诉单详情
     */
    ComplaintDetailVo getComplaintDetail(Integer sequeue);

    /**
     * 确认被投诉订单
     */
    List<String> checkOrderBeComplainted(List<String> orderSequeues);

    /**
     * 根据uid统计 用户是否被投诉过
     *
     * @param uid 用户uid
     * @return 被投诉单数 0 未被投诉
     */
    Integer checkUserBeComplained(String uid);
}
