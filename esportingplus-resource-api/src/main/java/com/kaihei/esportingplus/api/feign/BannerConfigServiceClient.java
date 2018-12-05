package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.params.freeteam.BannerSaveParams;
import com.kaihei.esportingplus.api.params.freeteam.BannerUpdateParams;
import com.kaihei.esportingplus.api.vo.freeteam.BannerConfigAppVo;
import com.kaihei.esportingplus.api.vo.freeteam.BannerConfigVo;
import com.kaihei.esportingplus.api.vo.freeteam.BannerDictConfigVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhangfang
 */
@FeignClient(name = "esportingplus-resource-service", path = "/banner", fallbackFactory = BannerConfigServiceClientFallbackFactory.class)
public interface BannerConfigServiceClient {

    /**
     * 获取banner位
     */
    @GetMapping("/list")
    public ResponsePacket<PagingResponse<BannerConfigVo>> findBannerConfig(
            @RequestParam(value = "user_type", required = false)Integer userType,
            @RequestParam(value = "position", required = true) String position,
            @RequestParam(value = "offset", required = true) Integer offset,
            @RequestParam(value = "limit", required = true) Integer limit);

    @GetMapping("/{banner_id}")
    public ResponsePacket<BannerConfigVo> findBannerConfigByBannerId(@PathVariable("banner_id") Integer bannerId);

    /**
     * 获取轮播Banner
     */
    @GetMapping("/carousel/{user_type}/list")
    public ResponsePacket<BannerConfigAppVo> findCarouselBannerConfig(
            @RequestHeader("Authorization") String token,
            @PathVariable("user_type")Integer userType,
            @RequestParam(value = "position", required = true) String position);

    /**
     * 添加banner位
     */
    @PostMapping("/save")
    public ResponsePacket<Void> saveBannerConfig(@RequestBody BannerSaveParams params);

    /**
     * 修改banner位
     */
    @PutMapping("/update")
    public ResponsePacket<Void> updateBannerConfig(@RequestBody BannerUpdateParams params);

    /**
     * 删除banner配置
     */
    @DeleteMapping("/delete/{id}")
    public ResponsePacket<Void> deleteBannerConfig(@PathVariable("id") Integer id);

    @GetMapping("/config/dict")
    public ResponsePacket<BannerDictConfigVo> findBannerDictConfigVo();

    @PutMapping("config/dict/update")
    public ResponsePacket<Void> updateBannerDictConfigVo(@RequestBody BannerDictConfigVo vo);
}
