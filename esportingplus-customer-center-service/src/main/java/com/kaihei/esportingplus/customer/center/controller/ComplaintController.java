package com.kaihei.esportingplus.customer.center.controller;

import com.kaihei.esportingplus.api.feign.CompaintServiceClient;
import com.kaihei.esportingplus.api.params.ComplainCreateParam;
import com.kaihei.esportingplus.api.params.ComplaintQueryParam;
import com.kaihei.esportingplus.api.params.StudioComplainQueryParams;
import com.kaihei.esportingplus.api.vo.ComplaintDetailVo;
import com.kaihei.esportingplus.api.vo.ComplaintListVo;
import com.kaihei.esportingplus.api.vo.StudioComplaintListVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.customer.center.domain.service.ICustomerCenterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单投诉服务接口
 * @author zhouhui
 */
@RestController
@RequestMapping("/complaints")
@Api(tags = {"订单投诉服务接口"})
public class ComplaintController implements CompaintServiceClient {

    @Autowired
    private ICustomerCenterService customerCenterService;

    @PostMapping("/create")
    @ApiOperation(value = "创建投诉单")
    @Override
    public ResponsePacket<Void> createComplaint(@RequestHeader(name = "Authorization") String token, @RequestBody ComplainCreateParam complainCreateParam) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, complainCreateParam);
        customerCenterService.createComplaint(complainCreateParam);
        return ResponsePacket.onSuccess();
    }

    /**
     * 测试 {@link com.kaihei.esportingplus.customer.center.controller.ComplaintControllerTest#listComplaintTest()}
     */
    @PostMapping("/list")
    @ApiOperation(value = "分页查询投诉单列表")
    @Override
    public ResponsePacket<PagingResponse<ComplaintListVo>> listComplaint(@RequestBody ComplaintQueryParam complainQueryParam) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, complainQueryParam);
        return ResponsePacket.onSuccess(customerCenterService.listComplaint(complainQueryParam));
    }

    @PostMapping("/studiolist")
    @ApiOperation(value = "分页查询投诉单列表（工作室）")
    @Override
    public ResponsePacket<PagingResponse<StudioComplaintListVo>> studiolist(
            @RequestBody StudioComplainQueryParams studioComplainQueryParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, studioComplainQueryParams);
        return ResponsePacket.onSuccess(customerCenterService.studiolist(studioComplainQueryParams));
    }

    /**
     *
     */
    @ApiOperation(value = "获取投诉单详情")
    @Override
    public ResponsePacket<ComplaintDetailVo> getComplaintDetail(
            @PathVariable Integer oid) {
        return ResponsePacket.onSuccess(customerCenterService.getComplaintDetail(oid));
    }


    @ApiOperation("确认被投诉订单")
    @Override
    public ResponsePacket<List<String>> checkOrderBeComplainted(
            @RequestParam List<String> orderSequeues) {
        return ResponsePacket
                .onSuccess(customerCenterService.checkOrderBeComplainted(
                        orderSequeues));
    }

    /**
     * 根据uid统计 用户是否被投诉过
     *
     * @param uid 用户uid
     * @return 被投诉单数 0 未被投诉
     */
    @Override
    public ResponsePacket<Integer> checkUserBeComplained(@PathVariable String uid) {
        return ResponsePacket.onSuccess(customerCenterService.checkUserBeComplained(uid));
    }
}
