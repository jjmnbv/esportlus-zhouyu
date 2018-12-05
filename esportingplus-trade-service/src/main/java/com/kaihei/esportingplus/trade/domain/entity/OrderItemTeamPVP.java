package com.kaihei.esportingplus.trade.domain.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "trade_order_item_team_pvp")
public class OrderItemTeamPVP {
    /**
     * 付费车队队员订单详情主键ID
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 订单用户UID
     */
    private String uid;

    /**
     * 所属订单ID
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 车队序列号
     */
    @Column(name = "team_sequeue")
    private String teamSequeue;

    /**
     * 车队状态，0:未开车，1:已开车
     */
    @Column(name = "team_status")
    private Byte teamStatus;
    /**
     * 用户鸡牌号
     */
    @Column(name = "user_chicken_id")
    private String userChickenId;
    /**
     * 用户昵称
     */
    @Column(name = "user_nickname")
    private String userNickname;
    /**
     * 队员身份，0：老板，1：暴鸡，2：暴娘，3：队长
     */
    @Column(name = "user_identity")
    private Byte userIdentity;

    /**
     * 暴鸡等级 100: 普通暴鸡 101: 优选暴鸡 102: 超级暴鸡 300: 暴娘
     */
    @Column(name = "user_baoji_level")
    private Integer userBaojiLevel;

    /**
     * 车队队员订单状态(0:已支付,未开车,主动退出(老板); 1:已支付,未开车,被踢出(老板); 2:已支付,未开车,队长解散车队(老板); 3:已开车,主动退出(暴鸡和老板); 4:已开车,队长解散车队(所有队员); 5:车队正常结束(所有队员); -1:未知状态,忽略处理）
     */
    @Column(name = "user_status")
    private Byte userStatus;

    /**
     * 游戏ID
     */
    @Column(name = "game_id")
    private Integer gameId;

    /**
     * 游戏名称
     */
    @Column(name = "game_name")
    private String gameName;

    /**
     * 游戏大区ID
     */
    @Column(name = "game_zone_id")
    private Integer gameZoneId;

    /**
     * 游戏大区名称
     */
    @Column(name = "game_zone_name")
    private String gameZoneName;

    /**
     * 段位ID
     */
    @Column(name = "game_dan_id")
    private Integer gameDanId;

    /**
     * 段位名称
     */
    @Column(name = "game_dan_name")
    private String gameDanName;
    /**
     * 玩法模式, 0:上分; 1:陪玩;
     */
    @Column(name = "play_mode")
    private Byte playMode;

    /**
     * 结算类型, 1:局; 2:小时
     */
    @Column(name = "settlement_type")
    private Byte settlementType;

    /**
     * 结算数量(保留1位有效数字)
     */
    @Column(name = "settlement_number")
    private BigDecimal settlementNumber;
    /**
     * 暴鸡收益或老板实际支付金额, 单位:分
     */
    private Integer price;

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

