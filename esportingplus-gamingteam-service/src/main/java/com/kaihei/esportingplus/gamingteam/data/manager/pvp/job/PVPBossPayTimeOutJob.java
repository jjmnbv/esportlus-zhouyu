//package com.kaihei.esportingplus.gamingteam.data.manager.pvp.job;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.dangdang.ddframe.job.api.ShardingContext;
//import com.dangdang.ddframe.job.api.simple.SimpleJob;
//import com.kaihei.esportingplus.gamingteam.data.manager.pvp.PVPTeamMemberCacheManager;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * 老板支付超时任务
// */
//@Component
//public class PVPBossPayTimeOutJob implements SimpleJob {
//
//
//    @Autowired
//    private PVPTeamMemberCacheManager pvpTeamMemberCacheManager;
//
//    /**
//     * 执行作业.
//     *
//     * @param shardingContext 分片上下文
//     */
//    @Override
//    public void execute(ShardingContext shardingContext) {
//        JSONObject jsonObject = JSON.parseObject(shardingContext.getJobParameter());
//        String teamSequence = jsonObject.getString("teamSequence");
//        String uid = jsonObject.getString("uid");
//        Integer gameId = jsonObject.getInteger("gameId");
//
//        //下车
//        pvpTeamMemberCacheManager.removeRedisTeamMember(gameId, teamSequence, uid);
//
//    }
//}
