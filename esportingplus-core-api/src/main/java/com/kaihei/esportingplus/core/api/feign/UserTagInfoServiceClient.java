package com.kaihei.esportingplus.core.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.core.api.params.UserTagInfoParam;
import com.kaihei.esportingplus.core.api.vo.PageInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @program: esportingplus
 * @description: 推送消息
 * @author: xusisi
 * @create: 2018-12-04 15:54
 **/
@FeignClient(name = "esportingplus-core-service", path = "/user_tag", fallbackFactory = UserTagInfoServiceClientFallback.class)
public interface UserTagInfoServiceClient {

    @PostMapping("/tag")
    public ResponsePacket createTagInfo(@RequestBody UserTagInfoParam userTagInfoParam);

    @PutMapping("/tag")
    public ResponsePacket updateTagInfo(@RequestBody UserTagInfoParam userTagInfoParam);

    @GetMapping("/list")
    public ResponsePacket<PageInfo> getRecords(@RequestParam(value = "page", required = true) Integer page,
                                               @RequestParam(value = "size", required = true) Integer size);

    @GetMapping("/tag")
    public ResponsePacket checkTagNameIsExist(@RequestParam(value = "tagName", required = true) String tagName);

    @GetMapping("/tag/{tagId}")
    public ResponsePacket getTagInfo(@PathVariable Integer tagId);
}
