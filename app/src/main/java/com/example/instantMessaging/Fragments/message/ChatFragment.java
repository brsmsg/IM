package com.example.instantMessaging.Fragments.message;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.RSA.RsaEncryptUtil;
import com.example.common.app.Fragment;
import com.example.factory.Factory;
import com.example.factory.model.MsgUI;
import com.example.factory.model.api.webSocket.Msg;
import com.example.factory.model.api.webSocket.WebSocketModel;
import com.example.factory.presenter.Chat.ChatContract;
import com.example.factory.utils.webSocket.WebSocketUtils;
import com.example.instantMessaging.Activities.MainActivity;
import com.example.instantMessaging.Activities.MessageActivity;
import com.example.instantMessaging.Fragments.message.adapter.ChatRecyclerAdapter;
import com.example.instantMessaging.R;

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

    private ChatRecyclerAdapter mChatAdapter;

    private MyReceiver mReceiver;

    @BindView(R.id.txt_contact)
    TextView contactName;

    @BindView(R.id.edit_msg)
    EditText mContent;

    @BindView(R.id.recycler_msg)
    RecyclerView mRecycler;

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
        //动态注册广播
        mReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        //设置广播类型
        intentFilter.addAction("com.example.broadcast.MESSAGE");
        Objects.requireNonNull(getActivity()).registerReceiver(mReceiver, intentFilter);


        mPresenter.start();
    }

    @Override
    public void setPresenter(ChatContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @SuppressLint("ShowToast")
    @Override
    public void showError(int string) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), getString(string), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void initUI(List<MsgUI> msgList) {
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mChatAdapter = new ChatRecyclerAdapter(getContext(), msgList);

        mRecycler.setAdapter(mChatAdapter);
    }

    @Override
    public void refreshUI(MsgUI item) {
        mChatAdapter.add(item);
        if(item.getType() == MsgUI.TYPE_SEND){
            mContent.setText("");
        }
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
            mPresenter.sendMessage(msg, myPortrait, myId, mOppositeId, mPublicKey);
        }else{
            showError(R.string.err_message_empty);
        }
    }

    /**
     * 点击解密img
     */
    @OnClick(R.id.img_decrypt)
    void decrypt(){
        mChatAdapter.refresh(mPrivateKey);
        //向messageFragment发送广播
        mPresenter.updateSession(getActivity(), mOppositeId, mChatAdapter.getLastMsg());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销广播
        Objects.requireNonNull(getActivity()).unregisterReceiver(mReceiver);
    }

    /**
     * 广播接收器
     */
    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getExtras().getString("MSG");

            mPresenter.receiveMessage(msg, mPortrait);
        }
    }

}
