package com.lrnews.article.controller;

import com.lrnews.api.config.RabbitMQConfig;
import com.lrnews.api.controller.HelloControllerApi;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produce")
public class HelloController implements HelloControllerApi {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public Object hello() {
        return "Hello Article Controller";
    }

    @Override
    public Object helloRabbitMQ() {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_ARTICLE,
                RabbitMQConfig.BINDING_ROUTING_KEY,
                "This Message is send by test function in HelloController - ArticleService");
        return null;
    }
}
