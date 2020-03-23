package com.example.factory.presenter.contact;

import android.util.Log;

import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.model.User;
import com.example.factory.model.api.contact.Contact;
import com.example.factory.utils.NetUtils;
import com.google.gson.reflect.TypeToken;

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
        contactView.setPresenter(this);
    }

    @Override
    public void start() {
        User user = new User("1");
//        String result = NetUtils.postJson(user, "123");


        String result = "{\n" +
                "    \"code\": 0,\n" +
                "    \"msg\": \"success\",\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"username\": \"15172382300\",\n" +
                "            \"faceImage\": \"http://101.200.240.107/images/1.jpg\",\n" +
                "            \"description\": 123456\n" +
                "        },\n" +
                "        {\n" +
                "            \"username\": \"18571549924\",\n" +
                "            \"faceImage\": \"http://101.200.240.107/images/2.jpg\",\n" +
                "            \"description\": kbh\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        if(result != null){
            Log.d("result", result);
            parseContactResult(result);
        }else{
            mContactView.showError(R.string.err_service);
        }



    }

    void parseContactResult(String result){
        Contact contact = Factory.getInstance().getGson()
                .fromJson(result, Contact.class);
        List<User> contactList = contact.getData();
        for(User contactitem:contactList){
            Log.d("name", contactitem.getUsername());
            Log.d("image", contactitem.getFaceImage());
        }
        mContactView.initContact(contactList);
    }
}
