package com.example.instantMessaging.Activities;

import android.content.Context;
import android.content.Intent;

import com.example.common.app.Activity;
import com.example.common.app.Fragment;
import com.example.factory.presenter.account.LoginPresenter;
import com.example.factory.presenter.account.RegisterPresenter;
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
     * 切换fragment
     */
    @Override
    public void changeFragment(){
        //判断切换哪一个fragment
        if(mCurrentFragment == mLoginFragment){
            if(mRegisterFragment == null){
                mRegisterFragment = new RegisterFragment();

            }
            mCurrentFragment = mRegisterFragment;

            mRegisterPresenter = new RegisterPresenter((RegisterFragment) mCurrentFragment);
        }else{
            mCurrentFragment = mLoginFragment;
        }

        //切换fragment进行显示
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_container, mCurrentFragment).commit();
    }

}