package com.skyblue.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.skyblue.shop.account.DeliveryAddressActivity;
import com.skyblue.shop.account.ProfileViewActivity;

public class AccountActivity extends AppCompatActivity {

    ImageView backButton;
    Button profileButton , deliveryAddButton;
    Button getOrderListBtn;

    Context context = this;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        backButton = findViewById(R.id.id_back);
        profileButton = findViewById(R.id.id_profile_button);
        deliveryAddButton = findViewById(R.id.id_delivery_button);
        getOrderListBtn = findViewById(R.id.id_account_order_list);

        getOrderListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DrawerMyOrderActivity.class);
                startActivity(intent);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, ProfileViewActivity.class);
                startActivity(intent);
            }
        });

        deliveryAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, DeliveryAddressActivity.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}