package com.kaihei.esportingplus.user.domain.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.config.RonyunUserIdGenerator;
import com.kaihei.esportingplus.common.paging.PagingRequest;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.user.api.vo.BaoJiCelebrityVo;
import com.kaihei.esportingplus.user.data.pyrepository.BaoJiCelebrityRepository;
import com.kaihei.esportingplus.user.domain.service.BaoJiCelebrityService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author linruihe
 * @Title: BaoJiCelebrityService
 * @Description: 暴鸡红人服务
 * @date 2018/11/17 15:31
 */
@Service
public class BaoJiCelebrityServiceImpl implements BaoJiCelebrityService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BaoJiCelebrityRepository baoJiCelebrityRepository;

    @Autowired
    private RonyunUserIdGenerator ronyunUserIdGenerator;

    @Override
    public PagingResponse<BaoJiCelebrityVo> getCelebrityList(int game, PagingRequest pagingRequest) {

        //开启分页插件
        Page<BaoJiCelebrityVo> page = PageHelper
                .startPage(pagingRequest.getOffset(), pagingRequest.getLimit())
                .doSelectPage(() -> baoJiCelebrityRepository.selectCelebrityList(game));

        List<BaoJiCelebrityVo> list = BeanMapper.map(page.getResult(), BaoJiCelebrityVo.class);
        PagingResponse<BaoJiCelebrityVo> pagingResponse = new PagingResponse<>(
                page.getPageNum(), page.getPageSize(),
                page.getTotal(), list);
        logger.info("cmd=getCelebrityList.selectCelebrityList | game={} | baoJiCelebrityVoList={}",
                game, JSONObject.toJSON(pagingResponse));
        List<BaoJiCelebrityVo> baoJiCelebrityVoList = pagingResponse.getList();
        for (BaoJiCelebrityVo baoJiCelebrityVo: baoJiCelebrityVoList) {
            String imId = ronyunUserIdGenerator.encodeIMUser(baoJiCelebrityVo.getUid());
            baoJiCelebrityVo.setImId(imId);
        }
        return pagingResponse;
    }
}
