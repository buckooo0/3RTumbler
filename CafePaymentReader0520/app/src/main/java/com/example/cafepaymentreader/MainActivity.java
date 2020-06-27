package com.example.cafepaymentreader;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
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
import com.google.firebase.firestore.FirebaseFirestore;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            myStartActivity(SignUpActivity.class);
        }else {
            myStartActivity(MemberInfoInit.class);

        }
        findViewById(R.id.logoutButton).setOnClickListener(onClickListener);
        findViewById(R.id.gotomebmberinitButton).setOnClickListener(onClickListener);
        findViewById(R.id.gotoNFCButton).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.logoutButton:
                    FirebaseAuth.getInstance().signOut();
                    myStartActivity(SignUpActivity.class);
                    break;
                case R.id.gotomebmberinitButton:
                    myStartActivity(MemberInfoInit.class);
                    break;
                case R.id.gotoNFCButton:
                    myStartActivity(NFCMainActivity.class);
            }
        }
    };


    private void startSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void myStartActivity(Class c){
        Intent intent=new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}