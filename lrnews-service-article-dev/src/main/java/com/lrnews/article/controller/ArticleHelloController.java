package com.lrnews.article.controller;

import com.lrnews.api.config.RabbitMQConfig;
import com.lrnews.api.controller.BaseController;
import com.lrnews.api.controller.HelloControllerApi;
import com.lrnews.api.controller.user.UserInfoControllerApi;
import com.lrnews.graceresult.JsonResultObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produce")
public class ArticleHelloController extends BaseController implements HelloControllerApi {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UserInfoControllerApi msUser; // Micro-service User. Target interface must mark with @FeignClient

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

//    @RequestMapping("/test")
//    public String test(){ // Version 1
//        List<ServiceInstance> instances = discoveryClient.getInstances(SERVICE_USER);
//        ServiceInstance userService = instances.get(0);
//        System.out.println("userService.getHost() = " + userService.getHost() + ", userService.getPort() = " + userService.getPort());
//        String url = "http://" + userService.getHost() + ":" + userService.getPort() + "/hello/test";
//        System.out.println(url);
//        ResponseEntity<String> entity = restOperations.getForEntity(url, String.class);
//        return entity.getBody();
//    }

    @RequestMapping("/test")
    public JsonResultObject test(){ // Version 2 - Feign
        return JsonResultObject.ok(msUser.getUserCommonInfo(""));
    }
}
