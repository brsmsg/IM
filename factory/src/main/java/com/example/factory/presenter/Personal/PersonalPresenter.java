package com.example.factory.presenter.Personal;

import com.example.factory.Factory;
import com.example.factory.model.User;
import com.example.factory.model.api.account.PersonalModel;
import com.example.factory.model.api.account.PersonalModel;

/**
 * The type Personal presenter.
 */
public class PersonalPresenter implements PersonalContract.Presenter {
    //用户信息更改的方法实现

    private User user;

/*    //绑定View与Presenter
    private PersonalContract.View mPersonalView;
    public PersonalPresenter(PersonalContract.View personalView){
        mPersonalView = personalView;
        mPersonalView.setPresenter(this);
    }*/

    @Override
    public User getUserPersonal() {
        return user;
    }

    //解析服务器返回数据
    @Override
    public void parsePersonalResult(String result) {
        PersonalModel personalModel = Factory.getInstance()
                .getGson().fromJson(result,PersonalModel.class);

        String id = personalModel.getId();
        String username = personalModel.getUserName();
        String portrait = personalModel.getPortrait();

    }

    //拉取用户界面数据
    @Override
    public void start() {

        Factory.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                /*PersonalModel personalModel = new PersonalModel();*/
            }
        });

    }


}
