package com.skyblue.shop.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.skyblue.shop.R;

public class MobileBrandViewActivity extends AppCompatActivity {

    TextView topNavTitleTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_brand_view);

        topNavTitleTxt = findViewById(R.id.id_smartphone_text);

        topNavTitleTxt.setText(getIntent().getStringExtra("title_id"));
    }
}