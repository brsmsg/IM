package com.example.factory.presenter.Friend;

import android.text.TextUtils;
import android.util.Log;

import com.example.factory.Factory;
import com.example.factory.model.FriendRequest;
import com.example.factory.model.api.friend.OperateFriendRequestModel;
import com.example.factory.model.api.friend.SearchFriendRequestModel;
import com.example.factory.utils.NetUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 包含查询好友请求和处理好友请求的功能
 */
public class SearchRequestPresenter implements SearchRequestContract.Presenter {

    //查询接收到的好友请求
    private final String searchRequestUrl = "http://118.31.64.83:8080/friend/searchFriendRequest";

    //处理接收到的好友请求
    private final String operateRequestUrl = "http://118.31.64.83:8080/friend/operateFriendRequest";

    private SearchRequestContract.View mSearchRequestView;

    //好友请求列表
    private List<FriendRequest> requestList = new ArrayList<>();


    //构造函数
    public SearchRequestPresenter(SearchRequestContract.View searchRequestView) {
        this.mSearchRequestView = searchRequestView;
        mSearchRequestView.setPresenter(this);
    }

    //查询接收到的好友请求
    @Override
    public void searchRequest(final String myId) {
        if(TextUtils.isEmpty(myId)){
            mSearchRequestView.showError(com.example.common.R.string.err_searchrequest);
        }else{
            Factory.getInstance().getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    String result = NetUtils.postKey(myId,searchRequestUrl);
                    Log.d("searchRequest", "result");
                    if (result != null){
                        parseRequestResult(result);
                    }else{
                        mSearchRequestView.showError(com.example.common.R.string.err_friendrequest_null);
                    }
                }
            });
        }

    }

    //解析返回的好友请求
    @Override
    public void parseRequestResult(String result) {
        SearchFriendRequestModel searchFriendRequestModel = Factory.getInstance()
                .getGson().fromJson(result,SearchFriendRequestModel.class);
        String msg = searchFriendRequestModel.getMsg();
        if (msg != null && msg.equals("success")){
            FriendRequest data = searchFriendRequestModel.getData();
            //获取sendUserId和sendDateTime来构造用于显示的FriendRequest实例
            FriendRequest dataShow = new FriendRequest(data.getSendUserId(),data.getRequestDateTime());
            mSearchRequestView.refreshUi(dataShow);
        }else{
            mSearchRequestView.showError(com.example.common.R.string.err_friendrequest_null);

        }
    }

    //处理好友请求
    @Override
    public void operateRequest(final String myId, final String friendId, final int operateType) {
        if (TextUtils.isEmpty(myId)){
            mSearchRequestView.showError(com.example.common.R.string.err_searchrequest);
        }else{
            Factory.getInstance().getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    OperateFriendRequestModel operateFriendRequestModel = new OperateFriendRequestModel(myId,friendId,operateType);
                    String result = NetUtils.postJson(operateFriendRequestModel,operateRequestUrl);
                    Log.d("operateRequest", "result");
                    if (result != null){
                        parseOperateResult(result);
                    }else{
                        mSearchRequestView.showError(com.example.common.R.string.err_service);
                    }
                }
            });
        }

    }

    //解析处理好友请求之后的结果
    @Override
    public void parseOperateResult(String result) {
        OperateFriendRequestModel operateFriendRequestModel = Factory.getInstance().getGson().fromJson(result,OperateFriendRequestModel.class);
        String msg = operateFriendRequestModel.getMsg();
        if (msg != null && msg.equals("success")){
            mSearchRequestView.operateSuccess(msg);
        }else{
            mSearchRequestView.showError(com.example.common.R.string.err_service);
        }

    }

    //更新数据并初始化FriendRequest数据
    @Override
    public void start() {
        List<FriendRequest> requestList = new ArrayList<>();
        mSearchRequestView.initRecycler(requestList);


    }
}
