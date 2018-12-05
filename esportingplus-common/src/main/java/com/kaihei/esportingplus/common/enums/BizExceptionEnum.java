package com.kaihei.esportingplus.common.enums;

/**
 * 业务异常类型 范围定义: 5001-5999 公共错误码 6001-6999 车队错误码 7001-7999 订单错误码 8001-8999 用户错误码 120001-120999 支付项目-内购打赏相关错误码 ...
 *
 * @author liangyi
 */
public enum BizExceptionEnum {

    // 公共错误码
    /**
     * 返回成功
     */
    SUCCESS(0, "success"),
    /**
     * 未知错误
     */
    UNKNOWN_ERROR(-1, "未知错误"),
    /**
     * 服务器内部异常
     */
    INTERNAL_SERVER_ERROR(500, "服务器正在打排位赛"),
    URL_NOT_FOUND(404, "访问地址不存在"),
    INVALID_TOKEN(401, "token非法"),
    PERMISSIONS_ACCESS_DENIED(403, "权限不足，访问拒绝"),
    INVALID_PY_TOKEN(4001, "pyton token校验失败"),
    BAD_REQUEST(400, "非法请求"),
    BLACK_LIST(423, "当前请求IP已被拉入黑名单"),
    TOO_MANY_REQUEST(429, "官人!用力太猛了，系统开启大招，排会儿队歇息一下吧"),
    READ_LIMIT_LUA_ERROR(600, "读取限流lua脚本配置异常"),
    /**
     * 参数验证失败
     */
    PARAM_VALID_FAIL(5001, "参数验证失败 [%s]"),
    /**
     * 文件上传
     */
    FILE_UPLOAD_FAIL(5002, "文件[%s]上传失败"),
    /**
     * 生成二维码
     */
    CREATE_TWO_CODE_FAIL(5003, "内容[%s]生成二维码失败"),
    CREATE_ELASTIC_JOB_FAIL(5004, "添加任务[%s]失败"),
    FILE_DOWNLOAD_FAIL(5006, "文件下载失败"),
    DICT_NOT_FOUND(5100, "未找到数据字典的数据"),
    /**
     * 服务熔断
     */
    HYSTRIX_SERVER(5555, "服务熔断"),
    GATEWAY_FALBACK(5556, "{0}[{1}]暂不可用，请稍后重试!"),
    APP_VERSION_CONFIG_NOT_FOUND(5557, "[%s]版本信息未配置, 请稍后再试!"),

    // 车队错误码
    /**
     * 车队不存在
     */
    TEAM_NOT_EXIST(6001, "车队不存在"),
    /**
     * 创建车队失败
     */
    TEAM_CREATE_FAILED(6002, "创建车队失败"),
    /**
     * 不是车队队长不能踢出队员、解散车队、立即开车、结束车队
     */
    TEAM_NOT_LEADER(6003, "只有队长才能操作哦"),
    /**
     * 不是暴鸡身份无权创建车队
     */
    TEAM_CREATE_NOT_BAOJI(6004, "暴鸡方可创建车队"),

    /**
     * 车队不在准备中
     */
    TEAM_IS_NOT_PREPARING(6005, "车队不在准备中"),
    /**
     * 车队进行中
     */
    TEAM_IS_RUNNING(6006, "车队正在进行中"),
    /**
     * 车队已解散
     */
    TEAM_HAS_DISMISSED(6007, "车队已解散"),
    /**
     * 车队已结束
     */
    TEAM_HAS_COMPLETED(6008, "车队已结束"),
    /**
     * 车队已满员
     */
    TEAM_MEMBER_IS_FULL(6009, "车队已满员"),
    /**
     * 立即开车时, 车队未满员
     */
    TEAM_MEMBER_NOT_FULL(6010, "车队未满员"),
    /**
     * 车队中无暴鸡队员
     */
    TEAM_MEMBER_HAS_NO_BAOJI(6011, "车队中无暴鸡队员"),
    /**
     * 车队中无老板队员
     */
    TEAM_MEMBER_HAS_NO_BOSS(6012, "车队中无老板队员"),
    /**
     * 车队中无任何队员
     */
    TEAM_HAS_NO_MEMBER(6013, "车队中无任何队员"),
    /**
     * 车队队员不存在
     */
    TEAM_MEMBER_NOT_EXIST(6014, "该队员已离开车队"),
    /**
     * 当前队员已离开车队
     */
    TEAM_CURRENT_MEMBER_HAS_LEAVED(6015, "你已离开车队"),
    /**
     * 车队位置数不能大于副本支持上限
     */
    TEAM_SEAT_UPPER_LIMIT(6016, "车队位置数大于设定上限"),
    /**
     * 车队位置数不能小于已入队位置数
     */
    TEAM_SEAT_DOWN_LIMIT(6017, "车队位置数不能小于已有队员数"),
    /**
     * 更新车队错误
     */
    TEAM_UPDATE_ERROR(6018, "更新车队失败"),
    /**
     * 车队队员不是老板身份
     */
    TEAM_MEMBER_IS_NOT_BOSS(6019, "该队员不是老板"),
    /**
     * 非车队老板创建订单
     */
    TEAM_MEMBER_NOT_BOSS(6020, "老板方可支付订单"),
    /**
     * 老板支付校验失败
     */
    TEAM_BOSS_PAYMENT_CALLBACK_ERROR(6021, "支付校验失败"),
    /**
     * 立即开车时, 并非所有车队队员的状态都是已入团
     */
    TEAM_MEMBER_NOT_ALL_JOINED_TEAM(6022, "车队还有尚未确认入团的队员"),
    /**
     * 立即开车时, 创建暴鸡订单车队状态错误(车队状态不是进行中...)
     */
    TEAM_CREATE_BAOJI_ORDER_STATUS_ERROR(6023, "车队创建暴鸡订单失败"),
    /**
     * 车队进行中队员状态错误
     */
    TEAM_RUNNING_MEMBER_STATUS_ERROR(6024, "队员状态错误"),
    /**
     * 车队进行中解散车队和车队正常结束的游戏结果不能为默认的(未知)
     */
    TEAM_RUNNING_GAME_RESULT_ERROR(6025, "比赛结果提交失败"),
    /**
     * 车队准备中解散车队的游戏结果肯定为默认的(未知)
     */
    TEAM_PREPARING_GAME_RESULT_ERROR(6026, "比赛结果提交失败"),
    /**
     * 结束车队时车队状态错误(车队状态不是进行中)
     */
    TEAM_END_STATUS_ERROR(6027, "比赛结果提交失败"),
    /**
     * 车队状态异常
     */
    TEAM_STATUS_ERROR(6028, "车队[%s]"),
    /**
     * 车队队员退出时, 车队状态错误(车队状态不是准备中或进行中...)
     */
    TEAM_MEMBER_QUIT_STATUS_ERROR(6029, "退出车队失败"),
    /**
     * 踢出队员时, 车队状态错误(车队状态不是准备中...)
     */
    TEAM_KICKOUT_MEMBER_STATUS_ERROR(6030, "踢出队员失败"),
    /**
     * 开车到解散/结束操作太快(30s内)
     */
    TEAM_OPERATE_TOO_FAST(6031, "您的操作太快了, 请稍后再试"),
    /**
     * 操作频繁(其实是 mq 消息发送失败了...)
     */
    TEAM_MQ_SEND_FAIL(6032, "您的操作太快了, 请稍后再试!"),
    /**
     * 踢出队员时, 发生IM消息原因不能为空
     */
    TEAM_MEMBER_OUT_REASON_NOT_EMPTY(6033, "成员被踢出原因不能为空"),
    /**
     * 车队类型的创建者使用的身份与车队类型支持的身份不同
     */
    TEAM_CREATOR_NOT_MATCH(6034, "车队类型与创建者身份不匹配"),

    /**
     * 车队非全员都在准备状态
     */
    TEAM_MEMBER_NOT_ALL_ONREADY(6035, "车队还有未准备的队员"),

