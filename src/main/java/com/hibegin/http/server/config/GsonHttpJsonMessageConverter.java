package com.hibegin.http.server.config;

import java.lang.reflect.Type;

public class GsonHttpJsonMessageConverter implements HttpJsonMessageConverter {
    @Override
    public String toJson(Object obj) throws Exception {
        Object gson = Class.forName("com.google.gson.Gson").getDeclaredConstructor().newInstance();
        return ((String) Class.forName("com.google.gson.Gson").getMethod("toJson", Object.class).invoke(gson, obj));
    }

    @Override
    public Object fromJson(String jsonStr) throws Exception {
        Object gson = Class.forName("com.google.gson.Gson").getDeclaredConstructor().newInstance();
        return Class.forName("com.google.gson.Gson").getMethod("fromJson", String.class, Type.class).invoke(gson, jsonStr, Object.class);
    }
}
