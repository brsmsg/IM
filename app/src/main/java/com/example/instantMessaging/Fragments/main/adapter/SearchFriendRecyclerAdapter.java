package com.example.instantMessaging.Fragments.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.factory.model.User;
import com.example.factory.model.db.Contact;
import com.example.factory.utils.DiffUtils;
import com.example.instantMessaging.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author brsmsg
 * @time 2020/4/18
 */
public class SearchFriendRecyclerAdapter
        extends RecyclerView.Adapter<SearchFriendRecyclerAdapter.ViewHolder>
        implements View.OnClickListener {

    private OnItemClickListener mOnItemClickListener;

    private List<User> mUserList;

    private Context mContext;

    public SearchFriendRecyclerAdapter(Context context, List<User> userList){
        this.mContext = context;
        this.mUserList = userList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_search_friend, parent, false);
        ViewHolder holder = new ViewHolder(view);
        //设置点击监听
        holder.mAddFriend.setOnClickListener(this);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //设置position的Tag
        holder.mAddFriend.setTag(position);

        User user = mUserList.get(position);
        if(user.getFaceImage() != null){
            Glide.with(mContext).load(user.getFaceImage()).into(holder.mPortrait);
        }
        holder.mDesc.setText(user.getDescription());
        holder.mUserName.setText(user.getUsername());
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    /**
     * 新增一条数据
     * @param user
     */
    public void add(User user){
        mUserList.add(user);
        notifyItemChanged(mUserList.size());
    }

    public void sendSuccess(int position){

    }

    /**
     * 点击事件对外接口
     * @param listener listener
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    /**
     * Onclick 实现
     * @param v view
     */
    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null){
            int pos = (int) v.getTag();
            mOnItemClickListener.onItemClick(v, mUserList.get(pos));
        }
    }

    /**
     * 监听接口
     */
    public interface OnItemClickListener{
        void onItemClick(View view, User user);
    }


    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.txt_userName_contact)
        TextView mUserName;

        @BindView(R.id.txt_desc_contact)
        TextView mDesc;

        @BindView(R.id.img_portrait_contact)
        ImageView mPortrait;

        @BindView(R.id.btn_add_friend)
        ImageView mAddFriend;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
