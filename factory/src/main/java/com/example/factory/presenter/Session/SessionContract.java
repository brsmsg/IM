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

        void refreshMsg(String id, String content);

        void conflict();
    }

    interface Presenter extends BasePresenter{
        //接收消息
        void receiveMessage(String content);
        //签收消息
        void signMessage(String id);
        //更新session消息
        void updateDecryptedMsg(String id, String content);
    }
}
