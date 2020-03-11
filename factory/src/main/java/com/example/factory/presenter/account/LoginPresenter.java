package com.example.factory.presenter.account;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.R2;
import com.example.factory.model.api.Account.LoginModel;
import com.example.factory.utils.NetUtils;
import com.google.gson.Gson;

import java.io.IOException;

import butterknife.BindString;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author brsmsg
 * @time 2020/3/7
 */
public class LoginPresenter implements LoginContract.Presenter{
    @BindString(R2.string.url_login)
    String loginUrl;

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
            mLoginView.showError(R.string.err_account_empty_input);
        }else{
            //开启子线程进行登录操作
            Factory.getInstance().getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    LoginModel loginModel = new LoginModel(userName, password);
//                    String loginModelJson = Factory.getInstance().getGson().toJson(loginModel);

//                    Log.d("LoginPresent", loginModelJson);
//                    OkHttpClient okHttpClient = new OkHttpClient();
//
//                    RequestBody body = RequestBody
//                                .create(loginModelJson,
//                            MediaType.parse("application/json; charset=utf-8"));
//
//                    Request request = new Request.Builder()
//                            .url(urlLogin)
//                            .post(body)
//                            .build();


//                    try {
//                        Response response = okHttpClient.newCall(request).execute();
//                        String result = response.body().string();
                    //解析并进行下一步处理


//                   String result = NetUtils.postJson(loginModel, loginUrl);


                        String result = "{\"status\":\"success\", \"userName\": \"kbh\", \"id\": \"1\"}";
//                        String result = null;
                        if(result != null) {
                            parseLoginResult(result);
                        }else{
                            mLoginView.showError(R.string.err_service);
                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
            });
        }

    }

    /**
     * 解析登录请求返回的数据
     * @param result 返回的json数据
     */
    @Override
    public void parseLoginResult(String result) {
        LoginModel loginModel = Factory.getInstance()
                .getGson().fromJson(result, LoginModel.class);
        String status = loginModel.getStatus();
        String id = loginModel.getId();
        String userName = loginModel.getUserName();

        if (status.equals("success")){
            mLoginView.loginSuccess();
        }else{

        }
    }
}
