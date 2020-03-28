package com.example.instantMessaging.Fragments.main;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.app.Fragment;
import com.example.common.widget.recycler.RecyclerAdapter;
import com.example.factory.model.User;
import com.example.factory.presenter.contact.ContactContract;
import com.example.instantMessaging.Activities.MessageActivity;
import com.example.instantMessaging.Fragments.main.adapter.ContactRecyclerAdapter;
import com.example.instantMessaging.R;

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

    private RecyclerAdapter<User> mContactAdapter;

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
    }

    @Override
    public void setPresenter(ContactContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showError(int string) {

    }

    @Override
    public void initContact(List<User> userList) {
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        ContactRecyclerAdapter adapter = new ContactRecyclerAdapter(getContext(), userList);
        //添加点击事件
        adapter.setOnItemClickListener(new ContactRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, User user) {
                Toast.makeText(getActivity(), user.getUsername(), Toast.LENGTH_SHORT).show();
                MessageActivity.show(getActivity(), user);
            }
        });
        mRecycler.setAdapter(adapter);
    }



}
