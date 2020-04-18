package com.example.factory.presenter.Friend;

import com.example.common.factory.base.BaseContract;
import com.example.common.factory.base.BasePresenter;
import com.example.common.factory.base.BaseView;
import com.example.factory.model.FriendRequest;

import java.util.List;

public interface SearchRequestContract extends BaseContract {

    interface View extends BaseView<Presenter>{

        //处理成功提示
        void operateSuccess(String msg);

        //初始化好友列表的内容
        void initRecycler(List<FriendRequest> requestList);

        //刷新UI界面，向RecyclerView中添加数据
        void refreshUi(FriendRequest friendRequest);

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
    }
}
