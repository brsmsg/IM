package com.example.instantMessaging.Activities;

import android.content.Context;
import android.content.Intent;

import com.example.common.app.Activity;
import com.example.common.app.Fragment;
import com.example.factory.presenter.account.LoginPresenter;
import com.example.factory.presenter.account.RegisterPresenter;
import com.example.instantMessaging.Activities.PopWindow.MPopupWindow;
import com.example.instantMessaging.Fragments.FragmentTrigger;
import com.example.instantMessaging.Fragments.LoginFragment;
import com.example.instantMessaging.Fragments.RegisterFragment;
import com.example.instantMessaging.R;


/**
 * @author brsmsg
 * @time 2020/3/6
 */
public class AccountActivity extends Activity implements FragmentTrigger {
    //当前Fragment
    private Fragment mCurrentFragment;
    //登录Fragment
    private LoginFragment mLoginFragment;
    //注册Fragment
    private RegisterFragment mRegisterFragment;

    //登录Presenter
    private LoginPresenter mLoginPresenter;
    //注册Presenter
    private RegisterPresenter mRegisterPresenter;

    /**
     * AccountActivity  显示入口
     * @param context
     */
    public static void show(Context context){
        context.startActivity(new Intent(context, AccountActivity.class));
    }


    @Override
    protected int getContentLayotId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        //fragment初始化，向AccountActivity里填充
        mLoginFragment = new LoginFragment();
        mCurrentFragment = mLoginFragment;
        getSupportFragmentManager().beginTransaction().
                add(R.id.layout_container, mCurrentFragment).commit();
        mLoginPresenter = new LoginPresenter((LoginFragment) mCurrentFragment);
    }

    /**
     * 切换fragment，做出修改，监听返回按键实现从注册返回登陆
     * @param type
     */
    @Override
    public void changeFragment(int type){
        //Fragment fragment = mLoginFragment;
        //从登陆切换到注册
        if(type==1){
            if(mRegisterFragment == null){
                mRegisterFragment = new RegisterFragment();
            }

            //mCurrentFragment = mRegisterFragment;
            //切换fragment进行显示
            getSupportFragmentManager().beginTransaction()
                    .hide(mLoginFragment)
                    .add(R.id.layout_container, mRegisterFragment)
                    .addToBackStack(null)
                    .commit();

            mRegisterPresenter = new RegisterPresenter( mRegisterFragment, this);
        }else{//进入行为获取界面的同时，切换为登陆
            //mCurrentFragment = mLoginFragment;
            getSupportFragmentManager().beginTransaction()
                    .hide(mRegisterFragment)
                    .show(mLoginFragment).commit();
        }
    }

}