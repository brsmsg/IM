package com.example.factory.presenter.Chat;

import android.content.Context;

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
        //发送消息
        void sendMessage(String content, String myPortrait, String myId, String oppositeId, String publicKey);

        //接受消息
        void receiveMessage(String content, String oppositePortrait);

        //更新会话界面
        void updateSession(Context context, String oppositeId, String lastMsg);
    }
}
