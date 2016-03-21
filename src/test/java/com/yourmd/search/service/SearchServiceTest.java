package com.yourmd.search.service;

import com.yourmd.search.Application;
import com.yourmd.search.elasticsearch.DictionaryIndexer;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author Evgeniy Bogdanov (ebogdanov@gmail.com).
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class SearchServiceTest extends TestCase {

    @Autowired
    SearchService searchService;

    @Autowired
    DictionaryIndexer dictionaryIndexer;

    @Test
    public void searchTestMissingIndex() {
        dictionaryIndexer.deleteIndex();
        List<String> result = searchService.search("I have terrible headache");
        org.junit.Assert.assertTrue(result.size() >= 1);
    }
}