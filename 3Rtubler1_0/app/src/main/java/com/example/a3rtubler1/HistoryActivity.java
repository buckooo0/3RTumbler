package com.example.a3rtubler1;

import android.content.Context;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.io.IOException;

public class HistoryActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private int myRank;
    private ImageView rankImage;
    private TextView rankText;
    private TextView historyView;
    private TextView treeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mAuth = FirebaseAuth.getInstance();
        rankImage = (ImageView) findViewById(R.id.rank_image);
        rankText = (TextView) findViewById(R.id.rank_text);
        historyView = (TextView)findViewById(R.id.my_history);
        treeView = (TextView)findViewById(R.id.tree_history);

        setHistory();
    }

    private void setHistory() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String docid = ((MemberInfoInit)MemberInfoInit.context).docId;
        final String[] tumbid = new String[1];

        DocumentReference docRef = db.collection("users").document(docid);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        tumbid[0] = document.getData().get("tumblerid").toString();
                        String value = document.get("paycount").toString();
                        String message = "paycount: " + value;
                        String rank;
                        int count = Integer.parseInt(value);
                        if (count < 60) {
                            rankImage.setImageResource(R.drawable.step1);
                            rank = "새싹";
                            rankText.setText(rank);
                        } else if (count < 120) {
                            rankImage.setImageResource(R.drawable.step2);
                            rank = "줄기";
                            rankText.setText(rank);
                        } else if (count < 180) {
                            rankImage.setImageResource(R.drawable.step3);
                            rank = "가지";
                            rankText.setText(rank);
                        } else if (count < 240) {
                            rankImage.setImageResource(R.drawable.step4);
                            rank = "묘목";
                            rankText.setText(rank);
                        } else if (count < 300) {
                            rankImage.setImageResource(R.drawable.step6);
                            rank = "나무";
                            rankText.setText(rank);
                        } else {
                            rankImage.setImageResource(R.drawable.step7);
                            rank = "큰나무";
                            rankText.setText(rank);
                        }

                        message = translateCo2(Integer.parseInt(value), rank);
                        historyView.setText(message);
                        message = translateTree(Integer.parseInt(value));
                        treeView.setText(message);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Search Failed", Toast.LENGTH_SHORT);
                }
            }
        });
/*
        CollectionReference userRef = db.collection("users");
        Query totalRank = userRef.orderBy("paycount");
        totalRank.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int index = 1;
                for (DocumentSnapshot ds: queryDocumentSnapshots.getDocuments()) {
                    if (ds.getString("tumblerid").toString() == tumbid[0]) {
                        myRank = index;
                    }
                    index++;
                }
            }
        });
*/
    }

    private String translateCo2(int value, String rank) {
        int co2 = value * 52;
        float tree = co2 / 60000;

        String message = "회원님의 현재 등급은 " + rank + "등급입니다" + "\n회원님이 지금까지 텀블러를 사용하신 횟수는 " + value + "회입니다\n" +
                "텀블러를 사용하며 절약한 이산화탄소량은 "  + co2 + "g입니다\n";
        return message;
    }

    private String translateTree(int value) {
        int co2 = value * 52;
        double tree = co2 / 187;

        String message = "나무 " + String.format("%.3f", tree / 100 ) + "그루를 심은 효과와 같습니다\n환경보호에 기여해 주셔서 감사합니다";
        return message;
    }
}

