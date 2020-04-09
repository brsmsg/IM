package com.example.factory.presenter.account;

import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.common.RSA.RsaEncryptUtil;
import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.model.User;
import com.example.factory.model.api.account.LoginModel;
import com.example.factory.utils.NetUtils;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;


/**
 * @author brsmsg
 * @time 2020/3/7
 */
public class LoginPresenter implements LoginContract.Presenter{
    private final String loginUrl = "http://118.31.64.83:8080/account/login";

    private LoginContract.View mLoginView;

    public LoginPresenter (LoginContract.View loginView){
        mLoginView = loginView;
        mLoginView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void login(final String username, final String password) {
        //判断用户名密码是否为空
        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
            mLoginView.showError(R.string.err_account_empty_input);
        }else{
            //开启子线程进行登录操作
            Factory.getInstance().getThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    //注册生成密钥对
                    try {
                        RsaEncryptUtil.genKeyPair();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    //生成公钥
                    String publicKey = RsaEncryptUtil.getPublicKey();
                    //生成私钥
                    String privateKey = RsaEncryptUtil.getPrivateKey();

//                    try {
//                        String encryptStr = RsaEncryptUtil.encrypt("hello", publicKey);
//                        Log.d("encrypt", encryptStr);
//
//                        String result = RsaEncryptUtil.decrypt(after, privateKey);
//                        Log.d("decrypt", result);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }


                    LoginModel loginModel = new LoginModel(username, password, publicKey);
                    //发送json并取得返回数据
                    String result = NetUtils.postJson(loginModel, loginUrl);


//                        String result = "{\"status\":\"success\", \"userName\": \"kbh\", \"id\": \"1\"}";
//                        String result = null;
                        if(result != null) {
                            Log.d("return", result);
                            parseLoginResult(result, publicKey, privateKey);
                        }else{
                            //请求服务器出现错误
                            mLoginView.showError(R.string.err_service);
                        }
                }
            });
        }

    }

    /**
     * 解析登录返回数据
     * @param result 返回数据
     * @param publicKey 公钥
     * @param privateKey 私钥
     */
    @Override
    public void parseLoginResult(String result, String publicKey, String privateKey) {
        LoginModel loginModel = Factory.getInstance()
                .getGson().fromJson(result, LoginModel.class);
        String msg = loginModel.getMsg();
        if(msg != null && msg.equals("success")) {
            User user = loginModel.getData();

            mLoginView.loginSuccess(user, publicKey, privateKey);
        }else{
            mLoginView.showError(R.string.err_parameter);
        }
    }

}
