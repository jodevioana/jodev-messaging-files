package com.jodev.messaging.resourceserver.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.util.ArrayList;


@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Value("${spring.data.mongodb.host}")
    private String databaseHost;

    @Value("${spring.data.mongodb.port}")
    private Integer databasePort;

    @Value("${spring.data.mongodb.username}")
    private String databaseUser;

    @Value("${spring.data.mongodb.password}")
    private String databasePass;

    @Bean
    public GridFsTemplate gridFsTemplate() throws Exception {
        return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
    }

    @Override
    public MongoClient mongoClient() {

        return new MongoClient(new ServerAddress(databaseHost, databasePort), new ArrayList<MongoCredential>() {

            {
                add(MongoCredential.createCredential(databaseUser, databaseName, databasePass.toCharArray()));
            }
        });
    }

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }
}
