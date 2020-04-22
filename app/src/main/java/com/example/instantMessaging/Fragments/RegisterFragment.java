package com.example.instantMessaging.Fragments;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.common.app.Fragment;
import com.example.common.app.Mapper;
import com.example.factory.presenter.account.RegisterContract;
import com.example.factory.utils.SpUtils;
import com.example.instantMessaging.Activities.BehaviorActivity;
import com.example.instantMessaging.R;

import butterknife.BindView;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author brsmsg
 * @time 2020/3/10
 */
public class RegisterFragment extends Fragment implements RegisterContract.View {

    private RegisterContract.Presenter mPresenter;

    private FragmentTrigger mTrigger;

    @BindView(R.id.reg_edit_username)
    EditText mRegUserName;

    @BindView(R.id.reg_edit_password)
    EditText mRegPassword;

    @BindView(R.id.btn_register)
    Button mRegister;

    public RegisterFragment(){ }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mTrigger = (FragmentTrigger) context;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        //初始化控件时让注册按钮不可用
//        mRegister.setEnabled(false);
    }

    @Override
    public void setPresenter(RegisterContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }


    @Override
    public void showError(int string) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), getString(string), Toast.LENGTH_SHORT).show();

                //登录控件可以重新使用
                mRegUserName.setEnabled(true);
                mRegPassword.setEnabled(true);
            }
        });


    }

    /**
     * 点击注册按钮
     */
    @OnClick(R.id.btn_register)
    public void register(){
        String regUserName = mRegUserName.getText().toString().trim();
        String regPassword = mRegPassword.getText().toString().trim();
        mPresenter.register(regUserName, regPassword);
    }

//    /**
//     * 点击上传行为按钮
//     */
//    @OnClick(R.id.btn_behavior)
//    public void recordBehavior(){
//
//        BehaviorActivity.show(getActivity());
//        mRegister.setEnabled(true);
//    }

    /**
     * 注册成功返回登录界面
     */
    @Override
    public void registerSuccess(String id) {
        getActivity().runOnUiThread(() -> {
            Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT).show();
            BehaviorActivity.show(getActivity(), id, mRegUserName.getText().toString().trim(), mRegPassword.getText().toString().trim(), "register");
            mTrigger.changeFragment();
        });

    }
}
