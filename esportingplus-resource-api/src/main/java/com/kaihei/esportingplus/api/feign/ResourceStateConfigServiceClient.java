package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.params.freeteam.BannerSaveParams;
import com.kaihei.esportingplus.api.params.freeteam.BannerUpdateParams;
import com.kaihei.esportingplus.api.params.freeteam.ResourceStateSaveParams;
import com.kaihei.esportingplus.api.params.freeteam.ResourceStateUpdateParams;
import com.kaihei.esportingplus.api.vo.freeteam.BannerConfigAppVo;
import com.kaihei.esportingplus.api.vo.freeteam.BannerConfigVo;
import com.kaihei.esportingplus.api.vo.freeteam.BannerDictConfigVo;
import com.kaihei.esportingplus.api.vo.freeteam.ResourceStateConfigVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import java.util.List;
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
@FeignClient(name = "esportingplus-resource-service", path = "/resource/state", fallbackFactory = ResourceStateConfigServiceClientFallbackFactory.class)
public interface ResourceStateConfigServiceClient {

    /**
     * 获取资源位
     * @param
     * @return
     */
    @GetMapping("/list")
    public ResponsePacket<List<ResourceStateConfigVo>> findResourceStateConfig(@RequestParam(value = "user_type", required = false) Integer userType, @RequestParam(value = "position", required = true)String position);


    /**
     * 获取一个资源位
     * @return
     */
    @GetMapping("/id/{resource_id}")
    public ResponsePacket<ResourceStateConfigVo> findResourceStateConfigById(@PathVariable("resource_id") Integer resourceId);

    /**
     * 添加资源位
     * @param resourceStateSaveParams
     */
    @PostMapping("/save")
    public ResponsePacket<Void> saveResourceStateConfig(@RequestBody ResourceStateSaveParams resourceStateSaveParams);
    /**
     * 修改资源位
     * @param resourceStateUpdateParams
     */
    @PutMapping("/update")
    public ResponsePacket<Void> updateResourceStateConfig(@RequestBody ResourceStateUpdateParams resourceStateUpdateParams);

    /**
     * 删除资源位
     * @param id
     */
    @DeleteMapping("/delete")
    public ResponsePacket<Void> deleteResourceStateConfig(@PathVariable("resource_id")Integer resourceId);
}
