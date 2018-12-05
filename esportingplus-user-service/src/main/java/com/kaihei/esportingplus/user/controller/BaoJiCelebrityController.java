package com.kaihei.esportingplus.user.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingRequest;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.user.api.feign.BaoJiCelebrityServiceClient;
import com.kaihei.esportingplus.user.api.vo.*;
import com.kaihei.esportingplus.user.domain.service.BaoJiCelebrityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/baojicelebrity")
@Api(tags = {"暴鸡红人服务接口"})
public class BaoJiCelebrityController implements BaoJiCelebrityServiceClient {

    @Autowired
    private BaoJiCelebrityService baoJiCelebrityService;

    @ApiOperation(value = "根据游戏类型获取红人列表")
    @Override
    public ResponsePacket<PagingResponse<BaoJiCelebrityVo>> getCelebrityList(@RequestParam(value = "game", defaultValue = "0")Integer game,
                                                                             @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                                                             @RequestParam(value = "limit", defaultValue = "10")Integer limit) {
        game = game == null ? 0 : game;
        PagingRequest pagingRequest = new PagingRequest();
        pagingRequest.setOffset(offset);
        pagingRequest.setLimit(limit);
        PagingResponse<BaoJiCelebrityVo> baoJiCelebrityVoPagingResponse = baoJiCelebrityService.getCelebrityList(game,pagingRequest);
        return ResponsePacket.onSuccess(baoJiCelebrityVoPagingResponse);
    }
}
