package com.example.qiaoxian.myfbchat.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.qiaoxian.myfbchat.R;
import com.example.qiaoxian.myfbchat.adapter.ConversationAdapter;
import com.example.qiaoxian.myfbchat.adapter.MyAdapter;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private TextView bigName;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference,databaseReferenceMy;
    Intent intent;
    private Button buttonSendMessage;
    private EditText inputMessage;
    private ConversationAdapter conversationAdapter;
    private List<Chat> chatList;
    private List<String> urlList;
    private RecyclerView recyclerViewConversation;
    private String myPortrait;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar=findViewById(R.id.messageToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerViewConversation=findViewById(R.id.messageRecycleView);
        urlList = new ArrayList<>();
        //HasFixedSize
        recyclerViewConversation.setHasFixedSize(true);
        //getApplicationContext
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        //stack
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewConversation.setLayoutManager(linearLayoutManager);

        bigName = (TextView)findViewById(R.id.messageBigName);
        buttonSendMessage = (Button)findViewById(R.id.messageButton);
        inputMessage = (EditText)findViewById(R.id.input);


        intent = getIntent();
        final String userid = intent.getStringExtra("userId");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();



        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = inputMessage.getText().toString();
                SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy.MM.dd  HH.m");
                Date date = new Date(System.currentTimeMillis());
                String time = simpleDateFormat.format(date);
                if(!msg.equals("")){
                    sendMessage(firebaseUser.getUid(),userid,msg,time,myPortrait);
                }else{
                    Toast.makeText(MessageActivity.this,"send something please",Toast.LENGTH_SHORT).show();
                }
                inputMessage.setText("");
            }


        });


        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                bigName.setText("u are talking to:"+user.getUsername());
                readMessage(firebaseUser.getUid(),userid,user.getImageURL());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });









    }

    private void sendMessage(String sender,String receiver,String message,String time,String url) {
        DatabaseReference databaseReferenceSend = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("time",time);
//        hashMap.put("url",url);
        databaseReferenceSend.child("chats").push().setValue(hashMap);
    }

    private void readMessage(final String myid, final String userid, final String url){
        chatList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("chats");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Chat chat = dataSnapshot1.getValue(Chat.class);
                    if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid)||
                            chat.getReceiver().equals(userid)&&chat.getSender().equals(myid)){
                        chatList.add(chat);
                    }

                    conversationAdapter = new ConversationAdapter(MessageActivity.this,chatList,url);
                    recyclerViewConversation.setAdapter(conversationAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }


}
