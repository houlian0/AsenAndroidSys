package com.asen.android.lib.base.core.network.urlconn.bean;

/**
 * ���ص��ļ���Ϣ
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class DownFileInfo {

    /**
     * �ļ�����
     */
    private String fileName;

    /**
     * �ļ���С
     */
    private long fileLength;

    /**
     * �ļ�����
     */
    private String fileType;

    /**
     * ���캯��
     *
     * @param fileName   �ļ�������
     * @param fileLength �ļ��Ĵ�С
     * @param fileType   �ļ�������
     */
    public DownFileInfo(String fileName, long fileLength, String fileType) {
        this.fileName = fileName;
        this.fileLength = fileLength;
        this.fileType = fileType;
    }

    /**
     * ��ȡ�ļ�������
     *
     * @return �ļ�������
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * �����ļ�������
     *
     * @param fileType �ļ�������
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * ��ȡ�ļ�������
     *
     * @return �ļ�������
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * �����ļ�������
     *
     * @param fileName �ļ�������
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * ��ȡ�ļ��Ĵ�С
     *
     * @return �ļ��Ĵ�С
     */
    public long getFileLength() {
        return fileLength;
    }

    /**
     * �����ļ��Ĵ�С
     *
     * @param fileLength �ļ��Ĵ�С
     */
    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    /**
     * ��ȡ�ļ��ĺ�׺��
     *
     * @return �ļ��ĺ�׺��
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
