package com.kaihei.esportingplus.api.vo.freeteam;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ChickenpointUseConfigVO {

    /**
     * 是否支持使用鸡分 1支持 0不支持
     */
    private Integer chickenUseSuport;

    /**
     * 鸡分兑换配置
     */
    private ExchangeRatio exchangeRatio;

    /**
     * 兑换时间配置
     */
    private ExchangeTime exchangeTime;

    /**
     * 最小兑换鸡分
     */
    private Integer minExchangeChickenPoint;

    /**
     * 最大兑换鸡分
     */
    private Integer maxExchangeChickenPoint;


    @Data
    public static class ExchangeRatio {

        /**
         * 使用鸡分
         */
        private Integer chickenPoint;
        /**
         * 可兑换暴击值
         */
        private Float baojiValue;

        /**
         * 比率
         */
        private Float value;


    }

    @Data
    public static class ExchangeTime {

        /**
         * 开始时间
         */
        private ExchangeTimeConfig from;
        /**
         * 结束时间
         */
        private ExchangeTimeConfig to;

        @Data
        public static class ExchangeTimeConfig {

            /**
             * 1234567 1是周日 7是周六
             */
            private Integer week;
            /**
             * 小时
             */
            private Integer hour;
            /**
             * 分钟
             */
            private Integer minute;
        }
    }


}
