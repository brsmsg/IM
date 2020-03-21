package com.example.factory.utils;

import android.util.Log;

import com.example.factory.Factory;
import com.example.factory.model.RawMotion;

import java.io.IOException;
import java.util.List;

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
     * 进行post操作
     * @param object 要传转化为json的model类
     * @param url 要请求的服务器url
     * @return json字符串
     */
    public static String postJson(Object object, String url){
        String result = null;

        //得到要发送的json字符串
        String json = Factory.getInstance().getGson().toJson(object);

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
            Log.d("success? ", String.valueOf(response.isSuccessful()));

            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            Log.d("result", result);
            return result;
        }
    }


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

        Log.d("body", body.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Log.d("request", request.toString());

        try {
            Response response = okHttpClient.newCall(request).execute();

            Log.d("success? ", String.valueOf(response.isSuccessful()));

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
