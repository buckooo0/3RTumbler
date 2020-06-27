package com.example.cafepaymentreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.util.Base64Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MenuAddActivity extends Activity {

    static final int REQUEST_CODE = 100;
    static final int RESULT_CODE = 1;
    private String cafeName;
    private EditText menuName;
    private EditText menuValue;
    private Button menuAdd;
    private Button menuCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_menu_add);

        Intent secondIntent = getIntent();
        cafeName =  secondIntent.getStringExtra("cafeName");
        menuName = (EditText)findViewById(R.id.add_menu);
        menuValue = (EditText)findViewById(R.id.add_value);
        menuAdd = (Button)findViewById(R.id.add_check);
        menuCancel = (Button)findViewById(R.id.add_cancel);

        menuAdd.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbUpdate();
            }
        });

        menuCancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void dbUpdate() {
        if ( !(menuName.getText().toString().matches("") || menuValue.getText().toString().matches(""))) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("menu").document(cafeName);

            Map<String, Object> data = new HashMap<>();
            data.put(menuName.getText().toString(), Integer.parseInt(menuValue.getText().toString()));

            docRef.update(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Intent intent = new Intent();
                            intent.putExtra("result", "Close Popup");
                            setResult(RESULT_CODE, intent);

                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "메뉴 추가에 실패하였습니다", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Toast.makeText(getApplicationContext(), "메뉴와 가격을 입력하세요", Toast.LENGTH_SHORT).show();
        }
    }
}
