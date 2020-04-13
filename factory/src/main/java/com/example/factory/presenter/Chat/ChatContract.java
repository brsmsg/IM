package com.example.factory.presenter.Chat;

import android.content.Context;

import com.example.common.factory.base.BaseContract;
import com.example.common.factory.base.BasePresenter;
import com.example.common.factory.base.BaseView;
import com.example.factory.model.MsgUI;
import com.example.factory.model.RawMotion;

import java.util.List;

/**
 * @author brsmsg
 * @time 2020/3/25
 */
public interface ChatContract extends BaseContract {
    interface View extends BaseView<Presenter>{
        //初始化ui
        void initUI(List<MsgUI> msgList);

        //刷新消息
        void refreshUI(MsgUI item);

        List<RawMotion> getRawMotionList();

        void clearMotionList();

        //判断是否是本人
        public boolean classify(List<String> resultIdList);

        void encryptMsg();
    }

    interface Presenter extends BasePresenter{
        //发送消息
        void sendMessage(String content, String myPortrait, String myId, String oppositeId, String publicKey);

        //接受消息
        void receiveMessage(String content, String oppositePortrait);

        //更新会话界面
        void updateSession(Context context, String oppositeId, String lastMsg);

        //移除点击事件
        List<RawMotion> removeClickEvent(List<RawMotion> rawMotionList);

        //判断准确率
        public boolean getPrecision(String myId, List<String> resultIdList);
    }
}
