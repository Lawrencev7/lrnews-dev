package com.lrnews.article.controller;

import com.lrnews.api.config.RabbitMQConfig;
import com.lrnews.api.controller.BaseController;
import com.lrnews.api.controller.HelloControllerApi;
import com.lrnews.api.controller.user.UserInfoControllerApi;
import com.lrnews.article.stream.StreamService;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.pojo.AppUser;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produce")
public class ArticleHelloController extends BaseController implements HelloControllerApi {

    private final RabbitTemplate rabbitTemplate;

    private final StreamService streamService;

    private final UserInfoControllerApi msUser; // Micro-service User. Mark target interface with @FeignClient

    public ArticleHelloController(RabbitTemplate rabbitTemplate, UserInfoControllerApi msUser, StreamService streamService) {
        this.rabbitTemplate = rabbitTemplate;
        this.msUser = msUser;
        this.streamService = streamService;
    }

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

//    原始方法：使用完整的url路径访问微服务
//    @RequestMapping("/test")
//    public String test(){
//        List<ServiceInstance> instances = discoveryClient.getInstances(SERVICE_USER);
//        ServiceInstance userService = instances.get(0);
//        System.out.println("userService.getHost() = " + userService.getHost() + ", userService.getPort() = " + userService.getPort());
//        String url = "http://" + userService.getHost() + ":" + userService.getPort() + "/hello/test";
//        System.out.println(url);
//        ResponseEntity<String> entity = restOperations.getForEntity(url, String.class);
//        return entity.getBody();
//    }
//    使用Feign后直接通过调用的形式访问

    @RequestMapping("/test")
    public JsonResultObject test(){ // Version 2 - Feign
        return JsonResultObject.ok(msUser.getUserCommonInfo(""));
    }

    @RequestMapping("/stream")
    public JsonResultObject testStream(){
        for (int i = 0; i < 10; i++) {
            streamService.send("[No." + i + "-msg]");
        }
        return JsonResultObject.ok();
    }
}