    /**
     * 车队状态不在开始状态
     */
    TEAM_IS_NOT_RUNNING(6036, "车队未处于进行中"),
    /**
     * 不规范操作
     */
    TEAM_UNSUPPORTED_OPERATION(6037, "不支持的操作"),
    /**
     * 免费次数不足
     */
    TEAM_FREE_CHANCE_NOT_ADEQATE(6038, "免费次数不够"),

    /**
     * 用户获取匹配信息时, 未找到匹配的历史记录
     */
    TEAM_FREE_MATCHING_HISTORY_NOT_FOUND(6039, "未找到匹配信息"),

    //匹配
    MATCHING_EXISTS(6100,"您已经在匹配中了"),
    MATCHING_FREECOUPONS_NONE(6101,"非常抱歉，您已经没有免单次数了"),


    // 游戏数据
    GAME_NOT_EXISTS(6200,"游戏信息不存在"),

    //订单错误码
    ORDER_UNKNOW_USER_STATUS(7000, "车队队员订单状态错误"),
    ORDERED(7001, "该游戏角色已接单"),
    ORDER_NOT_PAY(7002, "该用户[%s]未支付"),
    ORDER_NOT_EXIST(7003, "订单不存在"),
    ORDER_TEAM_BAOJI_EMPTY(7004, "车队[%s]暴鸡订单列表为空"),
    ORDER_TEAM_USER_EMPTY(7005, "车队[{0}]的用户[{1}]订单列表为空"),
    SINGLE_GAME_RAID_FAIL(7006, "获取单个游戏副本失败"),
    BATCH_BAOJI_LEVEL_RATE(7007, "批量获取暴鸡等级系数失败"),
    BATCH_BAOJI_RATE(7008, "获取副本内暴鸡占比失败"),
    ORDER_FEFUND_NOTIFY_FAIL(7009, "退款回调订单更新失败"),
    ORDER_PAID_NOTIFY_FAIL(7010, "支付回调订单更新失败"),
    ORDER_PAID_CONFIRM_NOTIFY_FAIL(7011, "支付确认回调订单更新失败"),
    ORDER_PROFIT_BAOJI_EMPTY(7012, "暴鸡数量为零"),
    PROFIT_DPS_FAIL(7013, "结算DPS收益失败"),
    PROFIT_ASSIST_FAIL(7014, "结算辅助收益失败"),
    INVALID_BAOJI_LEVEL(7015, "错误的暴鸡等级[{0}]"),
    ORDER_PAYED_EMPTY(7016, "老板[{0}]无已支付的订单"),
    ORDER_TEAM_ITEM_EMPTY(7017, "车队订单列表为空"),
    SUM_BOSS_AMOUT_FAIL(7018, "统计老板订单总金额失败"),
    COUPON_CANNOT_USED(7018, "优惠券不可用"),
    COUPON_USED_FAIL(7019, "优惠券消费失败"),
    INVALID_PAYED_AMOUT(7020, "订单已支付金额异常：[%s]"),
    TEAM_BAOJI_ORDER_ALREADY_CREATE(7021, "该车队暴鸡订单已经创建，请勿重复创建"),
    REFUND_MQ_SEND_FAIL(7022, "退款mq消息发送失败"),
    ORDER_STATUS_NOPAY(7023, "老板[{}]不存在未支付订单"),
    ORDER_PAYED_AND_LEAVETEAM_EMPTY(7024, "车队已支付订单和中途退出的老板订单列表为空"),
    PROFIT_INVALID(7025, "暴鸡收益结算错误：收益总和大于老板支付总额"),
    ORDER_STATUS_NOT_EXIST(7026, "订单不存在"),
    ORDER_WX_INFO_FAIL(7027, "获取微信支付订单失败"),
    ORDER_STATUS_NOT_PAID(7028, "订单[%s]支付状态不是已支付"),
    ORDER_CATEGORY_ERROR(7029,"游戏订单分类错误"),
    ORDER_ALREADY_PAID(7030,"订单已支付,请勿重复支付"),
    ORDER_ALREADY_FINISH(7031,"订单已完成,请勿重复支付"),
    ORDER_NOT_SETTLE_COMPLETE(7032,"订单还未结算完成，请稍后重试"),
    ORDER_FEOM_PAYMENT_FAIL(7034,"获取支付系统订单失败"),
    // 用户错误码
    /**
     * 游戏角色不存在
     */
    USER_GAME_ROLE_NOT_EXIST(8001, "游戏角色不存在"),
    /**
     * 游戏角色错误码
     */
    USER_GAME_ROLE_CERT_NOT_ALLOW_DELETE(8002, "请先解除认证，再删除角色"),
    /**
     * 操作人不一致
     */
    OPERATE_PERSON_DIFFER(8003, "非本人操作，拒绝"),
    USER_ROLE_NOT_CERT(8004, "游戏角色没有经过认证"),
    USER_ROLE_CREATE_FAST(8005, "请勿频繁创建角色"),
    /**
     * 用户头像不存在
     */
    USER_AVATAR_NOT_EXIST(8006, "用户头像不存在"),
    USER_SYSTEM_MAINTAINING(8007, "服务器维护中, 请稍后再试~"),
    /**
     * 用户头像不存在
     */
    USER_GAME_ROLE_IS_NOT_BOSS(8008, "该角色在此副本中不是老板"),
    USER_SYSTEM_THIRDPART_VALID_FAIL(8009, "第三方登录验证失败"),
    USER_MAX_REGIST_PER_DEVICE_LIMIT(8010, "该设备注册账户次数已超过上限，请使用其他设备注册。"),
    USER_SYSTEM_LOGIN_LOCK(8011, "此账号登录功能已被限制，请联系客服qq:2188245056"),
    USER_MAX_LOGIN_PER_DEVICE_LIMIT(8012, "该设备登录账户次数已超过上限，请使用其他设备登录。"),
    USER_SYSTEM_PHONE_REGIST_FAIL(8013, "手机号注册失败"),
    USER_VALIDATE_SENSITIVE_WORD(8014, "用户名含有敏感词，请修改后重新提交"),
    USER_VALIDATE_DESC_SENSITIVE_WORD(8016, "个人简介含有敏感词，请修改后重新提交"),

    USER_AVATAR_VERIFY(8017, "你的头像正在审核中，请稍后查看"),
    USER_AVATAR_VERIFY_FAIL(8018, "你的头像不符合规范，请重新上传"),

    USER_CERT_INFO_NOT_EXIT(8601, "用户未认证过该游戏"),
    USER_NOT_IN_WHITE_LIST(8701, "用户不在组建免费车队白名单里"),

    TIME_PATTERN(8886, "时间格式不正确！"),
    USER_EXIST(8887, "用户名已存在！"),
    PHONE_HAS_REGIST_USER(8890, "手机号已注册"),

    /**
     * 入参缺失
     */
    PARAM_ENTRY_ERROR(8889, "参数错误！"),
    /**
     * 用户不存在
     */
    USER_NOT_EXIST(8888, "用户不存在"),
    /**
     * 用户已在其他车队中
     */
    USER_IN_OTHER_TEAM(8100, "您已加入了其他车队"),
    /**
     * 用户不在当前车队中
     */
    USER_NOT_IN_CURRENT_TEAM(8200, "您不在当前车队中"),
    USER_UPDATE_AVATAR_FAIL(8201, "修改用户头像链接失败"),
    USER_PHONE_CODE_FAIL(8202, "验证码错误"),
    /**
     * 用户账号已绑定
     */
    USER_AUTH3_ACCOUNT_IS_BIND(8401, "该第三方账号已绑定过暴鸡账号"),
    USER_AUTH3_USERNAME_IS_INVALID(8401, "该第三方账号绑定用户名非法"),
    USER_AUTH3_PHONE_IS_INVALID(8402, "手机号格式错误"),
    USER_AUTH3_PHONE_IS_BIND(8403, "该手机号已绑定过暴鸡账号"),
    USER_AUTH3_ACCOUNT_BIND_FAIL(8404, "绑定第三方账号失败"),
    USER_AUTH3_PHONE_BIND_FAIL(8405, "绑定手机号码失败"),
    USER_AUTH3_GET_BIND_LIST(8406, "获取绑定列表失败"),
    USER_AUTH3_PLATFORM_UNBIND_EMPTY(8407, "未绑定对应的第三方账号"),
    USER_AUTH3_PLATFORM_UNBIND_FAIL(8408, "解绑第三方账号失败"),
    USER_AUTH3_UPDATEPHONE_CODE_LENGTH(8409, "请填写四位数字验证码"),
    USER_AUTH3_UPDATEPHONE_FAIL(8410, "更换绑定手机号码失败"),

