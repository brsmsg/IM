package com.example.instantMessaging.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.common.app.Activity;
import com.example.common.app.Fragment;
import com.example.factory.presenter.contact.ContactPresenter;
import com.example.instantMessaging.Fragments.main.ContactFragment;
import com.example.instantMessaging.Fragments.main.MessageFragment;
import com.example.instantMessaging.Fragments.main.MomentFragment;
import com.example.instantMessaging.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends Activity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Fragment mCurrentFragment;
    //消息界面
    private MessageFragment mMessageFragment;
    //联系人界面
    private ContactFragment mContactFragment;
    //朋友圈界面
    private MomentFragment mMomentFragment;

    private ContactPresenter mContactPresenter;

    @BindView(R.id.bottom_bar)
    BottomNavigationView mBottomBar;


    @Override
    protected int getContentLayotId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        //设置bottomNavbar监听
        mBottomBar.setOnNavigationItemSelectedListener(this);

        //初始化界面为消息界面
        mMessageFragment = new MessageFragment();
        mCurrentFragment = mMessageFragment;
        getSupportFragmentManager().beginTransaction()
                .add(R.id.layout_container_main, mMessageFragment).commit();
    }

    /**
     * 显示入口
     */
    public static void show(Context context){
        context.startActivity(new Intent(context, MainActivity.class));
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.main_message:
                changeFragment(mMessageFragment);
                return true;
            case R.id.main_contact:
                if(mContactFragment == null){
                    mContactFragment = new ContactFragment();
                }
                //创建联系人presenter实例
                mContactPresenter = new ContactPresenter(mContactFragment);
                changeFragment(mContactFragment);
                Log.d("testBottomVav", "contact");
                return true;
            case R.id.main_moment:
                if(mMomentFragment == null){
                    mMomentFragment = new MomentFragment();
                }
                changeFragment(mMomentFragment);
                Log.d("1213", "moment");
                return true;
        }
        return false;
    }

    /**
     * 切换fragement
     * @param fragment 要切换的目标fragment
     */
    private void changeFragment(Fragment fragment){
        if (mCurrentFragment != fragment){
            if(!fragment.isAdded()){
                getSupportFragmentManager().beginTransaction()
                        .hide(mCurrentFragment)
                        .add(R.id.layout_container_main, fragment).commit();
            }else{
                getSupportFragmentManager().beginTransaction()
                        .hide(mCurrentFragment)
                        .show(fragment).commit();
            }
            mCurrentFragment = fragment;
        }


    }


}
