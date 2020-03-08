package com.example.factory.presenter.account;

import android.text.TextUtils;
import android.widget.Toast;

import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.R2;

import java.io.IOException;

import butterknife.BindString;
import butterknife.BindView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author brsmsg
 * @time 2020/3/7
 */
public class LoginPresenter implements LoginContract.Presenter{
    @BindString(R2.string.url_login)
    String urlLogin;

    private LoginContract.View mLoginView;

    public LoginPresenter (LoginContract.View loginView){
        mLoginView = loginView;
        mLoginView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void login(final String userName, final String password) {
        //判断用户名密码是否为空
        if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)){
            mLoginView.StrError(R.string.err_account_login_empty_input);
        }else{
            //开启子线程进行登录操作
            Factory.getInstance().getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
//                    OkHttpClient okHttpClient = new OkHttpClient();
//                    FormBody body = new FormBody.Builder()
//                            .add("userName", userName)
//                            .add("password", password)
//                            .build();
//
//                    Request request = new Request.Builder()
//                            .url(urlLogin)
//                            .post(body)
//                            .build();
//
//                    try {
//                        Response response = okHttpClient.newCall(request).execute();
//                        String result = response.body().string();
//
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                     mLoginView.loginSuccess();
                }
            });
        }

    }
}
