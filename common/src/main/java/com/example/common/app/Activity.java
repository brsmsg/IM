package com.example.common.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.List;

import butterknife.ButterKnife;

/**
 * @author brsmsg
 * @time 2020/3/5
 */
public abstract class Activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initWindows();
        if(initArgs(getIntent().getExtras())) {
            //布局设置到界面中
            int layoutId = getContentLayotId();
            setContentView(layoutId);
            initWidget();
            initData();
        }else{
            finish();
        }
    }

    /**
     *
     */
    protected void initWindows(){

    }

    /**
     * 初始化相关参数
     * @param bundle 参数Bundle
     * @return 正确返回true，错误返回false
     */
    protected boolean initArgs(Bundle bundle){
        return true;
    }

    /**
     * 得到当前界面资源文件Id
     * @return Id
     */
    protected abstract int getContentLayotId();

    /**
     * 初始化控件
     */
    protected void initWidget(){
        ButterKnife.bind(this);
    }

    /**
     * 初始化数据
     */
    protected void initData(){

    }

    @Override
    public boolean onSupportNavigateUp() {
        //点击导航界面返回  finish
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        //获取当前activity所有fragmrnt
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        //判断是否为空
        if(fragments != null && fragments.size()>0){
            for(Fragment fragment:fragments){
                //判断是否为我们能处理的fragment类型
                if(fragment instanceof com.example.common.app.Fragment){
                    //判断是否拦截了返回按钮，如果拦截了，则在Fragment中处理返回事件
                    if(((com.example.common.app.Fragment) fragment).onBackPress()){
                        return;
                    }
                }
            }
        }

        super.onBackPressed();
        finish();
    }


}
