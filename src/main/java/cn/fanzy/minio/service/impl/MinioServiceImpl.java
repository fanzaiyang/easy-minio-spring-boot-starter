package cn.fanzy.minio.service.impl;

import cn.fanzy.minio.model.MinioResp;
import cn.fanzy.minio.properties.MinioConfigStorage;
import cn.fanzy.minio.service.MinioService;
import cn.fanzy.minio.util.ContentTypeUtils;
import cn.fanzy.minio.util.FileTypeUtils;
import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

/**
 * minio服务实现类
 *
 * @author fanzaiyang
 * @since 2021/10/15
 */
@Slf4j
public class MinioServiceImpl implements MinioService {
    private volatile MinioConfigStorage configStorage;

    private volatile MinioClient minioClient;

    private volatile MinioClient privateMinioClient;

    private volatile String bucketName;

    @Override
    public void setConfig(MinioConfigStorage config) {
        this.configStorage = config;
        minioClient = MinioClient.builder()
                .endpoint(config.getPublicEndpoint())
                .credentials(config.getAccessKey(), config.getSecretKey())
                .build();

        privateMinioClient = MinioClient.builder()
                .endpoint(config.getPrivateEndpoint())
                .credentials(config.getAccessKey(), config.getSecretKey())
                .build();
        this.bucketName = config.getBucketName();
    }

    @Override
    public MinioConfigStorage getConfig() {
        return this.configStorage;
    }


    @Override
    public MinioClient getClient() {
        return this.minioClient;
    }

    @Override
    public MinioClient getPublicClient() {
        return getClient();
    }

    @Override
    public MinioClient getPrivateClient() {
        return this.privateMinioClient;
    }

    @Override
    public MinioService bucketName(String bucketName) {
        if (!StringUtils.hasText(bucketName)) {
            log.warn("参数为空，使用默认存储桶。");
            return this;
        }
        this.bucketName = bucketName;
        return this;
    }

    @Override
    public String getBucketName() {
        return this.bucketName;
    }

    @Override
    public void makeBucket(String bucketName) {
        try {
            boolean exists = privateMinioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());
            if (exists) {
                log.debug("存储桶：{}已存在,无需重新创建！", bucketName);
                return;
            }
            privateMinioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            log.error("令牌桶创建异常！" + e.getMessage(), e);
            throw new RuntimeException("令牌桶创建失败！原因：" + e.getMessage());
        }
    }

    @Override
    public MinioResp upload(MultipartFile file, String objectName) {
        try {
            return upload(file.getInputStream(), objectName, file.getContentType(), file.getName());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public MinioResp upload(File file, String objectName) {

        try {
            FileInputStream inputStream = new FileInputStream(file);

            return upload(inputStream, objectName, ContentTypeUtils.getContentType(FileTypeUtils.getFileType(file)), file.getName());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("文件未找到！" + e.getMessage());
        }
    }

    @Override
    public MinioResp upload(File file, String objectName, String contentType) {
        try {
            FileInputStream inputStream = new FileInputStream(file);
            return upload(inputStream, objectName, contentType, file.getName());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("文件未找到！" + e.getMessage());
        }

    }

    @Override
    public MinioResp upload(InputStream inputStream, String objectName) {
        return upload(inputStream, objectName, ContentTypeUtils.getContentType(FileTypeUtils.getFileType(objectName)), FileTypeUtils.getFileName(objectName));
    }

    @Override
    public MinioResp upload(InputStream inputStream, String objectName, String contentType) {
        return upload(inputStream, objectName, contentType, FileTypeUtils.getFileName(objectName));
    }

    @Override
    public MinioResp upload(InputStream inputStream, String objectName, String contentType, String fileName) {
        try {
            makeBucket(bucketName);
            int available = inputStream.available();
            ObjectWriteResponse object = privateMinioClient.putObject(PutObjectArgs.builder()
                    .object(objectName).bucket(bucketName)
                    .contentType(contentType)
                    .stream(inputStream, available, -1)
                    .build());
            BigDecimal decimal = BigDecimal.valueOf((double) available / 1048576);
            if (!StringUtils.hasText(fileName)) {
                fileName = objectName.substring(fileName.lastIndexOf(".") + 1);
            }
            return MinioResp.builder()
                    .objectName(objectName).bucketName(object.bucket())
                    .fileName(fileName)
                    .previewUrl(getObjectUrl(objectName))
                    .fileMbSize(decimal.setScale(2, RoundingMode.HALF_UP).doubleValue())
                    .build();
        } catch (Exception e) {
            log.error("上传文件到MinIO失败！", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public InputStream getObject(String objectName) {
        try {
            return privateMinioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName).object(objectName)
                    .build());
        } catch (Exception e) {
            log.error("从MinIO获取文件失败！", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String getObjectUrl(String objectName) {
        return getObjectUrl(objectName, 7, TimeUnit.DAYS);
    }

    @Override
    public String getObjectUrl(String objectName, int expiry, TimeUnit timeUnit) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName).object(objectName)
                    .expiry(expiry, timeUnit)
                    .method(Method.GET)
                    .build());
        } catch (Exception e) {
            log.error("从MinIO获取文件URL失败！", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName).object(objectName)
                    .build());
        } catch (Exception e) {
            log.error("从MinIO删除文件失败！", e);
            throw new RuntimeException(e.getMessage());
        }
    }


}
