package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamAdvertiseHomeVo;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamHomeVo;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamScrollTemplateHomeVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "esportingplus-resource-service",
        path = "/freeteam/home", fallbackFactory = FreeTeamHomeClientFallbackFactory.class)
public interface FreeTeamHomeServiceClient {

    /**
     * 获取所有机型首页宣传图
     * @return
     */
    @GetMapping("/advertise/list")
    public ResponsePacket<List<FreeTeamAdvertiseHomeVo>> findAllFreeTeamAdvertise();
    /**
     * 新增首页宣传图
     * @return
     */
    @PostMapping("/advertise/save")
    public ResponsePacket<Void> saveFreeTeamAdvertiseHome(@RequestBody FreeTeamAdvertiseHomeVo freeTeamAdvertiseHomeVo);

    /**
     * 修改首页宣传图
     * @param freeTeamAdvertiseHomeVo
     * @return
     */
    @PutMapping("/advertise/update")
    public ResponsePacket<Void> updateFreeTeamAdvertiseHome(@RequestBody FreeTeamAdvertiseHomeVo freeTeamAdvertiseHomeVo);

    /**
     * 保存
     * @param freeTeamScrollTemplateHomeVo
     * @return
     */
    @PostMapping("/scroll/template/save")
    public ResponsePacket<Void> saveFreeTeamScrollTemplate(@RequestBody FreeTeamScrollTemplateHomeVo freeTeamScrollTemplateHomeVo);

    /**
     * 修改滚动模板
     * @param freeTeamScrollTemplateHomeVo
     * @return
     */
    @PutMapping("/scroll/template/update")
    public ResponsePacket<Void> updateFreeTeamScrollTemplate(@RequestBody FreeTeamScrollTemplateHomeVo freeTeamScrollTemplateHomeVo);

    /**
     * 删除滚动模板
     */
    @DeleteMapping("/scroll/template/{id}/delete")
    public ResponsePacket<Void> deleteFreeTeamScrollTemplate(@PathVariable("id") Integer id);

    /**
     * APP调用-根据机器类型获取宣传图
     */
    @GetMapping("/advertise/app/machine/{machine_type}")
    public ResponsePacket<FreeTeamAdvertiseHomeVo> findFreeTeamAdvertiseForApp(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable("machine_type")Integer machineType);

    /**
     * 内部调用-根据机器类型获取宣传图
     */
    @GetMapping("/advertise/machine/{machine_type}")
    public ResponsePacket<FreeTeamAdvertiseHomeVo> findFreeTeamAdvertise(
            @PathVariable("machine_type")Integer machineType);
}
