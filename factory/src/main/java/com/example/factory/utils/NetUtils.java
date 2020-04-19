package com.example.factory.utils;

import android.util.Log;

import com.example.factory.Factory;
import com.example.factory.model.RawMotion;
import com.example.factory.utils.webSocket.ClientWebSocketListener;
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
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

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

        Log.d("body", body.toString());

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
     * 输入要查询的ID和查询者名称来得到返回数据 查询好友和发出好友请求
     *
     * @param key   the key
     * @param value the value
     * @param url   the url
     * @return string result
     */
    public static String postKeyValue(String key, String value,String url)  {

        String result = null;

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("myId",key)
                .add("friendUsername",value)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try{
            Response response = client.newCall(request).execute();
            result = response.body().string();
        }catch (IOException e){
            e.printStackTrace();
        }finally {

            return result;
        }


    }

    /**
     * 输入要查询的ID来得到返回数据 拉取联系人和查询接收到的好友请求
     *
     * @param key the key
     * @param url the url
     * @return the string
     * @throws IOException the io exception
     */
    public static String postKey(String key, String url) {
        String result =null;

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("myId",key)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try{
            Response response = client.newCall(request).execute();
            result = response.body().string();
        }catch (IOException e){
            e.printStackTrace();
        }finally {

            return result;
        }



    }



}
