package com.example.common.widget.recycler;

/**
 * @author brsmsg
 * @time 2020/3/13
 */
public interface AdapterCallback<Data> {
    void update(Data data, RecyclerAdapter.ViewHolder<Data> holder);
}
