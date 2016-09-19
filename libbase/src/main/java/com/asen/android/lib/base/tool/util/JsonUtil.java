package com.asen.android.lib.base.tool.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * JSON解析和生成的工具类
 *
 * @author Asen
 * @version v1.0
 * @date 2016/9/8 15:47
 */
public class JsonUtil {

    /**
     * 将List集合转换成 JSONArray（只支持基本数据类型的集合）
     *
     * @param dataList 数据List集合
     * @return JSONArray对象
     */
    public static JSONArray listToJsonArray(List<Object> dataList) {
        JSONArray jsonArray = new JSONArray();
        if (dataList == null) return jsonArray;
        for (Object obj : dataList) {
            jsonArray.put(obj == null ? null : obj.toString());
        }
        return jsonArray;
    }

    /**
     * 将数组集合转换成 JSONArray（只支持基本数据类型的集合）
     *
     * @param dataList 数组集合
     * @return JSONArray对象
     */
    public static JSONArray arrayToJsonArray(Object[] dataList) {
        JSONArray jsonArray = new JSONArray();
        if (dataList == null) return jsonArray;
        for (Object obj : dataList) {
            jsonArray.put(obj == null ? null : obj.toString());
        }
        return jsonArray;
    }

    /**
     * 将List集合转换成 json串（只支持基本数据类型的集合）
     *
     * @param dataList 数据List集合
     * @return json串
     */
    public static String listToJson(List<Object> dataList) {
        return listToJsonArray(dataList).toString();
    }

    /**
     * 将 JSONArray对象 转换成 List集合（只支持基本数据类型的集合）
     *
     * @param jsonArray JSONArray对象
     * @return 数据List集合
     */
    public static List<Object> jsonArrayToList(JSONArray jsonArray) {
        List<Object> dataList = new ArrayList<>();
        if (jsonArray == null) return dataList;
        for (int i = 0; i < jsonArray.length(); i++) {
            Object opt = jsonArray.opt(i);
            dataList.add(opt == null || "null".equals(opt.toString()) ? null : opt);
        }
        return dataList;
    }

    /**
     * 将 JSONArray对象 转换成 List集合（只支持基本数据类型的集合）
     *
     * @param jsonArray JSONArray对象
     * @return 数据List集合
     */
    public static Object[] jsonArrayToArray(JSONArray jsonArray) {
        if (jsonArray == null) return new Object[]{};
        Object[] dataList = new Object[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            Object opt = jsonArray.opt(i);
            dataList[i] = opt == null || "null".equals(opt.toString()) ? null : opt;
        }
        return dataList;
    }

    /**
     * 将 json串 转换成 List集合（只支持基本数据类型的集合）
     *
     * @param json json串
     * @return 数据List集合
     * @throws JSONException
     */
    public static List<Object> jsonToList(String json) throws JSONException {
        return jsonArrayToList(new JSONArray(json));
    }

    /**
     * 将Map集合转换成 JSONObject（只支持基本数据类型的集合）
     *
     * @param dataMap 数据Map集合
     * @return JSONObject 对象
     * @throws JSONException
     */
    public static JSONObject mapToJsonObject(Map<String, Object> dataMap) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        if (dataMap == null) return jsonObject;
        Set<String> keySet = dataMap.keySet();
        for (String key : keySet) {
            jsonObject.put(key, dataMap.get(key));
        }
        return jsonObject;
    }

    /**
     * 将Map集合转换成 json串（只支持基本数据类型的集合）
     *
     * @param dataMap 数据Map集合
     * @return json串
     * @throws JSONException
     */
    public static String mapToJson(Map<String, Object> dataMap) throws JSONException {
        return mapToJsonObject(dataMap).toString();
    }

    /**
     * 将 JSONObject 转换成 Map集合（只支持基本数据类型的集合）
     *
     * @param jsonObject JSONObject
     * @return Map集合
     */
    public static Map<String, Object> jsonObjectToMap(JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<>();
        if (jsonObject == null) return map;

        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object opt = jsonObject.opt(key);
            map.put(key, opt == null || "null".equals(opt.toString()) ? null : opt);
        }

        return map;
    }

}
