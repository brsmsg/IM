package com.example.instantMessaging.Activities;

import android.content.Context;
import android.content.Intent;

import com.example.common.RSA.RsaEncryptUtil;
import com.example.common.app.Activity;
import com.example.factory.Factory;
import com.example.factory.model.RawMotion;
import com.example.factory.model.User;
import com.example.factory.model.api.account.LoginModel;
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

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * @author brsmsg
 * @time 2020/3/15
 */
public class BehaviorActivity extends Activity {
    private static String mOrientation;

    private String trainUrl = "http://101.200.240.107:8000/DataProcess";

    private String trainLeftUrl = "http://101.200.240.107:8000/leftDataProcess";
    private String trainRightUrl = "http://101.200.240.107:8000/rightDataProcess";
    private String trainUpUrl = "http://101.200.240.107:8000/upDataProcess";
    private String trainDownUrl = "http://101.200.240.107:8000/downDataProcess";

    private String predictUrl = "http://101.200.240.107:8000/predict";
    private final String loginUrl = "http://118.31.64.83:8080/account/login";

    private List<RawMotion> mRawMotionList = new ArrayList<>();
    private List<RawMotion> mDownRawMotionList = new ArrayList<>();
    private List<RawMotion> mLeftRawMotionList = new ArrayList<>();
    private List<RawMotion> mRightRawMotionList = new ArrayList<>();

    private Context mContext;

    public static final String KEY_ID = "ID";
    public static final String KEY_STATUS = "STATUS";
    public static final String KEY_USERNAME = "USER_NAME";
    public static final String KEY_PASSWORD = "PASSWORD";

    //滑动次数
    private int mTimes;
    //向各个方向的滑动次数
    private int mLeft;
    private int mRight;
    private int mUp;
    private int mDown;

    private String mStatus;
    //尝试机会
    private int mChances;
    private String mUsername;
    private String mPassword;

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

    @BindView(R.id.txt_behavior)
    TextView mBehaviorId;

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



    public static void show(Context context, String id, String username, String password, String status){
        Intent intent = new Intent(context, BehaviorActivity.class);
        intent.putExtra(KEY_ID, id);
        intent.putExtra(KEY_STATUS, status);
        intent.putExtra(KEY_USERNAME, username);
        intent.putExtra(KEY_PASSWORD, password);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        id = bundle.getString(KEY_ID);
        mStatus = bundle.getString(KEY_STATUS);
        mUsername = bundle.getString(KEY_USERNAME);
        mPassword = bundle.getString(KEY_PASSWORD);
        mContext = this;
        mChances = 3;
        mTimes = 40;
        mLeft = mRight = mUp = mDown = 10;
        //最先传向上滑动
        mOrientation = "up";
        return !TextUtils.isEmpty(id);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mBehaviorId.setText(id + "请向上滑动10次");
        if(mStatus.equals("register")){
            mUpload.setVisibility(View.VISIBLE);
            mPredict.setVisibility(View.INVISIBLE);
            //上传按钮不能使用
            mUpload.setEnabled(false);
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
                event.getPressure(), event. getSize(),
                event.getAction(), event.getEventTime());
//        Log.d("ActionDown", rawMotion.toString());
        mRawMotionList.add(rawMotion);

        if (event.getAction() == 1) {
            mTimes--;
            if (mTimes > 30) {
                Toast.makeText(this, "还需向上滑动" + mTimes % 10 + "次", Toast.LENGTH_SHORT).show();
            } else if (mTimes == 30) {

                Toast.makeText(this, "请点击上传按钮上传数据后，再向下滑动10次", Toast.LENGTH_SHORT).show();
                mBehaviorId.setText(id + "请向下滑动10次");
                for(RawMotion rawMotion1:mRawMotionList){
                    Log.d("upRawMotion", rawMotion1.toString());
                }
//                mOrientation = "down";
                mUpload.setEnabled(true);
            } else if (mTimes > 20) {
                Toast.makeText(this, "还需向下滑动" + mTimes % 10 + "次", Toast.LENGTH_SHORT).show();
            } else if (mTimes == 20) {

                Toast.makeText(this, "请点击上传按钮上传数据后，再向左滑动10次", Toast.LENGTH_SHORT).show();
                mBehaviorId.setText(id + "请向左滑动10次");
                //                    Log.d("downRawMotion", rawMotion1.toString());
                //用新list储存
                mDownRawMotionList.addAll(mRawMotionList);
                mOrientation = "down";
                mUpload.setEnabled(true);
            } else if (mTimes > 10) {
                Toast.makeText(this, "还需向左滑动" + mTimes % 10 + "次", Toast.LENGTH_SHORT).show();
            } else if (mTimes == 10) {

                Toast.makeText(this, "请点击上传按钮上传数据后，再向右滑动10次", Toast.LENGTH_SHORT).show();
                mBehaviorId.setText(id + "请向右滑动10次");
                //新list储存
                //                    Log.d("downRawMotion", rawMotion1.toString());
                mLeftRawMotionList.addAll(mRawMotionList);
                mOrientation = "left";
                mUpload.setEnabled(true);

            } else if (mTimes > 0) {
                Toast.makeText(this, "还需向右滑动" + mTimes % 10 + "次", Toast.LENGTH_SHORT).show();
            } else if (mTimes == 0) {
                //训练向右的数据
                //新list储存
                //                    Log.d("downRawMotion", rawMotion1.toString());
                mRightRawMotionList.addAll(mRawMotionList);
                mOrientation ="right";
                mUpload.setEnabled(true);
                Toast.makeText(this, "数据收集完成，请点击上传", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onTouchEvent(event);
    }

    @OnClick(R.id.btn_upload)
    public void submit(){
        for(RawMotion r1:mRawMotionList){
            Log.d("submit", r1.toString());
        }
        switch (mOrientation){
            case "up":
                Log.d("up", "up");
                Factory.getInstance().getThreadPool().execute(() -> {
                    for(RawMotion r:mRawMotionList){
                        Log.d("thread", r.toString());
                    }
                    NetUtils.postJson(mRawMotionList, trainUpUrl);
                    Log.d("size", String.valueOf(mRawMotionList.size()));
                    mRawMotionList.clear();
                });
                mUpload.setEnabled(false);
                break;
            case "down":
                Log.d("up", "down");
                Factory.getInstance().getThreadPool().execute(() -> {
                    NetUtils.postJson(mDownRawMotionList, trainDownUrl);
//                    Log.d("size", String.valueOf(mRawMotionList.size()));
                    mRawMotionList.clear();
                });
                mUpload.setEnabled(false);
                break;
            case "left":
                Log.d("up", "left");
                Factory.getInstance().getThreadPool().execute(() -> {
                    NetUtils.postJson(mLeftRawMotionList, trainLeftUrl);
//                    Log.d("size", String.valueOf(mRawMotionList.size()));
                    mRawMotionList.clear();
                });

                mUpload.setEnabled(false);
                break;
            case "right":
                Log.d("up", "right");
                Factory.getInstance().getThreadPool().execute(() -> {
                    NetUtils.postJson(mRightRawMotionList, trainRightUrl);
//                    Log.d("size", String.valueOf(mRawMotionList.size()));
                    mRawMotionList.clear();
                });

                mUpload.setEnabled(false);
                //发送广播登录
                Intent intent = new Intent();
                intent.putExtra("USERNAME", mUsername);
                intent.putExtra("PASSWORD", mPassword);
                intent.setAction("com.example.broadcast.LOGIN");
                mContext.sendBroadcast(intent);
                finish();
        }

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