package com.kaihei.esportingplus.api.vo.freeteam;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class BannerDictConfigVo {
    @NotNull(message = "轮播数量不能为空")
    @Min(value =1,message = "轮播数量必须大于0")
    private Integer count;
    @Min(value =1,message = "轮播时间必须大于0秒")
    private Integer seconds;

}
