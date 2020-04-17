package com.example.factory.utils;

import android.util.Log;

import com.example.factory.Factory;
import com.example.factory.model.RawMotion;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author brsmsg
 * @time 2020/3/11
 */
public class NetUtils {

    /**
     * post一个对象
     * @param object 要传转化为json的model类
     * @param url 要请求的服务器url
     * @return json字符串
     */
    public static String postJson(Object object, String url){
        String result = null;
        //获取gson对象
        Gson gson = Factory.getInstance().getGson();
        //得到要发送的json字符串
        String json = gson.toJson(object);

        Log.d("testJson", json);

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = RequestBody.create(json,
                MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();

            result = Objects.requireNonNull(response.body()).string();
            Log.d("result", result);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return result;
        }
    }

    /**
     * 发送json数组
     * @param objectList list
     * @param url url
     * @return 返回的信息
     */
    public static String postJson(List<RawMotion> objectList, String url){
        String result = null;

        String json = Factory.getInstance().getGson().toJson(objectList);
        if(json.length() > 4000){
            for(int i =0; i<json.length(); i+= 4000){
                if(i+4000<json.length()) {
                    Log.d("RawMotionList", json.substring(i, i + 4000));
                }else{
                    Log.d("RawMotionList", json.substring(i, json.length()));
                }
            }
        }else {
            Log.d("RawMotionList", json);
        }

        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody body = RequestBody.create(json,
                MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();

            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(result == null) {
                Log.d("result", "null");
            }else{
                Log.d("result", result);
            }
            return result;

        }
    }



    /**
     * 一个键值对
     * @param key
     * @param value
     * @param url
     * @return
     */
    public static String postKeyValue(String key, String value, String url){
        String result = null;

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add(key, value)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();

            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(result == null) {
                Log.d("result", "null");
            }else{
                Log.d("result", result);
            }
            return result;
        }

    }


    /**
     * 两个键值对
     * @param key1
     * @param value1
     * @param key2
     * @param value2
     * @param url
     * @return
     */
    public static String postKeyValue(String key1, String value1, String key2, String value2, String url){
        String result = null;

        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add(key1, value1)
                .add(key2, value2)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();

            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(result == null) {
                Log.d("result", "null");
            }else{
                Log.d("result", result);
            }
            return result;
        }

    }


}
