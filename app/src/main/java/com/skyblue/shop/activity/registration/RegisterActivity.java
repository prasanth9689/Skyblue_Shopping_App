package com.skyblue.shop.activity.registration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.skyblue.shop.AppConstants;
import com.skyblue.shop.Utils;
import com.skyblue.shop.activity.Home;
import com.skyblue.shop.activity.LoginActivity;
import com.skyblue.shop.R;
import com.skyblue.shop.SessionHandler;
import com.skyblue.shop.databinding.ActivityRegisterBinding;
import com.skyblue.shop.helper.CheckNetwork;
import com.skyblue.shop.helper.GlobalVariables;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private RegistrationHandler mRegisterSession;
    private SessionHandler session;
    private String mMobileNumberFull, mMobileNumber;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        CheckNetwork network = new CheckNetwork(getApplicationContext());
        network.registerNetworkCallback();

        session = new SessionHandler(getApplicationContext());
        mRegisterSession = new RegistrationHandler(getApplicationContext());

        if(session.isLoggedIn()){
            loadHome();
        }
        setOnClickListener();
    }

    private void setOnClickListener() {
        binding.mobileNo.setOnClickListener(v -> binding.checkUserExpectation.setText(""));
        binding.continueBtn.setOnClickListener(v -> {
           mMobileNumberFull =binding.ccp.getSelectedCountryCodeWithPlus() + binding.mobileNo.getText().toString();
           mMobileNumber = binding.mobileNo.getText().toString();

           if (mMobileNumberFull.isEmpty() || mMobileNumberFull.length() < 10) {
               binding.mobileNo.setError(getResources().getString(R.string.valid_number_is_required));
               binding.mobileNo.requestFocus();
               return;
           }

            if (GlobalVariables.isNetworkConnected){
                checkAlreadyExists();
            }else{
                Utils.showMessage(context, "Check Internet connection!");
            }
       });
        binding.back.setOnClickListener(view -> finish());
    }

    private void checkAlreadyExists() {
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        ProgressDialog pDialog = new ProgressDialog(context, R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage(getResources().getString(R.string.please_wait___));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.REGISTER_CHECK_USER,
                response -> {
                    pDialog.dismiss();
                    if (response.equals("1"))
                    {
                        NewUserNext();
                    }else
                    {
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.putExtra("mobile",mMobileNumber );
                        startActivity(intent);
                    }
                },
                volleyError -> {
                    pDialog.dismiss();
                    showMessageInSnackbar(getString(R.string.error_check_internet_connection));
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", "+91" + mMobileNumber);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void NewUserNext() {
        String country_name = binding.ccp.getSelectedCountryName();
        String country_name_code = binding.ccp.getSelectedCountryNameCode();
        String phone_code = binding.ccp.getSelectedCountryCodeWithPlus();

        mRegisterSession.saveMobileNo(mMobileNumber, country_name, country_name_code, phone_code);
        Intent intent = new Intent(context, VerifyPhoneActivity.class);
        startActivity(intent);
    }

    private void loadHome() {
        Intent i = new Intent(getApplicationContext(), Home.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
        }
    }

    private void showMessageInSnackbar(String message) {
        Snackbar snack = Snackbar.make(
                (((Activity) context).findViewById(android.R.id.content)),
                message, Snackbar.LENGTH_SHORT);
        snack.setDuration(Snackbar.LENGTH_SHORT);
        View view = snack.getView();
        TextView tv = view
                .findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        TextView tvAction = view
                .findViewById(com.google.android.material.R.id.snackbar_action);
        tvAction.setTextSize(16);
        tvAction.setTextColor(Color.WHITE);
        snack.show();
    }
}