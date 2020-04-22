package com.example.factory.utils.webSocket;

import android.util.Log;

import com.example.common.app.Mapper;
import com.example.factory.Factory;
import com.example.factory.model.api.webSocket.Msg;
import com.example.factory.model.api.webSocket.WebSocketModel;
import com.google.gson.Gson;

/**
 * @author brsmsg
 * @time 2020/3/31
 */
public class WebSocketUtils {

    public static String transJson(WebSocketModel model){
        Gson gson = Factory.getInstance().getGson();
        String jsonStr = gson.toJson(model);
        return jsonStr;
    }

    public static WebSocketModel getMessage(String message){
        WebSocketModel model = Factory.getInstance().getGson()
                .fromJson(message, WebSocketModel.class);
        return model;
    }

    public static void sendMessgae(String myId, String oppositeId, String content, String msgId, int type){
        Msg msg = new Msg(myId, oppositeId, content, msgId);
        WebSocketModel model;
        if(type == Mapper.CHAT_ENCRYPTED){
            //加密发送
            model = new WebSocketModel(2, msg, "");
        }else{
            //解密发送
            model = new WebSocketModel(6, msg, "");
        }
        String jsonStr = transJson(model);
        Log.d("sendMessage", jsonStr);
        Factory.getInstance().getWebSocket().send(jsonStr);
    }

    public static void sign(String msgIdList){
        WebSocketModel model = new WebSocketModel(3, new Msg("", "", "", ""), msgIdList);
        String jsonStr = transJson(model);
        Log.d("signJsonStr", jsonStr);
        Factory.getInstance().getWebSocket().send(jsonStr);
    }

}