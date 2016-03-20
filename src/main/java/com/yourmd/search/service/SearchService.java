package com.yourmd.search.service;

import com.yourmd.search.elasticsearch.MessageSearcher;
import com.yourmd.search.model.DictionaryWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Evgeniy Bogdanov (ebogdanov@gmail.com).
 */

@RestController
public class SearchService {

    @Autowired
    MessageSearcher messageSearcher;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public List<String> search(@RequestParam(value="query") String query) {
        return messageSearcher.searchMatches(query);
    }
    
}
