package com.example.mohit.afinal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
     private EditText mloginemailField;
     private EditText mloginpasswordField;
     private Button mloginBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button mlinkBtn;
    private ProgressDialog mProgess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    mAuth = FirebaseAuth.getInstance();
    mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
    mloginemailField=(EditText)findViewById(R.id.loginemailField);
    mloginpasswordField=(EditText)findViewById(R.id.loginpasswordField);
    mloginBtn=(Button)findViewById(R.id.loginBtn);
    mlinkBtn=(Button)findViewById(R.id.linkbtn);
    mProgess = new ProgressDialog(this);
    mlinkBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
        }
    });
    mloginBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            checkLogin();
        }
    });
    }

    private void checkLogin() {
        String email= mloginemailField.getText().toString().trim();
        String password = mloginpasswordField.getText().toString().trim();
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
          mProgess.setMessage("logging in");
            mProgess.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                  if(task.isSuccessful()){
                   mProgess.dismiss();
                      checkuserexist();
                  }
                  else{
                      Toast.makeText(LoginActivity.this,"Invalid email or password",Toast.LENGTH_LONG).show();
                  }
              }
          });
        }
    }

    private void checkuserexist() {
    final String user_id = mAuth.getCurrentUser().getUid();
    mDatabase.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.hasChild(user_id)){
                Intent mainIntent= new Intent(LoginActivity.this,MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
            }else{
                Toast.makeText(LoginActivity.this,"Register an account first",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
    }
}
