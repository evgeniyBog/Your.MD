# Your.MD
##Coding assignment

##Pre-requirements:

1.	Java 8
2.  [Gradle 2.12](http://gradle.org/gradle-download/)
3.  [Elasticsearch 1.7.5](https://www.elastic.co/downloads/past-releases/elasticsearch-1-7-5)

##Technologies used:
Java, Spring boot, Gradle, Elasticsearch, Ehcache, Logback

##Instructions:
Elasticsearch server version should be 1.7.5 (Spring Boot doesnt support 2.x versions as of today)

##Running application and tests

1. Execute from command line: 'gradlew bootRun'
2. Package and run: 'gradlew build && java -jar build/libs/your.md-0.0.1.jar'
3. Running tests: 'gradle clean test -i'


##How it is working:
Indexing appears once on first application startup (if ElasticSearch is running)

Note: Elasticsearch should be started before service startup.

Note: Sense tool could be installed as plugin for Chrome to work with index. Useful command:
- Searching
'''
GET yourmd/words/_percolate
{
    "doc" : {
        "message" : "What is Hypertension?"
    }
}
'''

- Return index statistics
''' 
GET yourmd/_stats
'''

- Delete index
'''
DELETE yourmd
'''
