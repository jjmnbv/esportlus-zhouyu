package com.kaihei.esportingplus.resource.domain.entity.freeteam;

import java.util.Date;
import javax.persistence.*;

@Table(name = "free_team_chickenpoint_exchange_baoji")
public class FreeTeamChickenPointExchangeBaoji {
    /**
     * 免费车队使用鸡分配置-主键ID
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 是否支持使用鸡分: 0=否 1=是
     */
    @Column(name = "use_chikenpoint")
    private Integer useChikenpoint;

    /**
     * 兑换比例：使用鸡分
     */
    @Column(name = "exchange_rate_chikenpoint")
    private Integer exchangeRateChikenpoint;

    /**
     * 兑换比例：可兑换暴击值
     */
    @Column(name = "exchange_rate_baoji_value")
    private String exchangeRateBaojiValue;

    /**
     * 兑换开始周id(来源dictionary)
     */
    @Column(name = "exchange_start_week_id")
    private Integer exchangeStartWeekId;

    /**
     * 兑换开始周的值(来源dictionary)
     */
    @Column(name = "exchange_start_week_value")
    private String exchangeStartWeekValue;

    /**
     * 兑换开始时间
     */
    @Column(name = "exchange_starttime")
    private Date exchangeStarttime;

    /**
     * 兑换结束周id(来源dictionary)
     */
    @Column(name = "exchange_end_week_id")
    private Integer exchangeEndWeekId;

    /**
     * 兑换结束周的值(来源dictionary)
     */
    @Column(name = "exchange_end_week_value")
    private String exchangeEndWeekValue;

    /**
     * 兑换结束时间
     */
    @Column(name = "exchange_endtime")
    private Date exchangeEndtime;

    /**
     * 单次可兑换的最低鸡分单位
     */
    @Column(name = "single_lowest_point")
    private Integer singleLowestPoint;

    /**
     * 单次可兑换的最高鸡分单位
     */
    @Column(name = "single_highest_point")
    private Integer singleHighestPoint;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    private Date gmtModified;

