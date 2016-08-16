package com.asen.android.lib.base.core.network.urlconn.bean;

/**
 * 下载的文件信息
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class DownFileInfo {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件大小
     */
    private long fileLength;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 构造函数
     *
     * @param fileName   文件的名称
     * @param fileLength 文件的大小
     * @param fileType   文件的类型
     */
    public DownFileInfo(String fileName, long fileLength, String fileType) {
        this.fileName = fileName;
        this.fileLength = fileLength;
        this.fileType = fileType;
    }

    /**
     * 获取文件的类型
     *
     * @return 文件的类型
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * 设置文件的类型
     *
     * @param fileType 文件的类型
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * 获取文件的名称
     *
     * @return 文件的名称
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 设置文件的名称
     *
     * @param fileName 文件的名称
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 获取文件的大小
     *
     * @return 文件的大小
     */
    public long getFileLength() {
        return fileLength;
    }

    /**
     * 设置文件的大小
     *
     * @param fileLength 文件的大小
     */
    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    /**
     * 获取文件的后缀名
     *
     * @return 文件的后缀名
     */
    public String getFileSuffix() {
        if (fileName != null) {
            if (fileName.contains(".")) {
                return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            } else {
                return "";
            }
        } else if ("application/vnd.android.package-archive".equals(fileType)) {
            return "apk";
        } else if (fileType == null) {
            return "";
        } else {
            return getFileType().substring(getFileType().indexOf("/") + 1, getFileType().length());
        }
    }

}
