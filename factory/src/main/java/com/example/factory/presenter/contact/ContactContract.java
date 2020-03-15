package com.example.factory.presenter.contact;

import com.example.common.factory.base.BaseContract;
import com.example.common.factory.base.BasePresenter;
import com.example.common.factory.base.BaseView;
import com.example.factory.model.User;

import java.util.List;

/**
 * @author brsmsg
 * @time 2020/3/13
 */
public interface ContactContract extends BaseContract {
    interface View extends BaseView<Presenter>{
        void initContact(List<User> userList);
    }

    interface Presenter extends BasePresenter{

    }
}
