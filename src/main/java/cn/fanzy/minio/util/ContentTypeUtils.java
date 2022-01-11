package cn.fanzy.minio.util;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

public class ContentTypeUtils {
    /**
     * 文件扩展名.ai
     */
    public static final String AI = "application/postscript";
    /**
     * 文件扩展名.jpg
     */
    public static final String JPG = "image/jpeg";
    /**
     * 文件扩展名.png
     */
    public static final String PNG = "image/png";

    /**
     * 文件扩展名.pdf
     */
    public static final String PDF = "application/pdf";
    /**
     * 文件扩展名.mp4
     */
    public static final String MP4 = "video/mp4";

    /**
     * 文件扩展名.mp3
     */
    public static final String MP3 = "video/mp3";
    /**
     * 文件扩展名.xls
     */
    public static final String XLS = "application/vnd.ms-excel";

    public static final String DOC = "application/msword";

    private static List<Type> contentTypes;

    public ContentTypeUtils() {
        contentTypes = Lists.newArrayList(
                new Type("AI", AI),
                new Type("JPG", JPG),
                new Type("PNG", PNG),
                new Type("PDF", PDF),
                new Type("MP4", MP4),
                new Type("MP3", MP3),
                new Type("XLS", XLS),
                new Type("DOC", DOC)
        );
    }

    public static String getContentType(String type) {
        Optional<Type> optional = contentTypes.stream().filter(item -> item.getType().equalsIgnoreCase(type)).findAny();
        if (optional.isPresent()) {
            return optional.get().getValue();
        }
        return "application/octet-stream";
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Type {
        private String type;
        private String value;
    }
}
