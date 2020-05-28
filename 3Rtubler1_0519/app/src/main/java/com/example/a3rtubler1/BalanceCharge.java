package com.example.a3rtubler1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;


public class BalanceCharge extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public static Context context;
    //private TextView currentbalance;
    //TextView currentbalance = (TextView) findViewById(R.id.currentbalance);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);
        context = this;
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.charge_button).setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch(v.getId()){
                case R.id.charge_button:
                    charge();
                    break;
                }
        }
    };
    private void charge() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String balanceamount =  ((EditText) findViewById(R.id.chargeamount)).getText().toString();

        if(balanceamount.length() > 0 && balanceamount.length() < 200000 ) {

            BalanceInfo balanceInfo = new BalanceInfo(balanceamount);

            int chargeamount = Integer.parseInt(balanceamount);

            String docid = ((MemberInfoInit)MemberInfoInit.context).docId;
            if(user != null) {
                DocumentReference docref = db.collection("users").document(docid);
                docref.update("balanceamount",  FieldValue.increment(chargeamount))
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
               // currentbalance.setText(balanceamount);
            }

        }else {
            startToast("충전은 1원이상 20만원 이하로만 가능합니다");
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
