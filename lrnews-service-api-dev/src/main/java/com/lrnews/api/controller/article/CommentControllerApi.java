package com.lrnews.api.controller.article;

import com.lrnews.bo.CommentReplyBO;
import com.lrnews.graceresult.JsonResultObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Comment business controller", tags = {"Article Service", "Comment Service", "Controller"})
@RequestMapping("/comment")
public interface CommentControllerApi {
    @ApiOperation(value = "Add comment", notes = "Add a comment under an article")
    @PostMapping("/addComment")
    JsonResultObject addComment(@RequestBody @Valid CommentReplyBO commentReplyBO, BindingResult bindingResult);

    @ApiOperation(value = "Query Comment Count", notes = "Get the total comment counts of current article")
    @GetMapping("/queryCommentCount")
    JsonResultObject queryCommentCount(@RequestParam String articleId);

    @ApiOperation(value = "Query Comment Count", notes = "Get the total comment counts of current article")
    @GetMapping("/listAllComment")
    JsonResultObject listAllComment(@RequestParam String articleId, @RequestParam Integer page, @RequestParam Integer pageSize);

    @ApiOperation(value = "Query Comment For Article", notes = "Writer get all comment for his/her articles")
    @GetMapping("/queryCommentOfMyArticle")
    JsonResultObject queryCommentOfMyArticle(@RequestParam String writerId,
                                             @RequestParam Integer page, @RequestParam Integer pageSize);

    @ApiOperation(value = "Query Comment For Article", notes = "Writer get all comment for his/her articles")
    @GetMapping("/deleteComment")
    JsonResultObject deleteComment(@RequestParam String writerId, @RequestParam String commentId);
}
