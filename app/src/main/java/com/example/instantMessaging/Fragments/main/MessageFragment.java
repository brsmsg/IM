package com.example.instantMessaging.Fragments.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.app.Fragment;
import com.example.factory.model.SessionUI;
import com.example.factory.model.db.Contact;
import com.example.factory.presenter.Session.SessionContract;
import com.example.instantMessaging.Activities.MainActivity;
import com.example.instantMessaging.Activities.MessageActivity;
import com.example.instantMessaging.Fragments.main.adapter.ContactRecyclerAdapter;
import com.example.instantMessaging.Fragments.main.adapter.SessionRecyclerAdapter;
import com.example.instantMessaging.R;

import java.util.List;
import java.util.Objects;
import java.util.zip.Inflater;

import butterknife.BindView;

/**
 * @author brsmsg
 * @time 2020/3/12
 */
public class MessageFragment extends Fragment implements SessionContract.View {

    private MyReceiver mReceiver;

    private SessionRecyclerAdapter mSessionAdapter;

    private SessionContract.Presenter mPresenter;

    private String myId;
    private String myPortrait;

    @BindView(R.id.recycler_session)
    RecyclerView mRecycler;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initData() {
        super.initData();
        //注册广播
        mReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.broadcast.MESSAGE");
        Objects.requireNonNull(getActivity()).registerReceiver(mReceiver, filter);

        mPresenter.start();
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        myId = bundle.getString(MainActivity.MY_ID);
        myPortrait = bundle.getString(MainActivity.MY_PORTRAIT);
    }

    @Override
    public void setPresenter(SessionContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showError(int string) {

    }

    @Override
    public void initRecycler(List<SessionUI> sessionList){
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mSessionAdapter = new SessionRecyclerAdapter(sessionList, getContext());
        mSessionAdapter.setOnItemClickLIstener(new SessionRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, SessionUI session) {
                //展示MessageActivity
                MessageActivity.show(getActivity(), session, myId, myPortrait);
                //签收消息
                mPresenter.signMessage(session.getId());
            }
        });


        mRecycler.setAdapter(mSessionAdapter);
    }

    @Override
    public void refreshUI(SessionUI session){
        mSessionAdapter.add(session);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getActivity()).unregisterReceiver(mReceiver);
    }



    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getExtras().getString("MSG");

            mPresenter.receiveMessage(msg);

        }
    }

}
