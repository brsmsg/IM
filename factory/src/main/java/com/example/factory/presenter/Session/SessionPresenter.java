package com.example.factory.presenter.Session;

import android.content.Context;
import android.util.Log;

import com.dbflow5.config.FlowManager;
import com.dbflow5.query.SQLite;
import com.example.common.RSA.RsaEncryptUtil;
import com.example.common.app.Mapper;
import com.example.factory.model.SessionUI;
import com.example.factory.model.api.webSocket.Msg;
import com.example.factory.model.api.webSocket.WebSocketModel;
import com.example.factory.model.db.Contact;
import com.example.factory.model.db.Contact_Table;
import com.example.factory.model.db.MyAppDB;
import com.example.factory.utils.SpUtils;
import com.example.factory.utils.webSocket.WebSocketUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author brsmsg
 * @time 2020/4/4
 */
public class SessionPresenter implements SessionContract.Presenter {

    private SessionContract.View mSessionView;
    //id 和 msgId 的key-value
    private Map<String, List<String>> msgMap = new HashMap<>();
    private List<String> msgIdList;
    private Context mContext;

    public SessionPresenter(SessionContract.View sessionView, Context context){
        mSessionView = sessionView;
        mSessionView.setPresenter(this);
        mContext = context;
    }

    @Override
    public void start() {
        List<SessionUI> sessionList = new ArrayList<>();
        mSessionView.initRecycler(sessionList);
    }

    @Override
    public void receiveMessage(String content){
        WebSocketModel model =  WebSocketUtils.getMessage(content);
        //强制下线
        if(model.getAction() == 7){
            mSessionView.conflict();
        }

        if(model.getAction() != 2 && model.getAction() != 6){
            return;
        }
        //消息内容
        Msg msg = model.getMessage();

        String myId = msg.getReceiveUserId();
        String oppositeId = msg.getSendUserId();
        String msgContent = msg.getMsg();
        String msgId = msg.getMsgId();
        //根据id查询其他信息数据
        Contact contact = SQLite.select()
                .from(Contact.class)
                .where(Contact_Table.id.is(oppositeId))
                .querySingle(FlowManager.getDatabase(MyAppDB.class));

        if(contact != null) {
            String portrait = contact.getFaceImage();
            String username = contact.getUsername();
            String id = contact.getId();
            String publicKey = contact.getPublicKey();

            SessionUI session = null;

            if(model.getAction() == 2){
                try {
                    session = new SessionUI(id, portrait, username,
                            RsaEncryptUtil.decrypt(msgContent, (String)SpUtils.getData(mContext, Mapper.SP_PRIVATE_KEY, "")),
                            publicKey);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(model.getAction() == 6){
                session = new SessionUI(id, portrait, username, msgContent, publicKey);
            }

            //未读消息的HashMap更新
            if(msgMap.get(id) == null){
                msgIdList = new ArrayList<>();
                msgIdList.add(msgId);
                msgMap.put(id, msgIdList);
            }else{
                msgIdList = msgMap.get(id);
                msgIdList.add(msgId);
                msgMap.put(id, msgIdList);
            }


            //UI 更新
            mSessionView.refreshUI(session);

        }
    }

    @Override
    public void signMessage(String id) {
        msgIdList = msgMap.get(id);
        StringBuilder msgIdStr = new StringBuilder();
        //没有需要签收消息
        if(msgIdList == null ){
            return;
        }
        //有消息待签收
        for(String msgId:msgIdList){
            if(msgIdStr.toString().equals("")){
                msgIdStr.append(msgId);
            }else{
                msgIdStr.append(",")
                        .append(msgId);
            }
        }
        Log.d("msgIdList", msgIdStr.toString());

        WebSocketUtils.sign(msgIdStr.toString());
    }

    @Override
    public void updateDecryptedMsg(String id, String content) {
        mSessionView.refreshMsg(id, content);
    }
}
