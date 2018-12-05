package com.kaihei.esportingplus.user.api.feign;


import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.user.api.vo.PointDateVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 暴鸡基础服务Feign
 * @zhangfang
 */
@FeignClient(name = "esportingplus-user-service", path = "/baojiinfo", fallbackFactory = BaoJiInfoServiceClientFallbackFactory.class)
public interface BaoJiInfoServiceClient {

    /**
     * 根据uid判断用户是否为暴鸡暴娘身份
     * @param uid
     * @return
     */
    @GetMapping("/getidentity")
    public ResponsePacket<Integer> getIdentityByUid(String uid);

    /**
     * 获取暴鸡中心—鸡分数据
     * @param uid
     * @return
     */
    @GetMapping("/get/baojidate")
    public ResponsePacket<PointDateVo> getUserPointDate(String uid);
}
