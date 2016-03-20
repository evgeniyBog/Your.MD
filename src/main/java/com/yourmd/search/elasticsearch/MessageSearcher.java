package com.yourmd.search.elasticsearch;

import java.util.List;

/**
 * @author Evgeniy Bogdanov (ebogdanov@gmail.com).
 */
public interface MessageSearcher {

    /**
     * Search for words from dictionary using case-insensitive ElasticSearch queries
     *
     * @param message - request string to be percolated through dictionary
     * @return List of matched vocabulary words
     */
    List<String> searchMatches(String message);
}
