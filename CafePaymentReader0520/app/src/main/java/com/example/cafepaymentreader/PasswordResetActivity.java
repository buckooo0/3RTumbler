package com.example.cafepaymentreader;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

//import android.util.Log;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseUser;

public class PasswordResetActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_input);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
          switch(v.getId()){
          }
        }
    };


    private void send() {

        String email = ((EditText) findViewById(R.id.emaileditText)).getText().toString();

   if(email.length() > 0  ) {

   mAuth.sendPasswordResetEmail(email)
           .addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   if (task.isSuccessful()) {
                       startToast("이메일을 보냈습니다.");
                   }
               }
           });
       }else {
       startToast("이메일을 입력해 주세요.");
   }
    }
    //listner에서는 Toast못씀으로 함수하나 만드렁줌
    private void startToast(String msg){
        Toast.makeText(this, msg,  Toast.LENGTH_SHORT).show();
    }



}



