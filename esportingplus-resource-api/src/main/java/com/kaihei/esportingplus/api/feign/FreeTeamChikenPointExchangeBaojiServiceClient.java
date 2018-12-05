package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamChickenPointUseVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *@Description: 免费车队-使用鸡分配置-兑换暴击值controller
 *
 *@author  Orochi-Yzh
 *@dateTime  2018/10/8 15:27
*/
@FeignClient(name = "esportingplus-resource-service",
        path = "/freeteam/chikenPoint", fallbackFactory = FreeTeamChikenPointExchangeBaojiClientFallbackFactory.class)
public interface FreeTeamChikenPointExchangeBaojiServiceClient {

    /**
     * 新增免费车队-使用鸡分配置-兑换暴击值
     * @param freeTeamChickenPointUseVO
     * @return
     */
    @PostMapping("/exchangeBaoji/add")
    ResponsePacket<Void> addFreeTeamChikenPointUse(
            @RequestBody FreeTeamChickenPointUseVO freeTeamChickenPointUseVO);

    /**
     * 修改免费车队-使用鸡分配置-兑换暴击值
     * @param freeTeamChickenPointUseVO
     * @return
     */
    @PostMapping("/exchangeBaoji/update")
    ResponsePacket<Void> updateFreeTeamChikenPointUse(
            @RequestBody FreeTeamChickenPointUseVO freeTeamChickenPointUseVO);

    /**
     * 根据ID删除免费车队-使用鸡分配置-兑换暴击值
     * @param id
     * @return
     */
    @DeleteMapping("/exchangeBaoji/{id}")
    ResponsePacket<Void> updateFreeTeamChikenPointUse(@PathVariable("id") Long id);

    /**
     * 根据ID查询免费车队-使用鸡分配置-兑换暴击值
     * @param id  使用鸡分配置id
     * @return
     */
    @GetMapping("/exchangeBaoji/{id}")
    ResponsePacket<FreeTeamChickenPointUseVO> getFreeTeamChikenPointUse(@PathVariable("id") Long id);

    /**
     * 根据兑换类型查询免费车队-使用鸡分配置-兑换暴击值
     * @param exchange_type 积分兑换类型：
     * 1=兑换暴击值
     * 2=兑换滴滴接单资格
     * 3=兑换开车资格
     * 4=兑换推荐位
     * @return
     */
    @GetMapping("/exchangeBaoji/{exchange_type}")
    ResponsePacket<FreeTeamChickenPointUseVO> getFreeTeamChikenPointUse(Integer exchange_type);


}
