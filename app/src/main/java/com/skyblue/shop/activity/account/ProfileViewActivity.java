package com.skyblue.shop.activity.account;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.skyblue.shop.R;
import com.skyblue.shop.SessionHandler;
import com.skyblue.shop.model.User;

public class ProfileViewActivity extends AppCompatActivity {
    private SessionHandler session;
    String userNameHolder , areaNameHolder , landMarkHolder , pinCodeHolder , cityNameHolder , stateNameHolder;
    ImageView backButton;
    TextView userName , mobileNumber , areaName , landMark , pinCode , cityName , stateName;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();

        backButton = findViewById(R.id.id_back);
         userName = findViewById(R.id.id_name);
         mobileNumber = findViewById(R.id.id_mobile);
         areaName = findViewById(R.id.id_area_street);
         landMark = findViewById(R.id.id_landmark);
         pinCode = findViewById(R.id.id_pin_code);
         cityName = findViewById(R.id.id_city);
         stateName = findViewById(R.id.id_state);

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
    }
}