package com.kaihei.esportingplus.user.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.user.api.vo.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 用户基础服务Feign
 * @zhangfang
 */
@FeignClient(name = "esportingplus-user-service", path = "/baojicelebrity", fallbackFactory = BaoJiCelebrityServiceClientFallbackFactory.class)
public interface BaoJiCelebrityServiceClient {

    /**
     * 根据游戏类型获取红人列表
     * @param game
     * @return
     */
    @GetMapping("/celebritylist")
    public ResponsePacket<PagingResponse<BaoJiCelebrityVo>> getCelebrityList(Integer game, Integer offset, Integer limit);
}
