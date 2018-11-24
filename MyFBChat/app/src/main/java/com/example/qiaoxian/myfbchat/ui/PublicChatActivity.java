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
import com.example.qiaoxian.myfbchat.adapter.PublicAdapter;
import com.example.qiaoxian.myfbchat.bean.Chat;
import com.example.qiaoxian.myfbchat.bean.Chat1;
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
import java.util.Map;

public class PublicChatActivity extends AppCompatActivity {

    private TextView bigName;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
//    Intent intent;
    private Button buttonSendMessage;
    private EditText inputMessage;
    private PublicAdapter publicAdapter;
    private List<Chat1> chatList1;
    private RecyclerView recyclerViewConversation;
    private ArrayList<Long> usernames;


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
        usernames = new ArrayList<>();

//        intent = getIntent();
//        final String userid = intent.getStringExtra("userId");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();






                buttonSendMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String msg = inputMessage.getText().toString();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd  HH.m");
                        Date date = new Date(System.currentTimeMillis());
                        final String time = simpleDateFormat.format(date);

                        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.
                                getUid());
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                String userName = user.getUsername();
                                if (!msg.equals("")) {
                                    sendMessage(firebaseUser.getUid(), msg, time,userName);
                                } else {
                                    Toast.makeText(PublicChatActivity.this, "send something please", Toast.LENGTH_SHORT).show();

                                }
                                inputMessage.setText("");

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });





                    }
                });


        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.
                getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                bigName.setText(user.getUsername());
                readMessage(firebaseUser.getUid(),user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendMessage(String sender,String message,String time,String username) {
        DatabaseReference databaseReferenceSend = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("message",message);
        hashMap.put("time",time);
        hashMap.put("username",username);
        databaseReferenceSend.child("publicChat").push().setValue(hashMap);
    }

    private void readMessage(final String myid, final String url){
        chatList1 = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("publicChat");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList1.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Chat1 chat1 = dataSnapshot1.getValue(Chat1.class);

                    chatList1.add(chat1);


                    publicAdapter = new PublicAdapter(PublicChatActivity.this,chatList1,url);
                    recyclerViewConversation.setAdapter(publicAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}

