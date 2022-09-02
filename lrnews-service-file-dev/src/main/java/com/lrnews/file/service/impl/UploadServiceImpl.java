package com.lrnews.file.service.impl;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
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

        StorePath path = fileClient.uploadFile(
                file.getInputStream(), file.getSize(), fileExtName, null);

        return path.getFullPath();
    }

    @Override
    public String uploadOSS(MultipartFile file,
                            String userId,
                            String fileExtName) throws Exception {

//        // Endpoint以杭州为例，其它Region请按实际情况填写。
//        String endpoint = fileResource.getEndpoint();
//        // 获取id和secret
//        String accessKeyId = aliyunResource.getAccessKeyID();
//        String accessKeySecret = aliyunResource.getAccessKeySecret();
//
//        // 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(endpoint,
//                accessKeyId,
//                accessKeySecret);
////        images/abc/10010/dog.png
//
//        String fileName = sid.nextShort();
//        String myObjectName = fileResource.getObjectName()
//                + "/" + userId + "/" + fileName + "." + fileExtName;
//
//        // 上传网络流。
//        InputStream inputStream = file.getInputStream();
//        ossClient.putObject(fileResource.getBucketName(),
//                myObjectName,
//                inputStream);
//
//        // 关闭OSSClient。
//        ossClient.shutdown();

        return "MockUploadOSS";
    }
}
