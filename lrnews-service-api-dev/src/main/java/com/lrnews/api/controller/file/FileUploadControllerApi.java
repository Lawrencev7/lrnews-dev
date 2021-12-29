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

    @ApiOperation(value = "To upload avatar for user", notes = "Avatar Uploader")
    @PostMapping("/uploadAvatar")
    public JsonResultObject uploadAvatar(@RequestParam String userId, MultipartFile file);
}
