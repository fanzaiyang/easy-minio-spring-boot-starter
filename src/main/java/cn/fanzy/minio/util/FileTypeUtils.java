package cn.fanzy.minio.util;

import org.springframework.util.StringUtils;

import java.io.File;

/**
 * 文件类型工具类
 *
 * @author fanzaiyang
 * @since 2021/08/24
 */
public class FileTypeUtils {

    /**
     * 根据文件名称得到文件类型
     *
     * @param fileName 文件名称
     * @return {@link String}
     */
    public static String getFileType(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            return null;
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 得到文件类型
     *
     * @param file 文件
     * @return {@link String}
     */
    public static String getFileType(File file) {
        if (file == null) {
            return null;
        }
        return getFileType(file.getName());
    }

    /**
     * 获取文件名称
     *
     * @param objectName 对象名称
     * @return {@link String}
     */
    public static String getFileName(String objectName) {
        if (objectName == null) {
            return null;
        }
        if (objectName.contains("/")) {
            return objectName.substring(objectName.lastIndexOf("/") + 1);
        }
        return objectName;
    }
}
