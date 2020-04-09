package com.example.instantMessaging.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.example.common.app.Activity;
import com.example.common.app.Fragment;
import com.example.factory.model.SessionUI;
import com.example.factory.model.User;
import com.example.factory.model.api.webSocket.WebSocketModel;
import com.example.factory.model.db.Contact;
import com.example.factory.presenter.Chat.ChatPresenter;
import com.example.factory.utils.webSocket.WebSocketUtils;
import com.example.instantMessaging.Fragments.message.ChatFragment;
import com.example.instantMessaging.R;

/**
 * @author brsmsg
 * @time 2020/3/21
 */
public class MessageActivity extends Activity {
    //聊天对象的用户名,头像，id以及各自的key
    public static final String KEY_USERNAME = "KEY_USERNAME";
    public static final String KEY_PORTRAIT_URL = "KEY_PORTRAIT_URL";
    public static final String KEY_OPPOSITE_ID = "KEY_OPPOSITE_ID";

    private String mUsername;
    private String mPortrait;
    private String mOppositeId;

    //自己的用户名，头像
    private String myId;
    private String myPortrait;

    private String mPublicKey;
    private String mPrivateKey;

    private ChatFragment mChatFragment;

    private ChatPresenter mChatPresenter;

    @Override
    protected int getContentLayotId() {
        return R.layout.activity_message;
    }

    /**
     * 从联系人fragment启动
     * @param context context
     * @param contact contact
     * @param myId myId
     * @param myPortrait myPortrait
     */
    public static void show(Context context, Contact contact, String myId, String myPortrait,
                            String publicKey, String privateKey){
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_USERNAME, contact.getUsername());
        intent.putExtra(KEY_PORTRAIT_URL, contact.getFaceImage());
        intent.putExtra(KEY_OPPOSITE_ID, contact.getId());
        intent.putExtra(MainActivity.MY_ID, myId);
        intent.putExtra(MainActivity.MY_PORTRAIT, myPortrait);
        intent.putExtra(MainActivity.PUBLIC_KEY, publicKey);
        intent.putExtra(MainActivity.PRIVATE_KEY, privateKey);

        context.startActivity(intent);
    }

    /**
     * 从session会话fragment启动
     * @param context context
     * @param session session
     * @param myId myId
     * @param myPortrait myPortrait
     */
    public static void show(Context context, SessionUI session, String myId, String myPortrait,
                            String publicKey, String privateKey){
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_USERNAME, session.getUsername());
        intent.putExtra(KEY_PORTRAIT_URL, session.getPortrait());
        intent.putExtra(KEY_OPPOSITE_ID, session.getId());
        intent.putExtra(MainActivity.MY_ID, myId);
        intent.putExtra(MainActivity.MY_PORTRAIT, myPortrait);
        intent.putExtra(MainActivity.PUBLIC_KEY, publicKey);
        intent.putExtra(MainActivity.PRIVATE_KEY, privateKey);

        context.startActivity(intent);
    }


    /**
     * 初始化参数
     * @param bundle 参数Bundle
     * @return true/flase
     */
    @Override
    protected boolean initArgs(Bundle bundle) {
        mUsername = bundle.getString(KEY_USERNAME);
        mPortrait = bundle.getString(KEY_PORTRAIT_URL);
        mOppositeId = bundle.getString(KEY_OPPOSITE_ID);
        myId = bundle.getString(MainActivity.MY_ID);
        myPortrait = bundle.getString(MainActivity.MY_PORTRAIT);
        mPublicKey = bundle.getString(MainActivity.PUBLIC_KEY);
        mPrivateKey = bundle.getString(MainActivity.PRIVATE_KEY);

        return !(TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(mPortrait))
                ||TextUtils.isEmpty(mOppositeId);
    }


    @Override
    protected void initWidget() {
        super.initWidget();
        mChatFragment = new ChatFragment();

        // 把数据传给fragment
        Bundle bundle = new Bundle();
        bundle.putString(KEY_USERNAME, mUsername);
        bundle.putString(KEY_PORTRAIT_URL, mPortrait);
        bundle.putString(KEY_OPPOSITE_ID, mOppositeId);
        bundle.putString(MainActivity.MY_ID, myId);
        bundle.putString(MainActivity.MY_PORTRAIT, myPortrait);
        bundle.putString(MainActivity.PUBLIC_KEY, mPublicKey);
        bundle.putString(MainActivity.PRIVATE_KEY, mPrivateKey);

        mChatFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.layout_container_message, mChatFragment).commit();

        mChatPresenter = new ChatPresenter(mChatFragment);

    }


}
