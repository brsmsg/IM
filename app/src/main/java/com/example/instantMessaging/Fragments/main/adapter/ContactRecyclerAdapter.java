package com.example.instantMessaging.Fragments.main.adapter;

import android.content.Context;
import android.util.Log;
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
import com.example.instantMessaging.Fragments.main.ContactFragment;
import com.example.instantMessaging.R;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author brsmsg
 * @time 2020/3/14
 */
public class ContactRecyclerAdapter
        extends RecyclerView.Adapter<ContactRecyclerAdapter.ViewHolder> implements View.OnClickListener {

    private OnItemClickListener mOnItemClickListener;

    private List<Contact> mContactList;

    private Context mContext;

    public ContactRecyclerAdapter(Context context, List<Contact> contactList){
        this.mContext = context;
        this.mContactList = contactList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_contact, parent, false);
        ViewHolder holder = new ViewHolder(view);
        //设置点击监听
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(position);

        Contact contact = mContactList.get(position);

        Glide.with(mContext).load(contact.getFaceImage()).into(holder.mPortrait);
        holder.mDesc.setText(contact.getDescription());
        holder.mUserName.setText(contact.getUsername());
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    /**
     * 替换部分数据
     * @param contactList
     */
    public void replace(List<Contact> contactList){
        DiffUtil.Callback callback = new DiffUtils<>(mContactList, contactList);
        for(Contact c:mContactList){
            Log.d("oldList", c.toString());
        }

        for(Contact c:contactList){
            Log.d("newList", c.toString());
        }

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);
        Log.d("difference", diffResult.toString());

        replaceAll(contactList);
        diffResult.dispatchUpdatesTo(this);
    }


    /**
     * 更新所有数据
     */
    public void replaceAll(List<Contact> contactList){
        mContactList.clear();
        if(contactList == null || contactList.size() == 0){
            return;
        }
        mContactList.addAll(contactList);
        notifyDataSetChanged();
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
            mOnItemClickListener.onItemClick(v, mContactList.get(pos));
        }
    }

    /**
     * 监听接口
     */
    public interface OnItemClickListener{
        void onItemClick(View view, Contact contact);
    }


    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.txt_userName_contact)
        TextView mUserName;

        @BindView(R.id.txt_desc_contact)
        TextView mDesc;

        @BindView(R.id.img_portrait_contact)
        ImageView mPortrait;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
