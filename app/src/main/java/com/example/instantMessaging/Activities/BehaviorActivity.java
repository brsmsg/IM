package com.example.instantMessaging.Activities;

import android.content.Context;
import android.content.Intent;

import com.example.common.app.Activity;
import com.example.factory.model.Feature;
import com.example.factory.model.RawMotion;
import com.example.instantMessaging.R;

import android.mtp.MtpEvent;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author brsmsg
 * @time 2020/3/15
 */
public class BehaviorActivity extends Activity {

    private int mStrokeCount = 0;

    private List<RawMotion> mRawMorionList = new ArrayList<>();

    private List<RawMotion> mTempList = new ArrayList<>();

    private List<Feature> mFeatureList = new ArrayList<>();

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

//        switch(event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                RawMotion rawMotion = new RawMotion(event.getX(), event.getY(),
//                        event.getPressure(), event.getSize(),
//                            event.getAction(), event.getEventTime());
//                Log.d("ActionDown", rawMotion.toString());
//                //加入List中
//                mRawMorionList.add(rawMotion);
//                break;
//
//
//            case MotionEvent.ACTION_UP:
//
//
//        }
        RawMotion rawMotion = new RawMotion(event.getX(), event.getY(),
                event.getPressure(), event.getSize(),
                event.getAction(), event.getEventTime());
        Log.d("ActionDown", rawMotion.toString());

//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                mRawMorionList.add(rawMotion);
//            case MotionEvent.ACTION_MOVE:
//                mRawMorionList.add(rawMotion);
//            case MotionEvent.ACTION_UP:
//                mRawMorionList.add(rawMotion);
//        }

        mRawMorionList.add(rawMotion);
        Log.d("rawsize: ", String.valueOf(mRawMorionList.size()));

        return super.onTouchEvent(event);
    }

    @OnClick(R.id.btn_behavior)
    public void submit(){
        for(RawMotion rawMotion:mRawMorionList){
            switch (rawMotion.getAction()){
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    mTempList.add(rawMotion);
                    break;
                case MotionEvent.ACTION_UP:
                    mTempList.add(rawMotion);
                    mStrokeCount ++;
                    getFeatures();
            }
        }
        Log.d("size", String.valueOf(mRawMorionList.size()));
    }

    public void getFeatures(){
        //创建feature对象
        Feature feature = new Feature();
        //存放pairwise距离list
        List<Double> distanceList = new ArrayList<>();
        //存放pairwise速度的list
        List<Double> speedList = new ArrayList<>();
        //存放pairwise时间的list
        List<Long> timeList = new ArrayList<>();
        //存放pairwise加速度的list
        List<Double> accList = new ArrayList<>();

        int size = mTempList.size();
        //起始xy坐标
        feature.setStartX(mTempList.get(0).getxCoordinate());
        feature.setStartY(mTempList.get(0).getyCoordinate());
        //结束xy坐标
        feature.setEndX(mTempList.get(size-1).getxCoordinate());
        feature.setEndY(mTempList.get(size-1).getyCoordinate());
        //滑动持续时间
        feature.setDuration(mTempList.get(size-1).getTime() - mTempList.get(0).getTime());
        //直接距离
        feature.setDirectDistance(calDistance(size-1, 0));
        //pairwise距离
        for(int i = 0; i < size - 1; i++){
            distanceList.add(calDistance(i+1, i));
        }
        //pairwise时间
        for(int i = 0; i < size-1; i++){
            timeList.add(mTempList.get(i+1).getTime() - mTempList.get(i).getTime());
        }
        //pairwise速度
        for(int i = 0; i < size-1; i++){
            speedList.add(distanceList.get(i)/timeList.get(i));
        }
        //pairwise加速度
        for(int i = 0; i < size-2 ;i++){
            accList.add(speedList.get(i+1) - speedList.get(i)/timeList.get(i));
        }
        //速度排序
        Collections.sort(speedList);
        for(int i = 0; i<speedList.size();i++) {
            Log.d("speedSorted", String.valueOf(speedList.get(i)));
        }
        //20分位速度



    }

    public double calDistance(int e1, int e2){
        return    Math.sqrt(
                Math.pow(mTempList.get(e1).getxCoordinate()
                        - mTempList.get(e2).getxCoordinate(), 2)+
                        Math.pow(mTempList.get(e1).getyCoordinate()
                                - mTempList.get(e2).getyCoordinate(), 2)
        );
    }
}
