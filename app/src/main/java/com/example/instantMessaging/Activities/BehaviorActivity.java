package com.example.instantMessaging.Activities;

import android.content.Context;
import android.content.Intent;

import com.example.common.app.Activity;
import com.example.factory.Factory;
import com.example.factory.model.RawMotion;
import com.example.factory.utils.NetUtils;
import com.example.instantMessaging.R;

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


    private List<RawMotion> mRawMorionList = new ArrayList<>();

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

    public static void show(Context context){
        context.startActivity(new Intent(context, BehaviorActivity.class));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mX.setText(String.valueOf(event.getX()));
        mY.setText(String.valueOf(event.getY()));
        mPressure.setText(String.valueOf(event.getPressure()));
        mAction.setText(String.valueOf(event.getAction()));
        mArea.setText(String.valueOf(event.getSize()));


        RawMotion rawMotion = new RawMotion("brsmsg", event.getX(), event.getY(),
                event.getPressure(), event.getSize(),
                event.getAction(), event.getEventTime());
        Log.d("ActionDown", rawMotion.toString());



        mRawMorionList.add(rawMotion);
        Log.d("rawsize: ", String.valueOf(mRawMorionList.size()));

        return super.onTouchEvent(event);
    }

    @OnClick(R.id.btn_behavior)
    public void submit(){
        Factory.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                NetUtils.postJson(mRawMorionList, trainUrl);
                Log.d("size", String.valueOf(mRawMorionList.size()));
            }
        });

    }

}