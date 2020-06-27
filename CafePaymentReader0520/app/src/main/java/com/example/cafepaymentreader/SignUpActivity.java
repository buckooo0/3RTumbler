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

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.signUpButton).setOnClickListener(onClickListener);
        findViewById(R.id.gotoLoginButton).setOnClickListener(onClickListener);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    @Override public void onBackPressed() {

        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
          switch(v.getId()){
              case R.id.signUpButton:
                  signUp();
              break;
              case R.id.gotoLoginButton:
                  myStartActivity(LogInActivity.class);
                  break;

          }
        }
    };


    private void signUp() {

        String email = ((EditText) findViewById(R.id.emaileditText)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordeditText)).getText().toString();
        String passwordcheck = ((EditText) findViewById(R.id.passwordcheckEditText)).getText().toString();

   if(email.length() > 0 && password.length()>0 && passwordcheck.length()>0) {
       if (password.equals(passwordcheck)) {

           mAuth.createUserWithEmailAndPassword(email, password)
                   .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()) {
                               FirebaseUser user = mAuth.getCurrentUser();
                               startToast("회원가입을 성공했습니다");
                               finish();
                           } else {
                               if (task.getException() != null)
                                   startToast(task.getException().toString());
                           }
                       }
                   });
       } else {
           startToast("비밀번호가 일치하지 않습니다");
       }
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

    //Listener에서 startactivity안되서 함수하나 만들어줌
 private void startLoginActivity(){
     Intent intent=new Intent(this,LogInActivity.class);
     startActivity(intent);
 }


}

