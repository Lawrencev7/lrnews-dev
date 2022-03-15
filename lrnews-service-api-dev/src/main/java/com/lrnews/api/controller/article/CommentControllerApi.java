package com.lrnews.api.controller.article;

import com.lrnews.bo.CommentReplyBO;
import com.lrnews.graceresult.JsonResultObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Api(value = "Comment business controller", tags = {"Article Service", "Comment Service", "Controller"})
@RequestMapping("/comment")
public interface CommentControllerApi {
    @ApiOperation(value = "Add comment", notes = "Add a comment under an article")
    @PostMapping("/addComment")
    JsonResultObject addComment(@RequestBody @Valid CommentReplyBO commentReplyBO, BindingResult bindingResult);

}
