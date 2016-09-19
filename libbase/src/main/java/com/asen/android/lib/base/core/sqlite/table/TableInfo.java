package com.asen.android.lib.base.core.sqlite.table;


import com.asen.android.lib.base.core.sqlite.field.FieldInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 表的信息，含字段集合和主键集合
 *
 * @author Asen
 * @version v1.0
 * @date 2016/4/5 11:51
 */
public class TableInfo {

    private String tableName; // 表名

    private List<FieldInfo> fieldList; // 字段信息集合

    private List<FieldInfo> idFieldList; // 主键字段信息

    private boolean isNeedGetIdAgain = false; // 是否需要重新获取ID

    public TableInfo(String tableName) {
        this.tableName = tableName;
        this.fieldList = new ArrayList<>();
    }

    public TableInfo(String tableName, List<FieldInfo> infoList) {
        this.tableName = tableName;
        this.fieldList = infoList == null ? new ArrayList<FieldInfo>() : infoList;
    }

    /**
     * 增加字段信息
     *
     * @param info 字段信息
     */
    public void addField(FieldInfo info) {
        if (info == null) return;
        isNeedGetIdAgain = true;
        fieldList.add(info);
    }

    /**
     * 增加字段信息的集合
     *
     * @param infoList 字段信息的集合
     */
    public void addFields(List<FieldInfo> infoList) {
        if (infoList == null || infoList.size() == 0) return;
        isNeedGetIdAgain = true;
        fieldList.addAll(infoList);
    }

    /**
     * 获得表名
     *
     * @return 表名
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * 获取所有的字段的List集合
     *
     * @return 所有的字段的List集合
     */
    public List<FieldInfo> getFieldList() {
        return fieldList;
    }

    /**
     * 获取所有的ID字段信息集合
     *
     * @return 所有的ID字段信息集合
     */
    public List<FieldInfo> getIdFieldList() {
        if (idFieldList == null) {
            idFieldList = new ArrayList<>();
            isNeedGetIdAgain = true;
        }
        if (isNeedGetIdAgain) {
            idFieldList.clear();
            for (FieldInfo info : fieldList) {
                if (info.isId()) {
                    idFieldList.add(info);
                }
            }
            isNeedGetIdAgain = false;
        }
        return idFieldList;
    }

    /**
     * 根据字段名称获取字段信息
     *
     * @param fieldName 字段名称
     * @return 字段信息
     */
    public FieldInfo getFieldInfoByFieldName(String fieldName) {
        if (fieldName == null) return null;
        for (FieldInfo info : fieldList) {
            if (fieldName.equals(info.getFieldName())) {
                return info;
            }
        }
        return null;
    }

}
