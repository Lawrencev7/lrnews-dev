package com.lrnews.api.controller.file;

import com.lrnews.graceresult.JsonResultObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "File uploader controller api", tags = {"File Service", "Controller"})
@RequestMapping("/file")
public interface FileUploadControllerApi {

    @ApiOperation(value = "Avatar Uploader", notes = "To upload avatar for user")
    @PostMapping("/uploadAvatar")
    JsonResultObject uploadAvatar(@RequestParam String userId, MultipartFile file);

    // Please delete this interface when FastDFS is established
    @ApiOperation(value = "Avatar Uploader", notes = "This is a test interface for who can not upload a \"MultipartFile\"")
    @PostMapping("/uploadAvatar-t")
    JsonResultObject uploadAvatarTest(@RequestParam String userId, String filename);
}
