package com.lrnews.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploaderService {
    /**
     * 通过FastDFS            上传文件
     * @param file          文件
     * @param fileExtName   文件扩展名
     * @return              文件ID
     */
    String uploadFDFS(MultipartFile file, String fileExtName) throws IOException;

    /**
     * 通过OSS上传文件
     * @param file          文件
     * @param userId        上传用户的ID
     * @param fileExtName   文件扩展名
     * @return              文件ID
     */
    String uploadOSS(MultipartFile file,
                     String userId,
                     String fileExtName) throws Exception;
}
