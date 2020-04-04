package com.example.factory.presenter.Chat;

import com.example.common.factory.base.BaseContract;
import com.example.common.factory.base.BasePresenter;
import com.example.common.factory.base.BaseView;
import com.example.factory.model.MsgUI;

import java.util.List;

/**
 * @author brsmsg
 * @time 2020/3/25
 */
public interface ChatContract extends BaseContract {
    interface View extends BaseView<Presenter>{
        void initUI(List<MsgUI> msgList);

        void refreshUI(MsgUI item);

    }

    interface Presenter extends BasePresenter{
        void sendMessage(String content, String myPortrait, String myId, String oppositeId);

        void receiveMessage(String content, String oppositePortrait);
    }
}
