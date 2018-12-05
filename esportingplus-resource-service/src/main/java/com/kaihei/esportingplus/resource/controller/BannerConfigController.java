package com.kaihei.esportingplus.resource.controller;

import com.kaihei.esportingplus.api.feign.BannerConfigServiceClient;
import com.kaihei.esportingplus.api.params.freeteam.BannerSaveParams;
import com.kaihei.esportingplus.api.params.freeteam.BannerUpdateParams;
import com.kaihei.esportingplus.api.vo.freeteam.BannerConfigAppVo;
import com.kaihei.esportingplus.api.vo.freeteam.BannerDictConfigVo;
import com.kaihei.esportingplus.api.vo.freeteam.BannerConfigVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingRequest;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.resource.domain.service.freeteam.BannerConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 免费车队配置
 *
 * @author
 */
@RestController
@RequestMapping("/banner")
@Api(tags = {"banner配置接口"})
public class BannerConfigController implements BannerConfigServiceClient {

    @Autowired
    BannerConfigService bannerConfigService;

    /**
     * 获取banner位
     */
    @ApiOperation(value = "查询所有banner")
    @Override
    public ResponsePacket<PagingResponse<BannerConfigVo>> findBannerConfig(
            @ApiParam(value = "用户类型 0:全量 1:老板 2:暴鸡暴娘", required = false)@RequestParam(value = "user_type", required = false) Integer userType,
            @ApiParam(value = "投放位置代码 banner_free_team_home:免费车队首页,banner_baoji_bn_center:暴鸡中心,my_foot:我的底部", required = true)@RequestParam(value = "position", required = true) String position,
            @RequestParam(value = "offset", required = true) Integer offset,
            @RequestParam(value = "limit", required = true) Integer limit) {
        PagingRequest pagingRequest = new PagingRequest();
        pagingRequest.setOffset(offset);
        pagingRequest.setLimit(limit);
        return ResponsePacket
                .onSuccess(bannerConfigService.findBannerConfig(userType, position, pagingRequest));
    }

    /**
     * 获取一个banner位
     */
    @ApiOperation(value = "根据banner_id查询banner")
    @Override
    public ResponsePacket<BannerConfigVo> findBannerConfigByBannerId(
            @PathVariable("banner_id") Integer bannerId) {
        return ResponsePacket.onSuccess(bannerConfigService.findBannerConfigById(bannerId));
    }

    /**
     * 获取轮播Banner
     */
    @ApiOperation(value = "查询可以轮播的banner")
    @Override
    public ResponsePacket<BannerConfigAppVo> findCarouselBannerConfig(
            @RequestHeader("Authorization") String token,
            @ApiParam(value = "用户类型 0:全量 1:老板 2:暴鸡暴娘", required = true) @PathVariable("user_type") Integer userType,
            @ApiParam(value = "投放位置代码 banner_free_team_home:免费车队首页,banner_baoji_bn_center:暴鸡中心,my_foot:我的底部", required = true) @RequestParam(value = "position", required = false) String position) {
        UserSessionContext.getUser();
        BannerConfigAppVo vo = new BannerConfigAppVo();
        vo.setSeconds(bannerConfigService.getBannerDictConfigVo().getSeconds());
        vo.setBanners(bannerConfigService.findCarouselBannerConfig(userType, position));
        return ResponsePacket.onSuccess(vo);
    }

    /**
     * 添加banner位
     */
    @ApiOperation(value = "新增一个banner")
    @Override
    public ResponsePacket<Void> saveBannerConfig(@RequestBody BannerSaveParams params) {
        bannerConfigService.saveBannerConfig(params);
        return ResponsePacket.onSuccess();
    }

    /**
     * 修改banner位
     */
    @ApiOperation(value = "修改一个banner")
    @Override
    public ResponsePacket<Void> updateBannerConfig(@RequestBody BannerUpdateParams params) {
        bannerConfigService.updateBannerConfig(params);
        return ResponsePacket.onSuccess();
    }

    /**
     * 删除banner配置
     */
    @ApiOperation(value = "删除一个banner")
    @Override
    public ResponsePacket<Void> deleteBannerConfig(@PathVariable("id") Integer id) {
        bannerConfigService.deleteBannerConfig(id);
        return ResponsePacket.onSuccess();
    }

    /**
     * 获取banner字典配置
     */
    @ApiOperation(value = "获取banner字典配置")
    @Override
    public ResponsePacket<BannerDictConfigVo> findBannerDictConfigVo() {
        return ResponsePacket
                .onSuccess(bannerConfigService.getBannerDictConfigVo());
    }

    /**
     * 修改banner字典配置
     */
    @ApiOperation(value = "修改banner字典配置")
    @Override
    public ResponsePacket<Void> updateBannerDictConfigVo(@RequestBody BannerDictConfigVo vo) {
        bannerConfigService.updateBannerDictConfigVo(vo);
        return ResponsePacket.onSuccess();
    }
}
