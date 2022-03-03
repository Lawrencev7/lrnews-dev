package com.lrnews.api.controller.article;

import com.lrnews.graceresult.JsonResultObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Api(value = "Article portal controller", tags = {"Article Service", "Controller" ,"Portal"})
@RequestMapping("/portal/article")
public interface ArticlePortalControllerApi {
    @ApiOperation(value = "Portal query articles", notes = "Portal articles list.")
    @GetMapping("/articleList")
    JsonResultObject queryArticleList(@RequestParam String keyword, @RequestParam String category,
                                      @ApiParam(name = "page", value = "Current query page")
                                      @RequestParam Integer page,
                                      @ApiParam(name = "pageSize", value = "Current page size")
                                      @RequestParam Integer pageSize);

    @ApiOperation(value = "Query popular article", notes = "Query popular article for homepage.")
    @GetMapping("/queryPopularArticle")
    JsonResultObject queryPopularArticle();

    @ApiOperation(value = "Query articles for writer", notes = "Query articles list for one writer.")
    @GetMapping("/writerArticleList")
    JsonResultObject queryArticleListForWriter(@RequestParam String userId,
                                      @ApiParam(name = "page", value = "Current query page")
                                      @RequestParam Integer page,
                                      @ApiParam(name = "pageSize", value = "Current page size")
                                      @RequestParam Integer pageSize);


}
