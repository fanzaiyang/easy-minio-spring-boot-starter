package cn.fanzy.minio;

import cn.fanzy.minio.properties.MinioConfigStorage;
import cn.fanzy.minio.properties.MinioProperties;
import cn.fanzy.minio.service.MinioService;
import cn.fanzy.minio.service.impl.MinioServiceImpl;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * minio配置
 *
 * @author fanzaiyang
 * @since 2021/09/16
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(MinioProperties.class)
@ConditionalOnProperty(prefix = "minio", name = {"enable"}, havingValue = "true", matchIfMissing = true)
public class MinioConfiguration {
    @Autowired
    private MinioProperties minioProperties;

    /**
     * MinioConfigStorage
     */
    public static List<MinioConfigStorage> minioConfigs;
    private static final Map<String, MinioService> minioServices = Maps.newLinkedHashMap();

    /**
     * 得到minio服务
     * 默认取出第一个
     *
     * @return {@link MinioService}
     */
    public static MinioService getMinioService() {
        return getMinioService(minioConfigs.get(0).getAlias());
    }

    /**
     * 得到minio服务
     *
     * @param alias 别名
     * @return {@link MinioService}
     */
    public static MinioService getMinioService(String alias) {
        if (!StringUtils.hasText(alias)) {
            log.warn("别名为空，返回默认第1个。");
            return getMinioService();
        }
        return minioServices.get(alias);
    }

    /**
     * 初始化服务
     */
    @PostConstruct
    public void initServices() {
        minioConfigs = minioProperties.getMinioConfigs();
        if (CollectionUtils.isEmpty(minioConfigs)) {
            log.warn("【MinIO组件】: 请在配置文件中添加MinIO相关配置！参数以minio开头。");
            return;
        }
        for (MinioConfigStorage bean : minioConfigs) {
            MinioService service = new MinioServiceImpl();
            service.setConfig(bean);
            minioServices.put(bean.getAlias(), service);
        }
        log.debug("【MinIO组件】: 开启 <MinIO组件> 相关的配置");
    }
}
