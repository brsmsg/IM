package com.example.instantMessaging.Fragments.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.app.Fragment;
import com.example.factory.model.FriendRequest;
import com.example.factory.model.User;
import com.example.factory.presenter.Friend.SearchRequestContract;
import com.example.instantMessaging.Activities.MainActivity;
import com.example.instantMessaging.Fragments.main.adapter.SearchRecyclerAdapter;
import com.example.instantMessaging.R;

import java.util.List;

import butterknife.BindView;

/**
 * @author brsmsg
 * @time 2020/3/12
 */
public class SearchFragment extends Fragment implements SearchRequestContract.View {
    //接收或拒绝好友请求
    private final static int ACCEPT = 1;
    private final static int REFUSE = 2;

    //调取处理逻辑
    private SearchRequestContract.Presenter mPresenter;

    //绑定RecyclerView布局
    @BindView(R.id.recycler_search)
    RecyclerView mRecycler;

    //引入适配器
    private SearchRecyclerAdapter mSearchAdapter;

    private String myId;

    //空构造函数
    public SearchFragment() {
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search;
    }

    //数据初始化,fragment实例创建时调用
    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
        //查询接收到的好友请求，包含解析返回的好友请求parseRequestResult，再调用refreshUI
        mPresenter.searchRequest(myId);


    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        //从主活动获取当前用户id信息，用户发送接收还是拒绝好友请求
        myId = bundle.getString(MainActivity.MY_ID);
    }


    //传递requestList，初始化RecyclerView，adapter内部有点击事件
    @Override
    public void initRecycler(List<FriendRequest> requestList) {
        //设置线性布局
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        //adapter实例化
        mSearchAdapter = new SearchRecyclerAdapter(requestList,getContext());
        //布局设置adapter
        mRecycler.setAdapter(mSearchAdapter);
        //设置item中的点击事件
        mSearchAdapter.setOnItemClickListener(mItemClickListener);

    }

    //刷新好友请求列表
    @Override
    public void refreshRecycler(List<FriendRequest> requestList) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("refreshRecycler",requestList.get(2).getSendUserId());
                mSearchAdapter.addData(requestList);
            }
        });
    }

    //根据处理好友请求后返回的结果进行处理
    @Override
    public void operateResult(String sendUserId) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSearchAdapter.removeData(sendUserId);
            }
        });

    }

    //点击事件的实现，处理好友请求
    private SearchRecyclerAdapter.OnItemClickListener mItemClickListener = new SearchRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            switch(view.getId()){
                case R.id.accept_friend:
                    FriendRequest friendRequestAccept = mSearchAdapter.mRequestList.get(position);
                    friendId = friendRequestAccept.getSendUserId();
                    operateType = ACCEPT;
                    Toast.makeText(view.getContext(),"接受好友请求",Toast.LENGTH_SHORT).show();
                    //处理好友请求
                    mPresenter.operateRequest(myId,friendId,operateType);
/*                    //删除处理完的数据
                    mSearchAdapter.removeData(position);*/
                    break;

                case R.id.refuse_friend:
                    FriendRequest friendRequestRefuse = mSearchAdapter.mRequestList.get(position);
                    friendId = friendRequestRefuse.getSendUserId();
                    operateType = REFUSE;
                    Toast.makeText(view.getContext(),"拒绝好友请求",Toast.LENGTH_SHORT).show();
                    mPresenter.operateRequest(myId,friendId,operateType);
/*                    mSearchAdapter.removeData(position);*/
                    break;
                default:
                    break;
            }
        }
    };

    //存储是接收还是拒绝
    private int operateType;
    //存储发出好友请求的好友id
    private String friendId;


    //搜索好友成功
    @Override
    public void searchSuccess(User data) {

    }

    //输出返回值中的错误信息
    @Override
    public void showError(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();

    }

    //发送好友请求成功
    @Override
    public void sendRequestSuccess(String msg) {

    }

    @Override
    public void setPresenter(SearchRequestContract.Presenter presenter) {
        this.mPresenter = presenter;

    }

    //输出客户端给出的错误信息
    @Override
    public void showError(int string) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(),string,Toast.LENGTH_SHORT).show();
            }
        });

    }
}
