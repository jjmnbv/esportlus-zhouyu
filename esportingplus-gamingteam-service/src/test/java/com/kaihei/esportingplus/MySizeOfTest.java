package com.kaihei.esportingplus;

import com.carrotsearch.sizeof.RamUsageEstimator;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.PVPFreeTeamBossMatchingVO;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.PVPFreeTeamMatchingRouteVO;
import java.util.ArrayList;

public class MySizeOfTest {

    public static void main(String[] args) {
        PVPFreeTeamBossMatchingVO vo = new PVPFreeTeamBossMatchingVO();
        vo.setUid("asdadafa");
        vo.setGameDanId(20);
        vo.setGameDanName("黄金一");
        vo.setTeamTypeId(1);
        vo.setTeamTypeDisplayName("王者荣耀-一局");
        vo.setGameZoneId(20);
        vo.setGameZoneName("微信大区-一区");
        System.out.println(RamUsageEstimator.sizeOf(vo));
        System.out.println("一个Integer对象大小为："+RamUsageEstimator.sizeOf(new Integer(1)));
        System.out.println("一个String对象大小为："+RamUsageEstimator.sizeOf(new String("a")));
        System.out.println("一个char对象大小为："+RamUsageEstimator.sizeOf(new char[1]));
        System.out.println("一个ArrayList对象大小为："+RamUsageEstimator.sizeOf(new ArrayList<>()));
        System.out.println("一个Object对象大小为："+RamUsageEstimator.sizeOf(new Object()));
        System.out.println("一个Long对象大小为："+RamUsageEstimator.sizeOf(new Long(10000000000L)));

        PVPFreeTeamMatchingRouteVO vo2 = new PVPFreeTeamMatchingRouteVO();
        vo2.setTeamTypeId(111);
        vo2.setGameZoneId(111);
        System.out.println(RamUsageEstimator.sizeOf(vo2));
        System.out.println(1024*1024*1024/50);
    }
}