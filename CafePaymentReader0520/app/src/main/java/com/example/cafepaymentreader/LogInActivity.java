package com.example.cafepaymentreader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//import android.util.Log;

public class LogInActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.logInbutton).setOnClickListener(onClickListener);
        findViewById(R.id.gotopasswordresetButton).setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
          switch(v.getId()){
              case R.id.logInbutton:
                logIn();
              break;
              case R.id.gotopasswordresetButton:
                  myStartActivity(PasswordResetActivity.class);
                  break;

          }
        }
    };


    private void logIn() {

        String email = ((EditText) findViewById(R.id.emaileditText)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordeditText)).getText().toString();

   if(email.length() > 0 && password.length()>0 ) {

       mAuth.signInWithEmailAndPassword(email, password)
               .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {
                           FirebaseUser user = mAuth.getCurrentUser();
                           startToast("로그인에 성공하였습니다.");
                           myStartActivity(MainActivity.class);
                          } else {
                           if(task.getException() != null){
                               startToast(task.getException().toString());
                           }
                       }
                   // ...
                   }
               });
       }else {
       startToast("이메일 또는 비밀번호를 입력해주세요");
   }
    }
    //listner에서는 Toast못씀으로 함수하나 만드렁줌
    private void startToast(String msg){
        Toast.makeText(this, msg,  Toast.LENGTH_SHORT).show();
    }

    private void myStartActivity(Class c){
        Intent intent=new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}



