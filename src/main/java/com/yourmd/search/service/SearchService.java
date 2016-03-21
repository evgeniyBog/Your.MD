package com.yourmd.search.service;

import com.yourmd.search.elasticsearch.DictionaryIndexer;
import com.yourmd.search.elasticsearch.MessageSearcher;
import com.yourmd.search.model.DictionaryWord;
import org.elasticsearch.indices.IndexMissingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Main REST service with only one method to search
 * @author Evgeniy Bogdanov (ebogdanov@gmail.com).
 */

@RestController
public class SearchService {

    @Autowired
    MessageSearcher messageSearcher;

    @Autowired
    DictionaryIndexer dictionaryIndexer;

    /**
     * Searches for given string in index.
     * If index was not created on Service startup, method will create index.
     *
     * @param query - string query to search through dictionary
     * @return List of String results, representing dictionary words that have matched query
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public List<String> search(@RequestParam(value="query") String query) {
        List<String> results;
        try {
            results = messageSearcher.searchMatches(query);
        } catch (IndexMissingException e) {
            dictionaryIndexer.createIndex();
            results = messageSearcher.searchMatches(query);
        }
        return results;
    }
    
}
