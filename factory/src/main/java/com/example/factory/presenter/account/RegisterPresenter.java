package com.example.factory.presenter.account;

import android.text.TextUtils;

import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.R2;
import com.example.factory.model.api.Account.RegisterModel;
import com.example.factory.utils.NetUtils;


import butterknife.BindString;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * @author brsmsg
 * @time 2020/3/10
 */
public class RegisterPresenter implements RegisterContract.Presenter{
    @BindString(R2.string.url_register)
    String registerUrl;

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
                    RegisterModel registerModel = new RegisterModel(userName, password);

//                    String result = NetUtils.postJson(registerModel, registerUrl);
                    String result = "{\"status\": \"success\"}";
                    if(result != null){
                        parseRegisterResult(result);
                    }else {
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
        String status = registerModel.getStatus();
        if(status.equals("success")){
            mRegisterView.registerSuccess();
        }else{
            mRegisterView.showError(R.string.err_service);
        }
    }

    @Override
    public void start() {

    }

}
