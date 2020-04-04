package com.example.factory.presenter.Session;

import android.util.Log;

import com.dbflow5.config.FlowManager;
import com.dbflow5.query.SQLite;
import com.example.factory.model.SessionUI;
import com.example.factory.model.api.webSocket.Msg;
import com.example.factory.model.api.webSocket.WebSocketModel;
import com.example.factory.model.db.AppDatabase;
import com.example.factory.model.db.Contact;
import com.example.factory.model.db.Contact_Table;
import com.example.factory.utils.webSocket.WebSocketUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author brsmsg
 * @time 2020/4/4
 */
public class SessionPresenter implements SessionContract.Presenter {

    private SessionContract.View mSessionView;

    public SessionPresenter(SessionContract.View sessionView){
        mSessionView = sessionView;
        mSessionView.setPresenter(this);
    }

    @Override
    public void start() {
        List<SessionUI> sessionList = new ArrayList<>();
        mSessionView.initRecycler(sessionList);
    }

    @Override
    public void receiveMessage(String content){
        WebSocketModel model =  WebSocketUtils.getMessage(content);
        Msg msg = model.getMessage();
        String myId = msg.getReceiveUserId();
        String oppositeId = msg.getSendUserId();
        String msgContent = msg.getMsg();

        //根据id查询其他信息数据
        Contact contact = SQLite.select()
                .from(Contact.class)
                .where(Contact_Table.id.is(oppositeId))
                .querySingle(FlowManager.getDatabase(AppDatabase.class));

        if(contact != null) {
            String portrait = contact.getFaceImage();
            String username = contact.getUsername();
            String id = contact.getId();
            Log.d("SessionPortrait", portrait);
            Log.d("SessionUsername", username);


            SessionUI session = new SessionUI(id, portrait, username, msgContent);
            mSessionView.refreshUI(session);
        }
    }


}
