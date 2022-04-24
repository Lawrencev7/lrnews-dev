package com.lrnews.article.fs.controller;

import com.lrnews.api.config.RabbitLazyMQConfig;
import com.lrnews.api.config.RabbitMQConfig;
import com.lrnews.api.controller.HelloControllerApi;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/article-fs-hello")
public class HelloController implements HelloControllerApi {

    final RabbitTemplate rabbitTemplate;

    public HelloController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public Object hello() {
        return "Hello Article Controller";
    }

    @Override
    public Object helloRabbitMQ() {
        rabbitTemplate.convertAndSend(RabbitLazyMQConfig.EXCHANGE_ARTICLE_LAZY,
                RabbitLazyMQConfig.BINDING_ROUTING_KEY,
                "This message is send by lazy mq test function in HelloController - Article-fs Service");
        return "Done";
    }
}
