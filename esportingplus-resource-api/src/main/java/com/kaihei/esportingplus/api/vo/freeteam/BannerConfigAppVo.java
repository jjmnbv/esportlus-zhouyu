package com.kaihei.esportingplus.api.vo.freeteam;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class BannerConfigAppVo implements Serializable {

    private static final long serialVersionUID = 3245491116627305129L;
    private Integer seconds;
    private List<BannerConfigVo> banners;

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

    public List<BannerConfigVo> getBanners() {
        return banners;
    }

    public void setBanners(List<BannerConfigVo> banners) {
        this.banners = banners;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
