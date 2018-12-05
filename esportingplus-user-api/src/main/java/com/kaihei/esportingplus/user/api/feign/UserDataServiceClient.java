package com.kaihei.esportingplus.user.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.user.api.vo.MembersUserPointItemVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 用户数据服务Feign
 * @linruihe
 */
@FeignClient(name = "esportingplus-user-service", path = "/userdata", fallbackFactory = UserDataServiceClientFallbackFactory.class)
public interface UserDataServiceClient {

    @PostMapping("/incr/data")
    ResponsePacket<MembersUserPointItemVo> incrUserFreeData(@RequestParam(value = "acceptList", required = true) List<String> acceptList,
                                                     @RequestParam(value = "placeList", required = true) List<String> placeList);

}
