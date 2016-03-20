package com.yourmd.search.elasticsearch;

import com.yourmd.search.dictionary.Dictionary;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.percolator.PercolatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;

/**
 * @author Evgeniy Bogdanov (ebogdanov@gmail.com).
 */
@PropertySource(value = "classpath:project.properties")
public final class DictionaryIndexerImpl implements DictionaryIndexer {
    private static Logger logger = LoggerFactory.getLogger(DictionaryIndexerImpl.class);

    private String mapping;
    private String settings;

    @Autowired
    Dictionary dictionary;

    @Autowired
    Client client;

    @Value("${elasticsearch.mapping.location}")
    private String mappingFileLocation;
    @Value("${elasticsearch.settings.location}")
    private String settingsFileLocation;
    @Value("${elasticsearch.index.name}")
    private String indexName;

    public DictionaryIndexerImpl() {
    }

    @Override
    public Boolean createIndex() {
        if (getIndexedDocuments().equals(dictionary.getSize())) {
            return false;
        }

        try {
            this.mapping = new String(Files.readAllBytes(Paths.get(mappingFileLocation).toAbsolutePath()));
        } catch (IOException e) {
            logger.error("Unable to read ElasticSearch mapping file", e);
        }

        try {
            this.settings = new String(Files.readAllBytes(Paths.get(settingsFileLocation).toAbsolutePath()));
        } catch (IOException e) {
            logger.error("Unable to read ElasticSearch settings file", e);
        }

        CreateIndexRequestBuilder indexBuilder = client.admin().indices().prepareCreate(indexName);
        indexBuilder.setSettings(settings);
        indexBuilder.addMapping("words", mapping);
        indexBuilder.execute().actionGet();

        try {
            bulkIndex(dictionary.getDictionary());
        } catch (IOException e) {
            logger.error("Failed to create index: ", e);
        }
        return true;
    }

    @Override
    public void forceCreateIndex() {
        deleteIndex();
        createIndex();
    }

    @Override
    public void deleteIndex() {
        client.admin().indices().prepareDelete(indexName)
                .setIndicesOptions(IndicesOptions.fromOptions(true, true, true, false)).get();
    }

    @Override
    public Long getIndexedDocuments() {
        IndicesStatsResponse indicesStatsResponse = client.admin().indices().prepareStats(indexName).execute().actionGet();
        return indicesStatsResponse.getIndex(indexName).getPrimaries().docs.getCount();
    }

    private void bulkIndex(Map<Integer, String> dictionary) throws IOException {
        BulkProcessor bulkProcessor = BulkProcessor.builder(
                client,
                new BulkProcessor.Listener() {
                    @Override
                    public void beforeBulk(long executionId, BulkRequest request) {
                        logger.info("Processing: " + request.numberOfActions());
                    }

                    @Override
                    public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {

                    }

                    @Override
                    public void afterBulk(long executionId, BulkRequest request, Throwable failure) {

                    }
                })
                .setBulkActions(5000)
                .setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB))
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                .setConcurrentRequests(1)
                .build();


        for (Map.Entry<Integer, String> word : dictionary.entrySet()) {
            if (word.getKey() % 1000 == 0) {
                logger.info("... Indexed " + word.getKey());
            }
            IndexRequest indexRequest =
                    new IndexRequest(indexName, PercolatorService.TYPE_NAME, word.getKey().toString()).source(
                            jsonBuilder()
                                    .startObject()
                                    .field("query", matchPhraseQuery("message", word.getValue()))
                                    .endObject());
            bulkProcessor.add(indexRequest);
        }
        bulkProcessor.close();
    }

}