    /**
     * 用户鸡分错误码
     */
    USER_POINT_EXCHANGE_CONFIG_EMPTY(8301, "鸡分兑换配置未配置"),
    USER_POINT_EXCHANGE_SWITCH_DISABLE(8302, "鸡分兑换通道已关闭"),
    USER_POINT_EXCHANGE_TIME_FRAME_ERROR(8303, "当前时间不在鸡分兑换时间范围内"),
    USER_POINT_EXCHANGE_AMOUNT_FRAME_ERROR(8304, "当前兑换暴鸡值不在兑换范围内"),
    USER_POINT_EXCHANGE_AVAILABLE_AMOUNT(8305, "可用积分不足"),
    USER_AWARD_POINT_NOT_FOUND(8306, "未获取到车队对应鸡分配置奖励值"),
    STAR_AWARD_POINT_NOT_FOUND(8307, "未匹配到星值对应鸡分配置奖励值"),
    INCR_POINT_FAIL(8307, "插入鸡分明细失败"),

    ACTION_NO_FIND_METHOD(4002, "未找到该方法"),

    /**
     * 照片
     */
    PICTURE_NO_FIND(8500, "未找到需要替换的图片"),
    PICTURE_VERIFYING(8501, "照片正在审核中，无法修改"),
    PICTURE_URL_UNLAWFUL(8502, "非法URL"),
    PICTURE_OVER_MAX(8503, "已超过最大数量限制"),
    PICTURE_OVER_MIN(8504, "至少保留一张照片"),

    /**
     * 微信小程序
     */
    WX_MP_AUTH_FAIL(8308, "微信小程序认证失败"),
    WX_PHONE_HAS_BINDED(8309, "该微信已经绑定手机"),
    PHONE_HAS_BINDED_WX(8310, "手机号已经绑定微信"),

    /**
     * 用户分享拉新错误码
     */
    USER_AWARD_BAOJICOIN_ERROR(8206, "增加暴击币失败"),

    USER_SHARE_NOT_ERROR(8207,"该分享类型不存在"),

    //API鉴权
    TOKEN_EXPIRED(11000, "Token过期"),
    AUTH_REVOKE_FAIL(11002, "Token注销失败,已经被注销"),
    EMPTY_TOKEN(11003, "Authorization 参数为空"),
    DECODE_TOKEN_FAIL(11004, "解析BASIC TOKEN失败"),
    INVALID_BASIC_TOKEN(11005, "错误的BASIC TOKEN"),
    INVALID_CLIENT(11005, "错误的ClientId或ClientSecret"),
    INIT_AUTHURL_ERROR(11007, "初始化权限异常"),
    LOAD_AUTHURL_ERROR(11008, "获取权限异常"),
    REFRESH_TOKEN_ERROR(11009, "token 刷新失败"),
    UID_INFO_EMPTY(11010, "数据异常，找不到用户信息，需重新登陆。Token:[{0}]"),
    INIT_CLIENT_ERROR(110011, "预热客户端信息异常"),

    // 游戏资源错误码
    /**
     * 游戏副本不存在
     */
    GAME_RAID_NOT_EXIST(9001, "游戏副本不存在"),
    /**
     * 游戏职业不匹配
     **/
    GAME_CAREER_NOT_MATCH(9002, "游戏职业不匹配"),
    /**
     * 游戏服务器不匹配
     **/
    ZONE_SERVER_NOT_MATCH(9003, "游戏服务器不匹配"),

    ZONE_SERVER_NOT_EXIST(9004, "游戏服务器不存在"),
    RAID_CERT_NOT_EXIST_RAID(9005, "认证的副本没有可加入副本"),
    //客户中心错误码
    /**
     * 同一个车队只能被同一个老板投诉一次
     */
    COMPLAINT_NOT_ALLOWED_TWICE(9901, "同一个车队只能被同一个老板投诉一次"),

    /**
     * 评价星评必须在1、2、3、4、5范围内
     * */
    EVALUATE_STAR_ILLEGAL(9902,"请给出星评价哦"),
    /**
     * 评价内容最多只能输入1000汉字
     * */
    EVALUATE_CONTENT_LENGTH_OVER(9903,"最多只能输入1000字哦~"),
    /**
     * 评价内容检测有违规内容
     * */
    EVALUATE_CONTENT_ILLEGAL(9904,"系统检测有违规内容，请重新编辑"),
    /**
     * 只有已服务订单才能评价
     * */
    EVALUATE_SERVICE_NOT_END(9905,"只有已确认服务的服务订单才可以评价哦~"),
    /**
     * 只有车队成员才能评价车队订单
     * */
    EVALUATE_NOT_TEAM_MEMBER(9906,"只有车队成员才能评价车队订单哦~"),
    //阿里云过滤错误码
    /**
     * 需要人工审核
     */
    ALI_SCAN_SUGGESTION_REVIEW(10001, "需要人工审核"),
    /**
     * 文本违规
     */
    ALI_SCAN_SUGGESTION_BLOCK(10002, "文本违规"),
    /**
     * 任务解析失败
     */
    ALI_SCAN_TASK_PROCESS_FAIL(10003, "任务解析失败"),
    /**
     * 响应包处理失败
     */
    ALI_SCAN_DETECT_NOT_SUCCESS(10004, "响应包处理失败"),
    /**
     * HTTP请求响应失败
     */
    ALI_SCAN_RESPONSE_NOT_SUCCESS(10005, "HTTP请求响应失败"),

    //支付项目-内购打赏相关错误码
    /**
     * 暴鸡币打赏和用户钱包流水相关-异常相关提示编码
     */
    USER_WALLETS_SELECT(120001, "用户余额查询服务接口异常"),
    GCOIN_BILL_SELECT(120002, "暴鸡币流水查询服务接口异常"),
    STARLIGHT_BILL_SELECT(120003, "暴击值流水查询服务接口异常"),
    GCOIN_CONSUME(120004, "暴鸡币兑换暴击值服务接口异常"),
    CREATE_GCOIN_ORDER_FAILURE(120005, "暴鸡币订单创建服务接口异常"),
    BILL_SAVE(120006, "流水保存接口异常"),
    ORDER_ENTITY_TYPE_EMPTY(120007, "订单实体类型不存在,流水保存失败！"),
    CURRENCY_TYPE_EMPTY(120008, "扣款货币类型类型不存在,扣款订单流水保存失败！"),
    USER_STARLIGHT_SELECT(120009, "用户累计暴击值查询服务接口异常"),
    /**
     * 暴鸡币打赏和用户钱包流水相关-业务处理条件不足的相关提示编码
     */
    USER_WALLETS_TYPE_EMPTY(120010, "用户钱包类型不能为空！"),
    WALLETS_BILL_NO_DATA(120011, "流水查询无数据！"),
    USER_GCOIN_NOT_ENOUGH(120012, "当前用户暴鸡币余额数不足！"),
    CONSUME_ORDER_NON_EXIST(120013, "打赏订单不存在，请重新下单！"),
    CONSUME_ORDER_STATE_PAYSUCCESS(120014, "打赏订单已处理，请不要重复请求！"),
    BILL_TIME_PATTERN(120015, "时间格式不正确"),
    BILL_TIME_DEFICIENCY(120016, "缺少开始或结束时间"),

