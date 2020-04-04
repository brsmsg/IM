package com.example.factory.utils.webSocket;

import android.content.Context;
import android.util.Log;

import com.example.factory.model.api.webSocket.Msg;
import com.example.factory.model.api.webSocket.WebSocketModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * @author brsmsg
 * @time 2020/3/31
 */
public class ClientWebSocketListener extends WebSocketListener {

    private static Msg msg;

    private Context mContext;



    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        //建立连接时回调
        super.onOpen(webSocket, response);
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        //收到消息
        super.onMessage(webSocket, text);
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
        super.onMessage(webSocket, bytes);
    }

    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        //准备关闭连接
        super.onClosing(webSocket, code, reason);
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        //连接完全关闭
        super.onClosed(webSocket, code, reason);
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        //连接失败
        super.onFailure(webSocket, t, response);
    }

}
