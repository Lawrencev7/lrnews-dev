package com.lrnews.config.controller;

import com.lrnews.graceresult.JsonResultObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello(){
        return "Hello Config center Service";
    }

    @GetMapping("/help")
    public JsonResultObject help(){
        return JsonResultObject.ok("·文件的命名规范为：{application}-{profile}.{后缀}\n" +
                "·访问：{分支label}/{application}-{profile}.{后缀} 直接获得文件内容\n" +
                "·访问：/{application}/{profile}/{分支label} 返回Json格式的响应数据\n" +
                "·默认情况下认为分支label是master，如果不是需要配置或在访问时显示指出。");
    }
}
