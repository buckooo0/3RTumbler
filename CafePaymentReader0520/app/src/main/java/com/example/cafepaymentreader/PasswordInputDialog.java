package com.example.cafepaymentreader;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import javax.annotation.Nullable;

public class PasswordInputDialog extends DialogFragment {

    /*
    public static final String TAG_EVENT_DIALOG = "dialog_event";

    private EditText passwordInput;
    private Button sendPassword;
    private String password;

    public PasswordInputDialog() {}
    public static PasswordInputDialog getInstance() {
        PasswordInputDialog e = new PasswordInputDialog();
        return e;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.activity_password_input, container);
        passwordInput = (EditText) v.findViewById(R.id.passwordeditText);
        sendPassword = (Button) v.findViewById(R.id.sendButton);
        setCancelable(false);

        sendPassword.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        password = passwordInput.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("password", password);

        dismiss();
    }

 */
    private Fragment fragment;

    public PasswordInputDialog() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_password_input, container, false);

        Bundle args = getArguments();
        String value = args.getString("key");

        /*
         * DialogFragment를 종료시키려면? 물론 다이얼로그 바깥쪽을 터치하면 되지만
         * 종료하기 버튼으로도 종료시킬 수 있어야겠죠?
         */
        // 먼저 부모 프래그먼트를 받아옵니다.
        //findFragmentByTag안의 문자열 값은 Fragment1.java에서 있던 문자열과 같아야합니다.
        //dialog.show(getActivity().getSupportFragmentManager(),"tag");
        fragment = getActivity().getSupportFragmentManager().findFragmentByTag("tag");

        // 아래 코드는 버튼 이벤트 안에 넣어야겠죠?
        if (fragment != null) {
            DialogFragment dialogFragment = (DialogFragment) fragment;
            dialogFragment.dismiss();
        }
        return view;
    }
}
