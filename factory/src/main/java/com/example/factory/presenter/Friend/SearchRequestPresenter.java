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

/*    //用于显示的好友请求列表，包含sendUserId和sendDateTime
    private List<FriendRequest> requestList = new ArrayList<>();*/


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
                    //测试用数据
                    /*String result = "{\n" +
                            "    \"code\": 0,\n" +
                            "    \"msg\": \"success\",\n" +
                            "    \"data\": [\n" +
                            "        {\n" +
                            "            \"id\": \"brsmsg_1586334335390388232\", \n"+
                            "            \"sendUserId\": \"15172382300\",\n" +
                            "            \"receiveUserId\": \"brsmsg_1584881024758574067\",\n" +
                            "            \"isAccept\": 0,\n" +
                            "            \"requestDateTime\": \"2020-03-23T13:03:46\"\n" +
                            "        },\n" +
                            "        {\n" +
                            "            \"id\": \"brsmsg_1585897820995103737\", \n"+
                            "            \"sendUserId\": \"18571549924\",\n" +
                            "            \"receiveUserId\": \"brsmsg_1584881024758574067\",\n" +
                            "            \"isAccept\": 0,\n" +
                            "            \"requestDateTime\": \"2020-03-23T13:03:46\"\n" +
                            "        },\n" +
                            "        {\n" +
                            "            \"id\": \"brsmsg_1585897820995103737\", \n"+
                            "            \"sendUserId\": \"17354424537\",\n" +
                            "            \"receiveUserId\": \"brsmsg_1584881024758574067\",\n" +
                            "            \"isAccept\": 0,\n" +
                            "            \"requestDateTime\": \"2020-03-23T13:03:46\"\n" +
                            "        }\n" +
                            "    ]\n" +
                            "}";*/

                    Log.d("searchRequest", result);
                    if (result != null){
                       /* Logger.json(result);*/
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

            //改成list，存放返回的所有data信息
            List<FriendRequest> dataList =  searchFriendRequestModel.getData();
            //循环遍历dataList，获取sendUserId和sendDateTime来构造用于显示的requestList实例
            List<FriendRequest> requestList = new ArrayList<>();
            for (FriendRequest data : dataList){
                //创建用于显示的FriendRequest实例
                FriendRequest friendRequest = new FriendRequest(data.getSendUserId(),data.getRequestDateTime());
                //每次在列表末尾添加数据
                requestList.add(friendRequest);
            }
            //刷新界面，调用SearchFragment中的方法，其包含在runOnUiThread中
            mSearchRequestView.refreshRecycler(requestList);
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
                    Log.d("operateRequest",result);
                    if (result != null){
                        parseOperateResult(result,friendId);
                    }else{
                        mSearchRequestView.showError(com.example.common.R.string.err_service);
                    }
                }
            });
        }
    }

    //解析处理好友请求之后的结果
    @Override
    public void parseOperateResult(String result, String sendUserId) {
        OperateFriendRequestModel operateFriendRequestModel = Factory.getInstance().getGson().fromJson(result,OperateFriendRequestModel.class);
        String msg = operateFriendRequestModel.getMsg();
        if (msg != null && msg.equals("success")){
            mSearchRequestView.operateResult(sendUserId);
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


    //只管初始化Recycler，传入空的requestList
    @Override
    public void start() {
        List<FriendRequest> requestList = new ArrayList<>();
        mSearchRequestView.initRecycler(requestList);

    }
}
