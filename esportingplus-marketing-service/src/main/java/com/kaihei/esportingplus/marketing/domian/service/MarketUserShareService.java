package com.kaihei.esportingplus.marketing.domian.service;

import com.kaihei.esportingplus.marketing.api.vo.ShareVo;

/**
 * @Auther: linruihe
 * @Date: 2018-12-03 14:38
 * @Description:
 */
public interface MarketUserShareService {

    ShareVo getShareByType(String uid, String shareuid, String type);
}
