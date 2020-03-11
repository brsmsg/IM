package com.example.factory.presenter.account;

import com.example.common.factory.base.BaseContract;
import com.example.common.factory.base.BasePresenter;
import com.example.common.factory.base.BaseView;

/**
 * @author brsmsg
 * @time 2020/3/10
 */
public interface RegisterContract extends BaseContract {
    interface View extends BaseView<Presenter>{
        //注册成功
        void registerSuccess();
    }

    interface Presenter extends BasePresenter{
        //用户行为记录
        void behaviorRecord();

        //发起注册请求
        void register(String userName, String password);

        //解析返回的结果
        void parseRegisterResult(String result);
    }
}
