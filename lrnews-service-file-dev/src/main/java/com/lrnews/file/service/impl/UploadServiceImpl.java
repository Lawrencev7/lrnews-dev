package com.lrnews.file.service.impl;

import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.lrnews.file.service.UploaderService;
import com.lrnews.utils.RandomStringName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UploadServiceImpl implements UploaderService {

    public final FastFileStorageClient fileClient;

    private static final Logger logger = LoggerFactory.getLogger(UploaderService.class);

    public UploadServiceImpl(FastFileStorageClient fileClient) {
        this.fileClient = fileClient;
    }

    @Override
    public String uploadFDFS(MultipartFile file, String fileExtName) throws IOException {

//        StorePath path = fileClient.uploadFile(file.getInputStream(), file.getSize(), fileExtName, null);
//        return path.getFullPath();

        String  name = RandomStringName.getRandomFileName();
        logger.info("Mock File Service - Mock file name {}", name);
        return name;
    }
}
