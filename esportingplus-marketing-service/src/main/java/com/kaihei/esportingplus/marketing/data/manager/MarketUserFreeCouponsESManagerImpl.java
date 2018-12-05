package com.kaihei.esportingplus.marketing.data.manager;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.marketing.domian.entity.MarketUserFreeCoupons;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author zl.zhao
 * @description:
 * @date: 2018/11/20 15:27
 */
@Component
public class MarketUserFreeCouponsESManagerImpl implements MarketUserFreeCouponsESManager {

    Logger logger = LoggerFactory.getLogger(MarketUserFreeCouponsESManagerImpl.class);

    private final String INDEX_NAME = "market_free_coupons_v1";
    private final String INDEX_TYPE = "free_coupons";
    private final String INDEX_PARTICIPLE = "participle";

    @Resource
    private TransportClient client;

    @Override
    public Map<String, Object> getUserFreeCouponsES(Long couponsId) {
        GetRequestBuilder builder = client.prepareGet(INDEX_NAME, INDEX_TYPE, couponsId.toString());
        logger.debug("getUserFreeCouponsES >> response >> {}", builder.get().getSource());

        return builder.get().getSource();
    }


    @Override
    public void delUserFreeCouponsES(Long couponsId) {
        DeleteRequestBuilder builder = client.prepareDelete(INDEX_NAME, INDEX_TYPE, couponsId.toString());
        logger.debug("delUserFreeCouponsES >> response >> {}", builder.get());
    }


    @Override
    public void saveUserFreeCouponsES(List<MarketUserFreeCoupons> couponsList, String dataType) {
        try {
            for (MarketUserFreeCoupons coupons : couponsList) {
                String json = freeCouponsESJson(coupons, dataType);
                IndexResponse response = client.prepareIndex(INDEX_NAME, INDEX_TYPE, coupons.getId().toString()).
                        setSource(JSON.parseObject(json, Map.class)).execute().actionGet();
                logger.debug("saveUserFreeCouponsES >> response >> {}", response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String freeCouponsESJson(MarketUserFreeCoupons coupons, String dataType) throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder().startObject()
                .field("id", coupons.getId())
                .field("uid", coupons.getUid())
                .field("status", coupons.getStatus())
                .field("source", coupons.getSource())
                .field("type", coupons.getType())
                .field("invalidTime", coupons.getInvalidTime())
                .field("createTime", coupons.getCreateTime())
                .field("updateTime", coupons.getUpdateTime())
                .field("dataType", dataType)
                .field("participle", coupons.getUid() + dataType).endObject();

        return builder.string();
    }

    @Override
    public Integer termQuery(String value) {
        QueryBuilder queryBuilder = QueryBuilders.termQuery(INDEX_PARTICIPLE, value);
        SearchResponse response = client.prepareSearch(INDEX_NAME).setQuery(queryBuilder).setSize(1000).get();
        Integer count = (int) response.getHits().totalHits;
        return count;
    }

    /**
     * 批量查询
     *
     * @param couponsIds 次数券id集合
     * @return
     */
    @Override
    public List<MarketUserFreeCoupons> getUserFreeCouponsESByBatch(List<Long> couponsIds) {
        //查询对象拼接
        MultiGetRequestBuilder builder  = client.prepareMultiGet();
        for(Long id : couponsIds) {
            builder = builder.add(INDEX_NAME, INDEX_TYPE, id.toString());
        }
        MultiGetResponse multiGetItemResponses  = builder.get();

        logger.debug("getUserFreeCouponsES >> response >> {}", JacksonUtils.toJson(multiGetItemResponses.getResponses()));

        List<MarketUserFreeCoupons> couponsList = Lists.newLinkedList();
        for (MultiGetItemResponse itrm : multiGetItemResponses.getResponses()) {
            GetResponse gr = itrm.getResponse();
            if (gr != null && gr.isExists()) {
                String json = gr.getSourceAsString();
                MarketUserFreeCoupons coupons = JacksonUtils.toBean(json.toString(), MarketUserFreeCoupons.class);
                couponsList.add(coupons);
            }
        }
            return couponsList;
    }

    /**
     * 批量删除
     *
     * @param couponsIds 次数券id集合
     * @return
     */
    @Override
    public void delUserFreeCouponsESByBatch(List<Long> couponsIds) {
        BulkRequestBuilder builder  = client.prepareBulk();
        for(Long id : couponsIds) {
            builder = builder.add(client.prepareDelete(INDEX_NAME, INDEX_TYPE, id.toString()).request());
        }
        BulkResponse bulkResponse  = builder.get();

        logger.debug("getUserFreeCouponsES >> response >> {}", JacksonUtils.toJson(bulkResponse.status()));
    }
}
