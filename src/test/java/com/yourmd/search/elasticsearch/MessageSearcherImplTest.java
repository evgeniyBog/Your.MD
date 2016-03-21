package com.yourmd.search.elasticsearch;

import com.yourmd.search.Application;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Evgeniy Bogdanov (ebogdanov@gmail.com).
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class MessageSearcherImplTest extends TestCase {

    @Autowired
    MessageSearcher messageSearcher;

    @Autowired
    DictionaryIndexer dictionaryIndexer;

    @Before
    public void before() throws IOException {
        Boolean created = dictionaryIndexer.createIndex();

        if (created) {
            //Not the best way, but health status from Elasticsearch doesn't return if indexing have finished.
            //Had to introduce sleep for 3 seconds, while all the documents will be indexed.
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testMessageSearch() {

        List<String> matches = messageSearcher.searchMatches("I have a sore throat and headache.");

        Assert.assertEquals(2, matches.size());

        String firstMatch = matches.get(0);
        String seconMatch = matches.get(1);

        //Matches could be returned in different orders
        Assert.assertTrue(firstMatch.equals("headache") && seconMatch.equals("sore throat") ||
                seconMatch.equals("headache") && firstMatch.equals("sore throat"));

    }

    @Test
    public void testCapitalWordMessage() {
        List<String> matches = messageSearcher.searchMatches("I have HTN.");
        Assert.assertEquals(1, matches.size());
        String firstMatch = matches.get(0);
        //Matches could be returned in different orders
        Assert.assertTrue(firstMatch.equals("HTN"));
    }

    @Test
    public void testCaseSensitiveWordMessage() throws IOException {
        List<String> matches = messageSearcher.searchMatches("What is HyperTension?");
        Assert.assertEquals(1, matches.size());
        String firstMatch = matches.get(0);
        //Matches could be returned in different orders
        Assert.assertTrue(firstMatch.equals("hypertension"));
    }

}