    /**
     * 充值和兑现-业务处理条件不足的相关提示错误码
     */
    // 充值错误码错误码
    PAYMENT_ORDER_NOT_EXIST(120021, "对应的订单不存在"),
    //暴鸡币余额账户不可用
    GACCOUNT_UNAVALIABLE(120022, "暴鸡币余额账户不可用"),

    PAYMENT_FINISH(120023, "订单已经充值成功"),
    WITHDRAW_FINISH(120024, "订单已经提现成功"),
    EXCHANGE_FINISH(120028, "订单已经兑换成功"),

    STARTACCOUNT_UNAVALIABLE(120025, "暴击值余额账户不可用"),
    STARTNUMBER_LESS(120026, "兑换金额不能大于暴击值余额"),
    APPLE_PAY_FAILED(120027, "苹果支付失败"),

    EXCHANGE_SERVICE_REJECT(121001, "暂不能提供暴击值兑换暴鸡币服务"),

    COMSUMER_LOCK(120029, "该消息正在处理中"),
    APPLE_RECIPT_NOT_EXIST(120030, "APPLE票据信息不能为空"),
    MESSAGE_IS_CONSUMING(120031, "该消息正在处理中"),

    STARACCOUNT_NOT_EXIST(120032, "暴击值账户不存在"),
    BALANCE_WITHDRAW__FAIL(120033, "可使用暴击值余额不足，提现失败"),
    WITHDRAW_REPEAT__FAIL(120036, "提现请求正在处理中，请勿重复提交"),
    GCOINCCOUNT_NOT_EXIST(120037, "暴鸡币账户不存在"),
    WITHDRAW_BOTH__NULL__FAIL(120038, "订单号及流水号不能同时为空"),
    WITHDRAW_PARAMS_NOT_MATCH(120039, "提现参数类型不匹配"),

    //暴鸡币支付
    GCOINPAYMENT_CLOSE(120034, "暴鸡币支付订单已经超时关闭"),
    GCOINPAYMENT_FINISH(120035, "暴鸡币支付订单已经支付成功"),
    /**
     * 充值和兑现-异常相关提示编码
     */
    //回调接口异常
    RECALL_API_EXCEPTION(120041, "回调接口异常"),
    //链接错误
    CONNECTION_EXCEPTION(120042, "链接错误"),
    PAYMENT_API_EXCEPTION(120043, "回调发送充值订单异常"),
    WITHDRAW_API_EXCEPTION(120044, "回调发送提现订单异常"),

    /**
     * 充值和兑现-消息队列相关提示编码
     */
    ROCKETMQ_PRODUCER_ERROR(120050, "rocketMq生产者出问题了~"),
    ROCKETMQ_COMSUMER_ERROR(120051, "rocketMq消费者出问题了~"),

    //金额入参必须是大于0的整数
    INPUT_PARAM_NUMBER_ERROR(120100, "金额入参必须是大于0的整数"),

    //暴鸡币账号相关错误码
    GCOIN_ACCOUNT_FROZEN(120101, "暴鸡币账号已冻结"),
    GCOIN_ACCOUNT_UNAVAILABLE(120102, "暴鸡币账号不可用"),
    GCOIN_ACCOUNT_BALANCE_NOT_ENOUGH(120103, "暴鸡币账户余额不足"),
    GCOIN_ACCOUNT_BALANCE_NOT_EXIST(120104, "暴鸡币账户不存在"),

    //暴击值账号相关错误
    STAR_ACCOUNT_FROZEN(120105, "暴击值账户已冻结"),
    STAR_ACCOUNT_UNAVAILABLE(120106, "暴击值账户不可用"),
    STAR_ACCOUNT_BALANCE_NOT_ENOUGH(120107, "暴击值账户余额不足"),

    ACCOUNT_CHECK_PARAM_ERROR(120108, "查询账号信息时，用户类型不匹配"),

    //暴鸡币充值相关错误码
    GCOIN_RECHARGE_ORDER_NOT_EXIST(120201, "暴鸡币充值订单不存在"),
    GCOIN_RECHARGE_ORDER_CLOSED(120202, "暴鸡币充值订单已关闭"),
    GCOIN_RECHARGE_ORDER_FINISHED(120203, "暴鸡币充值订单已完成 "),

    //暴鸡币充值-苹果支付相关错误码
    GCOIN_RECHARGE_APPLEPAY_SERVER_EXCEPTION(120300, "暴鸡币充值-苹果支付-苹果服务器异常"),
    GCOIN_RECHARGE_APPLEPAY_RECIPT_NOT_EXIST(120301, "暴鸡币充值-苹果支付-票据不存在"),
    GCOIN_RECHARGE_APPLEPAY_DEVICEID_NOT_EXIST(120302, "暴鸡币充值-苹果支付-设备ID不存在"),
    GCOIN_RECHARGE_APPLEPAY_CURRENCYTYPE_NOT_EXIST(120303, "暴鸡币充值-苹果支付-货币类型不存在"),
    GCOIN_RECHARGE_APPLEPAY_PRODUCT_SEARCH_ERROR(120304, "暴鸡币充值-苹果支付-查询APPLIE支付商品信息失败"),
    GCOIN_RECHARGE_APPLEPAY_RECIPT_IS_USED(120305, "暴鸡币充值-苹果支付-票据已经使用过"),

    //暴鸡币支付相关错误码
    GCOIN_PAYMENT_ORDER_CREATING(120400, "暴鸡币支付，该订单正在处理中"),
    GCOIN_PAYMENT_ORDER_NOT_EXIST(120401, "暴鸡币支付，该业务订单不存在"),
    GCOIN_PAYMENT_ORDER_CLOSED(120402, "暴鸡币支付，该业务订单支付超时关闭或结清，无法再支付"),
    GCOIN_PAYMENT_ORDER_PAIED(120403, "暴鸡币支付，该业务订单已支付完成 "),
    GCOIN_PAYMENT_ORDER_EXIST(120404, "暴鸡币支付，该业务订单已经存在"),

    GCOIN_PAYMENT_SEARCH_PARAM_ERROR(120405, "查询暴鸡币支付订单时，参数不完整（orderId或者orderType和outTradeNo二选一)"),
    GCOIN_PAYMENT_SEARCH_ORDER_NOT_FOUND(120406, "查询暴鸡币支付订单时，订单不存在"),

    //暴鸡币退款时，关联的支付订单的相关错误码
    GCOIN_REFUND_PAYMENT_ORDER_NOT_EXIST(120501, "暴鸡币退款，该支付订单不存在"),
    GCOIN_REFUND_PAYMENT_ORDER_NOT_PAY(120502, "暴鸡币退款，该支付订单未支付"),
    GCOIN_REFUND_PAYMENT_ORDER_SETTLED(120503, "暴鸡币退款，该支付订单已经退款完毕结清"),
    GCOIN_REFUND_PAYMENT_ORDER_AMOUNT_NOT_ENOUGH(120504, "暴鸡币退款，要退款金额大于可退金额"),
    GCOIN_REFUND_PAYMENT_ORDER_EXIST(120505, "暴鸡币退款，该支付订单已存在"),

    //暴鸡币退款时，退款订单的相关错误码
    GCOIN_REFUND_ORDER_CREATING(120600, "暴鸡币退款，该订单正在处理中"),
    GCOIN_REFUND_ORDER_NOT_EXIST(120601, "暴鸡币退款，该退款订单不存在"),
    GCOIN_REFUND_ORDER_NOT_PAY(120602, "暴鸡币退款，该退款订单未支付"),
    GCOIN_REFUND_ORDER_FINISHED(120603, "暴鸡币退款，该退款订单完成"),
    GCOIN_REFUND_ORDER_CLOSED(120604, "暴鸡币退款，订单支付超时已关闭、或结清，无法再退款"),

