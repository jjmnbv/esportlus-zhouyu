package com.kaihei.esportingplus.gamingteam.api.enums;

/**
 * PVP 车队队员外显状态
 * @author liangyi
 */
public interface PVPTeamMemberDisplayStatus {

    /**
     * 免费车队老板
     */
    interface PVPFreeMemberBoss {
        /**
         * 待准备
         */
        int WAITING_PREPARE = 0;

        /**
         * 已准备
         */
        int PREPARED = 1;

        /**
         * 服务进行中
         */
        int SERVICE_IN_PROGRESS = 2;

        /**
         * 开车后退出
         */
        int TEAM_STARTED_QUIT = 3;
    }

    /**
     * 免费车队暴鸡
     */
    interface PVPFreeMemberBaoji {
        /**
         * 待准备
         */
        int WAITING_PREPARE = 0;

        /**
         * 已准备
         */
        int PREPARED = 1;

        /**
         * 服务进行中
         */
        int SERVICE_IN_PROGRESS = 2;

        /**
         * 开车后退出
         */
        int TEAM_STARTED_QUIT = 3;
    }

    /**
     * 免费车队队长
     */
    interface PVPFreeMemberLeader {

        /**
         * 已准备(待开车)
         */
        int PREPARED = 0;

        /**
         * 立即开车, 此时所有人都已准备且车队已满员
         */
        int START_TEAM_NOW = 1;

        /**
         * 服务进行中
         */
        int SERVICE_IN_PROGRESS = 2;
    }

}
