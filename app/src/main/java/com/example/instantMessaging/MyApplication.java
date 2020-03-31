package com.example.instantMessaging;

import android.app.Application;
import android.content.Context;
import android.media.audiofx.DynamicsProcessing;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.dbflow5.config.FlowConfig;
import com.dbflow5.config.FlowLog;
import com.dbflow5.config.FlowManager;

/**
 * @author brsmsg
 * @time 2020/3/20
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化数据库
        FlowManager.init(new FlowConfig.Builder(this).build());
        //设置日志显示
        FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
