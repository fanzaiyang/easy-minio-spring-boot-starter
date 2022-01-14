package cn.fanzy.minio;

import cn.fanzy.minio.model.MinioResp;
import io.minio.GetBucketPolicyArgs;
import io.minio.MinioClient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.InputStream;

@SpringBootTest
public class MinioTests {
    /**
     * 上传文件到MinIO服务器
     */
    @SneakyThrows
    @Test
    void testUploadMin() {
        MinioResp resp = MinioConfiguration.getMinioService().upload(new File("1.jpg"), "1.jpg");
        System.out.println(resp);
    }

    /**
     * 如果设置多个MinIO配置，你可这样指定特定某一个。
     */
    @SneakyThrows
    @Test
    void testUploadMin2() {
        // 这里表示，获取别名为alias的服务类。
        MinioResp resp = MinioConfiguration.getMinioService("alias").upload(new File("1.jpg"), "1.jpg");
        System.out.println(resp);
    }

    @SneakyThrows
    @Test
    void testUploadMin3() {
        // 这里表示，不使用配置文件的存储桶，而指定另外一个新的。
        MinioResp resp = MinioConfiguration.getMinioService("alias")
                .bucketName("新的存储桶")
                .upload(new File("1.jpg"), "1.jpg");
        System.out.println(resp);
    }

    /**
     * 获取临时文件地址
     */
    @Test
    void testPreview() {
        String objectUrl = MinioConfiguration.getMinioService().getObjectUrl("1.jpg");
        System.out.println(objectUrl);
    }

    /**
     * 从MinIO下载文件到流
     */
    @SneakyThrows
    @Test
    void testDownload() {
        InputStream inputStream = MinioConfiguration.getMinioService().getObject("1.jpg");
    }

    /**
     * MinIO client其他用法
     */
    @SneakyThrows
    @Test
    void testMinioClient() {
        MinioClient client = MinioConfiguration.getMinioService().getClient();
        String test = client.getBucketPolicy(GetBucketPolicyArgs.builder()
                .bucket("test")
                .build());
        System.out.println(test);

    }
}
