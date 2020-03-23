package com.example.factory.presenter.contact;

import android.util.Log;

import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.model.User;
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


        String result = "[{\"username\": \"KBH\",\"faceImage\": \"http://101.200.240.107/images/1.jpg\", \"description\": \"hahahahahaha\"}, " +
                "{\"username\": \"KangBaihan\", \"faceImage\": \"http://101.200.240.107/images/2.jpg\", \"description\": \"happyeveryday\"}]";
        if(result != null){
            parseContactResult(result);
        }else{
            mContactView.showError(R.string.err_service);
        }



    }

    void parseContactResult(String result){
        List<User> contactList = Factory.getInstance().getGson()
                .fromJson(result, new TypeToken<List<User>>(){}.getType());
        for(User contact: contactList){
//            Log.d("Contact", "userName： " + contact.getUserName());
//            Log.d("Contact", "portrait： " + contact.getPortrait());
//            Log.d("Contact", "desc： " + contact.getDesc());

        }
        mContactView.initContact(contactList);
    }
}
