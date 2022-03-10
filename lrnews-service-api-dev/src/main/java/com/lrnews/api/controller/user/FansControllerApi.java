package com.lrnews.api.controller.user;

import com.lrnews.graceresult.JsonResultObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Api(value = "Test controller api value", tags = {"hello-controller", "The first test controller for this project"})
@RequestMapping("/fans")
public interface FansControllerApi {

    @ApiOperation(value = "Writer Is Followed By Me", notes = "Check if current writer were followed by me")
    @PostMapping("/isFollowedByMe")
    JsonResultObject isFollowedByMe(@RequestParam String writerId, @RequestParam HttpServletRequest request);

    @ApiOperation(value = "Follow someone", notes = "To follow a writer")
    @PostMapping("/follow")
    JsonResultObject follow(@RequestParam String writerId, @RequestParam HttpServletRequest request);

    @ApiOperation(value = "Unfollow someone", notes = "To cancel following writer")
    @PostMapping("/unfollow")
    JsonResultObject unfollow(@RequestParam String writerId, @RequestParam HttpServletRequest request);

    @ApiOperation(value = "Fans List", notes = "Get fans list for me")
    @PostMapping("/fansList")
    JsonResultObject fansList(@RequestParam String  writerId, @RequestParam Integer page, @RequestParam Integer pageSize);

    @ApiOperation(value = "Fans Graph", notes = "Get fans graph VO of writer")
    @PostMapping("/fansGraph")
    JsonResultObject fansGraph(@RequestParam String  writerId);
}
