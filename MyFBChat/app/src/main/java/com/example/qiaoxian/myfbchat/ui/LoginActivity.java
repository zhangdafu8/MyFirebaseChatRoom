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

public class LoginActivity extends AppCompatActivity {

    private EditText emailLogin, passwordLogin;
    private Button signinLogin,registerLogin;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolBar);
        //setSupportActionBar
        setSupportActionBar(toolbar);
        //setDisplayHomeAsUpEnabled
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseAuth = FirebaseAuth.getInstance();
        initView();
        signinLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String GetEmail = emailLogin.getText().toString();
                String GetPassword = passwordLogin.getText().toString();
                if(TextUtils.isEmpty(GetEmail)||TextUtils.isEmpty(GetPassword)){
                    Toast.makeText(LoginActivity.this,"you miss something",Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.signInWithEmailAndPassword(GetEmail,GetPassword).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(LoginActivity.this,
                                        MainPageActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                //close what
                                finish();
                            }else{
                                Toast.makeText(LoginActivity.this,
                                        "Username or password is not corrected",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
        registerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,
                        RegisterActivity.class);
                //addFlags
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //when back to stack
                finish();
            }
        });
    }

    private void initView() {
        emailLogin = (EditText)findViewById(R.id.loginEmail);
        passwordLogin = (EditText)findViewById(R.id.loginPassword);
        signinLogin = (Button)findViewById(R.id.loginSubmit);
        registerLogin = (Button)findViewById(R.id.loginRegister);
    }
}
