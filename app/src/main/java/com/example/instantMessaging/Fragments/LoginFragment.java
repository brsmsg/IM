package com.example.instantMessaging.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.common.app.Fragment;
import com.example.common.app.Mapper;
import com.example.factory.model.User;
import com.example.factory.presenter.account.LoginContract;
import com.example.factory.utils.SpUtils;
import com.example.instantMessaging.Activities.MainActivity;
import com.example.instantMessaging.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.*;

/**
 * @author brsmsg
 * @time 2020/3/6
 */
public class LoginFragment extends Fragment implements LoginContract.View {

    private LoginContract.Presenter mPresenter;

    private FragmentTrigger mTrigger;

    private SharedPreferences sp;

    private SharedPreferences.Editor editor;

    private MyReceiver mReceiver;

    //用户名key
    public static final String USERNAME = "USERNAME";
    //密码key
    public static final String PASSWORD = "PASSWORD";
//    //记住密码key
//    public static final String REM_ISCHECK = "REM_ISCHECK";
//    //自动登录key
//    public static final String AUTO_ISCHECK = "AUTO_ISCHECK";
//    //公钥key
//    public static final String PUBLIC_KEY = "PUBLIC_KEY";
//    public static final String PRIVATE_KEY = "PRIVATE_KEY";

    @BindView(R.id.edit_username)
    EditText mUserName;

    @BindView(R.id.edit_password)
    EditText mPassword;

    @BindView(R.id.btn_login)
    Button mLogin;

    @BindView(R.id.checkbox_rmb_pwd)
    CheckBox mRemember;

    @BindView(R.id.checkbox_auto_login)
    CheckBox mAutoLogin;

    public LoginFragment(){ }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //实例化trigger
        mTrigger = (FragmentTrigger) context;
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = checkNotNull( presenter);
    }

    @Override
    protected void initData() {
        super.initData();

        //动态注册广播
        mReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        //设置广播类型
        intentFilter.addAction("com.example.broadcast.LOGIN");
        Objects.requireNonNull(getActivity()).registerReceiver(mReceiver, intentFilter);

        //实例化sharedPreference对象
//        sp = getActivity().getApplication().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        //实例化editor
//        editor = sp.edit();
        //默认记住联系人
//        mUserName.setText(sp.getString("USERNAME", ""));

        mUserName.setText((String) SpUtils.getData(getActivity(), Mapper.SP_USERNAME, ""));
        //获得登录状态

        //test 不自动登录
//        editor.putBoolean(AUTO_ISCHECK, false).commit();

//        mRemember.setChecked(sp.getBoolean(REM_ISCHECK, true));
//        mAutoLogin.setChecked(sp.getBoolean(AUTO_ISCHECK, false));

        mRemember.setChecked((boolean) SpUtils.getData(getActivity(), Mapper.SP_REM_ISCHECK, true));
        mAutoLogin.setChecked((boolean) SpUtils.getData(getActivity(), Mapper.SP_AUTO_ISCHECK, true));

        //判断密码多选框状态
        if((boolean)SpUtils.getData(getActivity(), Mapper.SP_REM_ISCHECK, true)){
            mPassword.setText((String)SpUtils.getData(getActivity(), Mapper.SP_PASSWORD, ""));
            //自动登录选中
            if((boolean)SpUtils.getData(getActivity(), Mapper.SP_AUTO_ISCHECK, false)){
                login();
            }
        }
//        if(sp.getBoolean(REM_ISCHECK, true)){
//            mPassword.setText(sp.getString("PASSWORD", ""));


            //自动登录选中
//            if(sp.getBoolean(AUTO_ISCHECK, false)){
//                login();
//            }
//        }

    }

    /**
     * 显示字符串错误
     * @param string 错误字符串
     */
    @Override
    public void showError(int string) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            Toast.makeText(getActivity(), getString(string), Toast.LENGTH_SHORT).show();

            //登录控件可以重新使用
            mUserName.setEnabled(true);
            mPassword.setEnabled(true);
            mLogin.setEnabled(true);
        });


    }


    /**
     * 点击登录按钮事件
     */
    @OnClick(R.id.btn_login)
    public void login(){
        //保存记住密码自动登录选项
//        editor.putBoolean(REM_ISCHECK, mRemember.isChecked());
//        editor.putBoolean(AUTO_ISCHECK, mAutoLogin.isChecked());

        String userName = mUserName.getText().toString().trim();
        String password = mPassword.getText().toString().trim();


        mPresenter.login(userName, password, getActivity());
    }

    @OnCheckedChanged({R.id.checkbox_rmb_pwd,R.id.checkbox_auto_login})
    public void checkedChange(CompoundButton cButton, boolean isChecked){
        switch (cButton.getId()){
            case R.id.checkbox_rmb_pwd:
                //是否勾选记住密码
                if(mRemember.isChecked()){
//                    editor.putBoolean(REM_ISCHECK, true).commit();
                    SpUtils.saveData(getActivity(), Mapper.SP_REM_ISCHECK, true);
                }else{
                    //不记住密码后会自动取消自动给登录
//                    editor.putBoolean(REM_ISCHECK, false).commit();
                    SpUtils.saveData(getActivity(), Mapper.SP_REM_ISCHECK, false);
                    mAutoLogin.setChecked(false);
                    SpUtils.saveData(getActivity(), Mapper.SP_AUTO_ISCHECK, false);
//                    editor.putBoolean(AUTO_ISCHECK, false).commit();
                }
                break;
            case R.id.checkbox_auto_login:
                //是否勾选自动登录
                if(mAutoLogin.isChecked()){
                    //勾选自动登录后会自动记住密码
//                    editor.putBoolean(AUTO_ISCHECK, true).commit();
                    SpUtils.saveData(getActivity(), Mapper.SP_AUTO_ISCHECK, true);
                    mRemember.setChecked(true);
//                    editor.putBoolean(REM_ISCHECK, true).commit();
                    SpUtils.saveData(getActivity(), Mapper.SP_REM_ISCHECK, true);
                }else{
                    SpUtils.saveData(getActivity(), Mapper.SP_AUTO_ISCHECK, false);
//                    editor.putBoolean(AUTO_ISCHECK, false).commit();
                }
                break;
        }
    }

    /**
     * 登录成功
     */
    @Override
    public void loginSuccess(User user, String publicKey, String privateKey) {
        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show());
        SpUtils.saveData(getActivity(), Mapper.SP_PASSWORD_CURRENT, mPassword.getText().toString().trim());
        //登录成功才记住用户名以及密码
        if(mRemember.isChecked()){
            SpUtils.saveData(getActivity(), Mapper.SP_USERNAME, mUserName.getText().toString().trim());
            SpUtils.saveData(getActivity(), Mapper.SP_PASSWORD, mPassword.getText().toString().trim());
        }

        MainActivity.show(getActivity(), user, publicKey, privateKey);
        getActivity().finish();

    }


    /**
     * 点击跳转注册界面
     */
    @OnClick(R.id.txt_register)
    public void goToRegister(){
        //通过类型1明确执行从登陆到注册的fragment转换
        mTrigger.changeFragment(1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //注销监听
        Objects.requireNonNull(getActivity()).unregisterReceiver(mReceiver);
    }

    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String username = intent.getExtras().getString("USERNAME");
            String password = intent.getExtras().getString("PASSWORD");
            mUserName.setText(username);
            mPassword.setText(password);
        }
    }


}
