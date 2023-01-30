package com.skyblue.shop.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.skyblue.shop.R;

public class DrawerAllGategoriesActivity extends AppCompatActivity {

    ImageView backButton;

    private static final String HEADPHONE_COMMON_ID = "1";

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_all_gategories);

        backButton = findViewById(R.id.id_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onClickHeadphones(View view){

        Intent intent = new Intent(context, ProductsGridViewActivity.class);
        startActivity(intent);
    }
}