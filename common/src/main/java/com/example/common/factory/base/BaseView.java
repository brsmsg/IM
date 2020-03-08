package com.example.common.factory.base;

/**
 * @author brsmsg
 * @time 2020/3/7
 */
public interface BaseView<T> {
    //view中设置presenter，constructor中调用
    void setPresenter(T presenter);

    //显示字符串错误
    void StrError(int string);
}
