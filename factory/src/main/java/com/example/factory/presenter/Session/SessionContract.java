package com.example.factory.presenter.Session;

import com.example.common.factory.base.BaseContract;
import com.example.common.factory.base.BasePresenter;
import com.example.common.factory.base.BaseView;
import com.example.factory.model.SessionUI;

import java.util.List;

/**
 * @author brsmsg
 * @time 2020/4/4
 */
public interface SessionContract extends BaseContract {
    interface View extends BaseView<Presenter>{
        void initRecycler(List<SessionUI> sessionList);

        void refreshUI(SessionUI session);
    }

    interface Presenter extends BasePresenter{
        void receiveMessage(String content);
    }
}
