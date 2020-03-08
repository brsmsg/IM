package com.example.common.factory.base;

/**
 * @author brsmsg
 * @time 2020/3/7
 */
public interface BaseContract {

    interface View extends BaseView<Presenter>{}


    interface Presenter extends BasePresenter{}
}
