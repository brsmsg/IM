package com.example.instantMessaging.Fragments.main;

import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.app.Fragment;
import com.example.factory.model.FriendRequest;
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



    //调取处理逻辑
    private SearchRequestContract.Presenter mPresenter;

    //绑定RecyclerView布局
    @BindView(R.id.recycler_search)
    RecyclerView mRecycler;

    //引入适配器
    private SearchRecyclerAdapter mSearchAdapter;

    private String myId;


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search;
    }

    //数据初始化,由Presenter补充实现
    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        //从主活动获取当前用户id信息，用户发送接收还是拒绝好友请求
        myId = bundle.getString(MainActivity.MY_ID);
    }

    @Override
    public void operateSuccess(String msg) {

    }

    //初始化RecyclerView，adapter内部有点击事件
    @Override
    public void initRecycler(List<FriendRequest> requestList) {
        //设置线性布局
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        //adapter实例化
        mSearchAdapter = new SearchRecyclerAdapter(requestList,getContext());
        //布局设置适配器
        mRecycler.setAdapter(mSearchAdapter);

    }

    @Override
    public void refreshUi(FriendRequest friendRequest) {
        mSearchAdapter.addData(friendRequest);

    }

    @Override
    public void setPresenter(SearchRequestContract.Presenter presenter) {
        this.mPresenter = presenter;

    }

    @Override
    public void showError(int string) {

        Toast.makeText(this.getContext(),string,Toast.LENGTH_SHORT).show();

    }
}
