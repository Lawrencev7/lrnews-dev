package com.lrnews.api.controller.article;

import com.lrnews.bo.ArticleBO;
import com.lrnews.graceresult.JsonResultObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Api(value = "Article business controller", tags = {"Article Service", "Controller"})
@RequestMapping("/article")
public interface ArticleControllerApi {
    @ApiOperation(value = "Publish article", notes = "Publish article for a log in user")
    @PostMapping("/publish")
    JsonResultObject publish(@RequestBody @Valid ArticleBO articleBO, BindingResult bindingResult);
}
