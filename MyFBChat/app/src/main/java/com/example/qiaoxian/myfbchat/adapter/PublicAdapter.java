package com.example.qiaoxian.myfbchat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qiaoxian.myfbchat.R;
import com.example.qiaoxian.myfbchat.bean.Chat;
import com.example.qiaoxian.myfbchat.bean.Chat1;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class PublicAdapter extends RecyclerView.Adapter<PublicAdapter.ViewHolder> {
    public static final int MSG_LEFT = 0;
    public static final int MSG_Right = 1;
    public Context mContext;
    public List<Chat1> mChats1;
    public String mImageUrl,myUrl;
    FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;


    public PublicAdapter(Context mContext, List<Chat1> mChats1, String imageUrl){
        this.mContext = mContext;
        this.mChats1 = mChats1;
        this.mImageUrl = imageUrl;
        this.myUrl = myUrl;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_Right) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.right_public, parent, false);
            return new PublicAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.left_public, parent, false);
            return new PublicAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Chat1 chat1 = mChats1.get(position);
        holder.userName.setText(chat1.getUsername()+":");
        holder.Content.setText(chat1.getMessage());
        holder.timeNow.setText(chat1.getTime());

        //TODO
    }

    @Override
    public int getItemCount() {
        return mChats1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView Content;
        public ImageView userImage;
//        public ImageView myImage;
        public TextView timeNow;
        public TextView userName;
        public ViewHolder(View itemView) {
            super(itemView);
            Content = itemView.findViewById(R.id.content);
            userImage = itemView.findViewById(R.id.portrait);
//            myImage = itemView.findViewById(R.id.portraitr);
            timeNow = itemView.findViewById(R.id.time);
            userName = itemView.findViewById(R.id.publicWhatAName);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChats1.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_Right;
        }else{
            return MSG_LEFT;
        }
    }
}