    public OrderItemTeamPVP(Long id, String uid, Long orderId, String teamSequeue, Byte teamStatus,String userChickenId,String userNickname, Byte userIdentity, Integer userBaojiLevel, Byte userStatus, Integer gameId, String gameName, Integer gameZoneId, String gameZoneName, Integer gameDanId, String gameDanName,Byte playMode,Byte settlementType,BigDecimal settlementNumber, Integer price, Date gmtCreate, Date gmtModified) {
        this.id = id;
        this.uid = uid;
        this.orderId = orderId;
        this.teamSequeue = teamSequeue;
        this.teamStatus = teamStatus;
        this.userChickenId = userChickenId;
        this.userNickname = userNickname;
        this.userIdentity = userIdentity;
        this.userBaojiLevel = userBaojiLevel;
        this.userStatus = userStatus;
        this.gameId = gameId;
        this.gameName = gameName;
        this.gameZoneId = gameZoneId;
        this.gameZoneName = gameZoneName;
        this.gameDanId = gameDanId;
        this.gameDanName = gameDanName;
        this.playMode =playMode;
        this.settlementType =settlementType;
        this.settlementNumber =settlementNumber;
        this.price = price;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public OrderItemTeamPVP() {
        super();
    }

    /**
     * 获取付费车队队员订单详情主键ID
     *
     * @return id - 付费车队队员订单详情主键ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置付费车队队员订单详情主键ID
     *
     * @param id 付费车队队员订单详情主键ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取订单用户UID
     *
     * @return uid - 订单用户UID
     */
    public String getUid() {
        return uid;
    }

    /**
     * 设置订单用户UID
     *
     * @param uid 订单用户UID
     */
    public void setUid(String uid) {
        this.uid = uid == null ? null : uid.trim();
    }

    /**
     * 获取所属订单ID
     *
     * @return order_id - 所属订单ID
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 设置所属订单ID
     *
     * @param orderId 所属订单ID
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取车队序列号
     *
     * @return team_sequeue - 车队序列号
     */
    public String getTeamSequeue() {
        return teamSequeue;
    }

    /**
     * 设置车队序列号
     *
     * @param teamSequeue 车队序列号
     */
    public void setTeamSequeue(String teamSequeue) {
        this.teamSequeue = teamSequeue == null ? null : teamSequeue.trim();
    }

    /**
     * 获取车队状态，0:未开车，1:已开车
     *
     * @return team_status - 车队状态，0:未开车，1:已开车
     */
    public Byte getTeamStatus() {
        return teamStatus;
    }

    /**
     * 设置车队状态，0:未开车，1:已开车
     *
     * @param teamStatus 车队状态，0:未开车，1:已开车
     */
    public void setTeamStatus(Byte teamStatus) {
        this.teamStatus = teamStatus;
    }

    /**
     * 获取队员身份，0：老板，1：暴鸡，2：暴娘，3：队长
     *
     * @return user_identity - 队员身份，0：老板，1：暴鸡，2：暴娘，3：队长
     */
    public Byte getUserIdentity() {
        return userIdentity;
    }

    /**
     * 设置队员身份，0：老板，1：暴鸡，2：暴娘，3：队长
     *
     * @param userIdentity 队员身份，0：老板，1：暴鸡，2：暴娘，3：队长
     */
    public void setUserIdentity(Byte userIdentity) {
        this.userIdentity = userIdentity;
    }

    /**
     * 获取暴鸡等级 100: 普通暴鸡 101: 优选暴鸡 102: 超级暴鸡 300: 暴娘
     *
     * @return user_baoji_level - 暴鸡等级 100: 普通暴鸡 101: 优选暴鸡 102: 超级暴鸡 300: 暴娘
     */
    public Integer getUserBaojiLevel() {
        return userBaojiLevel;
    }

    /**
     * 设置暴鸡等级 100: 普通暴鸡 101: 优选暴鸡 102: 超级暴鸡 300: 暴娘
     *
     * @param userBaojiLevel 暴鸡等级 100: 普通暴鸡 101: 优选暴鸡 102: 超级暴鸡 300: 暴娘
     */
    public void setUserBaojiLevel(Integer userBaojiLevel) {
        this.userBaojiLevel = userBaojiLevel;
    }

    /**
     * 获取车队队员订单状态(0:已支付,未开车,主动退出(老板); 1:已支付,未开车,被踢出(老板); 2:已支付,未开车,队长解散车队(老板); 3:已开车,主动退出(暴鸡和老板); 4:已开车,队长解散车队(所有队员); 5:车队正常结束(所有队员); -1:未知状态,忽略处理）
     *
     * @return user_status - 车队队员订单状态(0:已支付,未开车,主动退出(老板); 1:已支付,未开车,被踢出(老板); 2:已支付,未开车,队长解散车队(老板); 3:已开车,主动退出(暴鸡和老板); 4:已开车,队长解散车队(所有队员); 5:车队正常结束(所有队员); -1:未知状态,忽略处理）
     */
    public Byte getUserStatus() {
        return userStatus;
    }

    /**
     * 设置车队队员订单状态(0:已支付,未开车,主动退出(老板); 1:已支付,未开车,被踢出(老板); 2:已支付,未开车,队长解散车队(老板); 3:已开车,主动退出(暴鸡和老板); 4:已开车,队长解散车队(所有队员); 5:车队正常结束(所有队员); -1:未知状态,忽略处理）
     *
     * @param userStatus 车队队员订单状态(0:已支付,未开车,主动退出(老板); 1:已支付,未开车,被踢出(老板); 2:已支付,未开车,队长解散车队(老板); 3:已开车,主动退出(暴鸡和老板); 4:已开车,队长解散车队(所有队员); 5:车队正常结束(所有队员); -1:未知状态,忽略处理）
     */
    public void setUserStatus(Byte userStatus) {
        this.userStatus = userStatus;
    }

    /**
     * 获取游戏ID
     *
     * @return game_id - 游戏ID
     */
    public Integer getGameId() {
        return gameId;
    }

    /**
     * 设置游戏ID
     *
     * @param gameId 游戏ID
     */
    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    /**
     * 获取游戏名称
     *
     * @return game_name - 游戏名称
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * 设置游戏名称
     *
     * @param gameName 游戏名称
     */
    public void setGameName(String gameName) {
        this.gameName = gameName == null ? null : gameName.trim();
    }

    /**
     * 获取游戏大区ID
     *
     * @return game_zone_id - 游戏大区ID
     */
    public Integer getGameZoneId() {
        return gameZoneId;
    }

    /**
     * 设置游戏大区ID
     *
     * @param gameZoneId 游戏大区ID
     */
    public void setGameZoneId(Integer gameZoneId) {
        this.gameZoneId = gameZoneId;
    }

    /**
     * 获取游戏大区名称
     *
     * @return game_zone_name - 游戏大区名称
     */
    public String getGameZoneName() {
        return gameZoneName;
    }

    /**
     * 设置游戏大区名称
     *
     * @param gameZoneName 游戏大区名称
     */
    public void setGameZoneName(String gameZoneName) {
        this.gameZoneName = gameZoneName == null ? null : gameZoneName.trim();
    }

    /**
     * 获取段位ID
     *
     * @return game_dan_id - 段位ID
     */
    public Integer getGameDanId() {
        return gameDanId;
    }

    /**
     * 设置段位ID
     *
     * @param gameDanId 段位ID
     */
    public void setGameDanId(Integer gameDanId) {
        this.gameDanId = gameDanId;
    }

    /**
     * 获取段位名称
     *
     * @return game_dan_name - 段位名称
     */
    public String getGameDanName() {
        return gameDanName;
    }

    /**
     * 设置段位名称
     *
     * @param gameDanName 段位名称
     */
    public void setGameDanName(String gameDanName) {
        this.gameDanName = gameDanName == null ? null : gameDanName.trim();
    }
    /**
     * 获取玩法模式, 0:上分; 1:陪玩;
     *
     * @return play_mode - 玩法模式, 0:上分; 1:陪玩;
     */
    public Byte getPlayMode() {
        return playMode;
    }

    /**
     * 设置玩法模式, 0:上分; 1:陪玩;
     *
     * @param playMode 玩法模式, 0:上分; 1:陪玩;
     */
    public void setPlayMode(Byte playMode) {
        this.playMode = playMode;
    }

    /**
     * 获取结算类型, 1:局; 2:小时
     *
     * @return settlement_type - 结算类型, 1:局; 2:小时
     */
    public Byte getSettlementType() {
        return settlementType;
    }

    /**
     * 设置结算类型, 1:局; 2:小时
     *
     * @param settlementType 结算类型, 1:局; 2:小时
     */
    public void setSettlementType(Byte settlementType) {
        this.settlementType = settlementType;
    }

    /**
     * 获取结算数量(保留1位有效数字)
     *
     * @return settlement_number - 结算数量(保留1位有效数字)
     */
    public BigDecimal getSettlementNumber() {
        return settlementNumber;
    }

    /**
     * 设置结算数量(保留1位有效数字)
     *
     * @param settlementNumber 结算数量(保留1位有效数字)
     */
    public void setSettlementNumber(BigDecimal settlementNumber) {
        this.settlementNumber = settlementNumber;
    }
    /**
     * 获取暴鸡收益或老板实际支付金额, 单位:分
     *
     * @return price - 暴鸡收益或老板实际支付金额, 单位:分
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * 设置暴鸡收益或老板实际支付金额, 单位:分
     *
     * @param price 暴鸡收益或老板实际支付金额, 单位:分
     */
    public void setPrice(Integer price) {
        this.price = price;
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

    public String getUserChickenId() {
        return userChickenId;
    }

    public void setUserChickenId(String userChickenId) {
        this.userChickenId = userChickenId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }
}