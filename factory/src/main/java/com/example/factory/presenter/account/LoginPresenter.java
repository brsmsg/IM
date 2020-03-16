package com.example.factory.presenter.account;

import android.text.TextUtils;
import android.util.Log;

import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.model.api.Account.AccountModel;
import com.example.factory.utils.NetUtils;


/**
 * @author brsmsg
 * @time 2020/3/7
 */
public class LoginPresenter implements LoginContract.Presenter{
//    @BindString(R2.string.url_login)
//    String loginUrl;

    private String loginUrl = "http://118.31.64.83:8080/account/login";

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
                    AccountModel accountModel = new AccountModel(userName, password);
                    //发送json并取得返回数据
                    String result = NetUtils.postJson(accountModel, loginUrl);


//                        String result = "{\"status\":\"success\", \"userName\": \"kbh\", \"id\": \"1\"}";
//                        String result = null;
                        if(result != null) {
                            Log.d("return", result);
                            parseLoginResult(result);
                        }else{
                            //请求服务器出现错误
                            mLoginView.showError(R.string.err_service);
                        }
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
        AccountModel accountModel = Factory.getInstance()
                .getGson().fromJson(result, AccountModel.class);

        Log.d("accountModel", accountModel.toString());
        String status = accountModel.getMsg();
        String id = accountModel.getCode();
        String data = accountModel.getData();

        if (status == null){
            mLoginView.showError(R.string.err_parameter);
        }else{
            mLoginView.loginSuccess();
        }
    }
}
