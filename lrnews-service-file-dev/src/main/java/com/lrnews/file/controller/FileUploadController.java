package com.lrnews.file.controller;

import com.lrnews.api.controller.file.FileUploadControllerApi;
import com.lrnews.file.resource.FileResource;
import com.lrnews.file.service.UploaderService;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.graceresult.ResponseStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileUploadController implements FileUploadControllerApi {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    final UploaderService uploaderService;

    final FileResource fileResource;

    public FileUploadController(UploaderService uploaderService, FileResource fileResource) {
        this.uploaderService = uploaderService;
        this.fileResource = fileResource;
    }

    @Override
    public JsonResultObject uploadAvatar(String userId, MultipartFile file) {
        if (file == null) {
            logger.error("Uploaded a null file");
            return JsonResultObject.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }

        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            logger.error("File name is blank");
            return JsonResultObject.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }

        String extName = checkFileAndGetExtName(filename);
        if (StringUtils.isBlank(extName)) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.FILE_FORMATTER_FAILD);
        }

        try {
            String path = uploaderService.uploadFDFS(file, extName);
            return JsonResultObject.ok(fileResource.getHost() + "/" + path);
        } catch (IOException e) {
            logger.error("File upload failed with exception {}" + e.getMessage());
            return JsonResultObject.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
    }

    private String checkFileAndGetExtName(String filename) {
        String[] part = filename.split("\\.");
        if (part.length != 2) {
            logger.error("Check file - failed: Failed to extract file extend name.");
            return "";
        }

        String fileExtName = part[1];
        if (fileExtName.equalsIgnoreCase("jpg")
                || fileExtName.equalsIgnoreCase("png")
                || fileExtName.equalsIgnoreCase("jpeg")) {
            logger.info("Check file - pass");
            return fileExtName;
        } else {
            logger.error("Check file - failed: Unsupported file type.");
            return "";
        }
    }

    @Override
    public JsonResultObject uploadAvatarTest(String userId, String filename) {
        if (filename == null) {
            logger.error("Test interface: Uploaded a null file");
            return JsonResultObject.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }

        if (StringUtils.isBlank(filename)) {
            logger.error("Test interface: File name is blank");
            return JsonResultObject.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }

        String extName = checkFileAndGetExtName(filename);
        if (StringUtils.isBlank(extName)) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.FILE_FORMATTER_FAILD);
        }

        try {
            String path = uploaderService.uploadFDFS(null, extName);
            return JsonResultObject.ok(fileResource.getHost() + "/" + path);
        } catch (IOException e) {
            logger.error("Test interface: File upload failed with exception {}" + e.getMessage());
            return JsonResultObject.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
    }

}
