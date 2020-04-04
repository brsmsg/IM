package com.example.instantMessaging.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.common.app.Activity;
import com.example.common.app.Fragment;
import com.example.factory.Factory;
import com.example.factory.model.User;
import com.example.factory.presenter.Session.SessionPresenter;
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
    //用户id,头像，用户名以及KEY
    public final static String MY_ID = "MY_ID";
    public final static String MY_PORTRAIT = "MY_PORTRAIT";
    public final static String MY_USERNAME = "MY_USERNAME";

    private String myId;
    private String myPortrait;
    private String myUsername;

    private Fragment mCurrentFragment;
    //消息界面
    private MessageFragment mMessageFragment;
    //联系人界面
    private ContactFragment mContactFragment;
    //朋友圈界面
    private MomentFragment mMomentFragment;

    private ContactPresenter mContactPresenter;
    private SessionPresenter mSessionPresenter;

    private Bundle bundle;

    @BindView(R.id.bottom_bar)
    BottomNavigationView mBottomBar;

    @BindView(R.id.txt_title)
    TextView mTitle;

    @BindView(R.id.img_portrait)
    ImageView mPortrait;

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
        //初始化sessionPresenter
        mSessionPresenter = new SessionPresenter(mMessageFragment);
    }

    @Override
    protected void initData() {
        super.initData();
        //三个属性赋值
        myId = getIntent().getExtras().getString(MY_ID);
        myUsername = getIntent().getExtras().getString(MY_USERNAME);
        myPortrait = getIntent().getExtras().getString(MY_PORTRAIT);
        //为bundle赋值
        bundle = new Bundle();
        bundle.putString(MY_ID, myId);
        bundle.putString(MY_USERNAME, myUsername);
        bundle.putString(MY_PORTRAIT, myPortrait);
        //传给MessageFragment
        mMessageFragment.setArguments(bundle);
        //初始化头像
        if ( myPortrait != null){
            Glide.with(this).load(myPortrait).into(mPortrait);
            Log.d("portraitUrl", myPortrait);
        }
        //初始化webSocket
//        Factory.getInstance().initWebSocket("ws://echo.websocket.org",this);
        Factory.getInstance().initWebSocket("ws://118.31.64.83:8081/ws", myId, this);
    }

    /**
     * 显示入口
     */
    public static void show(Context context, User user){
        Intent intent = new Intent(context, MainActivity.class);

        intent.putExtra(MY_ID, user.getId());
        intent.putExtra(MY_PORTRAIT, user.getFaceImage());
        intent.putExtra(MY_USERNAME, user.getUsername());
        Log.d("accountId", user.getId());
        context.startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.main_message:
                mTitle.setText(R.string.main_message);
                changeFragment(mMessageFragment);
                return true;
            case R.id.main_contact:
                mTitle.setText(R.string.main_contact);
                if(mContactFragment == null){
                    mContactFragment = new ContactFragment();
                    mContactFragment.setArguments(bundle);
                }
                //创建联系人presenter实例
                mContactPresenter = new ContactPresenter(mContactFragment);
                changeFragment(mContactFragment);
                Log.d("testBottomVav", "contact");
                return true;
            case R.id.main_moment:
                mTitle.setText(R.string.main_moment);
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
     * 切换fragment
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

    //获取id,用户名,头像
    public String getMyId(){
        return myId;
    }

    public String getMyUsername(){
        return myUsername;
    }

    public String getMyPortrait(){
        return myPortrait;
    }

}
