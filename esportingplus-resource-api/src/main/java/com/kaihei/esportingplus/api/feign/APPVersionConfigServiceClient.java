package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.vo.APPVersionConfigVO;
import com.kaihei.esportingplus.api.vo.APPVersionDetailConfigVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <pre>
 *  基于feign实现远程车队服务接口调用
 *  1. esportingplus-resource-service为服务名
 *  2. fallbackFactory指定断路器实现类<br/>
 *  -- 客户端版本信息配置 api
 * </pre>
 * @author liangyi
 */
@FeignClient(name = "esportingplus-resource-service",
        path = "/appversion", fallbackFactory = APPVersionConfigClientFallbackFactory.class)
public interface APPVersionConfigServiceClient {

    /**
     * 获取最新的版本信息
     * @param x 客户端请求标识, 平台_版本号_包名 x=i_280_kh
     * @return
     */
    @GetMapping("/latest/{x}")
    ResponsePacket<APPVersionConfigVO> getLatestVersion(@PathVariable("x") String x);

    /**
     * 获取版本信息
     * @return
     */
    @GetMapping("/backend/list")
    ResponsePacket<List<APPVersionDetailConfigVO>> getVersionList();

    /**
     * 新增或修改版本信息
     * @param appVersionDetailConfigVO
     * @return
     */
    @PostMapping("/backend/addOrUpdate")
    ResponsePacket<Void> addOrUpdateVersion(
            @RequestBody APPVersionDetailConfigVO appVersionDetailConfigVO);

    /**
     * 根据 id查询版本信息
     * @param id
     * @return
     */
    @GetMapping("/backend/{id}")
    ResponsePacket<APPVersionConfigVO> getVersionById(@PathVariable("id") Integer id);

    /**
     * 根据 id删除版本信息
     * @param id
     * @return
     */
    @GetMapping("/backend/delete/{id}")
    ResponsePacket<Void> deleteVersionById(@PathVariable("id") Integer id);
}
