package com.example.factory.presenter.Chat;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.example.common.RSA.RsaEncryptUtil;
import com.example.factory.Factory;
import com.example.factory.model.MsgUI;
import com.example.factory.model.RawMotion;
import com.example.factory.model.api.webSocket.Msg;
import com.example.factory.model.api.webSocket.WebSocketModel;
import com.example.factory.utils.NetUtils;
import com.example.factory.utils.webSocket.WebSocketUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author brsmsg
 * @time 2020/3/25
 */
public class ChatPresenter implements ChatContract.Presenter {

    private static final String predictUrl = "http://101.200.240.107:8000/predict";

    String resultDemo = "brsmsg_1586334335390388232,brsmsg_1586334335390388232";

    //准确率阀值
    private static final double THRESHOLD = 0.5;

    private ChatContract.View mChatView;

    private List<MsgUI> msgList;

    private List<RawMotion> mRawMotionList;

    private String myId;

    //定时器
    private Timer mTimer;
    private TimerTask mTimerTask;

    private List<String> resultIdList;

    private String resultStr = "";


    public ChatPresenter(ChatContract.View chatView){
        mChatView = chatView;
        mChatView.setPresenter(this);
    }

    @Override
    public void start() {
        msgList = new ArrayList<>();
        mChatView.initUI(msgList);

        //初始化结果list
        resultIdList = new ArrayList<>();

        mTimer = new Timer();

        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if(mChatView.getRawMotionList().size() > 0){
                    //获取motionEvent并移除点击事件
                    mRawMotionList = removeClickEvent(mChatView.getRawMotionList());
                    //没有滑动或者只滑动一次不进行判断
                    if(mRawMotionList.size() > 1){
                        //预测返回结果
                        resultStr = NetUtils.postJson(mRawMotionList, predictUrl);
//                        resultStr = resultDemo;
                    }
                    if(!TextUtils.isEmpty(resultStr)){
                        resultIdList = Arrays.asList(resultStr.split(","));
                        for(String s:resultIdList){
                            Log.d("id", s);
                        }

                    }


                    Log.d("benren?", String.valueOf(mChatView.classify(resultIdList)));
                    //判断是否是本人，不是本人就加密消息
                    if(!mChatView.classify(resultIdList)){
                        mChatView.encryptMsg();
                    }
                    //清空存放滑动数据的list
                    mRawMotionList.clear();
                    mChatView.clearMotionList();
                }

            }
        };
        //延迟60s，周期60s
        mTimer.schedule(mTimerTask, 10*1000, 10*1000);

    }

    /**
     * 发送消息
     * @param content 消息内容
     * @param myPortrait 我的头像
     * @param myId 我的id
     * @param oppositeId 接受方id
     * @param publicKey 公钥
     */
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

    /**
     * 接受消息
     * @param content 消息内容
     * @param oppositePortrait 对方头像
     */
    @Override
    public void receiveMessage(String content, String oppositePortrait){
        WebSocketModel model =  WebSocketUtils.getMessage(content);
        Msg msg = model.getMessage();
        String myId = msg.getReceiveUserId();
        String oppositeId = msg.getSendUserId();
        String msgContent = msg.getMsg();
        String msgId = msg.getMsgId();

        String decryptedMsg =  "";
        try {
            decryptedMsg = RsaEncryptUtil.decrypt(msgContent, RsaEncryptUtil.getPrivateKey());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!TextUtils.isEmpty(decryptedMsg)){
            MsgUI msgUI = new MsgUI(decryptedMsg, oppositePortrait, MsgUI.TYPE_RECEIVED, MsgUI.DECRYPTED);
            Log.d("receive", content);
            mChatView.refreshUI(msgUI);
            WebSocketUtils.sign(msgId);
        }
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

    /**
     * 去掉点击事件，只保留滑动事件
     * @param rawMotionList
     * @return
     */
    @Override
    public List<RawMotion> removeClickEvent(List<RawMotion> rawMotionList){
        for(int i = 0; i < rawMotionList.size(); i++) {
            if (rawMotionList.get(i).getAction() == 1) {
                if (rawMotionList.get(i - 1).getAction() == 0) {
                    rawMotionList.remove(i);
                    rawMotionList.remove(i - 1);
                    continue;
                } else if (rawMotionList.get(i - 2).getAction() == 0) {
                    rawMotionList.remove(i);
                    rawMotionList.remove(i - 1);
                    rawMotionList.remove(i - 2);
                    continue;
                } else if (rawMotionList.get(i - 3).getAction() == 0) {
                    rawMotionList.remove(i);
                    rawMotionList.remove(i - 1);
                    rawMotionList.remove(i - 2);
                    rawMotionList.remove(i - 3);
                    continue;
                } else if (rawMotionList.get(i - 4).getAction() == 0) {
                    rawMotionList.remove(i);
                    rawMotionList.remove(i - 1);
                    rawMotionList.remove(i - 2);
                    rawMotionList.remove(i - 3);
                    rawMotionList.remove(i - 4);
                    continue;
                }

            }
        }
        return rawMotionList;
    }

    @Override
    public boolean getPrecision(String myId, List<String> resultIdList){
        //总的id数
        int size = resultIdList.size();
        int rightCount = 0;

        for(int i = 0; i< size; i++){
            if(resultIdList.get(i).equals(myId)){
                rightCount ++;
            }
        }
        double precision = (double)rightCount/ (double)size;

        Log.d("precision", String.valueOf(precision));
        return precision >= THRESHOLD;
    }

}
