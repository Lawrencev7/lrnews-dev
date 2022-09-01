package com.lrnews.search.controller;

import com.lrnews.api.controller.HelloControllerApi;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.search.pojo.EsIdx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController implements HelloControllerApi {
    final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    private final ElasticsearchTemplate esTemp;

    public HelloController(ElasticsearchTemplate esTemp) {
        this.esTemp = esTemp;
    }

    public Object hello() {
        return JsonResultObject.ok("Test for file service");
    }

    @RequestMapping("/createIndex")
    public Object createIndex(){
        esTemp.createIndex(EsIdx.class);
        return JsonResultObject.ok();
    }

    @RequestMapping("/deleteIndex")
    public Object deleteIndex(){
        esTemp.createIndex(EsIdx.class);
        return JsonResultObject.ok();
    }



}
