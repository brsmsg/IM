package com.example.instantMessaging.Fragments.message.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.factory.model.Msg;
import com.example.instantMessaging.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author brsmsg
 * @time 2020/3/25
 */
public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ViewHolder> {
    private List<Msg> mMsgList;

    public ChatRecyclerAdapter(List<Msg> msgLIst){
        this.mMsgList = msgLIst;
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
        Msg msg = mMsgList.get(position);
        //左侧消息
        if(msg.getType() == Msg.TYPE_RECEIVED){
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
        }else{
            //右侧消息
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(msg.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
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
