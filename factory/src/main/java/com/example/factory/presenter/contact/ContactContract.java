package com.example.factory.presenter.contact;

import com.example.common.factory.base.BaseContract;
import com.example.common.factory.base.BasePresenter;
import com.example.common.factory.base.BaseView;
import com.example.factory.model.User;
import com.example.factory.model.db.Contact;

import java.util.List;

/**
 * @author brsmsg
 * @time 2020/3/13
 */
public interface ContactContract extends BaseContract {
    interface View extends BaseView<Presenter>{
        void initContact(List<Contact> contactList);

        void refreshContact(List<Contact> contactList);
    }

    interface Presenter extends BasePresenter{
        void refresh();
    }
}
