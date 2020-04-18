package com.example.instantMessaging.Fragments.main.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.factory.model.FriendRequest;
import com.example.instantMessaging.R;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder> {

    //返回的好友请求列表FriendRequest类
    private List<FriendRequest> mRequestList;
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
        holder.mAccept.setOnClickListener(new View.OnClickListener() {
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
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //setTag为当前view添加状态，之后直接点击调用getTag便可获取其position
        holder.itemView.setTag(position);
        //获取当前点击的FriendRequest实例
        FriendRequest friendRequest = mRequestList.get(position);
        //数据加载
        holder.mSendUserId.setText(friendRequest.getSendUserId());
        holder.mDateTime.setText(friendRequest.getRequestDateTime());

    }

    /**
     * Remove data.
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
     * Add data.
     *
     * @param friendRequest the friend request
     */
    public void addData(FriendRequest friendRequest){
        //相同的用户发出的好友申请保留最新的一条
        for(int i = 0; i<mRequestList.size(); i++){
            if(mRequestList.get(i).getSendUserId().equals(friendRequest.getSendUserId())){
                Log.d("need2remove", String.valueOf(i));
                mRequestList.remove(i);
                notifyItemChanged(i);
            }
        }
        mRequestList.add(0,friendRequest);
        notifyItemChanged(0);
    }








    @Override
    public int getItemCount() {
        return mRequestList.size();
    }

    /**
     * 为提高滑动效率而使用ViewHolder进行缓存
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_sendUserId)
        TextView mSendUserId;

        @BindView(R.id.txt_requestDateTime)
        TextView mDateTime;

        @BindView(R.id.accept_friend)
        Button mAccept;

        @BindView(R.id.refuse_friend)
        Button mRefuse;

         public ViewHolder(@NonNull View itemView) {
             super(itemView);
             ButterKnife.bind(this,itemView);
         }
     }
}
