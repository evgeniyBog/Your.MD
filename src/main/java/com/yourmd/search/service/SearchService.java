package com.yourmd.search.service;

import com.yourmd.search.model.DictionaryWord;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Evgeniy Bogdanov (ebogdanov@gmail.com).
 */

@RestController
public class SearchService {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value = "/greeting", method = RequestMethod.GET)
    public DictionaryWord greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new DictionaryWord(counter.incrementAndGet(),
                String.format(template, name));
    }
    
}
