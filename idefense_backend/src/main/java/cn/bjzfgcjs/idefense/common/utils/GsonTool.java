package cn.bjzfgcjs.idefense.common.utils;

import cn.bjzfgcjs.idefense.common.annotation.JsonSkipTag;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by weiliang on 15/11/20.
 */
public class GsonTool {
    private static Gson gson = new GsonBuilder()
            .setExclusionStrategies(new JsonExclusionStrategy(JsonSkipTag.class))
            .create();

    private static Gson gsonSimple = new Gson();

    public static String getString(JsonObject jo, String key){
        return getString(jo, key, null);
    }

    public static String getString(JsonObject jo, String key, String dv){
        return (jo.has(key) && !jo.get(key).isJsonNull()) ? jo.get(key).getAsString() : dv;
    }

    public static JsonArray getJsonArray(JsonObject jo, String key){
        return (jo.has(key) && !jo.get(key).isJsonNull()) ? jo.get(key).getAsJsonArray() : null;
    }

    public static Long getLong(JsonObject jo, String key){
        return getLong(jo, key, null);
    }

    public static Long getLong(JsonObject jo, String key, Long dv){
        return (jo.has(key) && !jo.get(key).isJsonNull()) ? (Long)jo.get(key).getAsLong() : dv;
    }

    public static Integer getInt(JsonObject jo, String key){
        return getInt(jo, key, null);
    }

    public static Integer getInt(JsonObject jo, String key, Integer dv){
        return (jo.has(key) && !jo.get(key).isJsonNull())? (Integer)jo.get(key).getAsInt() : dv;
    }

    public static Double getDouble(JsonObject jo, String key){
        return getDouble(jo, key, null);
    }

    public static Double getDouble(JsonObject jo, String key, Double dv){
        return (jo.has(key) && !jo.get(key).isJsonNull()) ? (Double)jo.get(key).getAsDouble() : dv;
    }

    public static Boolean getBoolean(JsonObject jo, String key){
        return getBoolean(jo, key, null);
    }

    public static Boolean getBoolean(JsonObject jo, String key, Boolean dv){
        return (jo.has(key) && !jo.get(key).isJsonNull()) ? (Boolean)jo.get(key).getAsBoolean() : dv;
    }

    public static JsonObject getJsonObject(JsonObject jo, String key){
        if(!jo.has(key)){
            return null;
        }

        JsonElement je = jo.get(key);
        if(je.isJsonObject()){
            return je.getAsJsonObject();
        }
        return null;
    }

    public static boolean isJosnValid(String str){
        try {
            new JsonParser().parse(str);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public static JsonObject parseJsonObject(String str){
        return new JsonParser().parse(str).getAsJsonObject();
    }
    public static JsonElement parseJsonObject(Reader reader){
        return new JsonParser().parse(reader);
    }
    public static JsonObject parseJsonObject(byte[] bytes, String charset) throws UnsupportedEncodingException {
        return new JsonParser().parse(new String(bytes, charset)).getAsJsonObject();
    }

    public static JsonArray parseJsonArray(String str){
        return new JsonParser().parse(str).getAsJsonArray();
    }

    public static JsonArray parseJsonArray(byte[] bytes, String charset) throws UnsupportedEncodingException {
        return new JsonParser().parse(new String(bytes, charset)).getAsJsonArray();
    }

    public static String parseString(String str){
        return new JsonParser().parse(str).getAsString();
    }


    public static String toJson(Object obj){
        return gson.toJson(obj);
    }

    public static JsonElement toJsonTree(Object obj){
        return gson.toJsonTree(obj);
    }

    public static JsonElement toJsonTreeFull(Object obj){
        return gsonSimple.toJsonTree(obj);
    }

    public static <T> T fromJson(String str, Class<T> clazz){
        try {
            return new Gson().fromJson(str, clazz);
        }catch (Exception e){
            return null;
        }

    }

    public static <T> T fromJson(JsonElement jsonElement, Class<T> clazz){
        try {
            return new Gson().fromJson(jsonElement, clazz);
        }catch (Exception e){
            return null;
        }

    }

    public static <T> List<T> fromJsonList(String str, Class<T> clazz){
        try {
            Type type = new TypeToken<ArrayList<JsonObject>>(){}.getType();
            ArrayList<JsonObject> jsonObjects = new Gson().fromJson(str, type);

            ArrayList<T> arrayList = new ArrayList<T>();
            for (JsonObject jsonObject : jsonObjects)
            {
                arrayList.add(new Gson().fromJson(jsonObject, clazz));
            }
            return arrayList;
        }catch (Exception e){
            return null;
        }
    }

    public static <T> List<T> fromJsonList(JsonElement jsonElement, Class<T> clazz){
        try {
            Type type = new TypeToken<ArrayList<T>>(){}.getType();
            return new Gson().fromJson(jsonElement, type);
        }catch (Exception e){
            return null;
        }
    }

    public static JsonObject shallowCopy(JsonObject src){
        if (src == null) {
            return null;
        }
        JsonObject dst = new JsonObject();
        for (Map.Entry<String, JsonElement> entry: src.entrySet()){
            dst.add(entry.getKey(), entry.getValue());
        }
        return dst;
    }
}
