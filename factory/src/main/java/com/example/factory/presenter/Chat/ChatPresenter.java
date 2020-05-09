package com.example.factory.presenter.Chat;

import android.content.Context;
import android.content.Intent;
import android.icu.util.LocaleData;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.common.RSA.RsaEncryptUtil;
import com.example.common.app.Mapper;
import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.model.MsgUI;
import com.example.factory.model.RawMotion;
import com.example.factory.model.api.History;
import com.example.factory.model.api.message.HistoryModel;
import com.example.factory.model.api.webSocket.Msg;
import com.example.factory.model.api.webSocket.WebSocketModel;
import com.example.factory.utils.NetUtils;
import com.example.factory.utils.SpUtils;
import com.example.factory.utils.webSocket.WebSocketUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author brsmsg
 * @time 2020/3/25
 */
public class ChatPresenter implements ChatContract.Presenter {

    private static final String predictUrl = "http://101.200.240.107:8000/predict";

    private static final String historyUrl = "http://118.31.64.83:8080/message/history";

    //周期
    private static final int PERIOD = 30*1000;

    //准确率阀值
    private static final double THRESHOLD = 0.5;

    private ChatContract.View mChatView;

    private List<MsgUI> msgList;

    private List<RawMotion> mRawMotionList;

    private Context mContext;

    private String myId;
    private String myPortrait;
    private String receiverId;
    private String receiverPortrait;

    //定时器
    private Timer mTimer;
    private TimerTask mTimerTask;

    private List<String> resultIdList;


    public ChatPresenter(ChatContract.View chatView, Context context){
        mChatView = chatView;
        mChatView.setPresenter(this);
        mContext = context;
    }

    @Override
    public void start() {
        //初始化消息list
        msgList = new ArrayList<>();
//        mChatView.initUI(msgList);

        //初始化结果list
        resultIdList = new ArrayList<>();

        mTimer = new Timer();

        mTimerTask = new TimerTask() {
            @Override
            public void run() {
//                Log.d("loop", "loop");
                if(mChatView.getRawMotionList().size() > 0){
                    //获取motionEvent并移除点击事件
                    mRawMotionList = removeClickEvent(mChatView.getRawMotionList());
                    //没有滑动或者只滑动一次不进行判断
                    if(mRawMotionList.size() > 1){
                        //预测返回结果
                        String resultStr = NetUtils.postJson(mRawMotionList, predictUrl);
                        if(resultStr != null && resultStr.subSequence(0,1).equals("b") ){
                            resultIdList = Arrays.asList(resultStr.split(","));
                            for(String s:resultIdList){
                                Log.d("id", s);
                            }
//                            Log.d("benren?", String.valueOf(mChatView.classify(resultIdList)));
                            //判断是否是本人，不是本人就加密消息
                            if(!mChatView.classify(resultIdList)){
                                mChatView.encryptMsg();
                            }
                        }else{
                            Looper.prepare();
                            Toast.makeText(mContext, "服务器返回错误", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }

                    //清空存放滑动数据的list
                    mRawMotionList.clear();
                    mChatView.clearMotionList();
                }

            }
        };
        //延迟60s，周期60s
        mTimer.schedule(mTimerTask, 0, PERIOD);

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
                            String myId, String oppositeId, String publicKey, int type) {

        if(type == MsgUI.UNDECRYPTED){
            //加密模式
            String encryptedMsg = "";
            //公钥加密
            try {
                encryptedMsg = RsaEncryptUtil.encrypt(content, publicKey);
                Log.i("发送的加密消息", encryptedMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(!TextUtils.isEmpty(encryptedMsg)) {
                //发送加密消息
                WebSocketUtils.sendMessgae(myId, oppositeId, encryptedMsg, "", MsgUI.UNDECRYPTED);
            }
        }else{
            //发送未加密消息
            WebSocketUtils.sendMessgae(myId, oppositeId, content, "", MsgUI.DECRYPTED);
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
    public void receiveMessage(String content, String oppositePortrait, String mOppositeId){
        WebSocketModel model =  WebSocketUtils.getMessage(content);
        int action = model.getAction();

        if(action == 7){
            //强制下线
            mChatView.conflict();
            return;
        }

        Msg msg = model.getMessage();
        String myId = msg.getReceiveUserId();
        String oppositeId = msg.getSendUserId();
        String msgContent = msg.getMsg();
        String msgId = msg.getMsgId();

        if(action == 2){
            String decryptedMsg =  "";
            try {
                Log.i("收到的加密消息", msgContent);
                //直接解密
//            decryptedMsg = RsaEncryptUtil.decrypt(msgContent, RsaEncryptUtil.getPrivateKey());
                decryptedMsg = RsaEncryptUtil.decrypt(msgContent, (String) SpUtils.getData(mContext, Mapper.SP_PRIVATE_KEY, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(!TextUtils.isEmpty(decryptedMsg)){
                MsgUI msgUI = new MsgUI(decryptedMsg, oppositePortrait, MsgUI.TYPE_RECEIVED, MsgUI.DECRYPTED);
                Log.d("receive", content);

                if(oppositeId.equals(mOppositeId)){
                    mChatView.refreshUI(msgUI);
                    //直接签收消息
                    WebSocketUtils.sign(msgId);
                }

            }
        }else if(action == 6){
            MsgUI msgUI = new MsgUI(msgContent, oppositePortrait, MsgUI.TYPE_RECEIVED, MsgUI.DECRYPTED);
            Log.d("receive", content);

            if(oppositeId.equals(mOppositeId)){
                mChatView.refreshUI(msgUI);
                //直接签收消息
                WebSocketUtils.sign(msgId);
            }
        }
    }


    /**
     * 更新session
     * @param context
     * @param oppositeId
     * @param lastMsg
     * @param action "decrypt"/"send"/"encrypt"
     */
    @Override
    public void updateSession(Context context, String oppositeId, String lastMsg, String publicKey, String action) {
        Intent intent = new Intent();
        intent.putExtra("ID", oppositeId);
        intent.putExtra("LAST_MSG", lastMsg);
        intent.putExtra("PUBLIC_KEY", publicKey);
        intent.putExtra("ACTION", action);
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

        if(resultIdList.size() == 0){
            return true;
        }

        for(int i = 0; i< size; i++){
            if(resultIdList.get(i).equals(myId)){
                rightCount ++;
            }
        }
        double precision = (double)rightCount/ (double)size;

        Log.d("precision", String.valueOf(precision));
        return precision >= THRESHOLD;
    }

    @Override
    public void getHistoryMessage(final String myId, final String receiverId){
        Factory.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String result = NetUtils.postKeyValue("myId", myId, "receiverId", receiverId, historyUrl);
                if (result != null) {
                    parseHistoryResult(result);
                } else {
                    mChatView.showError(R.string.err_service);
                }
            }
        });
    }

    @Override
    public void parseHistoryResult(String result){
        HistoryModel historyModel = Factory.getInstance()
                .getGson().fromJson(result, HistoryModel.class);
        String msg = historyModel.getMsg();
        if(msg.equals("success")){
            List<History> historyList = historyModel.getData();
            for(History history:historyList){
                MsgUI msgUI = mChatView.switchMsg(history);
                if(msgUI != null) {
                    msgList.add(msgUI);
                }
            }
            mChatView.initUI(msgList);
        }
    }

}
