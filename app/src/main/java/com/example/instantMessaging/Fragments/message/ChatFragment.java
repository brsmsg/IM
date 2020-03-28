package com.example.instantMessaging.Fragments.message;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.common.app.Fragment;
import com.example.factory.presenter.Chat.ChatContract;
import com.example.instantMessaging.Activities.MessageActivity;
import com.example.instantMessaging.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author brsmsg
 * @time 2020/3/21
 */
public class ChatFragment extends Fragment implements ChatContract.View {

    private String mContactName;

    private String mPortrait;

    @BindView(R.id.txt_contact)
    TextView contactName;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mContactName = bundle.getString(MessageActivity.KEY_USERNAME);
        mPortrait = bundle.getString(MessageActivity.KEY_PORTRAIT_URL);
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        contactName.setText(mContactName);
    }

    @Override
    public void setPresenter(ChatContract.Presenter presenter) {

    }

    @Override
    public void showError(int string) {

    }

    /**
     *
     */
    @OnClick(R.id.img_return)
    void goBack(){
        getActivity().finish();
    }



}
