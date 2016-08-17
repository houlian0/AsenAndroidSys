package com.asen.android.lib.base.tool.multimedia;

import java.io.File;
import java.util.List;

/**
 * ��ý����ӽӿ�
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:19
 */
public interface IMultimediaAddListener {

    /**
     * ��ý�����
     *
     * @param lastFile ���һ���ļ�
     * @param fileList ���е��ļ�����
     */
    public void multimediaAdd(File lastFile, List<File> fileList);
}
