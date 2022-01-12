package com.lrnews.file.controller;

import com.lrnews.api.controller.file.FileUploadControllerApi;
import com.lrnews.bo.NewAdminBO;
import com.lrnews.file.resource.FileResource;
import com.lrnews.file.service.UploaderService;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.mongodb.client.gridfs.GridFSBucket;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Arrays;
import java.util.Base64;

@RestController
public class FileUploadController implements FileUploadControllerApi {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    private final UploaderService uploaderService;

    private final FileResource fileResource;

    private final GridFSBucket gridFSBucket;

    public FileUploadController(UploaderService uploaderService, FileResource fileResource, GridFSBucket gridFSBucket) {
        this.uploaderService = uploaderService;
        this.fileResource = fileResource;
        this.gridFSBucket = gridFSBucket;
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

    @Override
    public JsonResultObject uploadToGridFS(NewAdminBO adminBO) {
        // Base64 encoded file string
        String faceImg64 = adminBO.getImg64();
        byte[] imgBytes = Base64.getDecoder().decode(faceImg64.trim());
        try {
            InputStream is = new ByteArrayInputStream(imgBytes);
            String fileId = gridFSBucket.uploadFromStream(adminBO.getUsername() + ".png", is).toString();
            logger.info("Uploaded user face with file id: " + fileId);
            return JsonResultObject.ok(fileId);
        }catch (Exception e){
            return JsonResultObject.errorMsg(e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        File file = new File("/home/lr/Pictures/admin123.jpeg");
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        int read = inputFile.read(buffer);
        System.out.println(read);
        inputFile.close();
        byte[] bytes = Base64.getEncoder().encode(buffer);
        System.out.println(Arrays.toString(bytes));
        FileOutputStream fos = new FileOutputStream("/home/lr/lr/mycode/imooc-news/dev/lrnews/lrnews-service-file-dev/src/main/java/com/lrnews/file/controller/img64.txt");
        fos.write(bytes);
//        Base64.getEncoder().encode();
    }
}
