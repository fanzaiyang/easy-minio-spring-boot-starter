package cn.fanzy.minio.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * minio属性
 *
 * @author fanzaiyang
 * @since 2021/09/22
 */
@Data
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    /**
     * 是否启用，默认启用
     */
    private Boolean enable;

    /**
     * Minio配置
     */
    private List<MinioConfigStorage> minioConfigs;
}
