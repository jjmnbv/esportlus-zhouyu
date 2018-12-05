package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.params.freeteam.ShareCopywriterConfigParams;
import com.kaihei.esportingplus.api.vo.freeteam.InvitionShareConfigVo;
import com.kaihei.esportingplus.api.vo.freeteam.ShareCopywriterConfigVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import feign.hystrix.FallbackFactory;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TaskConfigServiceFallbackFactory implements FallbackFactory<TaskConfigServiceClient> {

    @Override
    public TaskConfigServiceClient create(Throwable throwable) {
        return new TaskConfigServiceClient() {
            @Override
            public ResponsePacket<Void> onlineShareInvite(Integer id, Integer type) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> updateShareInvite(
                    InvitionShareConfigVo invitionShareConfigVo) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<InvitionShareConfigVo> findShareTaskConfig(
                    String categoryCode) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<InvitionShareConfigVo> findShareTaskConfigById(Integer id) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<InvitionShareConfigVo>> findAllShareTaskConfig() {
                return ResponsePacket.onHystrix(throwable);
            }

            @Override
            public ResponsePacket<List<InvitionShareConfigVo>> findAllShareTaskConfigForApp(
                    String token) {
                return ResponsePacket.onHystrix(throwable);
            }

            @Override
            public ResponsePacket<List<ShareCopywriterConfigVO>> findShareCopywriterConfigForBack(
                    String scene) {
                return ResponsePacket.onHystrix(throwable);
            }

            @Override
            public ResponsePacket<Void> saveThirdShareConfig(ShareCopywriterConfigParams params) {
                return ResponsePacket.onHystrix(throwable);
            }

            @Override
            public ResponsePacket<List<ShareCopywriterConfigVO>> findShareCopywriterConfigForApp(
                    String token, String scene) {
                return ResponsePacket.onHystrix(throwable);
            }

            @Override
            public ResponsePacket<ShareCopywriterConfigVO> findShareCopywriterConfigById(
                    Integer id) {
                return ResponsePacket.onHystrix(throwable);
            }


        };
    }
}
