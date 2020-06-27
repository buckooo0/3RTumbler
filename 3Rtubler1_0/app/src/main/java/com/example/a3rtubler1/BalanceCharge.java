package com.example.a3rtubler1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import io.flutter.embedding.android.FlutterActivity;


public class BalanceCharge extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public static Context context;
    //private TextView currentbalance;
    //TextView currentbalance = (TextView) findViewById(R.id.currentbalance);
    private TextView currentChargeBalance;
    private TextView balance;
    private EditText chargeAmount;
    private String currentBalance;
    private String chargeBalance;
    private Button plus1;
    private Button plus5;
    private Button plusInput;
    private RadioButton kakaoBtn;
    private RadioButton payBtn;
    private Boolean directCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);
        context = this;
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.charge_button).setOnClickListener(onClickListener);

        currentChargeBalance = (TextView)findViewById(R.id.charge_text);
        balance = (TextView)findViewById(R.id.balanace);
        plus1 = (Button)findViewById(R.id.plus_1);
        plus5 = (Button)findViewById(R.id.plus_5);
        plusInput = (Button)findViewById(R.id.plus_input);
        chargeAmount = (EditText)findViewById(R.id.chargeamount);
        plus1.setOnClickListener(onClickListener);
        plus5.setOnClickListener(onClickListener);
        plusInput.setOnClickListener(onClickListener);
        kakaoBtn = (RadioButton)findViewById(R.id.kakaopay_radio);
        payBtn = (RadioButton)findViewById(R.id.pay_radio);



        chargeAmount.setVisibility(View.INVISIBLE);

        checkBalance();
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch(v.getId()){
                case R.id.plus_1:
                    directCheck = false;
                    chargeAmount.setVisibility(View.INVISIBLE);
                    currentBalance = "충전 금액 10000원";
                    currentChargeBalance.setText(currentBalance);
                    chargeBalance = "10000";
                    break;
                case R.id.plus_5:
                    directCheck = false;
                    chargeAmount.setVisibility(View.INVISIBLE);
                    currentBalance = "충전 금액 50000원";
                    currentChargeBalance.setText(currentBalance);
                    chargeBalance = "50000";
                    break;
                case R.id.plus_input:
                    directCheck = true;
                    currentBalance = "충전 금액 직접 입력";
                    currentChargeBalance.setText(currentBalance);
                    chargeAmount.setVisibility(View.VISIBLE);
                    break;
                case R.id.charge_button:
                    if (directCheck) {
                        chargeBalance = ((EditText) findViewById(R.id.chargeamount)).getText().toString();
                    }

                    if(kakaoBtn.isChecked()) {
                        kakaopay();
                    } else if (payBtn.isChecked()) {
                        charge(chargeBalance);
                    } else {
                        Toast.makeText(context, "결제 방법을 선택해주세요", Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }
    };


    private void kakaopay(){
        startActivity(FlutterActivity.createDefaultIntent(this));
        //finish();
        //  navigateUpTo(new Intent(getBaseContext(), BalanceCharge.class));
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // String balanceamount =  ((EditText) findViewById(R.id.chargeamount)).getText().toString();

        //if(balanceamount.length() > 0 && balanceamount.length() < 200000 ) {

        //  BalanceInfo balanceInfo = new BalanceInfo(balanceamount);

        //  int chargeamount = Integer.parseInt(balanceamount);

        String docid = ((MemberInfoInit)MemberInfoInit.context).docId;
        if(user != null) {
            DocumentReference docref = db.collection("users").document(docid);
            docref.update("balanceamount",  FieldValue.increment(10000))
                    //db.collection("users").document(docid).set(balanceInfo, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //  startToast("잔액이 충전되었습니다");
                            checkBalance();
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

        //  }else {
        //      startToast("충전은 1원이상 20만원 이하로만 가능합니다");
        //  }
    }



    private void charge(String balance) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String balanceamount = balance;

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
                                checkBalance();
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

    private void checkBalance() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String docid = ((MemberInfoInit)MemberInfoInit.context).docId;
        if(user != null) {
            DocumentReference docref = db.collection("users").document(docid);
            docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            currentBalance = "현재잔액 " + document.get("balanceamount");
                            balance.setText(currentBalance);
                        } else {
                            startToast("No such document");
                        }
                    }
                }
            });
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
