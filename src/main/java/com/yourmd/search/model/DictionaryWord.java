package com.yourmd.search.model;

/**
 * @author Evgeniy Bogdanov (ebogdanov@gmail.com).
 */
public class DictionaryWord {

    private final long id;
    private final String content;

    public DictionaryWord(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

}
