package com.example.a3rtubler1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
//import com.google.firebase.auth.FirebaseUser;

public class PasswordResetActivity extends AppCompatActivity {

    public static Context context;
    private static final String TAG = "MemberInfoInitActivity";

    private String accountPassword;
    private String tumPassword;
    private Button modifyButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_modify);

        mAuth = FirebaseAuth.getInstance();
        modifyButton = (Button) findViewById(R.id.modify_button);
        context = this;

        modifyButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifyPassword();
            }
        });
    }

    private void modifyPassword() {

        accountPassword = ((EditText) findViewById(R.id.password_modify)).getText().toString();
        tumPassword = ((EditText) findViewById(R.id.tumpassword_modify)).getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String docid = ((MemberInfoInit)MemberInfoInit.context).docId;

        if (!(accountPassword.equals("") || tumPassword.equals(""))) {
            if (user != null) {

                user.updatePassword(accountPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "회원비번 변경 성공", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(context, "비밀번호 변경에 실패하였습니다", Toast.LENGTH_SHORT).show();
                        }
                    }

                });

                Map<String, Object> data = new HashMap<>();
                data.put("tumPassword", tumPassword);
                Toast.makeText(context, docid, Toast.LENGTH_SHORT).show();

                db.collection("users").document(docid).update(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "비밀번호 변경이 완료되었습니다", Toast.LENGTH_SHORT).show();

                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
            }
        } else {
            Toast.makeText(context, "변경할 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
        }
    }
}



