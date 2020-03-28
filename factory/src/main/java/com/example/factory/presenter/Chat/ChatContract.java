package com.example.factory.presenter.Chat;

import com.example.common.factory.base.BaseContract;
import com.example.common.factory.base.BasePresenter;
import com.example.common.factory.base.BaseView;

/**
 * @author brsmsg
 * @time 2020/3/25
 */
public interface ChatContract extends BaseContract {
    interface View extends BaseView<Presenter>{

    }

    interface Presenter extends BasePresenter{

    }
}
