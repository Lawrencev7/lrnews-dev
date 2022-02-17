package com.lrnews.api.controller.article;

import com.lrnews.bo.ArticleBO;
import com.lrnews.bo.ArticleQueryBO;
import com.lrnews.graceresult.JsonResultObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Api(value = "Article business controller", tags = {"Article Service", "Controller"})
@RequestMapping("/article")
public interface ArticleControllerApi {
    @ApiOperation(value = "Publish article", notes = "Publish article for a log in user")
    @PostMapping("/publish")
    JsonResultObject publish(@RequestBody @Valid ArticleBO articleBO, BindingResult bindingResult);

    @ApiOperation(value = "Query articles", notes = "Query articles published by someone user")
    @PostMapping("/queryMyArticles")
    JsonResultObject queryMyArticles(@RequestBody @Valid ArticleQueryBO query, BindingResult bindingResult);

    @ApiOperation(value = "Query articles", notes = "Query all articles. Only for admin")
    @PostMapping("/queryAllArticles")
    JsonResultObject queryAllArticles(@RequestParam Integer status, @RequestParam Integer page, @RequestParam Integer pageSize);

    @ApiOperation(value = "Do review", notes = "Giving a review result by admin")
    @PostMapping("/doReview")
    JsonResultObject doReview(@RequestParam String articleId, @RequestParam Integer reviewResult);

    @ApiOperation(value = "Do review", notes = "Giving a review result by admin")
    @PostMapping("/deleteArticle")
    JsonResultObject deleteArticle(@RequestParam String userId, @RequestParam String articleId);

    @ApiOperation(value = "Do review", notes = "Giving a review result by admin")
    @PostMapping("/withdraw")
    JsonResultObject withdraw(@RequestParam String userId, @RequestParam String articleId);
}
