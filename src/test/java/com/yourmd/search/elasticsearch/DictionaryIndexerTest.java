package com.yourmd.search.elasticsearch;

import com.yourmd.search.Application;
import com.yourmd.search.dictionary.Dictionary;
import junit.framework.TestCase;
import org.elasticsearch.action.percolate.PercolateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * @author Evgeniy Bogdanov (ebogdanov@gmail.com).
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@PropertySource("classpath:project.properties")
public class DictionaryIndexerTest extends TestCase {

    @Autowired
    DictionaryIndexer dictionaryIndexer;

    @Autowired
    Client client;

    @Autowired
    Dictionary dictionary;

    @Value("${elasticsearch.index.name}")
    private String indexName;

    @Before
    public void before() throws IOException {
        Boolean created = dictionaryIndexer.createIndex();

        if (created) {
            //Not the best way, but health status from Elasticsearch doesn't return if indexing have finished.
            //Had to introduce sleep for 3 seconds, while all the documents will be indexed.
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testIndexExists() {
        Assert.assertTrue(client.admin().indices().prepareExists(indexName).execute().actionGet().isExists());
    }

    @Test
    public void testNumberOfIndexedWords() {
        Long docCount = dictionaryIndexer.getIndexedDocuments();
        Assert.assertEquals(docCount, dictionary.getSize());
    }

    @Test
    public void testDelete() {
        dictionaryIndexer.deleteIndex();
    }


}