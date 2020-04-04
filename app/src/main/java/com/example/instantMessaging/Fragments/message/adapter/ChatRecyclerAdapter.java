package com.example.instantMessaging.Fragments.message.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.factory.model.MsgUI;
import com.example.instantMessaging.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author brsmsg
 * @time 2020/3/25
 */
public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ViewHolder> {
    private List<MsgUI> mMsgUIList;

    private Context mContext;

    public ChatRecyclerAdapter(Context context, List<MsgUI> msgUILIst){
        this.mContext = context;
        this.mMsgUIList = msgUILIst;
    }

    public void add(MsgUI msg){
        mMsgUIList.add(msg);
        notifyItemChanged(mMsgUIList.size() - 1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_msg, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MsgUI msgUI = mMsgUIList.get(position);
        //左侧消息
        if(msgUI.getType() == MsgUI.TYPE_RECEIVED){
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msgUI.getContent());
            Glide.with(mContext).load(msgUI.getPortrait()).into(holder.leftPortrait);
        }else if(msgUI.getType() == MsgUI.TYPE_SEND){
            //右侧消息
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(msgUI.getContent());
            Glide.with(mContext).load(msgUI.getPortrait()).into(holder.rightPortrait);
        }
    }

    @Override
    public int getItemCount() {
        return mMsgUIList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.layout_left)
        FrameLayout leftLayout;

        @BindView(R.id.layout_right)
        FrameLayout rightLayout;

        @BindView(R.id.img_portrait_chat_left)
        ImageView leftPortrait;

        @BindView(R.id.img_portrait_chat_right)
        ImageView rightPortrait;

        //接受的消息
        @BindView(R.id.txt_content_left)
        TextView leftMsg;

        //发送的消息
        @BindView(R.id.txt_content_right)
        TextView rightMsg;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
