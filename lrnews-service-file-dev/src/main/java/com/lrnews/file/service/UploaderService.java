package com.lrnews.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploaderService {
    String uploadFDFS(MultipartFile file, String fileExtName) throws IOException;

    String uploadOSS(MultipartFile file,
                     String userId,
                     String fileExtName) throws Exception;
}
