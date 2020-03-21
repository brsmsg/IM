package com.example.instantMessaging.Activities;

import android.content.Context;
import android.content.Intent;

import com.example.common.app.Activity;
import com.example.factory.model.User;
import com.example.instantMessaging.R;

/**
 * @author brsmsg
 * @time 2020/3/21
 */
public class MessageActivity extends Activity {

    @Override
    protected int getContentLayotId() {
        return R.layout.activity_message;
    }

    public void show(Context context, User user){
        context.startActivity(new Intent(context, MessageActivity.class));

    }
}
