package com.example.instantMessaging.Fragments.message;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.RSA.RsaEncryptUtil;
import com.example.common.app.Fragment;
import com.example.factory.model.MsgUI;
import com.example.factory.model.RawMotion;
import com.example.factory.model.api.History;
import com.example.factory.model.api.webSocket.Msg;
import com.example.factory.model.api.webSocket.WebSocketModel;
import com.example.factory.presenter.Chat.ChatContract;
import com.example.factory.utils.SpUtils;
import com.example.factory.utils.webSocket.WebSocketUtils;
import com.example.instantMessaging.Activities.BehaviorActivity;
import com.example.instantMessaging.Activities.MainActivity;
import com.example.instantMessaging.Activities.MessageActivity;
import com.example.instantMessaging.Fragments.message.adapter.ChatRecyclerAdapter;
import com.example.instantMessaging.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.*;

/**
 * @author brsmsg
 * @time 2020/3/21
 */
public class ChatFragment extends Fragment implements ChatContract.View {

    private ChatContract.Presenter mPresenter;

    //从MainActivity传来的name,portrait,id
    private String mContactName;
    private String mPortrait;
    private String mOppositeId;
    //自己的id, 头像
    private String myId;
    private String myPortrait;
    //公钥私钥
    private String mPublicKey;
    private String mPrivateKey;

    //加密解密状态
    private static int STATUS;
    //发送消息类型
    private static int TYPE;
//    private static final int ENCRYPTED = 0;
//    private static final int DECRYPTED = 1;

    private ChatRecyclerAdapter mChatAdapter;

    //加密消息广播
    private MyReceiver mReceiver;
    private PredictReceiver mPredictReceiver;
    //未加密消息广播
    private UnEncryptedReceiver mUnEncryptedReceiver;

    private List<RawMotion> mRawMotionList;



    @BindView(R.id.txt_contact)
    TextView contactName;

    @BindView(R.id.edit_msg)
    EditText mContent;

    @BindView(R.id.recycler_msg)
    RecyclerView mRecycler;

