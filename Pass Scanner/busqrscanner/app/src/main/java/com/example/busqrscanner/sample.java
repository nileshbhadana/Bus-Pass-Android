package com.example.busqrscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class sample extends AppCompatActivity {
TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");
        String custid = intent.getExtras().getString("custid");
        String email= intent.getExtras().getString("email");

        tv=findViewById(R.id.mytv);
        tv.setText(name+custid+email);

    }
}
