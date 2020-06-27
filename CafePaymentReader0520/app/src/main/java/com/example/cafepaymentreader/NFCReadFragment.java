package com.example.cafepaymentreader;


import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.tech.Ndef;
import android.os.Bundle;
//import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;

//import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

//import static com.example.cafepaymentreader.NFCMainActivity.totalpay;


public class NFCReadFragment extends DialogFragment {

  public static NFCReadFragment context2;
    public static final String TAG = NFCReadFragment.class.getSimpleName();

    public static NFCReadFragment newInstance() {

        return new NFCReadFragment();
    }

    private EditText passwordEdit;
    private TextView mTvMessage;
    private Button successBtn;
    private Listener mListener;
    private int totalpay;
    private String password;


    //@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context2 = this;
        View view = inflater.inflate(R.layout.fragment_read,container,false);
        initViews(view);
        Bundle bundle = getArguments();
        totalpay = bundle.getInt("pay");

        return view;
    }

    private void inputPassword() {

    }

    private void initViews(View view) {

        passwordEdit = (EditText) view.findViewById(R.id.tumPassword);
        mTvMessage = (TextView) view.findViewById(R.id.tv_message);
        successBtn = (Button)view.findViewById(R.id.pay_success);

        successBtn.setEnabled(false);
        successBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (NFCMainActivity)context;
        mListener.onDialogDisplayed();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.onDialogDismissed();
    }

    public void onNfcDetected(Ndef ndef){

        inputPassword();
        readFromNFC(ndef);
    }


    private void readFromNFC(Ndef ndef) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //String docid = ((MemberInfoInit)MemberInfoInit.context).docId;
        //totalpay = ((NFCMainActivity)NFCMainActivity.context2).totalpay;
        try{
            ndef.connect();
            NdefMessage ndefMessage = ndef.getNdefMessage();
            String message = new String(ndefMessage.getRecords()[0].getPayload());
            Log.d(TAG, "readFromNFC: "+message);
            mTvMessage.setText(message);
            //4.28
            password = passwordEdit.getText().toString();
            String tumbid = ((TextView)getView().findViewById(R.id.tv_message)).getText().toString();

            //리더기작동
            db.collection("users")
                    .whereEqualTo("tumblerid", tumbid)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    //String detectedtumblerid = document.getId();

                                    if(password.equals(document.get("tumPassword").toString())) {
                                        String value = document.get("balanceamount").toString();
                                        if (Integer.parseInt(value) >= totalpay) {

                                            DocumentReference docref = db.collection("users").document(document.getId());

                                            docref.update("balanceamount", FieldValue.increment(-totalpay))
                                                    //db.collection("users").document(docid).set(balanceInfo, SetOptions.merge())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                                            Log.w(TAG, " 결제 성공");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w(TAG, "결제 실패");
                                                            //Log.w(TAG, "Error writing document", e);
                                                        }
                                                    });
                                            docref.update("paycount", FieldValue.increment(1));

                                            successBtn.setEnabled(true);
                                        } else {
                                            Toast toast = Toast.makeText(getActivity(), "잔액 부족", Toast.LENGTH_LONG);
                                            ViewGroup group = (ViewGroup) toast.getView();
                                            TextView msgTextView = (TextView) group.getChildAt(0);
                                            msgTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                                            msgTextView.setTextColor(Color.RED);
                                            toast.show();
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), "패스워드가 틀렸습니다", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });




        } catch (IOException | FormatException e) {
            e.printStackTrace();
        }
    }


}

