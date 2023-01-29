package com.skyblue.shop.registration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.skyblue.shop.Home;
import com.skyblue.shop.LoginActivity;
import com.skyblue.shop.R;
import com.skyblue.shop.SessionHandler;
import com.skyblue.shop.databinding.ActivityRegisterBinding;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
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

        session = new SessionHandler(getApplicationContext());

        if(session.isLoggedIn()){
            loadHome();
        }
        setOnClickListener();
    }

    private void setOnClickListener() {
        binding.mobileNo.setOnClickListener((View.OnClickListener) v -> binding.checkUserExpectation.setText(""));
       binding.continueBtn.setOnClickListener(v -> {
           mMobileNumberFull =binding.ccp.getSelectedCountryCodeWithPlus() + binding.mobileNo.getText().toString();
           mMobileNumber = binding.mobileNo.getText().toString();

           if (mMobileNumberFull.isEmpty() || mMobileNumberFull.length() < 10) {
               binding.mobileNo.setError(getResources().getString(R.string.valid_number_is_required));
               binding.mobileNo.requestFocus();
               return;
           }

           InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
           imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

           ProgressDialog pDialog = new ProgressDialog(RegisterActivity.this, R.style.AppCompatAlertDialogStyle);
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
                           Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
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
                   params.put("mobile", mMobileNumber);
                   return params;
               }
           };
           RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
           requestQueue.add(stringRequest);
       });
    }

    private void NewUserNext() {
        SharedPreferences sp = getSharedPreferences(SHARED_PREFE_ID, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_PREFE_MOBILE, mMobileNumber);
        editor.apply();
        Intent intent = new Intent(RegisterActivity.this, VerifyPhoneActivity.class);
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