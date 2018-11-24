package com.example.qiaoxian.myfbchat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qiaoxian.myfbchat.R;
import com.example.qiaoxian.myfbchat.bean.Chat;
import com.example.qiaoxian.myfbchat.bean.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {
    public static final int MSG_LEFT = 0;
    public static final int MSG_Right = 1;
    public Context mContext;
    public List<Chat> mChats;
    public String mImageUrl,myUrl;
    FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;


    public ConversationAdapter(Context mContext, List<Chat> mChats, String imageUrl){
        this.mContext = mContext;
        this.mChats = mChats;
        this.mImageUrl = imageUrl;
        this.myUrl = myUrl;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_Right) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.right, parent, false);
            return new ConversationAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.left, parent, false);
            return new ConversationAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Chat chat = mChats.get(position);
        holder.Content.setText(chat.getMessage());
        Glide.with(mContext).load(mImageUrl).into(holder.userImage);
        holder.timeNow.setText(chat.getTime());
        //TODO
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView Content;
        public ImageView userImage;
//        public ImageView myImage;
        public TextView timeNow;
//        public TextView userName;
        public ViewHolder(View itemView) {
            super(itemView);
            Content = itemView.findViewById(R.id.content);
            userImage = itemView.findViewById(R.id.portrait);
//            myImage = itemView.findViewById(R.id.portraitr);
            timeNow = itemView.findViewById(R.id.time);
//            userName = itemView.findViewById(R.id.whoIsTaking);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChats.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_Right;
        }else{
            return MSG_LEFT;
        }
    }
}
