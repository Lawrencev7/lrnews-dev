package com.lrnews.api.controller.file;

import com.lrnews.bo.AdminBO;
import com.lrnews.graceresult.JsonResultObject;
import feign.RequestLine;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.lrnews.api.values.ServiceList.SERVICE_FILE;

@Api(value = "File uploader controller api", tags = {"File Service", "Controller"})
//@RequestMapping("/file")
@FeignClient(SERVICE_FILE)
public interface FileUploadControllerApi {

    @ApiOperation(value = "Avatar Uploader", notes = "To upload avatar for user")
    @PostMapping("/file/uploadAvatar")
    JsonResultObject uploadAvatar(@RequestParam String userId,@RequestParam MultipartFile file);

    @ApiOperation(value = "Face upload", notes = "Upload face to GridFS")
    @PostMapping("/file/uploadToGridFS")
    JsonResultObject uploadToGridFS(@RequestBody AdminBO adminBO);

    @ApiOperation(value = "Read face", notes = "Read admin face from GridFS")
    @GetMapping("/file/readAdminFace")
    JsonResultObject readAdminFace(@RequestParam String faceId, HttpServletRequest request, HttpServletResponse response);

    @ApiOperation(value = "Read encoded face image <Inner interface>",
            notes = "Read admin face image (BASE64 encoded) from GridFS. <Inner interface>")
    @GetMapping("/file/readFaceImg64")
    String readFaceImg64(@RequestParam String faceId) throws IOException;

    @ApiOperation(value = "Upload file", notes = "Upload multipart files")
    @PostMapping("/file/uploadFile")
    JsonResultObject uploadFile(@RequestParam String userId, MultipartFile[] files);
}
