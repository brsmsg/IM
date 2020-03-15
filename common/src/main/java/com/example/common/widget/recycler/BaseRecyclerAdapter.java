package com.example.common.widget.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * @author brsmsg
 * @time 2020/3/14
 */
public abstract class BaseRecyclerAdapter<Data>
        extends RecyclerView.Adapter<BaseRecyclerAdapter.ViewHolder<Data>> {

    private final List<Data> mDataList;

    private final int layout_id;

    protected BaseRecyclerAdapter(List<Data> mDataList, int layout_id) {
        this.mDataList = mDataList;
        this.layout_id = layout_id;
    }

    @NonNull
    @Override
    public ViewHolder<Data> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(this.layout_id, parent, false);
        ViewHolder<Data> holder = createViewHolder(view);
        return holder;
    }


    public abstract ViewHolder<Data> createViewHolder(View view);


    @Override
    public void onBindViewHolder(@NonNull ViewHolder<Data> holder, int position) {
        Data data = mDataList.get(position);
        holder.bindData(data);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public abstract static class ViewHolder<Data> extends RecyclerView.ViewHolder{


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public abstract void bindData(Data data);
    }
}
