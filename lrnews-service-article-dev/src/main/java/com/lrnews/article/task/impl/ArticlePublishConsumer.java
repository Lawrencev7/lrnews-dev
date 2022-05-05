package com.lrnews.article.task.impl;

import com.lrnews.article.service.ArticleService;
import com.lrnews.article.task.RabbitLazyMQConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class ArticlePublishConsumer implements RabbitLazyMQConsumer {

    final Consumer<String> articlePublisher;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public ArticlePublishConsumer(ArticleService articleService) {
        this.articlePublisher = articleService::updateArticleToPublish;
    }

    @Override
    public void watchQueueDelay(String payload, Message message) {
        logger.info("Received message to update article published ---> articleId={}", payload);
        articlePublisher.accept( /* Article Id */ payload);
    }
}
