package com.lrnews.file.controller;

import com.lrnews.api.controller.file.FileUploadControllerApi;
import com.lrnews.bo.AdminBO;
import com.lrnews.exception.CustomExceptionFactory;
import com.lrnews.file.resource.FileResource;
import com.lrnews.file.service.UploaderService;
import com.lrnews.graceresult.JsonResultObject;
import com.lrnews.graceresult.ResponseStatusEnum;
import com.lrnews.utils.FileUtil;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

import static com.lrnews.values.CommonTestStrings.UPLOAD_FACE_TO_DISK;

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

    @Override
    public JsonResultObject uploadToGridFS(AdminBO adminBO) {
        // Base64 encoded file string
        String faceImg64 = adminBO.getImg64();
        byte[] imgBytes = Base64.getDecoder().decode(faceImg64.trim());
        try {
            InputStream is = new ByteArrayInputStream(imgBytes);
            String fileId = gridFSBucket.uploadFromStream(adminBO.getUsername() + ".png", is).toString();
            logger.info("Uploaded user face with file id: " + fileId);
            return JsonResultObject.ok(fileId);
        } catch (Exception e) {
            return JsonResultObject.errorMsg(e.getMessage());
        }
    }

    @Override
    public JsonResultObject readFaceFile(@RequestParam String faceId, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(faceId) || "null".equalsIgnoreCase(faceId)) {
            return JsonResultObject.errorCustom(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
        }

        try {
            File imgFile = readGridFSByFaceId(faceId);
            if (Objects.isNull(imgFile)) {
                return JsonResultObject.errorCustom(ResponseStatusEnum.LOSE_FACE_ID);
            }
            // Wait for implementation: download file by browser
            return JsonResultObject.ok(imgFile.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Error when read image from GridFS: " + e.getMessage());
            return JsonResultObject.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public String readFaceImg64(String faceId) throws IOException {
        File face = readGridFSByFaceId(faceId);
        return FileUtil.fileToBase64(face);
    }

    @Override
    public JsonResultObject uploadFile(String userId, MultipartFile[] files) {
        if (files == null) {
            logger.error("No file upload.");
            return JsonResultObject.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }

        List<String> imageUrlList = new ArrayList<>();

        logger.info("Start upload files: total {}", files.length);
        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();
            if (StringUtils.isBlank(filename)) {
                logger.error("In Loop: File name is blank");
                continue;
            }

            String extName = checkFileAndGetExtName(filename);
            if (StringUtils.isBlank(extName)) {
                logger.error("In Loop: Extend name illegal");
            } else {
                try {
                    String path = uploaderService.uploadFDFS(file, extName);
                    logger.info("In Loop: Save file {}", filename);
                    imageUrlList.add(fileResource.getHost() + "/" + path);
                } catch (IOException e) {
                    logger.error("In Loop: File upload failed with exception {}" + e.getMessage());
                    return JsonResultObject.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
                }
            }
        }

        logger.info("Uploaded files: total {}", imageUrlList.size());
        return JsonResultObject.ok(imageUrlList);

    }

    private File readGridFSByFaceId(String faceId) throws IOException {
        GridFSFindIterable imgs = gridFSBucket.find(Filters.eq("_id", new ObjectId(faceId)));

        GridFSFile file = imgs.first();

        if (file == null) {
            logger.error("File not found: faceId=" + faceId);
            CustomExceptionFactory.onException(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
            return null;
        } else {
            logger.info("Read file from GridFS: filename=" + file.getFilename());
            File uploadPath = new File(UPLOAD_FACE_TO_DISK);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            File imgFile = new File(uploadPath.getAbsolutePath() + '/' + file.getFilename());
            imgFile.createNewFile();
            OutputStream os = new FileOutputStream(imgFile);
            gridFSBucket.downloadToStream(new ObjectId(faceId), os);

            return imgFile;
        }
    }

    /**
     * 检查文件扩展名是否支持并提取扩展名
     * @param filename      完整的文件名
     * @return              空字符串（如果检查未通过）或支持的文件的扩展名
     *                      jpg, jpeg 或 png
     */
    private String checkFileAndGetExtName(String filename) {
        String[] part = filename.split("\\.");
        if (part.length != 2) {
            logger.error("Check file - failed: Failed to extract file extend name.");
            return "";
        }

        String fileExtName = part[1];
        if ("jpg".equalsIgnoreCase(fileExtName)
                || "png".equalsIgnoreCase(fileExtName)
                || "jpeg".equalsIgnoreCase(fileExtName)) {
            logger.info("Check file - pass");
            return fileExtName;
        } else {
            logger.error("Check file - failed: Unsupported file type.");
            return "";
        }
    }

}
