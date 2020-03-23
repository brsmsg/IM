package com.example.factory.presenter.account;

import android.text.TextUtils;
import android.util.Log;

import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.model.api.account.AccountModel;
import com.example.factory.model.api.account.RegisterModel;
import com.example.factory.utils.NetUtils;

/**
 * @author brsmsg
 * @time 2020/3/10
 */
public class RegisterPresenter implements RegisterContract.Presenter{

    private String registerUrl = "http://118.31.64.83:8080/account/register";

    private RegisterContract.View mRegisterView;

    public RegisterPresenter(RegisterContract.View registerView){
        mRegisterView = registerView;
        mRegisterView.setPresenter(this);
    }


    @Override
    public void behaviorRecord() {

    }

    @Override
    public void register(final String userName, final String password) {
        if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            mRegisterView.showError(R.string.err_account_empty_input);
        }else{
            Factory.getInstance().getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    AccountModel accountModel = new AccountModel(userName, password);

                    String result = NetUtils.postJson(accountModel, registerUrl);
//                    String result = "{\"status\": \"success\"}";

                    Log.d("Register", "result");
                    if(result != null){
                        parseRegisterResult(result);
                    }else {
                        //请求服务器出现错误
                        mRegisterView.showError(R.string.err_service);
                    }
                }
            });
        }

    }

    @Override
    public void parseRegisterResult(String result) {
        RegisterModel registerModel = Factory.getInstance()
                .getGson().fromJson(result, RegisterModel.class);
        String msg = registerModel.getMsg();
        if(msg != null && equals("success")){
            mRegisterView.registerSuccess();
        }else{
            mRegisterView.showError(R.string.err_duplicate);
        }
    }

    @Override
    public void start() {

    }

}
