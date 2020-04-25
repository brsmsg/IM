package com.example.common.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.common.factory.base.BaseContract;
import com.example.common.factory.base.BasePresenter;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author brsmsg
 * @time 2020/3/5
 */
public abstract class Fragment extends androidx.fragment.app.Fragment {
    protected View mRoot;
    protected Unbinder mRootUnbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //初始化参数
        initArgs(getArguments());
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null){
            int layId = getContentLayoutId();
            //初始化根布局，不在创建时添加到container里
            View root = inflater.inflate(layId, container, false);
            initWidget(root);
            initData();
            mRoot = root;
        }else{
            if(mRoot.getParent()!=null){
                //把当前root从其父控件中移除
                ((ViewGroup)mRoot.getParent()).removeView(mRoot);
            }
        }

        return mRoot;
    }

    //初始化完成后
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        initData();
    }

    /**
     * 初始化相关参数
     * @param bundle 参数Bundle
     * @return 正确返回true，错误返回false
     */
    protected void initArgs(Bundle bundle){
    }

    /**
     * 得到当前界面资源文件Id
     * @return 资源文件Id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget(View root){
        mRootUnbinder = ButterKnife.bind(this, root);
    }

    /**
     * 初始化数据
     */
    protected void initData(){

    }

    /**
     * 返回按键触发
     * @return ture表示已处理返回逻辑，activity不用finish
     * false表示没有处理，activity自己走自己的逻辑
     */
    public boolean onBackPress(){
        return false;
    }

    //Fragment中使用ButterKnife应在onDestroyView中unbind
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //mRootUnbinder.unbind();
    }

}
