package com.sadi.dako.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sadi.dako.R;

public class MobileNumberActivity extends AppCompatActivity {

    Context con;
    private TextView tvNextBtn;
    private EditText etMobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_number);

        con = this;

        tvNextBtn = (TextView)findViewById(R.id.tvNextBtn);
        etMobile = (EditText)findViewById(R.id.etMobile);

        tvNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(con,UserActivity.class));
                finish();
            }
        });
    }

}
