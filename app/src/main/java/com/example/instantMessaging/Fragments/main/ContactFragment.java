package com.example.instantMessaging.Fragments.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.app.Fragment;
import com.example.common.widget.recycler.RecyclerAdapter;
import com.example.factory.model.User;
import com.example.factory.model.api.webSocket.WebSocketModel;
import com.example.factory.model.db.Contact;
import com.example.factory.presenter.contact.ContactContract;
import com.example.factory.utils.webSocket.WebSocketUtils;
import com.example.instantMessaging.Activities.MainActivity;
import com.example.instantMessaging.Activities.MessageActivity;
import com.example.instantMessaging.Fragments.main.adapter.ContactRecyclerAdapter;
import com.example.instantMessaging.Fragments.message.ChatFragment;
import com.example.instantMessaging.R;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;

import static com.google.common.base.Preconditions.*;

/**
 * @author brsmsg
 * @time 2020/3/12
 */
public class ContactFragment extends Fragment implements ContactContract.View{

    private ContactContract.Presenter mPresenter;

    @BindView(R.id.recycler_contact)
    RecyclerView mRecycler;

    private ContactRecyclerAdapter mContactAdapter;

    private String myId;
    private String myPortrait;

    private RefreshReceiver mReceiver;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_contact;
    }


    @Override
    protected void initData() {
        super.initData();

        //动态注册广播
        mReceiver = new RefreshReceiver();
        IntentFilter intentFilter = new IntentFilter();
        //设置广播类型
        intentFilter.addAction("com.example.broadcast.MESSAGE");
        Objects.requireNonNull(getActivity()).registerReceiver(mReceiver, intentFilter);

        mPresenter.start();
        mPresenter.refresh(myId);
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        myId = bundle.getString(MainActivity.MY_ID);
        myPortrait = bundle.getString(MainActivity.MY_PORTRAIT);

    }

    @Override
    public void setPresenter(ContactContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showError(int string) {

    }

    /**
     * 初始化联系人列表
     * @param contactList
     */
    @Override
    public void initContact(List<Contact> contactList) {
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mContactAdapter = new ContactRecyclerAdapter(getContext(), contactList);
        //添加点击事件
        mContactAdapter.setOnItemClickListener(new ContactRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Contact contact) {
                MessageActivity.show(getActivity(), contact, myId, myPortrait);
                Toast.makeText(getActivity(), contact.getUsername(), Toast.LENGTH_SHORT).show();
            }
        });
        mRecycler.setAdapter(mContactAdapter);
    }

    @Override
    public void refreshContact(List<Contact> contactList) {
        getActivity().runOnUiThread(() -> mContactAdapter.replace(contactList));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {  //不在最前端界面显示

        } else {  //重新显示到最前端刷新
            mPresenter.refresh(myId);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getActivity()).unregisterReceiver(mReceiver);
    }

    class RefreshReceiver extends BroadcastReceiver{



        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getExtras().getString("MSG");
            WebSocketModel model =  WebSocketUtils.getMessage(msg);
            int action = model.getAction();
            if(action == 5){
                mPresenter.refresh(myId);
            }
        }

    }


}
