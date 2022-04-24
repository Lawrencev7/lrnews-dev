package com.lrnews.article.fs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan("com.lrnews")
public class ArticleFSApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArticleFSApplication.class, args);
    }
}
