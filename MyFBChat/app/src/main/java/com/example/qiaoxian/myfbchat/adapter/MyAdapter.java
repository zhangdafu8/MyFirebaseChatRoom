package com.example.qiaoxian.myfbchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qiaoxian.myfbchat.R;
import com.example.qiaoxian.myfbchat.bean.User;
import com.example.qiaoxian.myfbchat.ui.MessageActivity;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends BaseAdapter {
    List<User> mUsers= new ArrayList<>();
    Context mContext;

    public MyAdapter(List<User> listDetail, Context context){
        mUsers= listDetail;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mUsers.size();
    }

    @Override
    public Object getItem(int i) {
        return mUsers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder =new ViewHolder();
        final User user = mUsers.get(i);
        if(view==null){
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.name_item,null);
            viewHolder.myImageView = (ImageView) view.findViewById(R.id.itemImage);
            viewHolder.myTextView= (TextView)view.findViewById(R.id.itemName);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();

        }
        viewHolder.myTextView.setText(user.getUsername());
        Glide.with(mContext).load(user.getImageURL()).into(viewHolder.myImageView);

        viewHolder.myTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userId", user.getId());
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    class ViewHolder{
        public ImageView myImageView;
        public TextView myTextView;

    }
}
