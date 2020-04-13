package com.example.instantMessaging.Activities;

import android.content.Context;
import android.content.Intent;

import com.example.common.app.Activity;
import com.example.factory.Factory;
import com.example.factory.model.RawMotion;
import com.example.factory.utils.NetUtils;
import com.example.instantMessaging.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

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
    private String predictUrl = "http://101.200.240.107:8000/predict";

    private List<RawMotion> mRawMotionList = new ArrayList<>();

    private Context mContext;

    public static final String KEY_ID = "ID";
    public static final String KEY_STATUS = "STATUS";

    private String mStatus;
    //尝试机会
    private int mChances;

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

    @BindView(R.id.btn_upload)
    Button mUpload;

    @BindView(R.id.btn_predict)
    Button mPredict;

    @Override
    protected int getContentLayotId() {
        return R.layout.activity_behavior;
    }

    public static void show(Context context, String id, String status){
        Intent intent = new Intent(context, BehaviorActivity.class);
        intent.putExtra(KEY_ID, id);
        intent.putExtra(KEY_STATUS, status);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        id = bundle.getString(KEY_ID);
        mStatus = bundle.getString(KEY_STATUS);
        mContext = this;
        mChances = 3;
        return !TextUtils.isEmpty(id);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        if(mStatus.equals("register")){
            mUpload.setVisibility(View.VISIBLE);
            mPredict.setVisibility(View.INVISIBLE);
        }else{
            mUpload.setVisibility(View.INVISIBLE);
            mPredict.setVisibility(View.VISIBLE);
        }
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

    @OnClick(R.id.btn_upload)
    public void submit(){
        Factory.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                NetUtils.postJson(mRawMotionList, trainUrl);
                Log.d("size", String.valueOf(mRawMotionList.size()));
            }
        });
        finish();
    }

    @OnClick(R.id.btn_predict)
    void predict(){
        Factory.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String result = NetUtils.postJson(mRawMotionList, predictUrl);
                mRawMotionList.clear();
                Log.d("myId", id);
                Log.d("predict", result);
                Looper.prepare();
                if(result.equals(id)){
                    Toast.makeText(getApplication(), "验证成功", Toast.LENGTH_SHORT).show();

                    //发送广播通知chatFragment更新
                    Intent intent = new Intent();
                    intent.putExtra("PREDICT", "success");
                    intent.setAction("com.example.broadcast.PREDICT");
                    mContext.sendBroadcast(intent);
                    finish();
                }else{
                    mChances -= 1;
                    if(mChances == 0){
                        finish();
                    }
                    Toast.makeText(getApplication(), "验证失败，还有"+String.valueOf(mChances) + "机会", Toast.LENGTH_SHORT).show();
                }
                Looper.loop();
            }
        });


    }

}