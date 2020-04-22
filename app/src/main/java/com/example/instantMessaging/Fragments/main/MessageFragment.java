package com.example.instantMessaging.Fragments.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dbflow5.config.FlowManager;
import com.dbflow5.query.SQLite;
import com.example.common.RSA.RsaEncryptUtil;
import com.example.common.app.Fragment;
import com.example.factory.model.SessionUI;
import com.example.factory.model.db.Contact;
import com.example.factory.model.db.Contact_Table;
import com.example.factory.model.db.MyAppDB;
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
import butterknife.OnClick;

/**
 * @author brsmsg
 * @time 2020/3/12
 */
public class MessageFragment extends Fragment implements SessionContract.View {

    private MyReceiver mReceiver;
    private DecryptedReceiver mDecryptedReceiver;

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

        //注册更新最新消息的广播
        mDecryptedReceiver = new DecryptedReceiver();
        IntentFilter decryptedFilter = new IntentFilter();
        decryptedFilter.addAction("com.example.broadcast.UPDATE_SESSION");
        getActivity().registerReceiver(mDecryptedReceiver, decryptedFilter);




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
        mSessionAdapter.setOnItemClickLIstener((view, session) -> {
            //展示MessageActivity
            MessageActivity.show(getActivity(), session, myId, myPortrait);
            Log.d("publicKey", session.getPublicKey());

            //签收消息
            mPresenter.signMessage(session.getId());
        });

        mRecycler.setAdapter(mSessionAdapter);
    }

    @Override
    public void refreshUI(SessionUI session){
        mSessionAdapter.add(session);
    }

    @Override
    public void refreshMsg(String id, String content) {
        mSessionAdapter.refresh(id, content);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getActivity()).unregisterReceiver(mReceiver);
        getActivity().unregisterReceiver(mDecryptedReceiver);
    }



    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getExtras().getString("MSG");
            mPresenter.receiveMessage(msg);
        }
    }


    class DecryptedReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            String id = intent.getExtras().getString("ID");
            String content = intent.getExtras().getString("LAST_MSG");
//            String publicKey = intent.getExtras().getString("PUBLIC_KEY");
            String action = intent.getExtras().getString("ACTION");


            //根据id查询其他信息数据
            Contact contact = SQLite.select()
                    .from(Contact.class)
                    .where(Contact_Table.id.is(id))
                    .querySingle(FlowManager.getDatabase(MyAppDB.class));

            String portrait = "";
            String username = "";
            String publicKey = "";
            if(contact != null){
                portrait = contact.getFaceImage();
                username = contact.getUsername();
                publicKey = contact.getPublicKey();

            }
            switch (action){
                //发送消息更新
                case "send":
                    SessionUI session = new SessionUI(id, portrait, username, content, publicKey);
                    //UI更新
                    refreshUI(session);
                    break;
                //加密解密更新
                case "encrypt":
                case "decrypt":
                    mPresenter.updateDecryptedMsg(id, content);
                    break;
            }

//            mPresenter.updateDecryptedMsg(id, content);
        }

    }
}
