package com.asen.android.lib.base.core.network.urlconn.bean;

/**
 * Simple to Introduction
 * 下载的文件信息
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class FileInfo {

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

    public FileInfo(String fileName, long fileLength, String fileType) {
        this.fileName = fileName;
        this.fileLength = fileLength;
        this.fileType = fileType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

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
