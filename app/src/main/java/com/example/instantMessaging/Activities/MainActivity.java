package com.example.instantMessaging.Activities;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.common.app.Activity;
import com.example.instantMessaging.R;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @Override
    protected int getContentLayotId() {
        return R.layout.activity_main;
    }

    /**
     * 显示入口
     */
    public static void show(Context context){
        context.startActivity(new Intent(context, MainActivity.class));
    }


}
