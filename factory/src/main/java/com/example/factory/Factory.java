package com.example.factory;

import android.app.Application;
import android.content.Context;

import com.dbflow5.config.FlowConfig;
import com.dbflow5.config.FlowManager;
import com.example.factory.utils.NetUtils;
import com.example.factory.utils.webSocket.ClientWebSocketListener;
import com.google.gson.Gson;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

/**
 * @author brsmsg
 * @time 2020/3/8
 */
public class Factory {
    private Context mContext;

    //创建Factory对象
    private static Factory mFactory = new Factory();

    //全局线程池
    private static Executor executor = Executors.newFixedThreadPool(4);

    //全局Gson
    private static Gson gson = new Gson();

    //全局websocketclient
    private static WebSocket mWebSocket;

    //全局webSocket监听
    private static ClientWebSocketListener mWebSocketListener = new ClientWebSocketListener();

    /**
     * 获取单例对象
     * @return
     */
    public static Factory getInstance(){
        return mFactory;
    }

    private Factory() {
    }

    public void init(Context context){
        mContext = context;
    }

    /**
     * 获取全局线程池
     * @return executor
     */
    public Executor getThreadPool(){
        return executor;
    }

    /**
     * 获取全局Gson
     * @return gson
     */
    public Gson getGson(){
        return gson;
    }

    public void initWebSocket(String url){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        mWebSocket = okHttpClient.newWebSocket(request, mWebSocketListener);
    }

    public WebSocket getWebSocket(){
        return mWebSocket;
    }

}
