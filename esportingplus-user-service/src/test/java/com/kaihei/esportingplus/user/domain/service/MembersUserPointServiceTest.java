package com.kaihei.esportingplus.user.domain.service;

import com.kaihei.esportingplus.common.paging.PagingRequest;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.user.BaseTest;
import com.kaihei.esportingplus.user.api.enums.UserPointItemType;
import com.kaihei.esportingplus.user.api.vo.UserPointItemsQueryVo;
import com.kaihei.esportingplus.user.api.vo.UserPointQueryVo;
import com.kaihei.esportingplus.user.domain.entity.MembersUserPointItem;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TODO 功能描述
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/9 15:57
 */
public class MembersUserPointServiceTest extends BaseTest {

    @Autowired
    private MembersUserPointService membersUserPointService;

    @Test
    public void testIncrPoint() {
        MembersUserPointItem item = membersUserPointService
                .incrPoint(11110, -500, UserPointItemType.EXCHANGE.getCode(),
                        1,"");
        Assert.assertNotNull(item);
        Assert.assertNotNull(item.getId());
    }

    @Test
    public void testQueryUserPoint() {
        UserPointQueryVo userPointQueryVo = membersUserPointService.queryUserPoint("11110");
        logger.debug("testQueryUserPoint .{}", FastJsonUtils.toJson(userPointQueryVo));
        Assert.assertNotNull(userPointQueryVo);
        Assert.assertNotNull(userPointQueryVo.getPointAmount());
        Assert.assertTrue(userPointQueryVo.getPointAmount().intValue() > 0);
    }

    @Test
    public void testListUserPorintItems() {
        PagingResponse<UserPointItemsQueryVo> pagingResponse = membersUserPointService
                .listUserPointItems("538", new PagingRequest(1, 10));
        logger.debug("testListUserPorintItems .{}", FastJsonUtils.toJson(pagingResponse));
        Assert.assertNotNull(pagingResponse);
    }

    @Test
    public void getUserAccumulatePoint(){
        membersUserPointService.getUserAccumulatePoint("1");
    }
}
