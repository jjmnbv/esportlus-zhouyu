package com.kaihei.esportingplus.user.data.manager;

import com.alibaba.fastjson.JSON;
import com.kaihei.esportingplus.user.domain.entity.MembersUser;
import java.io.IOException;
import java.util.Map;
import javax.annotation.Resource;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 终端用户ES管理
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/25 15:38
 */
@Component
public class MembersUserESManagerImpl implements MembersUserESManager{

    Logger logger = LoggerFactory.getLogger(MembersUserESManagerImpl.class);

    private final String INDEX_NAME = "members_v1";
    private final String INDEX_TYPE = "users";

    @Resource
    private TransportClient client;

    @Override
    public Map<String, Object> getMembersUserES(String uid) {
        GetRequestBuilder builder  = client.prepareGet(INDEX_NAME,INDEX_TYPE,uid);
        logger.debug("getMembersUserES >> response >> {}",builder.get().getSource());

        return builder.get().getSource();
    }

    @Override
    public void saveMembersUserES(MembersUser membersUser) {
        try {
            String json = membersUserToESJson(membersUser);
            IndexResponse response = client.prepareIndex(INDEX_NAME, INDEX_TYPE, membersUser.getUid()).setSource(JSON.parseObject(json, Map.class)).execute().actionGet();
            logger.debug("saveMembersUserES >> response >> {}",response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String membersUserToESJson(MembersUser membersUser) throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder().startObject()
                .field("uid", membersUser.getUid())
                .field("username", membersUser.getUsername())
                .field("phone", membersUser.getPhone())
                .field("thumbnail", membersUser.getThumbnail())
                .field("sex", membersUser.getSex())
                .field("desc", membersUser.getDesc())
                .field("region", membersUser.getRegion())
                .field("chicken_id", membersUser.getChickenId())
                .field("age", membersUser.getAge())
                .field("constellation", membersUser.getConstellation())
                .field("birthday", membersUser.getBirthday()).endObject();

        return builder.string();
    }


}