    GCOIN_REFUND_SEARCH_PARAM_ERROR(120605, "查询暴鸡币退款订单时，参数不完整（orderId或者orderType和outRefundNo二选一)"),
    GCOIN_REFUND_SEARCH_ORDER_NOT_FOUND(120606, "查询暴鸡币退款订单时，订单不存在"),

    //扣款相关错误码
    //扣款货币类型错误
    DEDUCT_CURRENCY_TYPE_ERROR(120700, "扣款货币类型错误"),
    //后台暴鸡币扣款操作相关错误
    GCOIN_DEDUCT_ORDER_EXIST(120701, "暴鸡币扣款订单已经存在"),
    //后台暴击值扣款相关错误
    STAR_DEDUCT_ORDER_EXIST(120702, "暴击值扣款订单已经存在"),
    //后台暴鸡币充值相关错误
    BACK_STAGE_GCOIN_PAYMENT_ORDER_EXIST(120703, "暴鸡币后台充值，订单已经存在"),

    //微信支付相关错误码
    TENPAY_PAY_SETTING_NOT_EXISTS(120910, "微信/QQ支付-支付配置没有配置该项"),
    TENPAY_PAY_SETTING_NULL_EXISTS(120911, "微信/QQ支付-支付配置为空"),
    TENPAY_PREPAY_ORDER_EXISTS(120912, "微信/QQ支付-预支付订单已经存在"),
    TENPAY_PREPAY_ORDER_POST_ERROR(120913, "微信/QQ支付-预支付订单网络请求错误"),
    TENPAY_PREPAY_ORDER_POST_CLOSE_ERROR(120914, "微信/QQ支付-预支付订单网络关闭错误"),
    TENPAY_PREPAY_ORDER_RETURN_ERROR(120915, "微信/QQ支付-预支付订单创建响应错误"),
    TENPAY_PREPAY_ORDER_PARAM_ERROR(120916, "微信/QQ支付--预支付订单参数错误"),
    TENPAY_PREPAY_ORDER_NET_ERROR(120917, "微信/QQ支付-预支付订单下单请求网络错误"),
    TENPAY_PREPAY_ORDER_CLOSE_TIME_ERROR(120918, "微信/QQ支付-订单关闭需在支付5分钟之后关闭"),
    TENPAY_PAY_ORDER_QUERY_RETURN_ERROR(120919, "微信/QQ支付-支付查询返回失败"),
    TENPAY_PREPAY_ORDER_QUERY_RETURN_SYNC_ERROR(120920, "微信/QQ支付-支付查询通同步数据异常"),
    TENPAY_PREPAY_ORDER_REFUND_AGAIN_ERROR(120921, "微信/QQ支付-已经存在退款记录"),
    TENPAY_PREPAY_ORDER_REFUND_MONEY_ERROR(120922, "微信/QQ支付-退款金额错误"),
    TENPAY_PREPAY_ORDER_REFUND_MONEY_OUT_ERROR(120923, "微信/QQ支付-退款金额超过订单总金额"),
    TENPAY_PREPAY_ORDER_REFUND_SYNC_ERROR(120926, "微信/QQ支付-退款同步数据库异常"),
    TENPAY_PREPAY_ORDER_REFUND_PARAM_ERROR(120927, "微信/QQ支付-退款参数构建异常"),
    TENPAY_PREPAY_ORDER_DO_REFUND_ERROR(120928, "微信/QQ支付-发起退款业务失败"),
    TENPAY_PREPAY_ORDER_REFUND_RETURN_ERROR(120929, "微信/QQ支付-退款业务返回失败"),
    TENPAY_PREPAY_ORDER_QUERY_CONS_ERROR(120930, "微信/QQ支付-构建查询参数异常"),
    TENPAY_PREPAY_ORDER_QUERY_DO_ERROR(120931, "微信/QQ支付-支持查询订单异常"),
    TENPAY_PREPAY_ORDER_REFUND_DO_ERROR(120932, "微信/QQ支付-执行退款异常"),
    TENPAY_PREPAY_ORDER_REFUND_DB_ERROR(120933, "微信/QQ支付-退款同步数据库失败"),
    TENPAY_PREPAY_ORDER_CONS_REFUND_QUERY_ERROR(120934, "微信/QQ支付-退款查询参数构建异常"),
    TENPAY_REFUND_ORDER_NOT_EXITS_ERROR(120935, "微信/QQ支付-不存在该笔退款"),
    TENPAY_RETURN_APP_PARAM_ERROR(120936, "微信/QQ支付-返回APP参数错误"),

    //支付宝-支付相关错误码
    ALIPAY_PAY_ORDER_EXIST(121101, "支付宝支付-支付订单已经存在"),
    ALIPAY_PAY_ORDER_CLOSED(121102, "支付宝支付-支付订单已经关闭"),
    ALIPAY_PAY_ORDER_CANCEL(121103, "支付宝支付-支付订单已经取消"),
    ALIPAY_PAY_ORDER_SUCCESS(121104, "支付宝支付-支付订单已经支付"),
    ALIPAY_PAY_ORDER_NOT_FOUND(121105, "支付宝支付-支付订单不存在"),
    ALIPAY_PAY_ORDER_CLOSED_NO_REFUND(121106, "支付宝支付-支付订单已经关闭无法退款"),
    ALIPAY_PAY_ORDER_CLOSING(121107, "支付宝支付-支付订单正在申请关闭中"),
    ALIPAY_PAY_ORDER_CANCELING(121108, "支付宝支付-支付订单正在申请取消中"),
    ALIPAY_PAY_ORDER_CREATE_EXCEPTION(121109, "支付宝支付-支付订单数据加密异常"),
    ALIPAY_PAY_ORDER_MONEY_NOT_EQUAL(121110, "支付宝回调-支付订单金额不一致"),
    ALIPAY_PAY_ORDER_APPID_NOT_EQUAL(121111, "支付宝回调-appId不一致"),

    //支付宝-退款相关错误码
    ALIPAY_REFUND_PAYORDER_UNPAIED(121201, "支付宝退款-支付订单未支付"),
    ALIPAY_REFUND_PAYORDER_NOT_EXIST(121202, "支付宝退款-支付订单不存在"),
    ALIPAY_REFUND_PAYORDER_CLOSED(121203, "支付宝退款-支付订单已经关闭"),
    ALIPAY_REFUND_PAYORDER_CANCEL(121204, "支付宝退款-支付订单已经取消"),
    ALIPAY_REFUND_PAYORDER_FINISHED(121205, "支付宝退款-支付交易结束，不可退款"),
    ALIPAY_REFUND_ORDER_NOT_EXIST(121206, "支付宝退款-退款订单不存在"),
    ALIPAY_REFUND_ORDER_EXIST(121207, "支付宝退款-退款订单已经存在"),

    ALIPAY_REFUND_ORDER_CREATE_EXCEPTION(121209, "支付宝退款-退款订单创建异常"),
    ALIPAY_REFUND_ORDER_MONEY_NOT_EQUAL(121210, "支付宝回调-退款订单金额不一致"),

    //支付宝-关闭订单相关错误码
    ALIPAY_CLOSE_PAYORDER_PAIED(121301, "支付宝关闭-支付订单已经支付"),
    ALIPAY_CLOSE_PAYORDER_NOT_EXIST(121302, "支付宝关闭-支付订单不存在"),
    ALIPAY_CLOSE_PAYORDER_CLOSING(121303, "支付宝关闭-支付订单正在申请取消中"),
    ALIPAY_CLOSE_PAYORDER_CLOSED(121304, "支付宝关闭-支付订单已经关闭"),
    ALIPAY_CLOSE_PAYORDER_CANCELING(121305, "支付宝关闭-支付订单正在申请取消中"),
    ALIPAY_CLOSE_PAYORDER_CANCEL(121306, "支付宝关闭-支付订单已经取消"),
    ALIPAY_CLOSE_PAYORDER_FINISHED(121307, "支付宝关闭-支付交易结束，不可关闭"),
    ALIPAY_CLOSE_ORDER_EXCEPTION(121308, "支付宝关闭-关闭订单异常"),
    ALIPAY_CLOSE_RETURN_OUT_TRADE_NO_NOT_EQUAL(121309, "支付宝关闭-关闭返回支付订单号不一致"),
    ALIPAY_CLOSE_RETURN_TRADE_NO_NOT_EQUAL(121310, "支付宝关闭-关闭返回支付宝交易订单号不一致"),

