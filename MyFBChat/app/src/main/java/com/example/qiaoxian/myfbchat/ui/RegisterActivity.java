package com.example.qiaoxian.myfbchat.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qiaoxian.myfbchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameRegister,emailRegister,passwordRegister,url;
    private Button submitRegister;
    //FirebaseAuth
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolBar);
        //setSupportActionBar
        setSupportActionBar(toolbar);
        //setDisplayHomeAsUpEnabled
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        auth = FirebaseAuth.getInstance();
        submitRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringUsername = usernameRegister.getText().toString();
                String stringEmail = emailRegister.getText().toString();
                String stringPassword = passwordRegister.getText().toString();
                String stringUrl = url.getText().toString();

                if(TextUtils.isEmpty(stringUsername)||TextUtils.isEmpty(stringEmail)||TextUtils.isEmpty(stringPassword)){
                    Toast.makeText(RegisterActivity.this,"you miss something.."
                            ,Toast.LENGTH_SHORT).show();

                }else if(stringPassword.length()<6){
                    Toast.makeText(RegisterActivity.this,"can your password be more than 5?"
                            ,Toast.LENGTH_SHORT).show();
                }else{
                    myRegister(stringUsername,stringEmail,stringPassword,stringUrl);
                }
            }
        });

    }

    private void initView() {
        usernameRegister = (EditText) findViewById(R.id.registerUsername);
        emailRegister = (EditText) findViewById(R.id.registerEmail);
        passwordRegister = (EditText)findViewById(R.id.registerPassword);
        submitRegister = (Button) findViewById(R.id.registerSubmit);
        url = (EditText)findViewById(R.id.registerUrl);
    }

    private void myRegister(final String username, String email, String password, final String url){
        //onCompleteListener
        auth.createUserWithEmailAndPassword(email,password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    //assert
                    assert  firebaseUser != null;
                    String userId = firebaseUser.getUid();
                    //databaseReference
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("id",userId);
                    hashMap.put("username",username);
                    hashMap.put("imageURL",url);

                    databaseReference.setValue(hashMap).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(RegisterActivity.this,
                                        MainPageActivity.class);
                                //addFlags
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                //when back to stack
                                finish();
                            }

                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this,"fail to register",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
