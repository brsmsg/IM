package com.example.instantMessaging.Fragments.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.factory.model.User;
import com.example.instantMessaging.Fragments.main.ContactFragment;
import com.example.instantMessaging.R;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author brsmsg
 * @time 2020/3/14
 */
public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ViewHolder> {

    private List<User> mUserList;

    private Context mContext;

    public ContactRecyclerAdapter(Context context, List<User> userList){
        this.mContext = context;
        this.mUserList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_contact, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUserList.get(position);

        Glide.with(mContext).load(user.getPortrait()).into(holder.mPortrait);
        holder.mDesc.setText(user.getDesc());
        holder.mUserName.setText(user.getUserName());
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.txt_userName_contact)
        TextView mUserName;

        @BindView(R.id.txt_desc_contact)
        TextView mDesc;

        @BindView(R.id.img_portrait_contact)
        ImageView mPortrait;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
