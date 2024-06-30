package com.pharmacyapp.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/13
 * @Time : 18:50
 **/

@Configuration
public class ElasticsearchConfig {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchConfig.class);

    @Autowired
    private Environment env;

    @Bean
    public RestHighLevelClient elasticsearchClient() {
        String host = env.getProperty("ELASTICSEARCH_HOST");
        String port = env.getProperty("ELASTICSEARCH_PORT");
        String username = env.getProperty("ELASTICSEARCH_USERNAME");
        String password = env.getProperty("ELASTICSEARCH_PASSWORD");

        logger.info("Elasticsearch Configuration - Host: {}, Port: {}, Username: {}", host, port, username);

        if (host == null || port == null || username == null || password == null) {
            logger.error("One or more Elasticsearch environment variables are missing.");
            throw new IllegalArgumentException("Elasticsearch environment variables are not set properly.");
        }

        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(host + ":" + port)
                .withBasicAuth(username, password)
                .build();

        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public ElasticsearchRestTemplate elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(elasticsearchClient());
    }
}
