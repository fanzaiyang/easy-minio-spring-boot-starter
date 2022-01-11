package cn.fanzy.minio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 响应
 *
 * @author fanzaiyang
 * @since 2021/09/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MinioResp {
    /**
     * 文件的名称
     */
    private String fileName;
    /**
     * 保存到Minio的名字（唯一）
     */
    private String objectName;
    /**
     * 预览地址
     */
    private String previewUrl;

    /**
     * 存储桶名称
     */
    private String bucketName;
    /**
     * 文件大小
     */
    private Double fileMbSize;

}
