package com.yourmd.search.elasticsearch;

/**
 * @author Evgeniy Bogdanov (ebogdanov@gmail.com).
 */

/**
 *
 */
public interface DictionaryIndexer {

    /**
     * Create an index If and Only If number of existing indexed documents
     * doesn't match number of documents in dictionary
     *
     * Index procedure implemented as bulk process, this is speed up indexing significantly
     *
     * @return - true if index is recreated, false if index is the same
     */
    Boolean createIndex();

    /**
     * Recreates the index regardless number of documents in the dictionary
     *
     */
    void forceCreateIndex();

    /**
     * Drops dictionary index
     *
     */
    void deleteIndex();


    /**
     * Returns amount of documents in the index.
     *
     * @return - number of documents in Index
     */
    Long getIndexedDocuments();
}
