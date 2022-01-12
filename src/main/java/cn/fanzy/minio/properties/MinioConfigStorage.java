package cn.fanzy.minio.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * minio配置存储
 *
 * @author fanzaiyang
 * @since 2021/09/22
 */
@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MinioConfigStorage {
    /**
     * 别名，要求唯一。默认同存储痛名称
     */
    private String alias;
    /**
     * 外网地址
     */
    private String publicEndpoint;

    /**
     * 访问密钥
     */
    private String accessKey;

    /**
     * 秘密密钥
     */
    private String secretKey;

    /**
     * <p>默认存储桶名称，请遵循命名规则</p>
     * <ul>
     *     <li>存储桶名称在Amazon S3中的所有现有存储桶名称中必须是唯一的。</li>
     *     <li>存储桶名称必须符合DNS命名约定。</li>
     *     <li>存储桶名称长度必须至少为3且不超过63个字符。</li>
     *     <li>存储桶名称不得包含大写字符或下划线。</li>
     *     <li>存储桶名称必须以小写字母或数字开头。</li>
     *     <li>存储桶名称必须是一系列一个或多个标签。相邻标签由单个句点（。）分隔。存储桶名称可以包含小写字母，数字和连字符。每个标签必须以小写字母或数字开头和结尾。</li>
     *     <li>存储桶名称不得格式化为IP地址（例如，192.168.5.4）。</li>
     *     <li>使用带有安全套接字层（SSL）的虚拟托管样式存储桶时，SSL通配符证书仅匹配不包含句点的存储桶。要解决此问题，请使用HTTP或编写自己的证书验证逻辑。我们建议您在使用虚拟托管样式存储桶时不要在存储桶名称中使用句点（“。”）。</li>
     * </ul>
     */
    private String bucketName;

    /**
     * 内网地址，可以有效提升文件保存/下载速度
     */
    private String privateEndpoint;

    /**
     * 得到别名
     *
     * @return {@link String}
     */
    public String getAlias() {
        if (!StringUtils.hasText(alias)) {
            log.warn("未设置别名，默认使用令牌桶作为别名。");
            return bucketName;
        }
        return alias;
    }

    /**
     * 获取内网端点
     *
     * @return {@link String}
     */
    public String getPrivateEndpoint() {
        if (!StringUtils.hasText(privateEndpoint)) {
            log.warn("未设置内网地址，默认使用公共地址。");
            return publicEndpoint;
        }
        return privateEndpoint;
    }
}
