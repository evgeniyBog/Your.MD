package com.yourmd.search.configuration;

import com.yourmd.search.dictionary.Dictionary;
import com.yourmd.search.elasticsearch.DictionaryIndexer;
import com.yourmd.search.elasticsearch.DictionaryIndexerImpl;
import com.yourmd.search.elasticsearch.MessageSearcher;
import com.yourmd.search.elasticsearch.MessageSearcherImpl;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.annotation.Resource;

/**
 * @author Evgeniy Bogdanov (ebogdanov@gmail.com).
 */
@Configuration
@PropertySource(value = "classpath:project.properties")
@EnableElasticsearchRepositories(basePackages = "com.yourmd.search")
public class AppConfiguration {
    @Resource
    private Environment environment;

    @Bean
    public Client client() {
        TransportClient client = new TransportClient();
        TransportAddress address = new InetSocketTransportAddress(environment.getProperty("elasticsearch.host"), Integer.parseInt(environment.getProperty("elasticsearch.port")));
        client.addTransportAddress(address);
        return client;
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(client());
    }

    @Bean
    public Dictionary dictionary() {
        return Dictionary.INSTANCE;
    }

    @Bean
    public DictionaryIndexer dictionaryIndexer() {
        return new DictionaryIndexerImpl();
    }

    @Bean
    @Scope("prototype")
    public MessageSearcher messageSearcher() {
        return new MessageSearcherImpl();
    }
}