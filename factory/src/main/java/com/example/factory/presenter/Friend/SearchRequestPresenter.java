package com.example.factory.presenter.Friend;

import android.text.TextUtils;
import android.util.Log;

import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.model.FriendRequest;
import com.example.factory.model.User;
import com.example.factory.model.api.friend.OperateFriendRequestModel;
import com.example.factory.model.api.friend.SearchFriendModel;
import com.example.factory.model.api.friend.SearchFriendRequestModel;
import com.example.factory.model.api.friend.SendFriendRequestModel;
import com.example.factory.utils.NetUtils;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    //搜索好友
    private final String searchFriendUrl = "http://118.31.64.83:8080/friend/search";

    //发送好友请求
    private final String sendFriendRequestUrl = "http://118.31.64.83:8080/friend/searchFriendRequest";

    private SearchRequestContract.View mSearchRequestView;

    //好友请求列表
    private List<FriendRequest> requestList = new ArrayList<>();


    //构造函数
    public SearchRequestPresenter(SearchRequestContract.View searchRequestView) {
        this.mSearchRequestView = searchRequestView;
        mSearchRequestView.setPresenter(this);
    }

    //查询接收到的好友请求，写入star方法中fragment创建时调用
    @Override
    public void searchRequest(final String myId) {
        if(TextUtils.isEmpty(myId)){
            mSearchRequestView.showError(com.example.common.R.string.err_searchrequest);
        }else{
            Factory.getInstance().getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    String result = NetUtils.postKey(myId,searchRequestUrl);
                    Log.d("searchRequest", result);
                    if (result != null){
                        Log.d("searchRequest", "result2");
                        String json = createJson().toString();
                        Logger.json(json);
                        Logger.json(result);
                        parseRequestResult(result);
                    }else{
                        mSearchRequestView.showError(com.example.common.R.string.err_friendrequest_null);
                    }
                }
            });
        }

    }
    //测试Logger库使用
    private JSONObject createJson() {
        try {
            JSONObject person = new JSONObject();
            person.put("phone", "12315");
            JSONObject address = new JSONObject();
            address.put("country", "china");
            address.put("province", "fujian");
            address.put("city", "xiamen");
            person.put("address", address);
            person.put("married", true);
            return person;
        } catch (JSONException e) {
            Logger.e(e, "create json error occured");
        }
        return null;
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

    //搜索好友
    @Override
    public void searchFriend(final String myId, final String friendUsername) {
        if(TextUtils.isEmpty(friendUsername)){
            mSearchRequestView.showError(R.string.err_friendname_empty);
        }else{
            Factory.getInstance().getThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    String result = NetUtils.postKeyValue(myId,friendUsername,searchFriendUrl);

                    Log.d("searchFriend", "result");
                    if (result != null){
                        parseFriendResult(result);
                    }else {
                        mSearchRequestView.showError(R.string.err_service);
                    }
                }
            });
        }

    }

    //解析返回的好友信息
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
            mSearchRequestView.searchSuccess(user);
        }else{
            //返回错误信息
            mSearchRequestView.showError(message);

        }
    }

    @Override
    public void sendFriendRequest(final String myId, final String friendUsername) {
        //检查能否获取当前用户名
        if (TextUtils.isEmpty(myId)){
            mSearchRequestView.showError(com.example.common.R.string.err_searchrequest);
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
                        mSearchRequestView.showError(com.example.common.R.string.err_service);
                    }
                }
            });
        }

    }

    @Override
    public void parseSendResult(String result) {
        SendFriendRequestModel sendFriendRequestModel = Factory.getInstance().getGson().fromJson(result,SendFriendRequestModel.class);
        String msg = sendFriendRequestModel.getMsg();
        if (msg != null && msg.equals("success")){
            mSearchRequestView.sendRequestSuccess(msg);
        }else{
            mSearchRequestView.showError(com.example.common.R.string.err_service);
        }

    }


    //更新数据并初始化FriendRequest数据
    @Override
    public void start() {
        //List<FriendRequest> requestList = new ArrayList<>();
        mSearchRequestView.initRecycler(requestList);

    }
}
