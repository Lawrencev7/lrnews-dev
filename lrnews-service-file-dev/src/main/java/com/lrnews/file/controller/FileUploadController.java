package com.lrnews.file.controller;

import com.lrnews.api.controller.file.FileUploadControllerApi;
import com.lrnews.file.service.UploaderService;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.graceresult.ResponseStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileUploadController implements FileUploadControllerApi {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    final UploaderService uploaderService;

    public FileUploadController(UploaderService uploaderService) {
        this.uploaderService = uploaderService;
    }

    @Override
    public JsonResultObject uploadAvatar(String userId, MultipartFile file) {
        if(file == null){
            logger.error("Uploaded a null file");
            return JsonResultObject.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }

        String filename = file.getOriginalFilename();
        if(StringUtils.isBlank(filename)){
            logger.error("File name is blank");
            return JsonResultObject.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }

        // Get extend name of file
        String extName = filename.split("\\.")[filename.length() - 1];

        if (!fileTypeIsSupported(extName)){
            logger.error("File type {} is not supported", extName);
            return JsonResultObject.errorCustom(ResponseStatusEnum.FILE_FORMATTER_FAILD);
        }

        try {
            String path = uploaderService.uploadFDFS(file, extName);
            return JsonResultObject.ok(path);
        } catch (IOException e) {
            logger.error("File upload failed with exception {}" + e.getMessage());
            return JsonResultObject.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
    }

    private boolean fileTypeIsSupported(String fileExtName){
        return fileExtName.equalsIgnoreCase("jpg")
                || fileExtName.equalsIgnoreCase("png")
                || fileExtName.equalsIgnoreCase("jpeg");
    }
}