    //支付宝-撤销订单相关错误码
    ALIPAY_CANCEL_PAYORDER_SUCCESS(121401, "支付宝撤销-支付订单已支付，请调用退款接口进行处理"),
    ALIPAY_CANCEL_PAYORDER_NOT_EXIST(121402, "支付宝撤销-支付订单不存在"),
    ALIPAY_CANCEL_PAYORDER_CLOSING(121403, "支付宝撤销-支付订单正在申请关闭中"),
    ALIPAY_CANCEL_PAYORDER_CLOSED(121404, "支付宝撤销-支付订单已经关闭"),
    ALIPAY_CANCEL_PAYORDER_CANCELING(121405, "支付宝撤销-支付订单正在申请撤销中"),
    ALIPAY_CANCEL_PAYORDER_CANCEL(121406, "支付宝撤销-支付订单已经撤销"),
    ALIPAY_CANCEL_PAYORDER_FINISHED(121407, "支付宝撤销-支付交易结束，不可撤销"),
    ALIPAY_CANCEL_CALL_EXCEPTION(121408, "支付宝撤销-调用撤销订单异常"),
    ALIPAY_CANCEL_CREATE_EXCEPTION(121409, "支付宝撤销-创建撤销订单异常"),
    ALIPAY_CANCEL_PAYORDER_UNPAIED(121410, "支付宝撤销-该订单未支付，请调用关闭接口进行处理"),
    ALIPAY_CANCEL_PAYORDER_NOT_EXIST_IN_ALI(121411, "支付宝撤销-该订单不存在于支付宝系统中，请调用关闭接口进行处理"),

    //支付宝查询相关错误码
    ALIPAY_PAY_ORDER_SEARCH_EXCEPTION(122301, "支付宝支付-支付订单查询异常"),
    ALIPAY_REFUND_SEARCH_EXCEPTION(122302, "支付宝退款-退款订单查询异常"),
    ALIPAY_REFUND_SEARCH_CALL_EXCEPTION(122303, "支付宝退款-调用退款接口异常"),
    ALIPAY_REFUND_SEARCH_RETURN_PARAMS_EXCEPTION(122304, "支付宝退款-退款查询没有对应的订单信息，表示退款失败"),
    ALIPAY_REFUND_SEARCH_REQUEST_NOT_EQUAL(122305, "支付宝退款-退款查询返回的退款订单号与商户提交的不一致"),
    ALIPAY_REFUND_SEARCH_PAY_TRADE_NO_NOT_EQUAL(122306, "支付宝退款-退款查询返回的支付订单号与商户提交的不一致"),
    ALIPAY_REFUND_SEARCH_REFUND_MONEY_NOT_EQUAL(122307, "支付宝退款-退款查询返回的退款金额与商户提交的不一致"),
    ALIPAY_REFUND_SEARCH_TRANSACTION_ID_NO_EQUAL(122308, "支付宝退款-退款查询返回的交易订单号与商户第一次收到的不一致"),
    ALIPAY_REFUND_REFUND_ORDER_NOT_EXIST(122309, "支付宝退款-退款订单不存在"),
    ALIPAY_REFUND_REFUND_ORDER_FAIL(122310, "支付宝退款-该退款订单退款失败"),
    ALIPAY_REFUND_REFUND_ORDER_SUCCESS(122311, "支付宝退款-该退款订单已经退款成功"),

    //支付配置相关错误码
    EXTERNAL_APP_CHANNEL_NOT_FOUND(122401, "没有查到对应的App-渠道配置信息"),
    EXTERNAL_APP_CHANNEL_DISABLE(122402, "该APP暂不支持该渠道的支付方式"),
    EXTERNAL_ALIPAY_CONFIG_NOT_EXIST(122403, "支付宝支付配置信息不存在"),
    EXTERNAL_TENPAY_CONFIG_NOT_EXIST(122404, "QQ支付、微信支付配置信息不存在"),
    EXTERNAL_CAPAY_CONFIG_NOT_EXIST(122405, "云账户配置信息不存在"),
    EXTERNAL_CHANNEL_NOT_FOUND(122406, "没有对应的渠道信息"),
    EXTERNAL_PAY_SETTING_CONFIG_NOT_EXIST(122407, "该支付配置信息不存在"),

    MONGODB_ALIPAY_PAYMENT_ORDER_NOT_EXITST(122601, "mongoDB中查不到对应的支付宝支付订单信息"),

    TENCENT_PAY_ORDERCREATE(123101, "腾讯支付-创建订单异常"),
    TENCENT_PAY_ORDERCLOSE_1(123102, "腾讯支付-关闭订单异常：订单已经存在"),
    TENCENT_PAY_ORDERCLOSE_2(123103, "腾讯支付-微信订单失败"),

    //第三方支付流水支付错误码
    EXTERNAL_TRADE_BILL_DATA_OUT_OF_RANGE(124101, "查询时间超出范围"),

    //APPSetting设置相关错误码
    APP_SETTING_APP_NAME_IS_EXIST(125001, "AppSetting配置-appName已经存在"),
    APP_SETTING_APP_ID_IS_NOT_EXIST(125002, "AppSetting配置-没有对应AppId的配置信息"),

    //支付订单相关错误码
    EXTERNAL_PAY_ORDER_EXIST(126101, "第三方支付-支付订单已经存在"),
    EXTERNAL_PAY_ORDER_CLOSED(126102, "第三方支付-支付订单已经关闭"),
    EXTERNAL_PAY_ORDER_CANCEL(126103, "第三方支付-支付订单已经取消"),
    EXTERNAL_PAY_ORDER_SUCCESS(126104, "第三方支付-支付订单已经支付"),
    EXTERNAL_PAY_ORDER_NOT_FOUND(126105, "第三方支付-支付订单不存在"),
    EXTERNAL_PAY_ORDER_CLOSED_NO_REFUND(126106, "第三方支付-支付订单已经关闭无法退款"),
    EXTERNAL_PAY_ORDER_CLOSING(126107, "第三方支付-支付订单正在申请关闭中"),
    EXTERNAL_PAY_ORDER_CANCELING(126108, "第三方支付-支付订单正在申请取消中"),
    EXTERNAL_PAY_ORDER_CREATE_EXCEPTION(126109, "第三方支付-支付订单数据加密异常"),
    EXTERNAL_PAY_ORDER_MONEY_NOT_EQUAL(126110, "第三方支付-支付订单金额不一致"),

    EXTERNAL_REFUND_ORDER_CLOSED(126111, "第三方支付-退款订单已经关闭"),
    EXTERNAL_REFUND_ORDER_CANCEL(126112, "第三方支付-退款订单已经取消"),
    EXTERNAL_REFUND_ORDER_SUCCESS(126113, "第三方支付-退款订单已经完成"),
    EXTERNAL_REFUND_ORDER_FAIL(126114, "第三方支付-退款订单已经失败"),
    EXTERNAL_REFUND_ORDER_NOT_ENOUGH_REFUND(121208, "申请退款-本次退款金额大于可退金额"),
    EXTERNAL_REFUND_ORDER_NOT_FOUND(121209, "第三方支付-退款订单不存在"),

