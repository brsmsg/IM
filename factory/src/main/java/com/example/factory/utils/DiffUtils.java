package com.example.factory.utils;

import android.util.Log;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

/**
 * @author brsmsg
 * @time 2020/3/30
 */
public class DiffUtils<T extends DiffUtils.Differ<T>> extends DiffUtil.Callback {
    private List<T> mOldList, mNewList;

    public DiffUtils(List<T> mOldList, List<T> mNewList) {
        this.mOldList = mOldList;
        this.mNewList = mNewList;
    }

    @Override
    public int getOldListSize() {
        //旧的数据大小
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        //新的数据大小
        return mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        //是否是同一个项目，用id判断
        T itemOld = mOldList.get(oldItemPosition);
        T itemNew = mNewList.get(newItemPosition);

        return itemNew.isSame(itemOld);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        //相等判断后判断是否有数据更改
        T itemOld = mOldList.get(oldItemPosition);
        T itemNew = mNewList.get(newItemPosition);

        return itemNew.isContentSame(itemOld);
    }

    //和自己的类型进行比较
    public interface Differ<T>{
        //是否同一个数据
        boolean isSame(T old);
        //是否内容相同
        boolean isContentSame(T old);
    }

}
