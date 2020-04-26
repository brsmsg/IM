package com.example.factory;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.dbflow5.config.FlowConfig;
import com.dbflow5.config.FlowManager;
import com.example.factory.model.api.webSocket.Msg;
import com.example.factory.model.api.webSocket.WebSocketModel;
import com.example.factory.utils.NetUtils;
import com.example.factory.utils.webSocket.ClientWebSocketListener;
import com.example.factory.utils.webSocket.WebSocketUtils;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
    private static ExecutorService executor = Executors.newFixedThreadPool(4);

    //全局Gson
    private static Gson gson = new Gson();

    //全局websocketclient
    private static WebSocket mWebSocket;

    //全局webSocket监听
    private static ClientWebSocketListener mWebSocketListener = new ClientWebSocketListener(){

    };

    /**
     * 获取单例对象
     * @return
     */
    public static Factory getInstance(){
        return mFactory;
    }

    private Factory() {
    }

    public void setContext(Context context){
        mContext = context;
    }

    /**
     * 获取全局线程池
     * @return executor
     */
    public ExecutorService getThreadPool(){
        return executor;
    }

    /**
     * 获取全局Gson
     * @return gson
     */
    public Gson getGson(){
        return gson;
    }

    /**
     * 初始化webSocket
     * @param url webSocket 路径
     */
    public void initWebSocket(String url, final String id, final Context context){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        mWebSocket = okHttpClient.newWebSocket(request, new ClientWebSocketListener(){
            //重写onOpen方法
            @Override
            public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
                super.onOpen(webSocket, response);
                WebSocketModel initModel = new WebSocketModel(1, new Msg(id,"", "", ""), "");
                String initJson = WebSocketUtils.transJson(initModel);
                webSocket.send(initJson);
                Log.d("onOpen", initJson);
            }

            //重写onMesaage方法
            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
                super.onMessage(webSocket, text);
                Log.d("onMessage return", text);

                //收到消息后发送广播
                Intent intent = new Intent();
                intent.putExtra("MSG", text);
                intent.setAction("com.example.broadcast.MESSAGE");
                context.sendBroadcast(intent);
            }
        });
    }

    public WebSocket getWebSocket(){
        return mWebSocket;
    }

}