    @BindView(R.id.chat_background)
    View chatBackground;


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mContactName = bundle.getString(MessageActivity.KEY_USERNAME);
        mPortrait = bundle.getString(MessageActivity.KEY_PORTRAIT_URL);
        mOppositeId = bundle.getString(MessageActivity.KEY_OPPOSITE_ID);
        myId = bundle.getString(MainActivity.MY_ID);
        myPortrait = bundle.getString(MainActivity.MY_PORTRAIT);
        mPublicKey = bundle.getString(MainActivity.PUBLIC_KEY);
        mPrivateKey = bundle.getString(MainActivity.PRIVATE_KEY);

    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        contactName.setText(mContactName);

    }

    @Override
    protected void initData() {
        super.initData();
        //状态初始化为解密
        STATUS = MsgUI.DECRYPTED;
        //默认发送不加密消息
        TYPE = MsgUI.DECRYPTED;

        mRawMotionList = new ArrayList<>();
        //注册监听
        ((MessageActivity) Objects.requireNonNull(getActivity())).registerTouchListener(mTouchListener);

        //动态注册广播
        mReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        //设置广播类型
        intentFilter.addAction("com.example.broadcast.MESSAGE");
        Objects.requireNonNull(getActivity()).registerReceiver(mReceiver, intentFilter);

        mPredictReceiver = new PredictReceiver();
        IntentFilter predictFilter = new IntentFilter();
        predictFilter.addAction("com.example.broadcast.PREDICT");
        getActivity().registerReceiver(mPredictReceiver, predictFilter);

        //注册接受未加密消息广播
        mUnEncryptedReceiver = new UnEncryptedReceiver();
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("com.example.broadcast.UNENCRYPTED_MESSAGE");
        getActivity().registerReceiver(mUnEncryptedReceiver, intentFilter2);

        mPresenter.start();
        //拉取聊天记录
        mPresenter.getHistoryMessage(myId, mOppositeId);
    }

    @Override
    public void setPresenter(ChatContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @SuppressLint("ShowToast")
    @Override
    public void showError(int string) {
        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), getString(string), Toast.LENGTH_SHORT).show());

    }

    /**
     * 初始化recyclerView
     * @param msgList List<MsgUI>
     */
    @Override
    public void initUI(List<MsgUI> msgList) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            mChatAdapter = new ChatRecyclerAdapter(getContext(), msgList);

            mRecycler.setAdapter(mChatAdapter);
            //滚动到底部
            if(mChatAdapter.getItemCount() > 0) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) mRecycler.getLayoutManager();
                layoutManager.scrollToPositionWithOffset(mChatAdapter.getItemCount() - 1, 0);
            }
        });

    }

    /**
     * 界面添加一条聊天消息
     * @param item MsgUI
     */
    @Override
    public void refreshUI(MsgUI item) {
        mChatAdapter.add(item);
        if(item.getType() == MsgUI.TYPE_SEND){
            mContent.setText("");
        }
        //滚动到底部
        Log.d("size", String.valueOf(mChatAdapter.getItemCount() - 1));
        mRecycler.smoothScrollToPosition(mChatAdapter.getItemCount()-1);
    }

    /**
     * 判断是否是用户本人
     * @param resultIdList 返回的idList
     * @return boolean
     */
    @Override
    public boolean classify(List<String> resultIdList){
        return mPresenter.getPrecision(myId, resultIdList);
    }

    /**
     * 对消息加密
     */
    @Override
    public void encryptMsg() {
        //状态设置为加密
        STATUS = MsgUI.UNDECRYPTED;

        if(getActivity()!=null) {
            //刷新会话
            mPresenter.updateSession(getActivity(), mOppositeId, "行为异常，请解锁后查看消息", mPublicKey, "encrypt");
            //销毁监听
            ((MessageActivity) getActivity()).unregisterTouchListener();
            //点击list清空
            clearMotionList();

            getActivity().runOnUiThread(() -> {
                //更新在ui线程执行
                mChatAdapter.encryptRefresh();
                Toast.makeText(getActivity(), "验证失败，消息已加密，请手动验证解密", Toast.LENGTH_SHORT).show();
                //锁定消息发送
                mContent.setEnabled(false);
            });

        }
    }

    @Override
    public MsgUI switchMsg(History historyMsg){
        MsgUI msgUI = new MsgUI();
        if(historyMsg == null){
            return null;
        }

        msgUI.setDecrypted(STATUS);
        try {
            msgUI.setContent(historyMsg.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(historyMsg.getSendUserId().equals(myId)){
            msgUI.setType(MsgUI.TYPE_SEND);
            msgUI.setPortrait(myPortrait);
        }else{
            msgUI.setType(MsgUI.TYPE_RECEIVED);
            msgUI.setPortrait(mPortrait);
        }

        return msgUI;
    }


    /**
     * 返回motionList
     * @return rawMotionList
     */
    @Override
    public List<RawMotion> getRawMotionList(){
        return mRawMotionList;
    }

    /**
     * 请除rawMotionList
     */
    @Override
    public void clearMotionList(){
        mRawMotionList.clear();
    }

    /**
     * 点击返回按钮
     */
    @OnClick(R.id.img_return)
    void goBack(){
        getActivity().finish();
    }

    /**
     * 点击发送
     */
    @OnClick(R.id.btn_send)
    void send(){
        String msg = mContent.getText().toString();

        if(!TextUtils.isEmpty(msg)) {
            mPresenter.sendMessage(msg, myPortrait, myId, mOppositeId, mPublicKey, TYPE);
            //更新会话
            mPresenter.updateSession(getActivity(), mOppositeId, msg, mPublicKey, "send");
        }else{
            showError(R.string.err_message_empty);
        }
    }


    @OnClick(R.id.btn_decrypt)
    void evaluate(){
        BehaviorActivity.show(getActivity(), myId, "predict");
    }



    /**
     * 点击解密img
     */
    @OnClick(R.id.img_decrypt)
    void decrypt() {
        if(TYPE == MsgUI.DECRYPTED){
            //界面不加密情况下
            TYPE = MsgUI.UNDECRYPTED;
            Toast.makeText(getActivity(), "您已进入加密模式, 所有信息已加密且仅当次会话可见", Toast.LENGTH_SHORT).show();
            chatBackground.setBackgroundColor(getResources().getColor(R.color.black));
        }else{
            TYPE = MsgUI.DECRYPTED;
            Toast.makeText(getActivity(), "加密模式已关闭", Toast.LENGTH_SHORT).show();
            chatBackground.setBackgroundColor(getResources().getColor(R.color.white));
        }


//        BehaviorActivity.show(getActivity(), myId, "predict");


//        弹出输入密码对话框
//        final EditText editText = new EditText(getActivity());
//        AlertDialog.Builder inputDialog = new AlertDialog.Builder(getActivity());
//        inputDialog.setTitle("请输入密码以解锁").setView(editText);
//        inputDialog.setPositiveButton("确定",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        SharedPreferences sp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
//
//                        String pwd = sp.getString("PASSWORD", "");
//                        Log.d("password", pwd);
//                        if (editText.getText().toString().trim().equals(pwd)){
//                            STATUS = DECRYPTED
//                            mChatAdapter.decryptRefresh();
//                            mContent.setEnabled(true);
//                        }
//                    }
//                }).show();

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销广播
        Objects.requireNonNull(getActivity()).unregisterReceiver(mReceiver);
        getActivity().unregisterReceiver(mPredictReceiver);
        getActivity().unregisterReceiver(mUnEncryptedReceiver);
        //注销监听
        ((MessageActivity) getActivity()).unregisterTouchListener();

    }


    /**
     * 实现回调接口
     */
    private MessageActivity.TouchListener mTouchListener = new MessageActivity.TouchListener() {
        //重写onTouch
        @Override
        public void onTouch(MotionEvent event) {
            RawMotion rawMotion = new RawMotion(myId, event.getX(), event.getY(),
                    event.getPressure(), event.getSize(),
                    event.getAction(), event.getEventTime());

            mRawMotionList.add(rawMotion);
        }
    };



    /**
     * 广播接收器,接受消息广播
     */
    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getExtras().getString("MSG");
            mPresenter.receiveMessage(msg, mPortrait);
        }
    }



    class UnEncryptedReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
        }
    }



    /**
     * 接受验证结果
     */
    class PredictReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getExtras().getString("PREDICT").equals("success")){
                mChatAdapter.decryptRefresh();
                mContent.setEnabled(true);

                //重新注册监听
                ((MessageActivity)getActivity()).registerTouchListener(mTouchListener);

                //向messageFragment发送广播
                String lastMsg = mChatAdapter.getLastMsg();
                if (TextUtils.isEmpty(lastMsg)) {
                    Toast.makeText(getActivity(), "没有需要解密的消息", Toast.LENGTH_SHORT).show();
                } else {
                    mPresenter.updateSession(getActivity(), mOppositeId, lastMsg, mPublicKey, "decrypt");
                }
            }
        }

    }



}
