package com.example.instantMessaging.Fragments.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.app.Fragment;
import com.example.common.widget.recycler.RecyclerAdapter;
import com.example.factory.model.User;
import com.example.factory.model.db.Contact;
import com.example.factory.presenter.contact.ContactContract;
import com.example.instantMessaging.Activities.MainActivity;
import com.example.instantMessaging.Activities.MessageActivity;
import com.example.instantMessaging.Fragments.main.adapter.ContactRecyclerAdapter;
import com.example.instantMessaging.R;
import com.xuexiang.xui.widget.picker.wheelview.WheelView;

import java.util.List;

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

    //empty constructor
    public ContactFragment(){

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_contact;
    }


    @Override
    protected void initData() {
        super.initData();
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
        mRecycler.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
    }

    @Override
    public void refreshContact(List<Contact> contactList) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mContactAdapter.replace(contactList);
            }
        });

    }


}
