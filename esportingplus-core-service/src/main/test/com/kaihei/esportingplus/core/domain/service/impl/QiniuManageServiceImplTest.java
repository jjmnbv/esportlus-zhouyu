package com.kaihei.esportingplus.core.domain.service.impl;

import com.kaihei.commons.cache.utils.JsonsUtils;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.core.BaseTest;
import com.kaihei.esportingplus.core.api.vo.QiniuImageCheckVo;
import com.kaihei.esportingplus.core.api.vo.QiniuTokenVo;
import com.kaihei.esportingplus.core.domain.service.QiniuManageService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class QiniuManageServiceImplTest extends BaseTest {
    @Autowired
    private QiniuManageService qiniuManageService;

    @Test
    public void getTokenByTokenType() {
        QiniuTokenVo qiniuTokenVo = qiniuManageService.getTokenByTokenType("thumbnail_uploadtoken",
                null, null, null);
        logger.debug("qiniuTokenVo .{}", FastJsonUtils.toJson(qiniuTokenVo));
        Assert.assertNotNull(qiniuTokenVo);
    }

    @Test
    public void checkImage() {
        QiniuImageCheckVo vo = qiniuManageService.checkQpulpImage("https://qn-bn-pub.kaiheikeji.com/album-4a1aa763c4e8af19a443f58d75c75bc1-tj6hOASn.png");
        System.out.println(JsonsUtils.toJson(vo));
    }
}