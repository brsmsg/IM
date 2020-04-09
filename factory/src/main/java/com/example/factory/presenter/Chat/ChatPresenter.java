package com.example.factory.presenter.Chat;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.example.common.RSA.RsaEncryptUtil;
import com.example.factory.Factory;
import com.example.factory.model.MsgUI;
import com.example.factory.model.api.webSocket.Msg;
import com.example.factory.model.api.webSocket.WebSocketModel;
import com.example.factory.utils.webSocket.WebSocketUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author brsmsg
 * @time 2020/3/25
 */
public class ChatPresenter implements ChatContract.Presenter {

    private ChatContract.View mChatView;

    private List<MsgUI> msgList;

    public ChatPresenter(ChatContract.View chatView){
        mChatView = chatView;
        mChatView.setPresenter(this);
    }

    @Override
    public void start() {
        List<MsgUI> msgList = new ArrayList<>();
        mChatView.initUI(msgList);
    }

    @Override
    public void sendMessage(String content, String myPortrait,
                            String myId, String oppositeId, String publicKey) {

        String encryptedMsg = "";
        //公钥加密
        try {
            encryptedMsg= RsaEncryptUtil.encrypt(content, publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!TextUtils.isEmpty(encryptedMsg)) {
            //发送消息
            WebSocketUtils.sendMessgae(myId, oppositeId, encryptedMsg, "");
        }
        //对聊天UI进行更新
        MsgUI msgUI = new MsgUI(content, myPortrait, MsgUI.TYPE_SEND, MsgUI.DECRYPTED);

        mChatView.refreshUI(msgUI);
    }

    @Override
    public void receiveMessage(String content, String oppositePortrait) {
        WebSocketModel model =  WebSocketUtils.getMessage(content);
        Msg msg = model.getMessage();
        String myId = msg.getReceiveUserId();
        String oppositeId = msg.getSendUserId();
        String msgContent = msg.getMsg();

        MsgUI msgUI = new MsgUI(msgContent,oppositePortrait, MsgUI.TYPE_RECEIVED, MsgUI.UNDECRYPTED);
        Log.d("receive", content);
        mChatView.refreshUI(msgUI);
    }

    @Override
    public void updateSession(Context context, String oppositeId, String lastMsg) {

        Intent intent = new Intent();
        intent.putExtra("ID", oppositeId);
        intent.putExtra("LAST_MSG", lastMsg);
        intent.setAction("com.example.broadcast.UPDATE_SESSION");
        Log.d("lastMsg", lastMsg);
        context.sendBroadcast(intent);

    }
}