    //重构相关错误码
    //支付宝支付
    EXTERNAL_ALIPAY_RESPONSE_IS_NULL(126200, "支付宝-返回response为空"),
    EXTERNAL_ALIPAY_CALL_FAIL(126201, "支付宝-接口调用失败"),
    EXTERNAL_ALIPAY_SEARCH_PAYMENT_CALL_FAIL(126202, "支付宝-查询支付订单调用失败"),
    EXTERNAL_ALIPAY_SEARCH_PAYMENT_ORDER_NOT_MATCH(126203, "支付宝-查询支付订单信息业务订单号不匹配"),
    EXTERNAL_ALIPAY_REFUND_CALL_FAIL(126204, "支付宝-退款接口调用失败"),
    EXTERNAL_ALIPAY_CANCEL_CALL_FAIL(126205, "支付宝-撤销接口调用失败"),
    EXTERNAL_ALIPAY_NOTIFY_VALIDATE_FAIL(126206, "支付宝-回调验签失败"),
    EXTERNAL_ALIPAY_REFUND_SYSTEM_EXCEPTION(126207, "支付宝-退款接口，系统异常，请再次提交重试"),

    EXTERNAL_TENPAY_RESPONSE_ERROR(126300, "微信/QQ支付-返回数据错误"),
    EXTERNAL_TENPAY_SIGN_INVALIDATION(126301, "微信/QQ支付-签名无效"),
    EXTERNAL_TENPAY_RETURN_CODE_INVALIDATION(126302, "微信/QQ支付-returnCode值无效"),
    EXTERNAL_TENPAY_SIGNATURE_INVALIDATION(126303, "微信/QQ支付-无效的签名算法"),
    EXTERNAL_TENPAY_HMACSHA256_ERROR(126304, "微信/QQ支付-生成 HMACSHA256 加密异常"),
    EXTERNAL_TENPAY_MD5_ERROR(126305, "微信/QQ支付-生成 MD5 加密异常"),
    EXTERNAL_TENPAY_XML_2_MAP_ERROR(126306, "微信/QQ支付-将xml报文转化为map异常"),
    EXTERNAL_TENPAY_SANDBOX_SIGNATURE_FAIL(126307, "微信/QQ支付-微信获取沙箱秘钥，生成签名失败"),
    EXTERNAL_TENPAY_SANDBOX_REQUEST_KEY_FAIL(126308, "微信/QQ支付-微信获取沙箱秘钥，请求失败"),
    EXTERNAL_TENPAY_SANDBOX_REQUEST_KEY_RESPONSE_FAIL(126308, "微信/QQ支付-微信获取沙箱签名响应解析失败"),
    EXTERNAL_TENPAY_MAP_2_XML_ERROR(126309, "微信/QQ支付-将map转XML异常"),

    EXTERNAL_TENPAY_CREATE_PAYMENT_FAIL(126311, "微信/QQ支付-创建预支付订单失败"),
    EXTERNAL_TENPAY_CREATE_PAYMENT_RETURN_ERROR(126312, "微信/QQ支付-预支付订单返回错误"),
    EXTERNAL_TENPAY_CREATE_PAYMENT_CALL_FAIL(126313, "微信/QQ支付-预支付订单创建调用失败"),
    EXTERNAL_TENPAY_CREATE_PAYMENT_SERVICE_FAIL(126314, "微信/QQ支付-预支付订单业务失败"),

    EXTERNAL_TENPAY_QUERY_PAYMENT_FAIL(126321, "微信/QQ支付-查询支付订单失败"),
    EXTERNAL_TENPAY_QUERY_PAYMENT_RETURN_ERROR(126322, "微信/QQ支付-查询支付订单返回错误"),
    EXTERNAL_TENPAY_QUERY_PAYMENT_CALL_FAIL(126323, "微信/QQ支付-查询支付订单创建调用失败"),
    EXTERNAL_TENPAY_QUERY_PAYMENT_SERVICE_FAIL(126324, "微信/QQ支付-查询支付订单业务失败"),

    EXTERNAL_TENPAY_REFUND_FAIL(126331, "微信/QQ支付-发起退款失败"),
    EXTERNAL_TENPAY_REFUND_RETURN_ERROR(126332, "微信/QQ支付-发起退款返回错误"),
    EXTERNAL_TENPAY_REFUND_CALL_FAIL(126333, "微信/QQ支付-发起退款调用失败"),
    EXTERNAL_TENPAY_REFUND_SERVICE_FAIL(126334, "微信/QQ支付-发起退款业务失败"),

    EXTERNAL_TENPAY_QUERY_REFUND_FAIL(126341, "微信/QQ支付-查询退款失败"),
    EXTERNAL_TENPAY_QUERY_REFUND_RETURN_ERROR(126342, "微信/QQ支付-查询退款返回错误"),
    EXTERNAL_TENPAY_QUERY_REFUND_CALL_FAIL(126343, "微信/QQ支付-查询退款调用失败"),
    EXTERNAL_TENPAY_QUERY_REFUND_SERVICE_FAIL(126344, "微信/QQ支付-查询退款业务失败"),

    EXTERNAL_TENPAY_CLOSE_PAYMENT_FAIL(126351, "微信/QQ支付-关闭订单失败"),
    EXTERNAL_TENPAY_CLOSE_PAYMENT_RETURN_ERROR(126352, "微信/QQ支付-关闭订单返回错误"),
    EXTERNAL_TENPAY_CLOSE_PAYMENT_CALL_FAIL(126353, "微信/QQ支付-关闭订单调用失败"),
    EXTERNAL_TENPAY_CLOSE_PAYMENT_SERVICE_FAIL(126354, "微信/QQ支付-关闭订单业务失败"),

    EXTERNAL_TENPAY_SEARCH_PAYMENT_ORDER_NOT_MATCH(126361, "微信/QQ支付-查询支付订单信息业务订单号不匹配"),

    EXTERNAL_TENPAY_NOTIFY_REQUEST_FAIL(126371, "微信/QQ支付-回调获取参数异常"),
    EXTERNAL_TENPAY_PAYMENT_NOTIFY_FAIL(126372, "微信/QQ支付-支付回调结果失败"),

    UNSUPPORT_PAY_CHANNEL(129999, "暂未支持该支付渠道"),

    UNAUTH_PAY_CHANNEL(129998, "未授权的支付应用"),

    PAY_CHANNEL_IS_DISABLE(129997, "该支付渠道已被禁用"),

    APP_PAY_CHANNEL_IS_CLOSED(129996, "该应用的支付权限已被关闭"),

    PAY_CHANNEL_SETTING_NOT_CONFIG(129995, "暂未设置该支付渠道的配置信息"),

    APPID_AND_SOURCE_IS_NOT_MATCH(129994, "该APPID没有对应的sourceID可以匹配"),

    //风控错误码
    RISK_DICT_NOT_INIT(130001, "风控数据字典没有初始化~"),
    RISK_VERIFY_NOT_PASS(130002, "风控校验不通过"),
    GLOBAL_RISK_SWITCH_CLOSED(133333, "风控总关开为关闭状态"),

    //订单收益分成结算
    ORDER_INCOME_REPEAT(140001, "订单重复结算"),

    ORDER_INCOME_RECODR_NULL(140002, "订单不存在"),
    ORDER_INCOME_RATIO(140003, "订单分成比例查询异常"),
    INVALID_DEDUCT_RATIO_VALUE(140004, "抽成比例值异常，只能为0-1之间"),

    BANNER_CONFIG_NOT_EXIST(150001, "banner配置不存在"),

    CHICKENPOINT_CONFIG_NOT_EXIST(150002, "积分任务不存在"),

    SHARE_INVITE_ALREADY_ONLINE(150003, "邀请分享任务已经上架，请勿重复上架"),

    FREE_TEAM_HOME_ADVERTISE_NOT_EXIST(150004, "免费车队该首页宣传图不存在"),

    FREE_TEAM_HOME_SCROLL_TEMPLATE_NOT_EXIST(150005, "免费车队该滚动模板不存在"),

    //底部tab参数列表为空
    FOOT_TAB_ARGS_EMPTY(160000, "底部tab参数列表为空"),

