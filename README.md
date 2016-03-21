# Your.MD Dictionary Search

##Pre-requirements:

1.	Java 8
2.  [Elasticsearch 1.7.5](https://www.elastic.co/downloads/past-releases/elasticsearch-1-7-5)

##Technologies used:
Java, Spring boot, Gradle, Elasticsearch, Ehcache, Logback

##Running application and tests

1. Start Elasticsearch 1.7.5 by running [ELASTIC_HOME]/bin/elasticsearch
2. Run application
    - Execute from command line: 'gradlew bootRun'
    - Package and run: 'gradlew build && java -jar build/libs/your.md-0.0.1.jar'
3. Query dictionary using any browser passing url:
    - http://localhost:8080/search?query=I have a sore throat and headache.

##Implementation details:
Indexing appears once on first application startup (if ElasticSearch is running).
Dictionary is cached from file into memory HashMap, and used by service to: a) Add words into Elasticsearch index b) Find word by id from index.
Each query from user is cached using Ehcache. So no index hit would occure in case of repeating query.

Note: Elasticsearch was started AFTER service start-up, indexing will occure on first search

Note: Sense tool could be installed as plugin for Chrome to work with index. Useful command:
- Searching
```
GET yourmd/words/_percolate
{
    "doc" : {
        "message" : "What is Hypertension?"
    }
}
```

- Return index statistics
``` 
GET yourmd/_stats
```

- Delete index
```
DELETE yourmd
```
