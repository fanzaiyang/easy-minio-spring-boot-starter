package cn.fanzy.minio.service;


import cn.fanzy.minio.model.MinioResp;
import cn.fanzy.minio.properties.MinioConfigStorage;
import io.minio.MinioClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * minio服务
 *
 * @author fanzaiyang
 * @since 2021/09/16
 */
public interface MinioService {

    /**
     * 设置MinIO配置
     *
     * @param config {@link MinioConfigStorage}配置
     */
    void setConfig(MinioConfigStorage config);

    /**
     * 获取配置
     *
     * @return {@link MinioConfigStorage}
     */
    MinioConfigStorage getConfig();

    /**
     * 获取公共MinioClient对象
     *
     * @return {@link MinioClient}
     */
    MinioClient getClient();

    /**
     * 获取公共MinioClient对象
     *
     * @return {@link MinioClient}
     */
    MinioClient getPublicClient();

    /**
     * 获取内网MinioClient对象
     *
     * @return {@link MinioClient}
     */
    MinioClient getPrivateClient();

    /**
     * 自定义bucket名称
     *
     * @param bucketName bucket名称
     * @return {@link MinioService}
     */
    MinioService bucketName(String bucketName);

    /**
     * 获取当前存储桶名称
     *
     * @return {@link String}
     */
    String getBucketName();

    /**
     * 检查并创建不存在的存储桶
     *
     * @param bucketName bucket名称
     */
    void makeBucket(String bucketName);

    /**
     * 上传
     *
     * @param file       文件
     * @param objectName 对象名称
     * @return {@link MinioResp}
     */
    MinioResp upload(MultipartFile file, String objectName);

    /**
     * 上传
     *
     * @param file       文件
     * @param objectName 对象名称
     * @return {@link MinioResp}
     */
    MinioResp upload(File file, String objectName);

    /**
     * 上传
     *
     * @param file        文件
     * @param objectName  对象名称
     * @param contentType 内容类型
     * @return {@link MinioResp}
     */
    MinioResp upload(File file, String objectName, String contentType);

    /**
     * 上传
     *
     * @param inputStream 输入流
     * @param objectName  对象名称
     * @return {@link MinioResp}
     */
    MinioResp upload(InputStream inputStream, String objectName);

    /**
     * 上传
     *
     * @param inputStream 输入流
     * @param objectName  对象名称
     * @param contentType 内容类型
     * @return {@link MinioResp}
     */
    MinioResp upload(InputStream inputStream, String objectName, String contentType);

    /**
     * 上传
     *
     * @param inputStream 输入流
     * @param objectName  对象名称
     * @param contentType 内容类型
     * @param fileName    文件名称
     * @return {@link MinioResp}
     */
    MinioResp upload(InputStream inputStream, String objectName, String contentType, String fileName);

    /**
     * 从MinIO得到对象流
     *
     * @param objectName 对象名称
     * @return {@link InputStream}
     */
    InputStream getObject(String objectName);

    /**
     * 从MinIO得到对象url（默认7天有效）
     *
     * @param objectName 对象名称
     * @return {@link String}
     */
    String getObjectUrl(String objectName);

    /**
     * 从MinIO得到对象url，自定义过期时间
     * <pre>
     *     过期时间设置0或大于7天的，为默认7天处理。
     * </pre>
     *
     * @param objectName 对象名称
     * @param expiry     到期
     * @param timeUnit   时间单位
     * @return {@link String}
     */
    String getObjectUrl(String objectName, int expiry, TimeUnit timeUnit);

    /**
     * 删除MinIO中的文件
     *
     * @param objectName 对象名称
     */
    void delete(String objectName);
}
