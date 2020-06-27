package com.example.cafepaymentreader;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//4/28
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.rpc.Code;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import io.grpc.Status;

public class NFCMainActivity extends AppCompatActivity implements Listener {

    static final int REQUEST_CODE = 100;
    static final int RESULT_CODE = 1;
    public static final String TAG = NFCMainActivity.class.getSimpleName();
    public int totalpay = 0;
    public static Context context2;

    private TextView pay;
    private TextView payprice;
    private Button payment;
    private Button menuAdd;
    private Button menuDelete;
    private Button clearList;
    private String cafeName;

    private ArrayList<CafeMenu> menuList;
    private ArrayList<CafeMenu> payList;
    private ListView menuListView;
    private ListView payListView;

    private NFCWriteFragment mNfcWriteFragment;
    private NFCReadFragment mNfcReadFragment;

    private boolean isDialogDisplayed = false;
    private boolean isWrite = false;
    private boolean deleteClicked = false;

    private NfcAdapter mNfcAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcmain);

        Intent secondIntent = getIntent();
        cafeName =  secondIntent.getStringExtra("cafeName");
        menuListView = findViewById(R.id.menu_list);
        payListView = findViewById(R.id.payment_list);
        menuList = new ArrayList<CafeMenu>();
        payList = new ArrayList<CafeMenu>();
        pay = (TextView)findViewById(R.id.total_pay);
        clearList = (Button)findViewById(R.id.paylist_clear);
        menuAdd = (Button)findViewById(R.id.btn_add);
        menuDelete = (Button)findViewById(R.id.btn_delete);


        initViews();

        initMenuData();

        initNFC();

      //context2=this;

        menuAdd.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddActivity();
            }
        });

    }

    private void startAddActivity() {
        Intent intent = new Intent(this, MenuAddActivity.class);
        intent.putExtra("cafeName", cafeName);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void initMenuData() {
        totalpay = 0;
        pay.setText(String.valueOf(totalpay));
        menuList.clear();
        payList.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("menu").document(cafeName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map = (HashMap)document.getData();

                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            menuList.add(new CafeMenu(entry.getKey(), Integer.parseInt(entry.getValue().toString())));
                            //Log.d(TAG, entry.getKey() + " and " + entry.getValue().toString());
                        }

                        setAdapter();

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    private void setAdapter() {

        final MenuAdapter menuAdapter = new MenuAdapter(this, menuList);
        final PayAdapter payAdapter = new PayAdapter(this, menuList);

        menuListView.setAdapter(menuAdapter);
        payListView.setAdapter(payAdapter);

        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                if(deleteClicked) {
                    totalpay = 0;
                    pay.setText(String.valueOf(totalpay));

                    for (CafeMenu s : menuList) {
                        s.setCount(0);
                    }

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference docRef = db.collection("menu").document(cafeName);

                    Map<String,Object> updates = new HashMap<>();
                    updates.put(menuList.get(position).getName(), FieldValue.delete());

                    docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "update");
                        }
                    });


                    menuList.remove(position);

                    payAdapter.notifyDataSetChanged();
                    menuAdapter.notifyDataSetChanged();

                    deleteClicked = false;
                } else {
                    totalpay += menuAdapter.getItem(position).getValue();
                    menuList.get(position).countUp();
                    pay.setText(String.valueOf(totalpay));

                    payAdapter.notifyDataSetChanged();
                    menuAdapter.notifyDataSetChanged();
                }
            }
        });

        clearList.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalpay = 0;
                pay.setText(String.valueOf(totalpay));

                for (CafeMenu s : menuList) {
                    s.setCount(0);
                }

                payAdapter.notifyDataSetChanged();
                menuAdapter.notifyDataSetChanged();
            }
        });

        menuDelete.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deleteClicked) {
                    deleteClicked = false;
                    Toast.makeText(getApplicationContext(), "메뉴 삭제가 취소되었습니다", Toast.LENGTH_SHORT).show();
                } else {
                    deleteClicked = true;
                    Toast.makeText(getApplicationContext(), "삭제할 메뉴를 선택해 주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_CODE){
                initMenuData();
            }
        }
    }

    private void initViews() {

        payprice = (TextView) findViewById(R.id.total_pay);
        menuAdd = (Button) findViewById(R.id.btn_add);
        payment = (Button)findViewById(R.id.btn_payment);


       // mBtWrite.setOnClickListener(view -> showWriteFragment());
        payment.setOnClickListener(view -> showReadFragment());

        //5.25
        menuAdd.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initNFC(){

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }


    private void showWriteFragment() {

        isWrite = true;

        mNfcWriteFragment = (NFCWriteFragment) getFragmentManager().findFragmentByTag(NFCWriteFragment.TAG);

        if (mNfcWriteFragment == null) {

            //mNfcWriteFragment = NFCWriteFragment.newInstance();
        }
        mNfcWriteFragment.show(getFragmentManager(),NFCWriteFragment.TAG);

    }

    private void showReadFragment() {

        //4.28
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //


        mNfcReadFragment = (NFCReadFragment) getFragmentManager().findFragmentByTag(NFCReadFragment.TAG);

        if (mNfcReadFragment == null) {

            mNfcReadFragment = NFCReadFragment.newInstance();
        }
        mNfcReadFragment.show(getFragmentManager(),NFCReadFragment.TAG);

        Bundle bundle = new Bundle();
        bundle.putInt("pay", totalpay);
        mNfcReadFragment.setArguments(bundle);

    }

    @Override
    public void onDialogDisplayed() {

        isDialogDisplayed = true;
    }

    @Override
    public void onDialogDismissed() {

        isDialogDisplayed = false;
        isWrite = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected,tagDetected,ndefDetected};
*/

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if(mNfcAdapter!= null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mNfcAdapter!= null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        Log.d(TAG, "onNewIntent: "+intent.getAction());

        if(tag != null) {
            Toast.makeText(this, getString(R.string.message_tag_detected), Toast.LENGTH_SHORT).show();

            Ndef ndef = Ndef.get(tag);

            if (isDialogDisplayed) {

                if (isWrite) {

                    //String messageToWrite = mEtMessage.getText().toString();
                    mNfcWriteFragment = (NFCWriteFragment) getFragmentManager().findFragmentByTag(NFCWriteFragment.TAG);
                  //  mNfcWriteFragment.onNfcDetected(ndef,messageToWrite);

                } else {

                    mNfcReadFragment = (NFCReadFragment)getFragmentManager().findFragmentByTag(NFCReadFragment.TAG);
                    mNfcReadFragment.onNfcDetected(ndef);
                }
            }
        }
    }

}
