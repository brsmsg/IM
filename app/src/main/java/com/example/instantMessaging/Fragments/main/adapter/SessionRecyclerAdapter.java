package com.example.instantMessaging.Fragments.main.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.factory.model.SessionUI;
import com.example.instantMessaging.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author brsmsg
 * @time 2020/4/4
 */
public class SessionRecyclerAdapter
        extends RecyclerView.Adapter<SessionRecyclerAdapter.ViewHolder>
        implements View.OnClickListener {
    private OnItemClickListener mOnItemClickLIstener;

    private List<SessionUI> mSessionList;

    private Context mContext;

    public SessionRecyclerAdapter(List<SessionUI> mSessionList, Context mContext) {
        this.mSessionList = mSessionList;
        this.mContext = mContext;
    }

    /**
     * 收到新的消息
     * @param session 要添加的会话
     */
    public void add(SessionUI session){
        //相同的用户删除原来会话并在最上面插入现有会话
        for(int i = 0; i<mSessionList.size(); i++){
            if(mSessionList.get(i).getId().equals(session.getId())){
                Log.d("need2remove", String.valueOf(i));
                mSessionList.remove(i);
                notifyItemChanged(i);
            }
        }
        mSessionList.add(0, session);
        notifyItemChanged(0);
    }

    /**
     * 解密后刷新最后一条消息
     * @param id 要更新的会话的联系人的id
     * @param encryptedContent 解密后的信息
     */
    public void refresh(String id, String encryptedContent){
        for(int i = 0; i < mSessionList.size(); i++){
            if(id.equals(mSessionList.get(i).getId())){
                mSessionList.get(i).setLastMsg(encryptedContent);
                notifyItemChanged(i);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_session, parent, false);
        ViewHolder holder = new ViewHolder(view);

        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(position);

        SessionUI session = mSessionList.get(position);

        holder.mUsername.setText(session.getUsername());
        holder.mLastMsg.setText(session.getLastMsg());
        Glide.with(mContext).load(session.getPortrait()).into(holder.mPortrait);
    }

    @Override
    public int getItemCount() {
        return mSessionList.size();
    }

    /**
     * 对外接口
     */
    public void setOnItemClickLIstener(OnItemClickListener listener){
        mOnItemClickLIstener = listener;
    }

    /**
     * onClick实现
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(mOnItemClickLIstener != null){
            int pos = (int) v.getTag();
            mOnItemClickLIstener.onItemClick(v, mSessionList.get(pos));
        }
    }


    /**
     * 监听接口
     */
    public interface OnItemClickListener{
        void onItemClick(View view, SessionUI session);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_session_portrait)
        ImageView mPortrait;

        @BindView(R.id.txt_session_username)
        TextView mUsername;

        @BindView(R.id.txt_session_last_msg)
        TextView mLastMsg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
