package com.skyblue.shop.activity.account;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.material.snackbar.Snackbar;
import com.skyblue.shop.AppConstants;
import com.skyblue.shop.R;
import com.skyblue.shop.SessionHandler;
import com.skyblue.shop.model.User;

import java.util.ArrayList;
import java.util.List;

public class DeliveryAddressEditActivity extends AppCompatActivity {
    private SessionHandler session;
    EditText nameEditText , areaEditText , landMarkEditText , pincodeEditText , cityEditText;
    Spinner stateSpinner;
    Button saveBtn;
    ImageView backBtn;
    String nameHolder , areaHolder , landMarkHolder , pinCodeHolder , cityHolder , stateHolder;
    Context context = this;
    String stateHolderEditText;
    private ProgressDialog progressDialog;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address_edit);

        session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();

        initializeVariable();

        nameEditText.setText(user.getName());
        areaEditText.setText(user.getArea());
        landMarkEditText.setText(user.getLandmark());
        pincodeEditText.setText(user.getPin_code());
        cityEditText.setText(user.getCity());

        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add(user.getState());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(adapter);

        stateSpinner.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(context,
                        R.array.array_state, android.R.layout.simple_spinner_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                stateSpinner.setAdapter(adapter1);
                return false;
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateHolderEditText = stateSpinner.getSelectedItem().toString();
                if (stateHolderEditText.equals(getString(R.string.select))){
                    showMessageInSnackbar(getString(R.string.please_select_state));
                }else {
                    validateInputs();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , DeliveryAddressActivity.class);
                overridePendingTransition(0,0);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initializeVariable() {
        nameEditText = findViewById(R.id.ideditTextName_edit_delivery_address);
        areaEditText = findViewById(R.id.id_area_delivery_address);
        landMarkEditText = findViewById(R.id.id_landmark_delivery_address);
        pincodeEditText = findViewById(R.id.id_pin_code_delivery_address);
        cityEditText = findViewById(R.id.id_city_delivery_address);
        stateSpinner = findViewById(R.id.id_spinner_state_delivery_address);
        saveBtn = findViewById(R.id.button_save);
        backBtn = findViewById(R.id.back_edit_delivery_address);
    }

    private void validateInputs() {

        nameHolder = nameEditText.getText().toString();
        areaHolder = areaEditText.getText().toString();
        landMarkHolder = landMarkEditText.getText().toString();
        pinCodeHolder = pincodeEditText.getText().toString();
        cityHolder = cityEditText.getText().toString();
        stateHolder = stateSpinner.getSelectedItem().toString();

        if (nameHolder.isEmpty() || nameHolder.length() < 2) {
            nameEditText.setError(getResources().getString(R.string.please_enter_name));
            nameEditText.requestFocus();
            return;
        }if ((areaHolder.isEmpty() || areaHolder.length() < 2))
        {
            areaEditText.setError(getString(R.string.please_enter_area_sector_name));
            areaEditText.requestFocus();
            return;
        }if ((landMarkHolder.isEmpty() || landMarkHolder.length() < 2))
        {
            landMarkEditText.setError(getString(R.string.please_enter_landmark));
            landMarkEditText.requestFocus();
            return;
        }if ((pinCodeHolder.isEmpty()))
        {
            pincodeEditText.setError(getString(R.string.please_enter_pin_code));
            pincodeEditText.requestFocus();
            return;
        }if (( pinCodeHolder.length() < 2))
        {
            pincodeEditText.setError(getString(R.string.error_please_enter_valid_pin_code));
            pincodeEditText.requestFocus();
            return;
        }
        if (cityHolder.isEmpty() || cityHolder.length() < 2) {
            cityEditText.setError(getString(R.string.please_enter_city_town_name));
            cityEditText.requestFocus();
        }else{
            updateNow();
        }
    }

    private void updateNow() {
        this.progressDialog = new ProgressDialog(context, R.style.AppCompatAlertDialogStyle);
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setMessage(getResources().getString(R.string.please_wait___));
        this.progressDialog.show();

        AndroidNetworking.post(AppConstants.UPDATE_DELIVERY_ADDRESS)
                .addBodyParameter("user_id" , user.getId())
                .addBodyParameter("name", user.getName())
                .addBodyParameter("area", user.getArea())
                .addBodyParameter("landmark", user.getLandmark())
                .addBodyParameter("pin_code", user.getPin_code())
                .addBodyParameter("city", user.getCity())
                .addBodyParameter("state", user.getState())
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        switch (Integer.parseInt(response )) {
                            case 0:
                                Toast.makeText(context , getResources().getText(R.string.failed_try_again), Toast.LENGTH_SHORT).show();
                                showMessageInSnackbar(String.valueOf(getResources().getText(R.string.failed_try_again)));
                                break;
                            case 1:
                                Toast.makeText(context , getResources().getText(R.string.saved_success), Toast.LENGTH_SHORT).show();
                                updateUserDetails();
                                break;

                            default:
                                break;
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUserDetails() {
        String mobile = user.getMobile();
        session.updateLoginUser(mobile , nameHolder , areaHolder , landMarkHolder , pinCodeHolder , cityHolder , stateHolder);
    }

    private void showMessageInSnackbar(String message) {
        Snackbar snack = Snackbar.make(
                (((Activity) context).findViewById(android.R.id.content)),
                message, Snackbar.LENGTH_SHORT);
        snack.setDuration(Snackbar.LENGTH_SHORT);//change Duration as you need
        //snack.setAction(actionButton, new View.OnClickListener());//add your own listener
        View view = snack.getView();
        TextView tv = (TextView) view
                .findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);//change textColor

        TextView tvAction = (TextView) view
                .findViewById(com.google.android.material.R.id.snackbar_action);
        tvAction.setTextSize(16);
        tvAction.setTextColor(Color.WHITE);

        snack.show();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        Intent intent = new Intent(context , DeliveryAddressActivity.class);
        overridePendingTransition(0,0);
        startActivity(intent);
        finish();
    }
}