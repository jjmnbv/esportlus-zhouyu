package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.vo.freeteam.BannerConfigVo;
import com.kaihei.esportingplus.api.vo.freeteam.ChickenpointTaskConfigVo;
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
@FeignClient(name = "esportingplus-resource-service", path = "/chickenpoint", fallbackFactory = ChickenpointConfigServiceClientFallbackFactory.class)
public interface ChickenpointConfigServiceClient {

    /**
     * 新增鸡分任务
     */
    @PostMapping("/task/config/save")
    public ResponsePacket<Void> saveChickpointTaskConfig(@RequestBody ChickenpointTaskConfigVo vo);

    /**
     * 修改鸡分任务
     */
    @PutMapping("/task/config/update")
    public ResponsePacket<Void> updateChickpointTaskConfig(
            @RequestBody ChickenpointTaskConfigVo vo);

    /**
     * 获取鸡分任务列表
     */
    @GetMapping("/task/config/list")
    public ResponsePacket<List<ChickenpointTaskConfigVo>> findChickpointTaskConfig();

    /**
     * APP调用-获取鸡分任务列表
     */
    @GetMapping("/task/config/app/list")
    public ResponsePacket<List<ChickenpointTaskConfigVo>> findChickpointTaskConfigForApp(
            @RequestHeader("Authorization") String token);

    /**
     * 删除鸡分任务
     */
    @DeleteMapping("/delete/{id}")
    public ResponsePacket<Void> deleteChickpointTaskConfig(@PathVariable("id") Long id);
}
