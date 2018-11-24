package com.example.qiaoxian.myfbchat.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qiaoxian.myfbchat.adapter.MyAdapter;
import com.example.qiaoxian.myfbchat.R;
import com.example.qiaoxian.myfbchat.bean.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainPageActivity extends AppCompatActivity {
    //user
    private FirebaseUser firebaseUser;
    //reference
    private DatabaseReference databaseReference;
    private TextView nameMenu;
//    private ViewPager viewPager;
    private ListView listView;
    private MyAdapter myAdapter;
    private List<User> myUserList;
    private TextView publicRoom;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Toolbar toolbar = findViewById(R.id.menuPageToolBar);
        //setSupportActionBar
        setSupportActionBar(toolbar);
        //setDisplayHomeAsUpEnabled
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nameMenu = (TextView)findViewById(R.id.menuName);
        listView = (ListView)findViewById(R.id.listView);
        publicRoom = (TextView)findViewById(R.id.publicName);
        imageView = (ImageView)findViewById(R.id.myImage);
        publicRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.publicName:
                        Intent intentPublic = new Intent(MainPageActivity.this,PublicChatActivity.class);
                        startActivity(intentPublic);
                        break;
                }
            }
        });
        myUserList = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.
                getUid());
        //ValueEventListener
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                nameMenu.setText(user.getUsername());
                Glide.with(MainPageActivity.this).load(user.getImageURL()).into(imageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        obtainUsers();



    }

    private void obtainUsers() {
        final FirebaseUser firebaseUserMenu = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReferenceMenu = FirebaseDatabase.getInstance().getReference("Users");
        databaseReferenceMenu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myUserList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    assert firebaseUserMenu != null;
                    //!=
                    if(!user.getId().equals(firebaseUserMenu.getUid())){
                        myUserList.add(user);
                    }
                }
                listView.setAdapter(new MyAdapter(myUserList,MainPageActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //default menu
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainPageActivity.this,LoginActivity.class));
                finish();
                return true;
        }
        //false
        return false;
    }

}