    public FreeTeamChickenPointExchangeBaoji(Integer id, Integer useChikenpoint, Integer exchangeRateChikenpoint, String exchangeRateBaojiValue, Integer exchangeStartWeekId, String exchangeStartWeekValue, Date exchangeStarttime, Integer exchangeEndWeekId, String exchangeEndWeekValue, Date exchangeEndtime, Integer singleLowestPoint, Integer singleHighestPoint, Date gmtCreate, Date gmtModified) {
        this.id = id;
        this.useChikenpoint = useChikenpoint;
        this.exchangeRateChikenpoint = exchangeRateChikenpoint;
        this.exchangeRateBaojiValue = exchangeRateBaojiValue;
        this.exchangeStartWeekId = exchangeStartWeekId;
        this.exchangeStartWeekValue = exchangeStartWeekValue;
        this.exchangeStarttime = exchangeStarttime;
        this.exchangeEndWeekId = exchangeEndWeekId;
        this.exchangeEndWeekValue = exchangeEndWeekValue;
        this.exchangeEndtime = exchangeEndtime;
        this.singleLowestPoint = singleLowestPoint;
        this.singleHighestPoint = singleHighestPoint;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public FreeTeamChickenPointExchangeBaoji() {
        super();
    }

    /**
     * 获取免费车队使用鸡分配置-主键ID
     *
     * @return id - 免费车队使用鸡分配置-主键ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置免费车队使用鸡分配置-主键ID
     *
     * @param id 免费车队使用鸡分配置-主键ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取是否支持使用鸡分: 0=否 1=是
     *
     * @return use_chikenpoint - 是否支持使用鸡分: 0=否 1=是
     */
    public Integer getUseChikenpoint() {
        return useChikenpoint;
    }

    /**
     * 设置是否支持使用鸡分: 0=否 1=是
     *
     * @param useChikenpoint 是否支持使用鸡分: 0=否 1=是
     */
    public void setUseChikenpoint(Integer useChikenpoint) {
        this.useChikenpoint = useChikenpoint;
    }

    /**
     * 获取兑换比例：使用鸡分
     *
     * @return exchange_rate_chikenpoint - 兑换比例：使用鸡分
     */
    public Integer getExchangeRateChikenpoint() {
        return exchangeRateChikenpoint;
    }

    /**
     * 设置兑换比例：使用鸡分
     *
     * @param exchangeRateChikenpoint 兑换比例：使用鸡分
     */
    public void setExchangeRateChikenpoint(Integer exchangeRateChikenpoint) {
        this.exchangeRateChikenpoint = exchangeRateChikenpoint;
    }

    /**
     * 获取兑换比例：可兑换暴击值
     *
     * @return exchange_rate_baoji_value - 兑换比例：可兑换暴击值
     */
    public String getExchangeRateBaojiValue() {
        return exchangeRateBaojiValue;
    }

    /**
     * 设置兑换比例：可兑换暴击值
     *
     * @param exchangeRateBaojiValue 兑换比例：可兑换暴击值
     */
    public void setExchangeRateBaojiValue(String exchangeRateBaojiValue) {
        this.exchangeRateBaojiValue = exchangeRateBaojiValue == null ? null : exchangeRateBaojiValue.trim();
    }

    /**
     * 获取兑换开始周id(来源dictionary)
     *
     * @return exchange_start_week_id - 兑换开始周id(来源dictionary)
     */
    public Integer getExchangeStartWeekId() {
        return exchangeStartWeekId;
    }

    /**
     * 设置兑换开始周id(来源dictionary)
     *
     * @param exchangeStartWeekId 兑换开始周id(来源dictionary)
     */
    public void setExchangeStartWeekId(Integer exchangeStartWeekId) {
        this.exchangeStartWeekId = exchangeStartWeekId;
    }

    /**
     * 获取兑换开始周的值(来源dictionary)
     *
     * @return exchange_start_week_value - 兑换开始周的值(来源dictionary)
     */
    public String getExchangeStartWeekValue() {
        return exchangeStartWeekValue;
    }

    /**
     * 设置兑换开始周的值(来源dictionary)
     *
     * @param exchangeStartWeekValue 兑换开始周的值(来源dictionary)
     */
    public void setExchangeStartWeekValue(String exchangeStartWeekValue) {
        this.exchangeStartWeekValue = exchangeStartWeekValue == null ? null : exchangeStartWeekValue.trim();
    }

    /**
     * 获取兑换开始时间
     *
     * @return exchange_starttime - 兑换开始时间
     */
    public Date getExchangeStarttime() {
        return exchangeStarttime;
    }

    /**
     * 设置兑换开始时间
     *
     * @param exchangeStarttime 兑换开始时间
     */
    public void setExchangeStarttime(Date exchangeStarttime) {
        this.exchangeStarttime = exchangeStarttime;
    }

    /**
     * 获取兑换结束周id(来源dictionary)
     *
     * @return exchange_end_week_id - 兑换结束周id(来源dictionary)
     */
    public Integer getExchangeEndWeekId() {
        return exchangeEndWeekId;
    }

    /**
     * 设置兑换结束周id(来源dictionary)
     *
     * @param exchangeEndWeekId 兑换结束周id(来源dictionary)
     */
    public void setExchangeEndWeekId(Integer exchangeEndWeekId) {
        this.exchangeEndWeekId = exchangeEndWeekId;
    }

    /**
     * 获取兑换结束周的值(来源dictionary)
     *
     * @return exchange_end_week_value - 兑换结束周的值(来源dictionary)
     */
    public String getExchangeEndWeekValue() {
        return exchangeEndWeekValue;
    }

    /**
     * 设置兑换结束周的值(来源dictionary)
     *
     * @param exchangeEndWeekValue 兑换结束周的值(来源dictionary)
     */
    public void setExchangeEndWeekValue(String exchangeEndWeekValue) {
        this.exchangeEndWeekValue = exchangeEndWeekValue == null ? null : exchangeEndWeekValue.trim();
    }

    /**
     * 获取兑换结束时间
     *
     * @return exchange_endtime - 兑换结束时间
     */
    public Date getExchangeEndtime() {
        return exchangeEndtime;
    }

    /**
     * 设置兑换结束时间
     *
     * @param exchangeEndtime 兑换结束时间
     */
    public void setExchangeEndtime(Date exchangeEndtime) {
        this.exchangeEndtime = exchangeEndtime;
    }

    /**
     * 获取单次可兑换的最低鸡分单位
     *
     * @return single_lowest_point - 单次可兑换的最低鸡分单位
     */
    public Integer getSingleLowestPoint() {
        return singleLowestPoint;
    }

    /**
     * 设置单次可兑换的最低鸡分单位
     *
     * @param singleLowestPoint 单次可兑换的最低鸡分单位
     */
    public void setSingleLowestPoint(Integer singleLowestPoint) {
        this.singleLowestPoint = singleLowestPoint;
    }

    /**
     * 获取单次可兑换的最高鸡分单位
     *
     * @return single_highest_point - 单次可兑换的最高鸡分单位
     */
    public Integer getSingleHighestPoint() {
        return singleHighestPoint;
    }

    /**
     * 设置单次可兑换的最高鸡分单位
     *
     * @param singleHighestPoint 单次可兑换的最高鸡分单位
     */
    public void setSingleHighestPoint(Integer singleHighestPoint) {
        this.singleHighestPoint = singleHighestPoint;
    }

    /**
     * 获取创建时间
     *
     * @return gmt_create - 创建时间
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * 设置创建时间
     *
     * @param gmtCreate 创建时间
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * 获取修改时间
     *
     * @return gmt_modified - 修改时间
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * 设置修改时间
     *
     * @param gmtModified 修改时间
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}