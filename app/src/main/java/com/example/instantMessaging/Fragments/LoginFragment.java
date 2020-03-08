package com.example.instantMessaging.Fragments;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.common.app.Fragment;
import com.example.factory.presenter.account.LoginContract;
import com.example.instantMessaging.R;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.*;

/**
 * @author brsmsg
 * @time 2020/3/6
 */
public class LoginFragment extends Fragment implements LoginContract.View {

    private LoginContract.Presenter mPresenter;

    @BindView(R.id.edit_username)
    EditText mUserName;

    @BindView(R.id.edit_password)
    EditText mPassWord;

    @BindView(R.id.btn_login)
    Button mLogin;

    public LoginFragment(){

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull LoginContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    /**
     * 显示字符串错误
     * @param string 错误字符串
     */
    @Override
    public void StrError(int string) {
        Toast.makeText(this.getActivity(), getString(string), Toast.LENGTH_SHORT).show();

        //登录控件可以重新使用
        mUserName.setEnabled(true);
        mPassWord.setEnabled(true);
        mLogin.setEnabled(true);

    }


    /**
     * 点击登录按钮事件
     */
    @OnClick(R.id.btn_login)
    void submit(){
        String userName = mUserName.getText().toString().trim();
        String password = mPassWord.getText().toString().trim();
        mPresenter.login(userName, password);
    }

    /**
     * 登录成功
     */
    @Override
    public void loginSuccess() {
        this.getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run() {
                Toast.makeText(LoginFragment.this.getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
