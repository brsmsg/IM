package com.example.factory;

import android.content.Context;

import com.google.gson.Gson;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author brsmsg
 * @time 2020/3/8
 */
public class Factory {
    private Context mContext;

    //创建Factory对象
    private static Factory mFactory = new Factory();

    //全局线程池
    private static Executor executor = Executors.newFixedThreadPool(4);

    //全局Gson
    private static Gson gson = new Gson();

    /**
     * 获取单例对象
     * @return
     */
    public static Factory getInstance(){
        return mFactory;
    }

    private Factory() {
    }

    public void init(Context context){
        mContext = context;
    }

    /**
     * 获取全局线程池
     * @return executor
     */
    public Executor getThreadPool(){
        return executor;
    }

    /**
     * 获取全局Gson
     * @return gson
     */
    public Gson getGson(){
        return gson;
    }
}
