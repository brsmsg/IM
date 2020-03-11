package com.example.instantMessaging.Fragments;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.common.app.Fragment;
import com.example.factory.presenter.account.LoginContract;
import com.example.instantMessaging.Activities.AccountActivity;
import com.example.instantMessaging.Activities.MainActivity;
import com.example.instantMessaging.R;

import butterknife.BindView;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.*;

/**
 * @author brsmsg
 * @time 2020/3/6
 */
public class LoginFragment extends Fragment implements LoginContract.View {

    private LoginContract.Presenter mPresenter;

    private FragmentTrigger mTrigger;

    @BindView(R.id.edit_username)
    EditText mUserName;

    @BindView(R.id.edit_password)
    EditText mPassword;

    @BindView(R.id.btn_login)
    Button mLogin;

    public LoginFragment(){ }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        mPresenter.start();
//    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mTrigger = (FragmentTrigger) context;
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    /**
     * 显示字符串错误
     * @param string 错误字符串
     */
    @Override
    public void showError(int string) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), getString(string), Toast.LENGTH_SHORT).show();

                //登录控件可以重新使用
                mUserName.setEnabled(true);
                mPassword.setEnabled(true);
                mLogin.setEnabled(true);
            }
        });


    }


    /**
     * 点击登录按钮事件
     */
    @OnClick(R.id.btn_login)
    public void login(){
        String userName = mUserName.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        mPresenter.login(userName, password);
    }

    /**
     * 登录成功
     */
    @Override
    public void loginSuccess() {
        getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run() {
                Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();

            }
        });
        MainActivity.show(getActivity());
        getActivity().finish();

    }

    /**
     * 点击跳转注册界面
     */
    @OnClick(R.id.txt_register)
    public void goToRegister(){
        mTrigger.changeFragment();
    }
}
