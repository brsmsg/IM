package com.example.factory.presenter.Friend;

import com.example.common.factory.base.BaseContract;
import com.example.common.factory.base.BasePresenter;
import com.example.common.factory.base.BaseView;

public interface SendRequestContract extends BaseContract {

    interface View extends BaseView<Presenter>{

        //发送好友请求成功
        void sendRequestSuccess(String msg);

    }

    interface Presenter extends BasePresenter{
        //发送好友请求
        void sendFriendRequest(String myId,String friendUsername);
        //解析发出好友请求后的返回信息
        void parseSendResult(String result);
    }
}
