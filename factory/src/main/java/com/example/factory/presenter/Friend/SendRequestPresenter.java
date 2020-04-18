package com.example.factory.presenter.Friend;

import android.text.TextUtils;
import android.util.Log;

import com.example.factory.Factory;
import com.example.factory.model.api.friend.SendFriendRequestModel;
import com.example.factory.utils.NetUtils;

public class SendRequestPresenter implements SendRequestContract.Presenter{

    private final String sendFriendRequestUrl = "http://118.31.64.83:8080/friend/searchFriendRequest";

    private SendRequestContract.View mSendRequestView;

    public SendRequestPresenter(SendRequestContract.View sendRequestView) {
        this.mSendRequestView = sendRequestView;
        mSendRequestView.setPresenter(this);
    }

    //发送好友请求
    @Override
    public void sendFriendRequest(final String myId, final String friendUsername) {
        //检查能否获取当前用户名
        if (TextUtils.isEmpty(myId)){
            mSendRequestView.showError(com.example.common.R.string.err_searchrequest);
        }else{
            Factory.getInstance().getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    String result = NetUtils.postKeyValue(myId,friendUsername,sendFriendRequestUrl);
                    Log.d("sendFriendRequest", "result");
                    if (result!=null){
                        parseSendResult(result);
                    }else{
                        //出现服务器错误无法返回成功信息
                        mSendRequestView.showError(com.example.common.R.string.err_service);
                    }
                }
            });
        }

    }

    //解析返回信息
    @Override
    public void parseSendResult(String result) {

        SendFriendRequestModel sendFriendRequestModel = Factory.getInstance().getGson().fromJson(result,SendFriendRequestModel.class);
        String msg = sendFriendRequestModel.getMsg();
        if (msg != null && msg.equals("success")){
            mSendRequestView.sendRequestSuccess(msg);
        }else{
            mSendRequestView.showError(com.example.common.R.string.err_service);
        }

    }

    @Override
    public void start() {

    }
}
