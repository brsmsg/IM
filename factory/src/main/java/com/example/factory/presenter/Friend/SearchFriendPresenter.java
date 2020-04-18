package com.example.factory.presenter.Friend;

import android.text.TextUtils;
import android.util.Log;

import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.model.User;
import com.example.factory.model.api.friend.SearchFriendModel;
import com.example.factory.utils.NetUtils;

import java.io.IOException;

public class SearchFriendPresenter implements SearchFriendContract.Presenter{

    private final String searchFriendUrl = "http://118.31.64.83:8080/friend/search";

    private SearchFriendContract.View mSearchFriendView;

    //View与Presenter绑定
    public SearchFriendPresenter(SearchFriendContract.View searchFriendView) {
        this.mSearchFriendView = searchFriendView;
        mSearchFriendView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void searchFriend(final String myId, final String friendUsername) {
        if(TextUtils.isEmpty(friendUsername)){
            mSearchFriendView.showError(R.string.err_friendname_empty);
        }else{
            Factory.getInstance().getThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    String result = NetUtils.postKeyValue(myId,friendUsername,searchFriendUrl);

                    Log.d("searchFriend", "result");
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
            mSearchFriendView.showError(message);

        }


    }
}
