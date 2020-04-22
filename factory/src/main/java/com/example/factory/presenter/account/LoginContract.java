package com.example.factory.presenter.account;

import android.content.Context;

import com.example.common.factory.base.BaseContract;
import com.example.common.factory.base.BasePresenter;
import com.example.common.factory.base.BaseView;
import com.example.factory.model.User;
import com.google.gson.Gson;

/**
 * @author brsmsg
 * @time 2020/3/7
 */
public interface LoginContract extends BaseContract {
    interface View extends BaseView<Presenter>{
        //登录成功
        void loginSuccess(User user, String publicKey, String privateKey);

    }

    interface Presenter extends BasePresenter{
        //发起登录请求/
        void login(String userName, String password, Context context);

        //解析登录请求的返回数据
        void parseLoginResult(String result, String publicKey, String privateKey);
    }
}
