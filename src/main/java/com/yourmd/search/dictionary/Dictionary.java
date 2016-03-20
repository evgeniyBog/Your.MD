package com.yourmd.search.dictionary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Evgeniy Bogdanov (ebogdanov@gmail.com).
 */
public enum Dictionary {
    INSTANCE;

    private static Logger logger;
    private static final String FILE_PATH = "src/main/resources/phrases";

    private static Map<Integer, String> dictionary;

    Dictionary() {
        loadDictionaryFromFile();
    }

    /**
     * Load dictionary from file
     * FILE_PATH relative file to path
     */
    private static void loadDictionaryFromFile() {
        Instant start = Instant.now();
        logger = LoggerFactory.getLogger(Dictionary.class);
        dictionary = new HashMap<>();
        logger.info("Starting to cache dictionary from " + FILE_PATH);
        Path dictionaryPath = Paths.get(FILE_PATH).toAbsolutePath();
        try (BufferedReader br = Files.newBufferedReader(dictionaryPath)) {
            String line;
            Integer index = 0;
            while ((line = br.readLine()) != null) {
                dictionary.put(index++, line);
            }
        } catch (IOException e) {
            logger.error("Could not retrieve dictionary from " + FILE_PATH, e);
        }
        Instant end = Instant.now();
        logger.info("Ended caching dictionary " +
                "size:" + dictionary.size()
                + " in " + Duration.between(start, end).toMillis() + " ms");
    }

    /**
     * Search for index of the word from dictionary
     * This implementation takes O(n) time to find index bu value
     *
     * @param value - word from dictionary
     * @return First matched index of the word.
     */
    public Integer getIndexOf(String value) {
        for (Map.Entry<Integer, String> entry : dictionary.entrySet())
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        return null;
    }


    /**
     * Retrieves the word using it's index in the dictionary
     *
     * @param index - index to search for a value
     * @return Retrieves Dictionary value by index (line number)
     */
    public String getValue(Integer index) {
        return dictionary.get(index);
    }

    /**
     * This method returns deep copy of dictionary (for indexing purpose)
     *
     * @return Map of id and words. deep copy of original map
     */
    public Map<Integer, String> getDictionary() {
        Map<Integer, String> copyMap = new HashMap<>();
        for (Map.Entry<Integer, String> originalEntry : dictionary.entrySet()) {
            copyMap.put(originalEntry.getKey(), originalEntry.getValue());
        }
        return copyMap;
    }

    /**
     * Retrieve size of dictionary
     * @return - size of dictionary
     */
    public Long getSize() {
        return new Long(dictionary.size());
    }

}
