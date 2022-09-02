package com.lrnews.api.controller.file;

import com.lrnews.bo.AdminBO;
import com.lrnews.graceresult.JsonResultObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;

import static com.lrnews.api.values.ServiceList.SERVICE_FILE;

@Api(value = "File uploader controller api", tags = {"File Service", "Controller"})
@FeignClient(SERVICE_FILE)
public interface FileUploadControllerApi {

    /**
     * 上传用户头像文件
     * @param userId    用户id
     * @param file      头像文件
     */
    @ApiOperation(value = "Avatar Uploader", notes = "Upload avatar for user")
    @PostMapping("/file/uploadAvatar")
    JsonResultObject uploadAvatar(@RequestParam String userId,@RequestParam MultipartFile file);

    @ApiOperation(value = "Face upload", notes = "Upload face to GridFS")
    @PostMapping("/file/uploadToGridFS")
    JsonResultObject uploadToGridFS(@RequestBody AdminBO adminBO);

    @ApiOperation(value = "Read face", notes = "Read admin face from GridFS")
    @GetMapping("/file/readFace")
    JsonResultObject readFaceFile(@RequestParam String faceId, HttpServletRequest request, HttpServletResponse response);

    @ApiOperation(value = "Read encoded face image <Inner interface>",
            notes = "Read admin face image (BASE64 encoded) from GridFS. <Inner interface>")
    @GetMapping("/file/readFaceImg64")
    String readFaceImg64(@RequestParam String faceId) throws IOException;

    @ApiOperation(value = "Upload file", notes = "Upload multipart files")
    @PostMapping("/file/uploadFile")
    JsonResultObject uploadFile(@RequestParam String userId, MultipartFile[] files);
}
