package com.kaihei.esportingplus.marketing.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
public class ElasticsearchConfig {

    private final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchConfig.class);

    @Value("${elasticsearch.hosts}")
    private String hosts;

    @Value("${elasticsearch.cluster.name}")
    private String clusterName;

    @PostConstruct
    void init() {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }


    @Bean(name = "transportClient")
    public TransportClient client() {
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
        initNodes(client, hosts);
        return client;
    }

    private void initNodes(TransportClient client, String hosts) {
        String[] hostAndPorts = hosts.split(",");
        Arrays.asList(hostAndPorts).forEach(v -> {
            String[] array = v.split(":");
            if (array.length == 2) {
                try {
                    client.addTransportAddress(new TransportAddress(InetAddress.getByName(array[0]),
                            Integer.parseInt(array[1])));
                } catch (UnknownHostException e) {
                    LOGGER.error(String.format("初始化elastic-search节点[%s]异常", array.toString()), e);
                }
            }
        });
    }
}