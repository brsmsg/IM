package com.example.instantMessaging.Activities;

import android.content.Context;
import android.content.Intent;

import com.example.common.app.Activity;
import com.example.common.app.Fragment;
import com.example.factory.presenter.account.LoginContract;
import com.example.factory.presenter.account.LoginPresenter;
import com.example.instantMessaging.Fragments.LoginFragment;
import com.example.instantMessaging.R;

/**
 * @author brsmsg
 * @time 2020/3/6
 */
public class AccountActivity extends Activity {
    //登录Fragment
    private LoginFragment mLoginFragment;
    //注册Fragment
    private Fragment mRegisterFragment;

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
        getSupportFragmentManager().beginTransaction().
                add(R.id.layout_container, mLoginFragment).commit();
        LoginPresenter mLoginPresenter = new LoginPresenter(mLoginFragment);
    }
}