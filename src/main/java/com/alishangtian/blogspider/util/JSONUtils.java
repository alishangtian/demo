package com.alishangtian.blogspider.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;

/**
 * @author xunqiang.hu
 * @date 2019/10/9
 */
@Log4j2
public class JSONUtils {

    /**
     * 默认的objectMapper  线程安全的,多线程调用效率低
     */
    private static Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyyy-MM-dd HH:mm:ss").create();

    private JSONUtils() {
    }

    /**
     * 转换json(对效率要求比较高的服务请自己 new 个 mapper)
     *
     * @param obj
     * @return
     */
    public static String toJSONString(Object obj) {
        return toJSONString(obj, gson);
    }

    public static String toJSONString(Object obj, Gson gson) {
        if (obj == null) {
            return null;
        }
        try {
            return gson.toJson(obj);
        } catch (Exception e) {
            log.error("JSONUtils toJSONString failed ! obj :{}", obj.toString(), e);
            return null;
        }
    }

    /**
     * 有格式的
     *
     * @param obj
     * @return
     */
    public static String toJSONStringPretty(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return gson.toJson(obj);
        } catch (Exception e) {
            log.error("JSONUtils toJSONStringPretty failed ! obj :{}", obj.toString(), e);
            return null;
        }
    }

    /**
     * 字符串转对象 (对效率要求比较高的服务请使用自己的mapper)
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        return parseObject(json, clazz, gson);
    }

    public static <T> T parseObject(String json, Class<T> clazz, Gson gson) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            return gson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            log.error("JSONUtils parseObject failed ! json :{}", json, e);
            return null;
        }
    }

    public static <T> T parseObject(String json, Type typeReference) {
        if (StringUtils.isEmpty(json) || typeReference == null) {
            return null;
        }
        try {
            return gson.fromJson(json, typeReference);
        } catch (JsonSyntaxException e) {
            log.error("JSONUtils parseObject failed ! json :{}", json, e);
            return null;
        }
    }
}