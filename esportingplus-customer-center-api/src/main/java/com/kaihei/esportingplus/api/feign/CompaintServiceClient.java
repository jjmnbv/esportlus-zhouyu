package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.params.ComplainCreateParam;
import com.kaihei.esportingplus.api.params.ComplaintQueryParam;
import com.kaihei.esportingplus.api.params.StudioComplainQueryParams;
import com.kaihei.esportingplus.api.vo.ComplaintDetailVo;
import com.kaihei.esportingplus.api.vo.ComplaintListVo;
import com.kaihei.esportingplus.api.vo.StudioComplaintListVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 基于feign实现远程客服服务接口调用<br/> 1. esportingplus-customer-center-service为服务名 <br/> 2.
 * fallbackFactory指定断路器实现类<br/>
 */
@FeignClient(name = "esportingplus-customer-center-service", path = "/complaints", fallbackFactory = CompaintClientFallbackFactory.class)
public interface CompaintServiceClient {

    /**
     *@Description:创建投诉单
    */
    @PostMapping("/create")
    ResponsePacket<Void> createComplaint(@RequestHeader(name = "Authorization") String token, @RequestBody ComplainCreateParam complainCreateParam);

    /**
     * @Description:分页查询投诉单
     */
    @PostMapping("/list")
    ResponsePacket<PagingResponse<ComplaintListVo>> listComplaint(
            @RequestBody ComplaintQueryParam complainQueryParam);

    /**
     * @Description:获取投诉单详情
     */
    @GetMapping("compaint/{oid}")
    ResponsePacket<ComplaintDetailVo> getComplaintDetail(@PathVariable(name = "oid") Integer oid);

    /**
     * @Description:分页查询投诉单(工作室)
     */
    @PostMapping("/studiolist")
    ResponsePacket<PagingResponse<StudioComplaintListVo>> studiolist(@RequestBody StudioComplainQueryParams studioComplainQueryParams);

    /**
     * @Description:确认被投诉订单（工作室）
     */
    @PostMapping("/checkOrderBeComplainted")
    ResponsePacket<List<String>> checkOrderBeComplainted(
            @RequestParam("orderSequeues") List<String> orderSequeues);

    /**
     * 根据uid统计 用户是否被投诉过
     *
     * @param uid 用户uid
     * @return 被投诉单数 0 未被投诉
     */
    @GetMapping("beComplained/{uid}")
    ResponsePacket<Integer> checkUserBeComplained(@PathVariable("uid") String uid);
}
