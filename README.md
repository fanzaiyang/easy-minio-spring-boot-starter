# easy-minio-spring-boot-starter

#### 介绍

这个一个让你的Spring Boot项目快速集成`MinIO`的扩展，支持多MinIO客户端接入。 内置常用上传和下载等方法。
> 官方文档地址：https://docs.min.io/docs/java-client-api-reference.html

#### 特征

* 公私地址分离，优化上传和下载速度。
    * 当配置了内网地址时，上传和下载使用内网地址，提升速度。
* 多MinIO服务端支持
    * 配置支持多个MinIO服务器，方便自由切换。

#### 使用说明

* 引入POM依赖


*
版本：[![Maven Central](https://img.shields.io/maven-central/v/cn.fanzy.minio/easy-minio-spring-boot-starter.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22cn.fanzy.minio%22%20AND%20a:%22easy-minio-spring-boot-starter%22)

```xml

<dependency>
    <groupId>cn.fanzy.minio</groupId>
    <artifactId>easy-minio-spring-boot-starter</artifactId>
    <version>1.0.2</version>
</dependency>
```

* 修改配置文件

```yaml
minio:
  #enable: true # 默认开启
  minio-configs:
    # 第1个MinIO参数
    - alias: test1 #非必填：不填时同存储桶名字，该参数用于标识唯一，防止多个配置参数存储桶重名
      public-endpoint: http://地址1 #必填项：MinIO外网访问地址
      access-key: minioadmin #必填项：MinIO外网登录账号
      secret-key: minioadmin #必填项：MinIO外网登录密码
      bucket-name: test #必填项：MinIO存储桶名字
      private-endpoint: http://内网地址1 #非必填：当可以内网访问时建议配置此参数，提高传输速度。默认同public-endpoint
    # 第2个MinIO参数
    - alias: test2 #非必填：不填时同存储桶名字，该参数用于标识唯一，防止多个配置参数存储桶重名
      public-endpoint: http://地址2 #必填项：MinIO外网访问地址
      access-key: minioadmin #必填项：MinIO外网登录账号
      secret-key: minioadmin #必填项：MinIO外网登录密码
      bucket-name: test #必填项：MinIO存储桶名字
      private-endpoint: http://内网地址2 #非必填：当可以内网访问时建议配置此参数，提高传输速度。默认同public-endpoint
```

* 使用示例

```java

@SpringBootTest
class MinioTests {

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
    void testMinioClient() {
        MinioClient client = MinioConfiguration.getMinioService().getClient();
        boolean exists = client.bucketExists(BucketExistsArgs.builder()
                .bucket("test")
                .build());
        System.out.println(exists);

    }
}

```

* 内置方法不满足你的需要时

> 当内网方法无法满足你的需求时，你可以直接使用MinIO官方JavaSDK的方法。
> 官方使用方法见官网：https://docs.min.io/docs/java-client-api-reference.html

```java

@SpringBootTest
class MinioTests {
    /**
     * MinIO client其他用法
     */
    @SneakyThrows
    void testMinioClient() {
        // 通过MinioConfiguration获取MinioClient类（MinIO官方SDK）
        MinioClient client = MinioConfiguration.getMinioService().getClient();
        boolean exists = client.bucketExists(BucketExistsArgs.builder()
                .bucket("test")
                .build());
        System.out.println(exists);

    }
}
```

* 配置文件设置了多个MinIO时

> 你可以`MinioConfiguration.getMinioService(ALIAS)`获取特定某一个。
>
> 注意⚠️：当设置多个且未指定某个时，默认取第一个。

#### 更新日志

* 1.0.2
    * fix：获取content-type空指针问题。
* 1.0.1
    * fix：修改配置文件前缀为`minio`开头
#### 开发计划
> 方便开发者快速设置存储桶访问策略。
> 使`setBucketPolicy(SetBucketPolicyArgs args)` 方法，使用更便捷。
> 
> 关于策略文档见：https://docs.aws.amazon.com/AmazonS3/latest/userguide/using-iam-policies.html
#### 参与贡献

1. Fork 本仓库
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request