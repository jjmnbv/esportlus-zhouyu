package com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.matching;

import static com.kaihei.esportingplus.common.constant.RedisKey.PVP_FREE_TEAM_MATCHING_BOSS_QUEUE;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.PVPFreeTeamBossMatchingVO;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.PVPFreeTeamMatchingRouteVO;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *@Description:
 *  PVP 免费车队匹配池，多台服务器之间需要保持系统时间一致，否则匹配超时有的提前有的延后
 *
 *@author  Orochi-Yzh
 *@dateTime  2018/11/16 14:40
*/
@Component
public class PVPFreeTeamMatchingPool{

    private static final Logger LOGGER = LoggerFactory.getLogger(
            PVPFreeTeamMatchingPool.class);

    private CacheManager cacheManager = CacheManagerFactory.create();

    /**
     * 1.单生产者，单消费者 用 LinkedBlockingqueue
     *  适用此场景，请求到哪个节点，就加到哪个节点队列里面
     *  不用考虑重复问题，因为匹配要加锁，后续拿到消息后还要校验匹配的匹配记录 决定是否丢弃
     * 2.多生产者，单消费者 用 LinkedBlockingqueue
     * 3.单生产者 ，多消费者 用 ConcurrentLinkedQueue
     * 4.多生产者 ，多消费者 用 ConcurrentLinkedQueue
     *
     * 定制容量：防止OOM 防止被没带脑子上班然后被K.O掉
     * PVPFreeTeamMatchingVO 全字段都有值的情况下占用50byte左右(具体查看MySizeOfTest类)
     * 生产环境JVM配置为2G,此队列允许他占用1G,留1G给别人干活
     * 得出容量为：1* 1024*1024*1024/50 = 21474836 (约为2000w个)
     *  目标生产环境两个实例，理论上可以在存储4000w用户，等那时候都上市美国，冲出地球了
     */
    static LinkedBlockingQueue<PVPFreeTeamMatchingRouteVO> matchingQueue = new LinkedBlockingQueue<>(20000000);

    /**
     *@Description: 往匹配池里面丢用户
     *@param: [vo]
     *@return: void
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/11/16 11:01
    */
    public void pushMatchingPool(PVPFreeTeamBossMatchingVO vo) {

        // 加入到 redis 队列
        cacheManager.lpush(PVP_FREE_TEAM_MATCHING_BOSS_QUEUE + vo.getSettlementType()
                + ":" + vo.getTeamTypeId() + ":" + vo.getGameZoneId(), vo);

        //阻塞放入，满了就阻塞
        PVPFreeTeamMatchingRouteVO route = PVPFreeTeamMatchingRouteVO.builder()
                .settlementType(vo.getSettlementType())
                .teamTypeId(vo.getTeamTypeId())
                .gameZoneId(vo.getGameZoneId())
                .build();
        matchingQueue.offer(route);
    }

    /**
     *@Description: 系统重启后把已经匹配的用户捡起来丢到内存
     * 防止系统重启丢失已经匹配但没超时的用户,永远无法匹配
     *@param: [vo]
     *@return: void
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/11/16 11:01
     */
    public void pickMatchingsIntoPool() {

        Iterable<String> matchingedBoss = cacheManager.redissonClient().getKeys()
                .getKeysByPattern(PVP_FREE_TEAM_MATCHING_BOSS_QUEUE + "*");

        if(matchingedBoss.iterator().hasNext()){
            LOGGER.info("系统重启后发现有用户在匹配池里面，赶紧捡起来消费掉不然要被安排");
            //此队列无法确定大小，防止队列过大阻碍系统启动,考虑异步
            //由于队列消费过快，如果刚pop出来的时候系统重启,极大可能会丢失一个已匹配的用户；
            //也可能重新入列的时候有部分key为空：pvp:freeteam:matching:boss:null:14,导致路由失败。
            //以丢失一个匹配用户为代价，换取系统reload的可靠性，否则，重启后需要把整个匹配队列清除，
            // 完全从匹配历史reload到list队列中，防止队列重复。
            CompletableFuture.runAsync(()->
                matchingedBoss.forEach(key->{
                    long llen = cacheManager.llen(key);
                    for(int i = 0; i < llen; i++){
                        PVPFreeTeamBossMatchingVO redisMatchingVO = cacheManager.rpop(key,
                                PVPFreeTeamBossMatchingVO.class);

                        LOGGER.info("开始加载匹配用户，key: {}，uid: {}",key,redisMatchingVO.getUid());
                        pushMatchingPool(redisMatchingVO);
                    }
                    LOGGER.info("匹配用户加载完毕");

            }));
        }

    }

}
