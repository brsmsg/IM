package com.example.instantMessaging.Fragments.main.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.factory.model.FriendRequest;
import com.example.instantMessaging.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder> implements View.OnClickListener {

    //返回的好友请求列表FriendRequest类
    public List<FriendRequest> mRequestList;
    //自身的上下文
    private Context mContext;

    /**
     * 构造函数
     * @param requestList the request list
     * @param mContext    the m context
     */
    public SearchRecyclerAdapter(List<FriendRequest> requestList, Context mContext) {
        this.mRequestList = requestList;
        this.mContext = mContext;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //创建View实例，将RecyclerView子项cell_request填充进父布局
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_request,parent,false);
        //ViewHolder内部类的构造函数
        final ViewHolder holder = new ViewHolder(view);
        //给接受按钮设置点击事件
        /*holder.mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                FriendRequest friendRequest = mRequestList.get(position);
                Toast.makeText(v.getContext(),"接受好友请求",Toast.LENGTH_SHORT).show();
                removeData(position);
            }
        });
        holder.mRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                FriendRequest friendRequest = mRequestList.get(position);
                Toast.makeText(v.getContext(),"拒绝好友请求",Toast.LENGTH_SHORT).show();
                removeData(position);
            }
        });*/
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //setTag为当前view添加状态，之后直接点击调用getTag便可获取其position
        holder.itemView.setTag(position);
        //接收和拒绝按钮
        holder.mAccept.setTag(position);
        holder.mRefuse.setTag(position);

        //获取当前点击的FriendRequest实例
        FriendRequest friendRequest = mRequestList.get(position);
        //数据加载
        holder.mSendUserId.setText(friendRequest.getSendUserId());
        holder.mDateTime.setText(friendRequest.getRequestDateTime());

    }

    /**
     * 根据位置删除数据
     *
     * @param position the position
     */
    public void removeData(int position){
        mRequestList.remove(position);
        //删除动画
        notifyItemRemoved(position);
        //错位删除时整体刷新
        notifyDataSetChanged();
    }

    /**
     * 遍历mRequestList，根据sendUserId来删除时数据
     *
     * @param sendUserId the send user id
     */
    public void removeData(String sendUserId){
        for (int i = 0;i<mRequestList.size();i++){
            if (mRequestList.get(i).getSendUserId().equals(sendUserId)){
                mRequestList.remove(i);
                notifyItemRemoved(i);
                //错位删除时整体刷新
                notifyDataSetChanged();

            }
        }
    }

    /**
     * 添加整个列表
     *
     * @param requestList
     */
    public void addData(List<FriendRequest> requestList){
        //初始化后的mRequestList与传入的requestList进行比较，删除sendUserId相同的部分，保留最新的发送时间
        for (int i = 0; i<requestList.size();i++){//遍历刷新的好友请求
            for (int j = 0; j<mRequestList.size();j++){//遍历之前的好友请求
                if (requestList.get(i).getSendUserId().equals(mRequestList.get(j).getSendUserId())){
                    //删除mRequestList中重复的数据
                    removeData(j);
                }
            }
            mRequestList.add(0,requestList.get(i));
            notifyItemChanged(0);
        }

    }



    @Override
    public int getItemCount() {
        return mRequestList.size();
    }


    //点击事件回调
    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null){
            int pos = (int) v.getTag();//找到当前点击的部位
            mOnItemClickListener.onItemClick(v,pos);
        }

    }

    //自定义回调接口，实现click事件
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
    //声明自定义的接口
    private OnItemClickListener mOnItemClickListener;

    //定义方法并传给外面的使用者
    public void setOnItemClickListener(OnItemClickListener  listener) {
        this.mOnItemClickListener  = listener;
    }


    /**
     * 为提高滑动效率而使用ViewHolder进行缓存
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_sendUserId)
        TextView mSendUserId;

        @BindView(R.id.txt_requestDateTime)
        TextView mDateTime;

        @BindView(R.id.accept_friend)
        Button mAccept;

        @BindView(R.id.refuse_friend)
        Button mRefuse;

        //构造方法
         public ViewHolder(@NonNull View itemView) {
             super(itemView);
             ButterKnife.bind(this,itemView);

             mAccept.setOnClickListener(SearchRecyclerAdapter.this);
             mRefuse.setOnClickListener(SearchRecyclerAdapter.this);
         }


     }
}
