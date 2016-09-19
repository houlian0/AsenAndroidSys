package com.asen.android.lib.base.core.sqlite.builder;

import org.json.JSONException;

import java.sql.SQLException;

/**
 * 数据的builder的基本接口
 *
 * @author Asen
 * @version v1.0
 * @date 2016/5/20 13:35
 */
public interface IBaseBuilder {

    /**
     * 转成JSON串
     *
     * @return 失败返回null，否则返回对应的JSON串
     */
    public String toJson() throws JSONException, SQLException;

}
