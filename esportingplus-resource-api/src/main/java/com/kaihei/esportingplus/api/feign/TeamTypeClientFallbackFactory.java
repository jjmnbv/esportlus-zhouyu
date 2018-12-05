package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.params.freeteam.TeamTypeAppQueryParams;
import com.kaihei.esportingplus.api.params.freeteam.TeamTypeBackendQueryParams;
import com.kaihei.esportingplus.api.params.freeteam.TeamTypeParams;
import com.kaihei.esportingplus.api.vo.freeteam.CreateTeamGameTeamTypeVO;
import com.kaihei.esportingplus.api.vo.freeteam.TeamTypeDetailVO;
import com.kaihei.esportingplus.api.vo.freeteam.TeamTypeSimpleVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import feign.hystrix.FallbackFactory;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 车队类型 fallback
 * @author liangyi
 */
@Component
public class TeamTypeClientFallbackFactory
        implements FallbackFactory<TeamTypeServiceClient> {

    private static final Logger logger = LoggerFactory
            .getLogger(TeamTypeClientFallbackFactory.class);

    @Override
    public TeamTypeServiceClient create(Throwable throwable) {

        return new TeamTypeServiceClient() {

            /**
             * Python后台调用--根据 id 查询车队类型
             *
             * @param teamTypeId 车队类型 id
             */
            @Override
            public ResponsePacket<CreateTeamGameTeamTypeVO> getCreateTeamGameTeamTypeVOById(
                    Integer teamTypeId) {
                return ResponsePacket.onHystrix(throwable);
            }

            @Override
            public ResponsePacket<TeamTypeDetailVO> getTeamTypeById(Integer teamTypeId) {
                return ResponsePacket.onHystrix(throwable);
            }

            @Override
            public ResponsePacket<TeamTypeDetailVO> getTeamTypeDetail(
                    String token, TeamTypeAppQueryParams teamTypeAppQueryParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PagingResponse<TeamTypeSimpleVO>> getTeamTypeByPage(
                    TeamTypeBackendQueryParams teamTypeBackendQueryParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<TeamTypeSimpleVO>> getAllTeamTypeByBaojiIdentity(
                    String token, TeamTypeAppQueryParams teamTypeAppQueryParams) {
                return ResponsePacket.onHystrix();
            }


            @Override
            public ResponsePacket<Void> addTeamType(TeamTypeParams teamTypeParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> updateTeamType(
                    TeamTypeParams teamTypeParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> deleteTeamType(Integer teamTypeId) {
                return ResponsePacket.onHystrix();
            }

        };
    }
}
