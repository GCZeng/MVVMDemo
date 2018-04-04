package org.zgc.mvvmdemo.util;

import com.google.gson.Gson;

/**
 * Created by Nick on 2017/12/10
 */
public class JSONUtil {
    private static Gson mGson = null;

    static {
        mGson = new Gson();
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return mGson.fromJson(json, classOfT);
    }

    public static String toJson(Object object) {
        return mGson.toJson(object);
    }

}
