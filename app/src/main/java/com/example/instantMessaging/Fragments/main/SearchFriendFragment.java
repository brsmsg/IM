package com.example.instantMessaging.Fragments.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.app.Fragment;
import com.example.factory.model.User;
import com.example.factory.model.db.Contact;
import com.example.factory.presenter.Friend.SearchFriendContract;
import com.example.instantMessaging.Activities.MainActivity;
import com.example.instantMessaging.Fragments.main.adapter.SearchFriendRecyclerAdapter;
import com.example.instantMessaging.Fragments.main.adapter.SessionRecyclerAdapter;
import com.example.instantMessaging.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author brsmsg
 * @time 2020/4/18
 */
public class SearchFriendFragment extends Fragment implements SearchFriendContract.View {

    private SearchFriendContract.Presenter mPresenter;

    private SearchFriendRecyclerAdapter mAdapter;

    private String myId;

    @BindView(R.id.btn_search_friend)
    ImageView mSearchFriend;

    @BindView(R.id.edit_search_friend)
    EditText mSearchUsername;

    @BindView(R.id.recycler_search_friend)
    RecyclerView mRecycler;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_friend;
    }


    @Override
    public void setPresenter(SearchFriendContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        myId = bundle.getString(MainActivity.MY_ID);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }

    @Override
    public void showError(int string) {

    }

    @Override
    public void initRecycler(){
        List<User> userList = new ArrayList<>();
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new SearchFriendRecyclerAdapter(getContext(), userList);
        mAdapter.setOnItemClickListener(new SearchFriendRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, User user) {
                //点击添加好友
                mPresenter.sendFriendRequest(myId, user.getUsername());
            }
        });
        mRecycler.setAdapter(mAdapter);
        mRecycler.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()),DividerItemDecoration.VERTICAL));
    }

    @Override
    public void searchSuccess(User data) {
        //recycler添加
        getActivity().runOnUiThread(() -> mAdapter.add(data));
        //重置editText
        mSearchUsername.setText("");
    }

    /**
     * 添加好友请求成功
     * @param msg
     */
    @Override
    public void sendRequestSuccess(String msg) {
        getActivity().runOnUiThread(() -> {
            Toast.makeText(getActivity(), "请求发送成功", Toast.LENGTH_SHORT).show();
        });
    }



    @Override
    public void showError(String message) {
        getActivity().runOnUiThread(()->{
            if (message!=null){
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

            }
        });
    }



    @OnClick(R.id.btn_search_friend)
    void search(){
        String username = mSearchUsername.getText().toString().trim();
        mPresenter.searchFriend(myId, username);
    }

    //重写父类fragment的方法,返回值true表示拦截处理返回键
    @Override
    public boolean onBackPress() {
        super.onBackPress();
        getFragmentManager().popBackStack();
        
        return true;
    }
}
