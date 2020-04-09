package com.example.factory.presenter.contact;

import android.util.Log;

import com.dbflow5.adapter.ModelAdapter;
import com.dbflow5.config.FlowManager;
import com.dbflow5.query.SQLite;
import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.model.api.contact.GetContact;
import com.example.factory.model.db.AppDatabase;
import com.example.factory.model.db.Contact;
import com.example.factory.model.db.Contact_Table;

import java.util.List;

/**
 * @author brsmsg
 * @time 2020/3/13
 */
public class ContactPresenter implements ContactContract.Presenter{

    private ContactContract.View mContactView;

    private String contactUrl = "http://118.31.64.83:8080/friend/getFriend";

    public ContactPresenter(ContactContract.View contactView ){
        mContactView = contactView;
        mContactView.setPresenter(this);
    }

    @Override
    public void start() {
//        Contact contact = new Contact();
//        contact.setId("1");
//        contact.setDescription("123");
//        contact.setFaceImage("http://101.200.240.107/images/2.jpg");
//        contact.setUsername("kbh");
//        contact.save(FlowManager.getDatabase(AppDatabase.class));
//
//        Contact contact2 = new Contact();
//        contact2.setId("2");
//        contact2.setDescription("1234");
//        contact2.setFaceImage("http://101.200.240.107/images/2.jpg");
//        contact2.setUsername("KBH");
//        contact2.save(FlowManager.getDatabase(AppDatabase.class));

        //从数据库拿数据
        List<Contact> contactList = SQLite.select()
                .from(Contact.class)
                .queryList(FlowManager.getDatabase(AppDatabase.class));

        for(Contact c:contactList){
            Log.d("database Contact", c.toString());
        }

        mContactView.initContact(contactList);
    }

    /**
     * 从网络拿数据并更新
     */
    public void refresh(){
        //        String result = NetUtils.postJson(user, contactUrl);
        String result = "{\n" +
                "    \"code\": 0,\n" +
                "    \"msg\": \"success\",\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"id\": \"brsmsg_1586334335390388232\", \n"+
                "            \"username\": \"15172382300\",\n" +
                "            \"faceImage\": \"http://101.200.240.107/images/1.jpg\",\n" +
                "            \"description\": 123456\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"brsmsg_1585897820995103737\", \n"+
                "            \"username\": \"18571549924\",\n" +
                "            \"faceImage\": \"http://101.200.240.107/images/2.jpg\",\n" +
                "            \"description\": kbh\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        if(result != null){
//            Log.d("result", result);
            parseContactResult(result);
        }else{
            mContactView.showError(R.string.err_service);
        }
    }

    void parseContactResult(String result){
        GetContact getContact = Factory.getInstance().getGson()
                .fromJson(result, GetContact.class);
        List<Contact> newContactList = getContact.getData();
//        for(Contact contactitem:newContactList){
//            Log.d("name", contactitem.getUsername());
//            Log.d("image", contactitem.getFaceImage());
//        }
        mContactView.refreshContact(newContactList);

        //本地数据库更新
        ModelAdapter<Contact> adapter = FlowManager.getModelAdapter(Contact.class);
        adapter.saveAll(newContactList, FlowManager.getDatabase(AppDatabase.class));

        List<Contact> contactList = SQLite.select()
                .from(Contact.class)
                .queryList(FlowManager.getDatabase(AppDatabase.class));

//        for(Contact c:contactList){
//            Log.d("after save", c.toString());
//        }
    }

}