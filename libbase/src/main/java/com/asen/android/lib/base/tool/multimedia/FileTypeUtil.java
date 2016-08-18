package com.asen.android.lib.base.tool.multimedia;

/**
 * 文件类型工具类
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:19
 */
public class FileTypeUtil {

    /**
     * 根据文件后缀获得文件类型
     *
     * @param suffix 后缀名
     * @return 文件类型
     */
    public static FileType getFileTypeBySuffix(String suffix) {
        if ("jpg".equals(suffix) || "png".equals(suffix)) {
            return FileType.PICTURE;
        } else if ("mp3".equals(suffix)) {
            return FileType.AUDIO;
        } else if ("mp4".equals(suffix)) {
            return FileType.VIDEO;
        } else {
            return FileType.UNKNOWN;
        }
    }

}
