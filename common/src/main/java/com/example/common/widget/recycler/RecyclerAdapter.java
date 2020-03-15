package com.example.common.widget.recycler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author brsmsg
 * @time 2020/3/12
 */
public abstract class RecyclerAdapter<Data>
        extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>>
        implements View.OnClickListener, View.OnLongClickListener, AdapterCallback<Data>{

    private List<Data> mDataList;

    private AdapterListener mListener;

    /**
     * 构造函数模块
     */
    public RecyclerAdapter() {
        this(null);
    }

    public RecyclerAdapter(AdapterListener<Data> listener) {
        this(new ArrayList<Data>(), listener);
    }

    public RecyclerAdapter(List<Data> dataList, AdapterListener<Data> listener) {
        this.mDataList = dataList;
        this.mListener = listener;
    }

    /**
     * 创建一个ViewHolder
     * @param parent RecyclerView
     * @param viewType xml布局id
     * @return viewHolder
     */
    @NonNull
    @Override
    public ViewHolder<Data> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //得到layoutInflater把xml初始化为view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //把xml id为viewType的文件初始化为一个root view
        View root = inflater.inflate(viewType, parent, false);
        //得到viewHolder
        ViewHolder<Data> holder = onCreateViewHolder(root, viewType);

        //设置tag,双向绑定
        root.setTag(R.id.tag_recycler_holder, holder);
        //设置点击事件
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);

        //butterKnife绑定
        holder.mUnbinder = ButterKnife.bind(holder, root);
        //绑定callback
        holder.callback = this;
        return null;
    }


    protected abstract ViewHolder<Data> onCreateViewHolder(View root, int viewType);


    /**
     * 绑定数据到holder上
     * @param holder ViewHolder
     * @param position 坐标
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder<Data> holder, int position) {
        Data data = mDataList.get(position);
        //触发绑定方法
        holder.bind(data);
    }

    /**
     * 得到数据量
     */
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 返回整个list
     * @returen List<Data>
     */
    public List<Data> getItem(){
        return mDataList;
    }

    /**
     * 插入一条数据
     */
    public void add(Data data){
        mDataList.add(data);
        notifyItemInserted(mDataList.size() - 1);
    }

    /**
     * 插入一堆数据
     */
    public void add(Data... dataList){
        if(dataList != null && dataList.length > 0){
            int startPos = mDataList.size();
            Collections.addAll(mDataList, dataList);
            notifyItemRangeChanged(startPos, dataList.length);
        }
    }

    public void add(Collection<Data> dataList) {
        if (dataList != null && dataList.size() > 0) {
            int startPos = mDataList.size();
            Log.d("POSITION", String.valueOf(startPos));
            mDataList.addAll(dataList);
            notifyItemRangeInserted(startPos, dataList.size());
        }
    }

    /**
     * 删除操作
     */
    public void clear(){
        mDataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void update(Data data, ViewHolder<Data> holder){
        //得到当前viewHolder坐标
        int pos = holder.getAdapterPosition();
        if(pos >= 0){
            //修改并更新
            mDataList.remove(pos);
            mDataList.add(pos, data);
            //通知
            notifyItemChanged(pos);
        }
    }

    @Override
    public void onClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if(this.mListener != null){
            //得到坐标
            int pos = viewHolder.getAdapterPosition();
            this.mListener.onItemClick(viewHolder, mDataList.get(pos));
        }

    }

    @Override
    public boolean onLongClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if(this.mListener != null){
            int pos = viewHolder.getAdapterPosition();
            this.mListener.onItemLongClick(viewHolder, mDataList.get(pos));
            return true;
        }
        return false;
    }

    /**
     * 自定义监听器
     */
    public interface AdapterListener<Data>{
        //点击cell触发
        void onItemClick(RecyclerAdapter.ViewHolder holder, Data data);

        void onItemLongClick(RecyclerAdapter.ViewHolder holder, Data data   );
    }


    /**
     * 自定义ViewHoler
     * @param <Data> 泛型
     */
    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder {

        private AdapterCallback<Data> callback;

        private Unbinder mUnbinder;

        protected Data mData;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /**
         * 绑定数据的触发
         * @param data 绑定的数据
         */
        void bind(Data data){
            this.mData = data;
            onBind(data);
        }

        /**
         * 触发绑定数据时回调
         * @param data 绑定的数据
         */
        protected abstract void onBind(Data data);

        /**
         * holder自己对数据进行更新
         * @param data data数据
         */
        public void updateData(Data data){
            if(this.callback != null){
                this.callback.update(data, this);
            }
        }


    }

    /**
     * 对接口做一次实现AdapterListener
     * @param <Data>
     */
    public static abstract class AdapterListenerImpl<Data> implements AdapterListener<Data>{

        @Override
        public void onItemClick(ViewHolder holder, Data data) {

        }

        @Override
        public void onItemLongClick(ViewHolder holder, Data data) {

        }
    }

}
