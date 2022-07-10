package com.lrnews.article.fs.consumer;

import com.lrnews.api.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class ArticleFSConsumer {
    Logger logger = LoggerFactory.getLogger(ArticleFSConsumer.class);

    public static final String DOWNLOAD_ROUTING_KEY = "article.download.do";
    public static final String DELETE_ROUTING_KEY = "article.delete.do";

    final Consumer<String> delete;
    final Consumer<String> download;

    public ArticleFSConsumer(Consumer<String> delete, Consumer<String> download) {
        this.delete = delete;
        this.download = download;
    }

    @RabbitListener(queues = {RabbitMQConfig.QUEUE_ARTICLE_FS})
    public void watchQueue(String payload, Message message) {
        logger.info(" ---> Received message");
        matcher(message.getMessageProperties().getReceivedRoutingKey()).accept(payload);
        logger.info(" <--- Watch Exit");
    }

    private Consumer<String> matcher(String routingKey){
        return routingKey.equalsIgnoreCase(DOWNLOAD_ROUTING_KEY) ? download :
                routingKey.equalsIgnoreCase(DELETE_ROUTING_KEY) ? delete :
                (Consumer<String>) s -> logger.error("Match routing key failed.");
    }
}
