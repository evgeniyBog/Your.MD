package com.yourmd.search.elasticsearch;

import com.yourmd.search.dictionary.Dictionary;
import org.elasticsearch.action.percolate.PercolateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * @author Evgeniy Bogdanov (ebogdanov@gmail.com).
 */
@PropertySource(value = "classpath:project.properties")
public class MessageSearcherImpl implements MessageSearcher {
    private static Logger logger = LoggerFactory.getLogger(DictionaryIndexerImpl.class);
    @Autowired
    Client client;

    @Value("${elasticsearch.index.name}")
    private String indexName;

    @Autowired
    Dictionary dictionary;

    @Override
    @Cacheable("dictionaryCache")
    public List<String> searchMatches(String message) {
        logger.info("Searching index " + indexName + " for message text: " + message);
        PercolateResponse response = executePercolatorSearch(message);
        List<String> results = new LinkedList<>();
        for (PercolateResponse.Match match : response.getMatches()) {
            results.add(dictionary.getValue(Integer.valueOf(match.getId().toString())));
        }
        logger.info("Found matches number: " + results.size());
        return results;
    }


    private PercolateResponse executePercolatorSearch(String message) {
        XContentBuilder xContentBuilder = null;
        try {
            xContentBuilder = jsonBuilder().startObject()
                    .field("doc").startObject()
                    .field("message", message)
                    .endObject()
                    .endObject();
        } catch (IOException e) {
            logger.error("Error appeared while creating search query", e);
        }

        return client.preparePercolate()
                .setIndices(indexName)
                .setDocumentType("words")
                .setSource(xContentBuilder).execute().actionGet();
    }

}
