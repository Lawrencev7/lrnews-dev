package com.lrnews.article.fs.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class GridFSConfig {

    @Value("${spring.data.mongodb.database}")
    private String mongoDB;

    @Bean
    private String getMongoDB(){
        return mongoDB;
    }

    @Bean
    public GridFSBucket gridFSBucket(MongoClient client){
        MongoDatabase mongoDatabase = client.getDatabase(getMongoDB());
        return GridFSBuckets.create(mongoDatabase);
    }
}
