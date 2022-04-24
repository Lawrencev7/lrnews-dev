package com.lrnews.api.controller.article.fs;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileNotFoundException;

@Api(value = "Article file service controller", tags = {"Article file service", "Controller"})
@RequestMapping("/article-fs")
public interface ArticleFSControllerApi {
    @ApiOperation(value = "Download article", notes = "Download article")
    @PostMapping("/download")
    Integer download(@RequestParam String articleId, @RequestParam String articleMongoId) throws FileNotFoundException;


    @ApiOperation(value = "Download article", notes = "Download article")
    @PostMapping("/delete")
    Integer delete(@RequestParam String articleId);
}
