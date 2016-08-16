package com.asen.android.lib.base.core.network.urlconn;

import com.asen.android.lib.base.core.network.urlconn.bean.DownFileInfo;
import com.asen.android.lib.base.tool.util.AppUtil;
import com.asen.android.lib.base.tool.util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ���װ���ļ�����
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class DownloadUtil {

    /**
     * ���� Content-Disposition �е��ļ�����
     */
    private static final String CONTENT_DISPOSITION = "content-disposition";

    /**
     * ���� Content-Length �е��ļ�����
     */
    private static final String CONTENT_LENGTH = "content-length";

    /**
     * ���� Content-Type �е��ļ�����
     */
    private static final String CONTENT_TYPE = "content-type";

    /**
     * ���뷽ʽ ISO8859_1
     */
    private static final String CHARSET_ISO8859_1 = "ISO8859_1";

    /**
     * ���뷽ʽ GB2312
     */
    private static final String CHARSET_GB2312 = "GB2312";

    /**
     * ���뷽ʽ UTF-8
     */
    private static final String CHARSET_UTF_8 = "UTF-8";

    /**
     * �����С
     */
    private static final int BUFFER_SIZE = 2048;

    /**
     * �����������л�ȡ�ļ���Ϣ
     *
     * @param connection ��������
     * @return �ļ���Ϣ
     * @throws UnsupportedEncodingException
     */
    public static DownFileInfo getFileInfoByConn(HttpURLConnection connection) throws UnsupportedEncodingException {
        String fileName = null;
        long fileLength = 0;
        String fileType = null;

        for (int i = 0; ; i++) {
            String mine = connection.getHeaderField(i);
            String key = connection.getHeaderFieldKey(i);
            if (mine == null && key == null) break;

            // ���� Content-Disposition �е��ļ�����
            if (CONTENT_DISPOSITION.equalsIgnoreCase(key)) {
                if (mine == null) continue;
                Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine);
                if (m.find()) {
                    fileName = URLDecoder.decode(m.group(1).replace("\"", ""), CHARSET_UTF_8);
                    if (Charset.forName(CHARSET_ISO8859_1).newEncoder().canEncode(fileName)) { // ��ͨ��ISO8859_1��ʽת��
                        fileName = new String(fileName.getBytes(CHARSET_ISO8859_1), Charset.forName(CHARSET_UTF_8));
                    } else if (Charset.forName(CHARSET_GB2312).newEncoder().canEncode(fileName)) { // ��ͨ��GB2312��ʽת��
                        fileName = new String(fileName.getBytes(CHARSET_GB2312), Charset.forName(CHARSET_UTF_8));
                    }
                }
            }
            // ���� Content-Length �е��ļ�����
            else if (CONTENT_LENGTH.equalsIgnoreCase(key)) {
                if (mine == null) continue;
                try {
                    fileLength = Long.valueOf(mine);
                } catch (NumberFormatException ignored) {
                }
            }
            // ���� Content-Type �е��ļ�����
            else if (CONTENT_TYPE.equalsIgnoreCase(key)) {
                fileType = mine;
            }
        }

        if (fileName == null) { // ��URL��β��ȡ�ļ���
            String file = connection.getURL().getPath();
            int index = file.lastIndexOf("/");
            if (index != -1) {
                fileName = URLDecoder.decode(file.substring(index + 1, file.length()), "UTF-8");
            }
        }

        return new DownFileInfo(fileName, fileLength, fileType);
    }

    /**
     * �����ļ���Ĭ��ԭ�����
     *
     * @param urlStr �����ļ���ַ
     * @param folder ���ر����ļ���·��
     * @throws IOException
     */
    public static File downLoadFile(String urlStr, File folder) throws IOException, HttpResponseException {
        return downLoadFile(urlStr, folder, null);
    }

    /**
     * �����ļ���Ĭ��ԭ�����
     *
     * @param urlStr             �����ļ���ַ
     * @param folder             ���ر����ļ���·��
     * @param onDownloadListener ���ؼ������ü����ڵ�ǰ��ִ���߳��½���
     * @throws IOException,HttpResponseException
     */
    public static File downLoadFile(String urlStr, File folder, OnDownloadListener onDownloadListener) throws IOException, HttpResponseException {
        return downLoadFile(urlStr, folder, true, onDownloadListener);
    }

    /**
     * �����ļ�
     *
     * @param urlStr     �����ļ���ַ
     * @param folder     ���ر����ļ���·��
     * @param isOriginal �Ƿ�ԭ�����
     * @throws IOException,HttpResponseException
     */
    public static File downLoadFile(String urlStr, File folder, boolean isOriginal) throws IOException, HttpResponseException {
        return downLoadFile(urlStr, folder, isOriginal, null);
    }

    /**
     * �����ļ�
     *
     * @param urlStr             �����ļ���ַ
     * @param folder             ���ر����ļ���·��
     * @param isOriginal         �Ƿ�ԭ�����
     * @param onDownloadListener ���ؼ������ü����ڵ�ǰ��ִ���߳��½���
     * @throws IOException,HttpResponseException
     */
    public static File downLoadFile(String urlStr, File folder, boolean isOriginal, OnDownloadListener onDownloadListener) throws IOException, HttpResponseException {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;

        boolean success = true; // �Ƿ�ɹ�
        File saveFile = null;
        DownFileInfo info = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection.setFollowRedirects(true);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty(
                    "Accept",
                    "image/gif, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
            conn.setRequestProperty("Accept-Language", "zh-CN");
            conn.setRequestProperty("Referer", urlStr);
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty(
                    "User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.connect();

            if (!folder.exists()) {
                folder.mkdirs();
            }

            info = DownloadUtil.getFileInfoByConn(conn);
            if (conn.getResponseCode() == 200) {
                inputStream = conn.getInputStream();
                byte[] bytes = new byte[BUFFER_SIZE];
                int length;
                saveFile = new File(folder, AppUtil.getUUid() + ".tmp~");
                outputStream = new FileOutputStream(saveFile);
                long currentLength = 0;
                while ((length = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, length);
                    currentLength += length;
                    if (onDownloadListener != null)
                        onDownloadListener.downloadProgress(info.getFileLength(), currentLength);
                }
            } else {
                throw new HttpResponseException("CONN ERROR: " + conn.getResponseCode());
            }
        } catch (IOException e) {
            success = false;
            throw e;
        } finally {
            // �ر��ļ���
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            // �ر��ļ���֮����ļ����в���
            if (success) { // �ɹ�
                if (saveFile != null && saveFile.exists()) {
                    if (isOriginal && info.getFileName() != null) { // ��Ҫԭ�ļ�����������ܻ��ԭ�ļ���
                        File newFile = new File(folder, info.getFileName());
                        FileUtil.rename(saveFile, newFile);
                        saveFile = newFile;
                    } else { // 32λ + ��ʵ��׺��
                        File newFile = new File(folder, saveFile.getName().substring(0, saveFile.getName().lastIndexOf(".") + 1) + info.getFileSuffix());
                        FileUtil.rename(saveFile, newFile);
                        saveFile = newFile;
                    }
                }
            } else { // ���ɹ�
                if (saveFile != null && saveFile.exists()) {
                    saveFile.delete();
                }
            }
        }

        return saveFile;
    }

    /**
     * �����ļ��Ľ��ȼ���
     */
    public interface OnDownloadListener {

        /**
         * �����ļ��Ľ���
         *
         * @param fileSize �ļ���С
         * @param progress �����ص��ļ���С
         */
        void downloadProgress(long fileSize, long progress);
    }


}
