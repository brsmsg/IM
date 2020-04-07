package com.example.factory.presenter.Personal;

import com.example.common.factory.base.BaseContract;
import com.example.common.factory.base.BasePresenter;
import com.example.common.factory.base.BaseView;
import com.example.factory.model.User;

public interface PersonalContract extends BaseContract {


    //个人信息显示的基本方法
    interface View extends BaseView<Presenter> {

        String getUserId();

        // 加载数据完成
        void onLoadDone(User user);

        // 是否发起聊天
        void allowSayHello(boolean isAllow);


    }

    //更改个人信息基本方法
    interface  Presenter extends BasePresenter {
        //获取用户信息
        User getUserPersonal();

        void parsePersonalResult(String result);

    }
}
