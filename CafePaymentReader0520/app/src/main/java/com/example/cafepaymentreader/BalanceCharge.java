package com.example.cafepaymentreader;

import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.io.IOException;

//import android.util.Log;


public class BalanceCharge extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balancecharge);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.chargeButton).setOnClickListener(onClickListener);
        findViewById(R.id.returnmainactivityButton).setOnClickListener(onClickListener);
        findViewById(R.id.charge10000Button).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch(v.getId()){
                case R.id.chargeButton:
                    decrement();
                    break;
                case R.id.returnmainactivityButton:
                    myStartActivity(MainActivity.class);
                    break;
                case R.id.charge10000Button:
                    charge10000();
                           break;

            }
        }
    };

   private void decrement() {

       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       // Access a Cloud Firestore instance from your Activity
       FirebaseFirestore db = FirebaseFirestore.getInstance();
       String docid = ((MemberInfoInit) MemberInfoInit.context).docId;


           String pn = ((TextView) findViewById(R.id.balancechargeamount)).getText().toString();

           //리더기작동
           db.collection("users")
                   .whereEqualTo("phone", pn)
                   .get()
                   .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                           if (task.isSuccessful()) {
                               for (QueryDocumentSnapshot document : task.getResult()) {

                                   startToast(document.getId() + " => " + document.getData());
                                   //String detectedtumblerid = document.getId();

                                   DocumentReference docref = db.collection("users").document(document.getId());
                                   docref.update("balanceamount", FieldValue.increment(-10000))

                                           //db.collection("users").document(docid).set(balanceInfo, SetOptions.merge())
                                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                                               @Override
                                               public void onSuccess(Void aVoid) {
                                                   startToast("1000원이 결제되었습니다");

                                               }
                                           })
                                           .addOnFailureListener(new OnFailureListener() {
                                               @Override
                                               public void onFailure(@NonNull Exception e) {
                                                   startToast("결제실패");
                                               }
                                           });


                               }
                           } else {
                               startToast("실패");
                           }
                       }
                   });

          }



    private void charge10000(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String docid = ((MemberInfoInit)MemberInfoInit.context).docId;
        if(user != null) {


            DocumentReference docref = db.collection("users").document(docid);
              docref.update("balanceamount",  FieldValue.increment(10000))

            //db.collection("users").document(docid).set(balanceInfo, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            startToast("잔액이 충전되었습니다");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            startToast("잔액충전 실패");
                            //Log.w(TAG, "Error writing document", e);
                        }
                    });
        }

    }


    private void charge() {

        String balanceamount =  ((EditText) findViewById(R.id.balancechargeamount)).getText().toString();

        if(balanceamount.length() > 0 && balanceamount.length() < 200000 ) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            BalanceInfo balanceInfo = new BalanceInfo(balanceamount);

            String docid = ((MemberInfoInit)MemberInfoInit.context).docId;
            if(user != null) {

               // DocumentReference docref = db.collection("users").document(docid);

                db.collection("users").document(docid).set(balanceInfo, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startToast("잔액이 충전되었습니다");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startToast("잔액충전 실패");
                                //Log.w(TAG, "Error writing document", e);
                            }
                        });
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
