package com.skyblue.shop.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.skyblue.shop.R;
import com.skyblue.shop.SessionHandler;
import com.skyblue.shop.User;

public class DeliveryAddressActivity extends AppCompatActivity {
    private SessionHandler session;

    String userNameHolder , areaNameHolder , landMarkHolder , pinCodeHolder , cityNameHolder , stateNameHolder;
    ImageView backButton;
    TextView userName , mobileNumber , areaName , landMark , pinCode , cityName , stateName;
    Button editDeliveryAddressBtn;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);

        initializeVariable();

        session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();

        userName.setText(user.getName());
        mobileNumber.setText(user.getMobile());
        areaName.setText(user.getArea());
        landMark.setText(user.getLandmark());
        pinCode.setText(user.getPin_code());
        cityName.setText(user.getCity());
        stateName.setText(user.getState());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editDeliveryAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryAddressActivity.this, DeliveryAddressEditActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initializeVariable() {
        backButton = findViewById(R.id.id_back);
        userName = findViewById(R.id.id_name);
        mobileNumber = findViewById(R.id.id_mobile);
        areaName = findViewById(R.id.id_area_street);
        landMark = findViewById(R.id.id_landmark);
        pinCode = findViewById(R.id.id_pin_code);
        cityName = findViewById(R.id.id_city);
        stateName = findViewById(R.id.id_state);
        editDeliveryAddressBtn = findViewById(R.id.edit_btn_delivey_address);
    }
}