    FOOT_TAB_INSIDE_ARGS_EMPTY(160001, "底部tab内部服务参数列表为空"),

    DUPLICATE_TAB_ORDER_INDEX(160003, "排序号[%d]重复"),

    DUPLICATE_TAB_ITEM_ORDER_INDEX(160004, "所有Tab[{0}]中，排序号[{1}]重复"),

    // 免费车队
    FREE_TEAM_TYPE_NOT_FOUND(170001, "免费车队类型[%s]不存在"),

    BAOJI_DAN_RANGE_NOT_FOUND(170002, "暴鸡接单范围不存在"),

    BAOJI_IDENTITY_NOT_MATCH_FREE_TEAM(170003, "可组建的身份不符合该车队要求"),

    FREE_TEAM_TIMES_ERROR(170004, "免费车队上车次数异常"),
    FREE_TEAM_DEVICE_WHITE_ERROR(170005, "该设备已在白名单中，请勿重复添加"),
    COIN_CONSUME_AWARD_PAY_ORDERNO_DUPLICATE(17006, "消费暴鸡币订单重复"),
    FREE_TEAM_DEVICE_WHITE_NOT_EXIST(170007, "该设备不存在"),
    FREE_TEAM_UID_NOT_NULL(170008, "免费车队成员uid不能为空"),
    SHUMEI_API_CALL_FAIL(170009, "调用数美API返回结果异常"),
    IMMACHINE_DEVICE_ALREADY_EXIST(170010, "该设备已在黑名单中，无需重复添加"),
    FREE_TEAM_COUPONS_ERROR(170011, "插入免费券失败"),

    // 云账户提现 19打头
    CLOUDPAY_ORDER_EXISTS(190001, "云账户提现-提现订单已经存在"),
    CLOUDPAY_ORDER_ENCRYPT_ERROR(190002, "云账户提现-构建加密参数异常"),
    CLOUDPAY_ORDER_NETWORK_ERROR(190003, "云账户提现-网络异常"),
    CLOUDPAY_BALANCE_ENCRYPT_ERROR(190004, "查询商户余额-构建加密参数异常"),
    CLOUDPAY_BALANCE_NETWORK_ERROR(190005, "查询商户余额-网络异常"),
    CLOUDPAY_ORDER_NOT_FUND(190006, "云账户提现-提现订单不存在"),
    CLOUDPAY_ORDER_AMOUNT_NOTEQUAL(190007, "云账户提现-提现订单金额不一致"),
    CLOUDPAY_ORDER_RETURN_ERROR(190008, "云账户提现-提交下单请求返回失败"),
    CLOUDPAY_ORDER_QUERY_NETWORK_ERROR(190009, "查询提现订单状态-网络异常"),
    CLOUDPAY_ORDER_QUERY_RETURN_ERROR(190010, "云账户提现-查询订单请求返回失败"),
    CLOUDPAY_ORDER_RETURN_NULL(190011, "云账户提现-下单请求返回为空"),
    CLOUDPAY_BALANCE_RETURN_NULL(190012, "云账户提现-查询商户余额请求返回为空"),
    CLOUDPAY_QUERY_RETURN_NULL(190013, "云账户提现-查询订单状态请求返回为空"),
    CLOUDPAY_MONEY_LESSTHAN_1_YUAN(190014, "云账户提现-提现至微信钱包金额不能小于1元"),
    CLOUDPAY_CHANNEL_NOT_SUPPORT(190015, "云账户提现-暂不支持该渠道提现"),
    WITHDRAW_OVER_LIMIT(190016, "已达到今天提现次数[%s]上限，明天再来试试！"),
    WITHDRAW_AMOUNT_MIN(190017, "提现金额最低为[%s]元"),
    WITHDRAW_AMOUNT_MAX(190018, "单次提现金额最高为[%s]元"),
    WITHDRAW_AUDIT_NOT_EXIST(190019, "提现审批记录不存在"),
    WITHDRAW_AUDIT_STATE_NOT_WAIT(190020, "提现申请已审批完成，请勿重复审批"),
    WITHDRAW_BLOCK_STATE_NOT_YET(190021, "提现申请已被截停，审批失败"),
    WITHDRAW_BLOCK_STATE_NOT_WAIT(190022, "提现已审批完成，截停失败"),
    WITHDRAW_BLOCK_MIS_OPERATION(190023, "错误操作"),
    WITHDRAW_FUNCTION_CLOSED(190023, "提现功能已关闭"),

    // 暴击值兑换订单-新增校验
    COMPLAIN_ORDER_EXIST_FAIL(300001, "兑换失败，存在进行中的订单"),

    //消息推送相关错误码
    PUSH_MESSAGE_URL_EMPTY(400001, "消息推送，后续动作不为APP首页时，跳转URL不能为空"),
    PUSH_MESSAGE_TAG_HAS_NO_USER(400002,"消息推送，tagName没有关联的用户id"),
    PUSH_MESSAGE_FILE_USERID_IS_EMPTY(400002,"消息推送，上传的文件用户id为空"),

    DICT_CATEGORY_CONFIG_NOT_INIT(180001, "数据字典分类没有初始化"),
    DICT_CATEGORY_CONFIG_NOT_MATCH(180002, "数据字典分类不匹配"),
    DICT_POSITION_ERROR(180003, "投放位置代码错误"),
    DICT_USER_TYPE_ERROR(180004, "用户类型错误"),
    IM_MSG_SEND_ERROR(200001,"im消息发送失败"),

    //core相关错误码
    CORE_CREATE_VERSION_LOG_FAILED(210001,"插入版本日志记录失败"),
    CORE_UPDATE_VERSION_LOG_FAILED(210002,"更新版本日志记录失败"),
    CORE_DISABLE_VERSION_LOG_FAILED(210003,"停用版本日志记录失败"),
    CORE_ENABLE_VERSION_LOG_FAILED(210004,"启用版本日志记录失败"),
    CORE_DELETE_VERSION_LOG_FAILED(210005,"删除版本日志记录失败"),
    CORE_VERSION_LOG_HAS_DELETED(210006,"删除版本日志记录失败"),
    CORE_FEEDBACK_INSERT_FAILED(200007, "插入反馈信息失败"),

    //后台管理
    BACKEND_SYSTEM_CREATE_DINGDING_INVALID_UNIONID(200001, "unionId不能为空"),
    BACKEND_SYSTEM_CREATE_USER_HAS_CREATED(200002, "用户已经创建，请勿重复创建"),
    BACKEND_SYSTEM_LOGIN_DINGDING_FAILED(200003, "钉钉账户尚未绑定或注册"),
    BACKEND_SYSTEM_LOGIN_PASSWORD_ERROR_MAX_TIME(200004, "您好，您今日输错密码次数超过3次，已自动冻结您的账号，请联系管理员！"),
    BACKEND_SYSTEM_LOGIN_ERROR_PASSWORD(200005, "密码错误，请重试"),
    BACKEND_SYSTEM_TOKEN_ENCRYPT_FAILED(200006, "token加密失败"),
    BACKEND_SYSTEM_EMAIL_INAVLID_FORMAT(200007, "邮箱格式不正确"),
    BACKEND_SYSTEM_USER_NOT_REGISTER(200008, "该用户尚未注册"),
    BACKEND_SYSTEM_USER_STATUS_ILLEGAL(200009, "用户状态非法");

    private int errCode;
    private String errMsg;

    BizExceptionEnum(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public static BizExceptionEnum fromCode(int code) {
        for (BizExceptionEnum c : BizExceptionEnum.values()) {
            if (c.errCode == code) {
                return c;
            }
        }
        return INTERNAL_SERVER_ERROR;
    }

    public static BizExceptionEnum fromDesc(String desc) {
        for (BizExceptionEnum c : BizExceptionEnum.values()) {
            if (c.errMsg.equals(desc)) {
                return c;
            }
        }
        return UNKNOWN_ERROR;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
