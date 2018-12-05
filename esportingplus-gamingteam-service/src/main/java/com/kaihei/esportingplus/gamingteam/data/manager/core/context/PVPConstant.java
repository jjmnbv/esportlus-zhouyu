package com.kaihei.esportingplus.gamingteam.data.manager.core.context;

/**
 * AOP的顺序配置类
 */
public class PVPConstant {

    public static final String TEAM_SEQUENCE_PREFIX = "GT";

    /* 清理JVM */
    public static final int PVP_TEAM_CLEAN_AOP_ORDER = 11;

    /* 缓存加载前、缓存存储后 */

    public static final int AFTER_RESTORE_PERFORMER_AOP_ORDER = 101;

    /* 缓存加载 */

    public static final int PVP_TEAM_BEFORE_OPERATION_AOP_ORDER = 1001;
    public static final int PVP_TEAM_BEFORE_INIT_AOP_ORDER = 1002;

    public static final int PVP_TEAM_MQ_AOP_ORDER = 1003;

    public static final int PVP_TEAM_AFTER_OPERATION_AOP_ORDER = 1004;
    public static final int PVP_TEAM_AFTER_INIT_AOP_ORDER = 1005;

    /* 缓存加载后、缓存存储前 */

    public static final int BEFORE_RESTORE_ANALIZE_AOP_ORDER = 10001;

    public static final int PERFORMER_AFTER_AOP_ORDER = 10002;

    public static final int PVP_TEAM_MEMBER_OPERATION_AOP_ORDER = 10003;

    public static final int PERFORMER_BEFORE_AOP_ORDER = 10004;

    public static final int POPULATOR_ORDER = Integer.MAX_VALUE - 1000;
}
