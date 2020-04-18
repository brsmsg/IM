package com.example.factory.presenter.Friend;

import com.example.common.factory.base.BaseContract;
import com.example.common.factory.base.BasePresenter;
import com.example.common.factory.base.BaseView;
import com.example.factory.model.FriendRequest;
import com.example.factory.model.User;

import java.util.List;

public interface SearchRequestContract extends BaseContract {

    interface View extends BaseView<Presenter>{

        //处理好友请求成功提示
        void operateSuccess(String msg);


        //初始化好友请求列表的内容
        void initRecycler(List<FriendRequest> requestList);
        //刷新好友请求界面，向RecyclerView中添加数据
        void refreshUi(FriendRequest friendRequest);



        //搜索到好友并显示
        void searchSuccess(User data);
        //返回message中携带的错误信息String类型
        void showError(String message);


        //发送好友请求成功
        void sendRequestSuccess(String msg);



    }

    interface Presenter extends BasePresenter{

        //查询接收到的好友请求
        void searchRequest(String myId);
        //解析返回的好友申请信息
        void parseRequestResult(String result);


        //处理好友请求
        void operateRequest(String myId,String friendId,int operateType);
        //解析处理结果
        void parseOperateResult(String result);


        //查询好友
        void searchFriend(String myId,String friendUsername);
        //解析返回的好友信息
        void parseFriendResult(String result);


        //发送好友请求
        void sendFriendRequest(String myId,String friendUsername);
        //解析发出好友请求后的返回信息
        void parseSendResult(String result);

    }
}
