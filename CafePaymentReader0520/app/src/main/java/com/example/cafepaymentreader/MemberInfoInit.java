package com.example.cafepaymentreader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.UserProfileChangeRequest;

public class MemberInfoInit extends AppCompatActivity {

    public String docId ;

    public static Context context;

    private static  final String TAG = "MemberInfoInitActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info_init);

        context = this;
        findViewById(R.id.checkButton).setOnClickListener(onClickListener);
        //4.28 코딩
        findViewById(R.id.gotonfcmainactivityButton).setOnClickListener(onClickListener);
        //5.5 코딩
        findViewById(R.id.gotobalancechargeactivityButton).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
          switch(v.getId()){
              case R.id.checkButton:
              profileUpdate();
              break;
              //4.28
              case R.id.gotonfcmainactivityButton:
              myStartActivity(NFCMainActivity.class);
              break;
              case R.id.gotobalancechargeactivityButton:
                  myStartActivity(BalanceCharge.class);
              break;

          }
        }
    };


    private void profileUpdate() {

        String name = ((EditText) findViewById(R.id.nameEditText)).getText().toString();
        String phone = ((EditText) findViewById(R.id.phoneEditText)).getText().toString();
        String birthday = ((EditText) findViewById(R.id.birthdayEditText)).getText().toString();


        if(name.length() > 0 && phone.length()>9 && birthday.length()> 5 ) {

       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            // Access a Cloud Firestore instance from your Activity
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            MemberInfo memberInfo = new MemberInfo(name, phone, birthday);

            if(user != null) {


                 docId = db.collection("users").document().getId();
//5.11
                Map<String, Object> data = new HashMap<>();
                data.put("balanceamount", "0");


                db.collection("users").document(docId).set(memberInfo)

                      .addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                           public void onSuccess(Void aVoid) {
                              startToast("회원정보 등록을 성공하였습니다");
                          }
                      })
                      .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startToast("회원정보 등록실패");
                                Log.w(TAG, "Error writing document", e);
                            }
                        });

                db.collection("users").document(docId).set(data, SetOptions.merge());
            }

   }else {
       startToast("회원정보를 입력해주세요");
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



