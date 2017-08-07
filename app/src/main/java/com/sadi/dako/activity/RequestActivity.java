package com.sadi.dako.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sadi.dako.R;

public class RequestActivity extends AppCompatActivity {

    Context con;
    private TextView tvNextBtn;
    private EditText etMobile;

    private RelativeLayout relReqCar,relReqBike;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request);

        con = this;

        relReqBike = (RelativeLayout)findViewById(R.id.relReqBike);
        relReqCar = (RelativeLayout)findViewById(R.id.relReqCar);

        relReqBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(con,MapsActivity.class));
                finish();
            }
        });

        relReqCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(con,MapsActivity.class));
                finish();
            }
        });
    }



}
