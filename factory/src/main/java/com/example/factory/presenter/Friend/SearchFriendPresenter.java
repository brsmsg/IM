package com.example.factory.presenter.Friend;

import android.text.TextUtils;
import android.util.Log;

import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.model.User;
import com.example.factory.model.api.friend.SearchFriendModel;
import com.example.factory.model.api.friend.SendFriendRequestModel;
import com.example.factory.model.db.Contact;
import com.example.factory.utils.NetUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchFriendPresenter implements SearchFriendContract.Presenter{

    private final String searchFriendUrl = "http://118.31.64.83:8080/friend/search";
    private final String sendFriendRequestUrl = "http://118.31.64.83:8080/friend/sendFriendRequest";

    private SearchFriendContract.View mSearchFriendView;

    //View与Presenter绑定
    public SearchFriendPresenter(SearchFriendContract.View searchFriendView) {
        this.mSearchFriendView = searchFriendView;
        mSearchFriendView.setPresenter(this);
    }

    @Override
    public void start() {
        mSearchFriendView.initRecycler();
    }

    /**
     * 搜索好友
     * @param myId 自己id
     * @param friendUsername 对方用户名
     */
    @Override
    public void searchFriend(final String myId, final String friendUsername) {
        if(TextUtils.isEmpty(friendUsername)){
            mSearchFriendView.showError(R.string.err_friendname_empty);
        }else{
            Factory.getInstance().getThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    String result = NetUtils.postKeyValue("myId",myId, "friendUsername", friendUsername,searchFriendUrl);

                    if (result != null){
                        parseFriendResult(result);
                    }else {
                        mSearchFriendView.showError(R.string.err_service);
                    }
                }
            });
        }
    }


    @Override
    public void parseFriendResult(String result) {
        SearchFriendModel searchFriendModel = Factory.getInstance()
                .getGson().fromJson(result,SearchFriendModel.class);
        //返回成功标志
        String msg = searchFriendModel.getMsg();
        //返回失败标志
        String message = searchFriendModel.getMessage();
        if (msg != null && msg.equals("success")){
            User user = searchFriendModel.getData();
            mSearchFriendView.searchSuccess(user);
        }else{
            //返回错误信息
            if (message!=null && message.equals("User doesn't exist."))
            mSearchFriendView.showError("用户不存在，请重新输入");
        }
    }


    /**
     * 发送好友请求
     * @param myId
     * @param friendUsername
     */
    @Override
    public void sendFriendRequest(final String myId, final String friendUsername) {
        //检查能否获取当前用户名
        if (TextUtils.isEmpty(myId)){
            mSearchFriendView.showError(com.example.common.R.string.err_searchrequest);
        }else{
            Factory.getInstance().getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    String result = NetUtils.postKeyValue("myId", myId,"friendUsername", friendUsername,sendFriendRequestUrl);
                    if (result!=null){
                        parseSendResult(result);
                    }else{
                        //出现服务器错误无法返回成功信息
                        mSearchFriendView.showError(com.example.common.R.string.err_service);
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
            mSearchFriendView.sendRequestSuccess(msg);
        }else{
            String message = sendFriendRequestModel.getMessage();
            Log.d("result", message);
//            mSearchFriendView.showError(com.example.common.R.string.err_service);
            //根据服务器返回类型进行显示
            switch (message){
                case "You can't add yourself as a friend.":
                    mSearchFriendView.showError("您不能添加自己为好友，请重新输入");
                    break;
                case "This user has been your friend.":
                    mSearchFriendView.showError("该用户已经是您的好友，请勿重复添加");
                    break;
                default:
                    break;

            }

        }

    }

}
