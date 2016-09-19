package com.asen.android.lib.base.core.sqlite.builder.impl;

import android.database.sqlite.SQLiteQueryBuilder;

import com.asen.android.lib.base.core.sqlite.builder.IQueryBuilder;
import com.asen.android.lib.base.core.sqlite.builder.type.BuilderType;
import com.asen.android.lib.base.core.sqlite.builder.type.SqliteType;
import com.asen.android.lib.base.core.sqlite.utils.SqlExceptionUtil;
import com.asen.android.lib.base.tool.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * 查询的Builder
 *
 * @author Asen
 * @version v1.0
 * @date 2016/9/9 16:58
 */
class QueryBuilder extends BaseSqlBuilder implements IQueryBuilder {

    private String table;

    private String[] columns;

    private String selection;

    private String[] selectionArgs;

    private String groupBy;

    private String having;

    private String orderBy;

    private String limit;

    /**
     * 构造函数
     *
     * @param sql    非查询SQL
     * @param params SQL中带'?'的参数，参数个数需与'?'个数一致
     */
    public QueryBuilder(String sql, Object[] params) {
        super(sql, params);
    }

    /**
     * 构造函数
     *
     * @param table         要查询的标明
     * @param columns       想要显示的列，若为空则返回所有列
     * @param selection     where子句，声明要返回的行的要求，如果为空则返回表的所有行
     * @param selectionArgs where子句对应的条件值
     * @param groupBy       分组方式，若为空则不分组
     * @param having        having条件，若为空则返回全部
     * @param orderBy       排序方式，为空则为默认排序方式
     * @param limit         限制返回的记录的条数，为空则不限制
     */
    public QueryBuilder(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        setBuilderType(BuilderType.ANDROID_SQL);
        this.table = table;
        this.columns = columns;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
        this.groupBy = groupBy;
        this.having = having;
        this.orderBy = orderBy;
        this.limit = limit;
    }

    @Override
    void refreshAndroidSql() throws SQLException {
        String sql = SQLiteQueryBuilder.buildQueryString(false, table, columns, selection, groupBy, having, orderBy, limit);
        setSql(sql);
        List<Object> paramsList = getParamsList();
        paramsList.clear();
        if (selectionArgs != null) {
            Collections.addAll(paramsList, selectionArgs);
        }
    }

    @Override
    void refreshObject2Android(Object senObj) throws SQLException {
        throw SqlExceptionUtil.create("QueryBuilder 不支持对象形式", null);
    }

    @Override
    void refreshObject2Sql(Object senObj) throws SQLException {
        throw SqlExceptionUtil.create("QueryBuilder 不支持对象形式", null);
    }

    @Override
    public String getTableName() throws SQLException {
        return table;
    }

    @Override
    public String[] getColumns() {
        return columns;
    }

    @Override
    public String getSelection() {
        return selection;
    }

    @Override
    public String[] getSelectionArgs() {
        return selectionArgs;
    }

    @Override
    public String getGroupBy() {
        return groupBy;
    }

    @Override
    public String getHaving() {
        return having;
    }

    @Override
    public String getOrderBy() {
        return orderBy;
    }

    @Override
    public String getLimit() {
        return limit;
    }

    @Override
    public String toJson() throws JSONException, SQLException {
        JSONObject jsonObject = new JSONObject();
        // sql语句操作的类型
        jsonObject.put(SqliteType.KEY_NAME, SqliteType.QUERY.getName());
        // builder的类型，sql语句类型，还是其他
        jsonObject.put(BuilderType.KEY_NAME, builderType.getName());

        // 拼写具体内容：
        JSONObject contentObj = new JSONObject();
        if (builderType == BuilderType.SQL) {
            // 1、拼写sql语句
            contentObj.put(BaseBuilder.KEY_SQL_SQL, sql);
            if (paramsList != null) {
                contentObj.put(BaseBuilder.KEY_SQL_PARAMS, JsonUtil.listToJsonArray(paramsList));
            } else {
                contentObj.put(BaseBuilder.KEY_SQL_PARAMS, null);
            }
        } else if (builderType == BuilderType.ANDROID_SQL) {
            // 2、拼写Android原生SQL
            contentObj.put(BaseBuilder.KEY_ANDROID_TABLE, table);
            contentObj.put(BaseBuilder.KEY_ANDROID_COLUMNS, JsonUtil.arrayToJsonArray(columns));
            contentObj.put(BaseBuilder.KEY_ANDROID_SELECTION, selection);
            contentObj.put(BaseBuilder.KEY_ANDROID_SELECTION_ARGS, JsonUtil.arrayToJsonArray(selectionArgs));
            contentObj.put(BaseBuilder.KEY_ANDROID_GROUP_BY, groupBy);
            contentObj.put(BaseBuilder.KEY_ANDROID_HAVING, having);
            contentObj.put(BaseBuilder.KEY_ANDROID_ORDER_BY, orderBy);
            contentObj.put(BaseBuilder.KEY_ANDROID_LIMIT, limit);
        }

        jsonObject.put(BaseBuilder.KEY_CONTENT, contentObj);

        return jsonObject.toString();
    }


    /**
     * 将jsonObject转换成 QueryBuilder对象
     *
     * @param jsonObject JSON对象
     * @return QueryBuilder对象
     */
    public static QueryBuilder fromJson(JSONObject jsonObject) throws SQLException {
        BuilderType builderType = BuilderType.getBuilderTypeByName(jsonObject.optString(BuilderType.KEY_NAME));
        JSONObject contentObj = jsonObject.optJSONObject(BaseBuilder.KEY_CONTENT);

        if (builderType == BuilderType.SQL) {
            // 纯SQL语句
            String sql = contentObj.optString(BaseBuilder.KEY_SQL_SQL);
            JSONArray paramsArray = contentObj.optJSONArray(BaseBuilder.KEY_SQL_PARAMS);
            return new QueryBuilder(sql, JsonUtil.jsonArrayToList(paramsArray).toArray());
        } else if (builderType == BuilderType.ANDROID_SQL) {
            // Android的SQL执行
            String table = contentObj.optString(BaseBuilder.KEY_ANDROID_TABLE);

            Object[] objectsColumns = JsonUtil.jsonArrayToArray(contentObj.optJSONArray(BaseBuilder.KEY_ANDROID_COLUMNS));
            String[] columns = null;
            if (objectsColumns.length > 0) {
                columns = new String[objectsColumns.length];
                for (int i = 0; i < objectsColumns.length; i++) {
                    columns[i] = (String) objectsColumns[i];
                }
            }

            String selection = contentObj.optString(BaseBuilder.KEY_ANDROID_SELECTION);

            Object[] objectsSelectionArgs = JsonUtil.jsonArrayToArray(contentObj.optJSONArray(BaseBuilder.KEY_ANDROID_SELECTION_ARGS));
            String[] selectionArgs = null;
            if (objectsSelectionArgs.length > 0) {
                selectionArgs = new String[objectsSelectionArgs.length];
                for (int i = 0; i < objectsSelectionArgs.length; i++) {
                    selectionArgs[i] = (String) objectsSelectionArgs[i];
                }
            }

            String groupBy = contentObj.optString(BaseBuilder.KEY_ANDROID_GROUP_BY);
            String having = contentObj.optString(BaseBuilder.KEY_ANDROID_HAVING);
            String orderBy = contentObj.optString(BaseBuilder.KEY_ANDROID_ORDER_BY);
            String limit = contentObj.optString(BaseBuilder.KEY_ANDROID_LIMIT);
            return new QueryBuilder(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        } else {
            throw SqlExceptionUtil.create("Known builderType!", null);
        }
    }

}