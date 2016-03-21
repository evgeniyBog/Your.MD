package com.yourmd.search;

import com.yourmd.search.elasticsearch.DictionaryIndexer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Evgeniy Bogdanov (ebogdanov@gmail.com).
 */
@Configuration
@ComponentScan(basePackages = "com.yourmd.search")
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
//        context.getBean(DictionaryIndexer.class).createIndex();
    }
}