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
 * 简易版的文件下载
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class DownloadUtil {

    /**
     * 解析 Content-Disposition 中的文件内容
     */
    private static final String CONTENT_DISPOSITION = "content-disposition";

    /**
     * 解析 Content-Length 中的文件内容
     */
    private static final String CONTENT_LENGTH = "content-length";

    /**
     * 解析 Content-Type 中的文件内容
     */
    private static final String CONTENT_TYPE = "content-type";

    /**
     * 编码方式 ISO8859_1
     */
    private static final String CHARSET_ISO8859_1 = "ISO8859_1";

    /**
     * 编码方式 GB2312
     */
    private static final String CHARSET_GB2312 = "GB2312";

    /**
     * 编码方式 UTF-8
     */
    private static final String CHARSET_UTF_8 = "UTF-8";

    /**
     * 缓存大小
     */
    private static final int BUFFER_SIZE = 2048;

    /**
     * 从网络连接中获取文件信息
     *
     * @param connection 网络连接
     * @return 文件信息
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

            // 解析 Content-Disposition 中的文件名称
            if (CONTENT_DISPOSITION.equalsIgnoreCase(key)) {
                if (mine == null) continue;
                Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine);
                if (m.find()) {
                    fileName = URLDecoder.decode(m.group(1).replace("\"", ""), CHARSET_UTF_8);
                    if (Charset.forName(CHARSET_ISO8859_1).newEncoder().canEncode(fileName)) { // 能通过ISO8859_1形式转码
                        fileName = new String(fileName.getBytes(CHARSET_ISO8859_1), Charset.forName(CHARSET_UTF_8));
                    } else if (Charset.forName(CHARSET_GB2312).newEncoder().canEncode(fileName)) { // 能通过GB2312形式转码
                        fileName = new String(fileName.getBytes(CHARSET_GB2312), Charset.forName(CHARSET_UTF_8));
                    }
                }
            }
            // 解析 Content-Length 中的文件内容
            else if (CONTENT_LENGTH.equalsIgnoreCase(key)) {
                if (mine == null) continue;
                try {
                    fileLength = Long.valueOf(mine);
                } catch (NumberFormatException ignored) {
                }
            }
            // 解析 Content-Type 中的文件内容
            else if (CONTENT_TYPE.equalsIgnoreCase(key)) {
                fileType = mine;
            }
        }

        if (fileName == null) { // 从URL结尾获取文件名
            String file = connection.getURL().getPath();
            int index = file.lastIndexOf("/");
            if (index != -1) {
                fileName = URLDecoder.decode(file.substring(index + 1, file.length()), "UTF-8");
            }
        }

        return new DownFileInfo(fileName, fileLength, fileType);
    }

    /**
     * 下载文件，默认原名输出
     *
     * @param urlStr 下载文件地址
     * @param folder 下载保存文件的路径
     * @throws IOException
     */
    public static File downLoadFile(String urlStr, File folder) throws IOException, HttpResponseException {
        return downLoadFile(urlStr, folder, null);
    }

    /**
     * 下载文件，默认原名输出
     *
     * @param urlStr             下载文件地址
     * @param folder             下载保存文件的路径
     * @param onDownloadListener 下载监听，该监听在当前的执行线程下进行
     * @throws IOException,HttpResponseException
     */
    public static File downLoadFile(String urlStr, File folder, OnDownloadListener onDownloadListener) throws IOException, HttpResponseException {
        return downLoadFile(urlStr, folder, true, onDownloadListener);
    }

    /**
     * 下载文件
     *
     * @param urlStr     下载文件地址
     * @param folder     下载保存文件的路径
     * @param isOriginal 是否原名输出
     * @throws IOException,HttpResponseException
     */
    public static File downLoadFile(String urlStr, File folder, boolean isOriginal) throws IOException, HttpResponseException {
        return downLoadFile(urlStr, folder, isOriginal, null);
    }

    /**
     * 下载文件
     *
     * @param urlStr             下载文件地址
     * @param folder             下载保存文件的路径
     * @param isOriginal         是否原名输出
     * @param onDownloadListener 下载监听，该监听在当前的执行线程下进行
     * @throws IOException,HttpResponseException
     */
    public static File downLoadFile(String urlStr, File folder, boolean isOriginal, OnDownloadListener onDownloadListener) throws IOException, HttpResponseException {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;

        boolean success = true; // 是否成功
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
            // 关闭文件流
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            // 关闭文件流之后对文件进行操作
            if (success) { // 成功
                if (saveFile != null && saveFile.exists()) {
                    if (isOriginal && info.getFileName() != null) { // 需要原文件名输出，且能获得原文件名
                        File newFile = new File(folder, info.getFileName());
                        FileUtil.rename(saveFile, newFile);
                        saveFile = newFile;
                    } else { // 32位 + 真实后缀名
                        File newFile = new File(folder, saveFile.getName().substring(0, saveFile.getName().lastIndexOf(".") + 1) + info.getFileSuffix());
                        FileUtil.rename(saveFile, newFile);
                        saveFile = newFile;
                    }
                }
            } else { // 不成功
                if (saveFile != null && saveFile.exists()) {
                    saveFile.delete();
                }
            }
        }

        return saveFile;
    }

    /**
     * 下载文件的进度监听
     */
    public interface OnDownloadListener {

        /**
         * 下载文件的进度
         *
         * @param fileSize 文件大小
         * @param progress 已下载的文件大小
         */
        void downloadProgress(long fileSize, long progress);
    }


}
