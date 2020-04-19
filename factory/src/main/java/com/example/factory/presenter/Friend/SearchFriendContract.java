package com.example.factory.presenter.Friend;

import com.example.common.factory.base.BaseContract;
import com.example.common.factory.base.BasePresenter;
import com.example.common.factory.base.BaseView;
import com.example.factory.model.User;

public interface SearchFriendContract extends BaseContract {
    interface View extends BaseView<Presenter> {

        //初始化recycler
        void initRecycler();

        //搜索到好友并显示
        void searchSuccess(User data);

        //返回message中携带的错误信息
        void showError(String message);

        //发送好友请求成功
        void sendRequestSuccess(String msg);
    }

    interface Presenter extends BasePresenter {

        //查询好友请求
        void searchFriend(String myId,String friendUsername);

        //解析返回的好友信息
        void parseFriendResult(String result);

        //发送好友请求
        void sendFriendRequest(String myId,String friendUsername);

        //解析发出好友请求后的返回信息
        void parseSendResult(String result);

    }
}
