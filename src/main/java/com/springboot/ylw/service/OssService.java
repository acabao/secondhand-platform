package com.springboot.ylw.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class OssService {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.access-key-id}")
    private String accessKeyId;

    @Value("${aliyun.oss.access-key-secret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;

    @Value("${aliyun.oss.url-prefix}")
    private String urlPrefix;

    /**
     * 上传文件到 OSS，返回可访问的公网 URL
     *
     * @param inputStream 文件输入流
     * @param objectName  OSS 对象名，如 "goods/uuid.jpg"
     * @param contentType 文件 MIME 类型，如 "image/jpeg"
     * @return 完整的 OSS 访问 URL
     */
    public String upload(InputStream inputStream, String objectName, String contentType) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentType(contentType);
            ossClient.putObject(bucketName, objectName, inputStream, meta);
            return urlPrefix + objectName;
        } finally {
            ossClient.shutdown();
        }
    }
}
