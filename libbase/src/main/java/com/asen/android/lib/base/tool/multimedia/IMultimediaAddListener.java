package com.asen.android.lib.base.tool.multimedia;

import java.io.File;
import java.util.List;

/**
 * 多媒体添加接口
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:19
 */
public interface IMultimediaAddListener {

    /**
     * 多媒体添加
     *
     * @param lastFile 最后一个文件
     * @param fileList 所有的文件集合
     */
    public void multimediaAdd(File lastFile, List<File> fileList);
}
