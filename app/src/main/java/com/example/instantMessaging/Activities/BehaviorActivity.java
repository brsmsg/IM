package com.example.instantMessaging.Activities;

import android.content.Context;
import android.content.Intent;

import com.example.common.app.Activity;
import com.example.factory.Factory;
import com.example.factory.model.RawMotion;
import com.example.factory.utils.NetUtils;
import com.example.instantMessaging.R;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author brsmsg
 * @time 2020/3/15
 */
public class BehaviorActivity extends Activity {

    private String trainUrl = "http://101.200.240.107:8000/dataProcess";

    private List<RawMotion> mRawMotionList = new ArrayList<>();

    public static final String KEY_ID = "ID";

    private static String id;
    @BindView(R.id.x)
    TextView mX;

    @BindView(R.id.y)
    TextView mY;

    @BindView(R.id.pressure)
    TextView mPressure;

    @BindView(R.id.area)
    TextView mArea;

    @BindView(R.id.action)
    TextView mAction;

    @BindView(R.id.btn_behavior)
    Button mButton;

    @Override
    protected int getContentLayotId() {
        return R.layout.activity_behavior;
    }

    public static void show(Context context, String id){
        Intent intent = new Intent(context, BehaviorActivity.class);
        intent.putExtra(KEY_ID, id);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        id = bundle.getString(KEY_ID);
        return !TextUtils.isEmpty(id);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mX.setText(String.valueOf(event.getX()));
        mY.setText(String.valueOf(event.getY()));
        mPressure.setText(String.valueOf(event.getPressure()));
        mAction.setText(String.valueOf(event.getAction()));
        mArea.setText(String.valueOf(event.getSize()));


        RawMotion rawMotion = new RawMotion(id, event.getX(), event.getY(),
                event.getPressure(), event.getSize(),
                event.getAction(), event.getEventTime());
        Log.d("ActionDown", rawMotion.toString());



        mRawMotionList.add(rawMotion);
        Log.d("rawSize: ", String.valueOf(mRawMotionList.size()));

        return super.onTouchEvent(event);
    }

    @OnClick(R.id.btn_behavior)
    public void submit(){
        Factory.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                NetUtils.postJson(mRawMotionList, trainUrl);
                Log.d("size", String.valueOf(mRawMotionList.size()));
            }
        });

    }

}