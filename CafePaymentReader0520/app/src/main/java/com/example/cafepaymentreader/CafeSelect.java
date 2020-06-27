package com.example.cafepaymentreader;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CafeSelect extends AppCompatActivity {

    private Button selectBtn;
    private String selectedCafe;
    private final ArrayList<String> items = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_select);

        items.add("스타벅스");
        items.add("탐앤탐스");
        items.add("커피빈");

        initView();

    }

    private void initView() {

        selectBtn = (Button)findViewById(R.id.cafe_select);

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, items);

        final ListView listview = (ListView) findViewById(R.id.cafe_list);
        listview.setAdapter(adapter);

        selectBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count, checked;
                count = adapter.getCount();
                if (count > 0) {
                    checked = listview.getCheckedItemPosition();
                    if (checked > -1 && checked < count) {

                        selectedCafe = items.get(checked).toString();

                        startNFCActivity();
                    }
                }
            }
        });
    }

    private void startNFCActivity() {
        Intent intent = new Intent(this, NFCMainActivity.class);
        intent.putExtra("cafeName", selectedCafe);
        startActivity(intent);
    }
}
