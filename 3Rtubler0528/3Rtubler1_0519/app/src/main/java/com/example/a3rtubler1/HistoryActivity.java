package com.example.a3rtubler1;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HistoryActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private TextView historyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcmain);

        mAuth = FirebaseAuth.getInstance();
        historyView = findViewById(R.id.my_history);
        
        setHistory();
    }

    private void setHistory() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String docid = ((MemberInfoInit) MemberInfoInit.context).docId;
        final String[] tumbid = new String[1];


            DocumentReference docRef = db.collection("users").document(docid);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            tumbid[0] = document.getData().get("tumblerid").toString();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Search Failed", Toast.LENGTH_SHORT);
                    }
                }
            });


            db.collection("users")
                    .whereEqualTo("tumblerid", tumbid[0])
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //String detectedtumblerid = document.getId();

                                    String value = document.get("paycount").toString();
                                    String message = "paycount: " + value;

                                    historyView.setText(message);

                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Search Failed", Toast.LENGTH_SHORT);
                            }
                        }
                    });
    }
}

