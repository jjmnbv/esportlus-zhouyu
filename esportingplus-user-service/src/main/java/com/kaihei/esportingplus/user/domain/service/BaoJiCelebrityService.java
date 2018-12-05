package com.kaihei.esportingplus.user.domain.service;

import com.kaihei.esportingplus.common.paging.PagingRequest;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.user.api.vo.BaoJiCelebrityVo;

/**
 * @Description: 暴鸡红人信息服务
 * @Author: linruihe
 * @Date: 2018年11月17日
 */
public interface BaoJiCelebrityService {
    /**
     * 根据游戏类型获取游戏列表
     * @param game
     * @return
     */
    public PagingResponse<BaoJiCelebrityVo> getCelebrityList(int game, PagingRequest pagingRequest);
}
