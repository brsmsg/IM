package com.example.instantMessaging.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.fragment.app.FragmentManager;

import com.example.common.app.Activity;
import com.example.common.app.Fragment;
import com.example.factory.model.User;
import com.example.instantMessaging.Fragments.message.ChatFragment;
import com.example.instantMessaging.R;

/**
 * @author brsmsg
 * @time 2020/3/21
 */
public class MessageActivity extends Activity {
    //用户名
    public static final String KEY_USERNAME = "KEY_USERNAME";
    //头像url
    public static final String KEY_PORTRAIT_URL = "KEY_PORTRAIT_URL";

    private ChatFragment mChatFragment;

    private String mUsername;

    private String mPortrait;

    @Override
    protected int getContentLayotId() {
        return R.layout.activity_message;
    }


    /**
     * 展示聊天界面
     * @param context
     * @param user
     */
    public static void show(Context context, User user){
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_USERNAME, user.getUsername());
        intent.putExtra(KEY_PORTRAIT_URL, user.getFaceImage());
        context.startActivity(intent);
    }

    /**
     *
     * @param bundle 参数Bundle
     * @return true/flase
     */
    @Override
    protected boolean initArgs(Bundle bundle) {
        mUsername = bundle.getString(KEY_USERNAME);
        mPortrait = bundle.getString(KEY_PORTRAIT_URL);
        return !(TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(mPortrait));
    }


    @Override
    protected void initWidget() {
        super.initWidget();
        mChatFragment = new ChatFragment();

        // 把数据传给fragment
        Bundle bundle = new Bundle();
        bundle.putString(KEY_USERNAME, mUsername);
        bundle.putString(KEY_PORTRAIT_URL, mPortrait);
        mChatFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.layout_container_message, mChatFragment).commit();

    }
}
