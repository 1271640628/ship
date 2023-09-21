package com.sugar.common.util;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class GsonUtil {
    private static <T> T newClass(Class<T> cls) throws IllegalAccessException, InstantiationException {
        T newCls = cls.newInstance();
        return newCls;
    }

    public static <T> T fromGson(String str, Class<T> cls) {
        try {
            if (str == null || str.isEmpty()) {
                T newCls = newClass(cls);
                return newCls;
            }
            String gsonStr = str.trim();
            Gson gson = new Gson();
            T t = gson.fromJson(gsonStr, cls);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> String toGson(T cls) {
        try {
            Gson gson = new Gson();
            String gsonStr = gson.toJson(cls);
            return gsonStr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> getGsonList(String str , Class<T[]> clazz) {
        if(str == null) return new ArrayList<>();
        T[] arr = new Gson().fromJson(str, clazz);
        List<T> list = new ArrayList<>();
        for(int i=0;i<arr.length;i++) {
            list.add(arr[i]);
        }
        return list;
    }
}
