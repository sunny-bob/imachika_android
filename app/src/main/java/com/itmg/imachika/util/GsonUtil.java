package com.itmg.imachika.util;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/22 0022.
 * 通过Gosn写的json转化工具
 */

public class GsonUtil {
    private static Gson gson = new Gson();
    static String json;
    //json转对象
    public static <T>Object praseJsonToModel(String json,Class<T> clazz){
        Object o = null;
        try {
            o = gson.fromJson(json,clazz);
        }catch (Exception e){
            e.printStackTrace();
        }
        return o;
    }
    //json转数组
    public static <T>List<Object> praseJsonToList(String json, Class<T> clazz){
        List<Object> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0;i<array.length();i++){
                Object o  = gson.fromJson(array.get(i).toString(),clazz);
                list.add(o);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
    //对象转json
    public static String objectToJson(Object o){
        json = gson.toJson(o);
        return json;
    }
    //数组转json
    public static String listToJson(List<?> list){
        json = gson.toJson(list);
        return json;
    }
    /**
      * 将Map转化为Json
      *
      * @param map
      * @return String
      */
    public static <T> String mapToJson(Map<String, T> map) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(map);
        return jsonStr;
    }
}
