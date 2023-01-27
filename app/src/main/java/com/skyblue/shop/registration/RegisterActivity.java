package com.skyblue.shop.registration;

import androidx.appcompat.app.AppCompatActivity;


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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;
import com.skyblue.shop.AppConstants;
import com.skyblue.shop.Home;
import com.skyblue.shop.LoginActivity;
import com.skyblue.shop.R;
import com.skyblue.shop.SessionHandler;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private SessionHandler session;
    Button buttonContinue;
    TextView textViewCountryCode, textViewCountryName,textViewCountryNameCode,TextViewNumberWithPlus, TextViewNumberOnly;
    TextView TextViewMobileNumberStatus , CheckUserExpectationTextView;
    EditText editTextCarrierNumber;
    String mobileNumberHolder , phoneNumber , GetStatusId;
    private ProgressDialog progressDialog;
    Context context = this;
    CountryCodePicker ccp;
    private String TempStatus = "1";
    int SHOW_PROGRESS_DIALOG = 1;
    int DISMISS_PROGRESS_DIALOG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        session = new SessionHandler(getApplicationContext());

        if(session.isLoggedIn()){
            loadHome();
        }

        initializeVariable();
        setOnClickListener();
    }

    private void setOnClickListener() {
        editTextCarrierNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckUserExpectationTextView.setText("");
            }
        });
        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String StringEditTextCarrierNumberWithPlus =ccp.getSelectedCountryCodeWithPlus()+editTextCarrierNumber.getText().toString();
                final String StringTextViewNumberOnly =editTextCarrierNumber.getText().toString();

                if (StringEditTextCarrierNumberWithPlus.isEmpty() || StringEditTextCarrierNumberWithPlus.length() < 10) {

                    editTextCarrierNumber.setError(getResources().getString(R.string.valid_number_is_required));
                    editTextCarrierNumber.requestFocus();
                    return;
                }

                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                ProgressDialog pDialog = new ProgressDialog(RegisterActivity.this, R.style.AppCompatAlertDialogStyle);
                pDialog.setMessage(getResources().getString(R.string.please_wait___));
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();

                phoneNumber = StringEditTextCarrierNumberWithPlus;
                mobileNumberHolder = editTextCarrierNumber.getText().toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.REGISTER_CHECK_USER,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String ServerResponse) {
                                pDialog.dismiss();
                                TextViewMobileNumberStatus.setText(ServerResponse);

                                GetStatusId = TextViewMobileNumberStatus.getText().toString().trim();

                                if (GetStatusId.equals(TempStatus))
                                {
                                    NewUserNext();
                                }else
                                {
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    intent.putExtra("mobile", mobileNumberHolder);
                                    startActivity(intent);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                pDialog.dismiss();
                                showMessageInSnackbar(getString(R.string.error_check_internet_connection));
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {

                        // Creating Map String Params.
                        Map<String, String> params = new HashMap<String, String>();

                        // Adding All values to Params.
                        params.put("mobile", phoneNumber);

                        return params;
                    }
                };

                // Creating RequestQueue.
                RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);

                // Adding the StringRequest object into requestQueue.
                requestQueue.add(stringRequest);
            }
        });
    }

    private void initializeVariable() {
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        buttonContinue = findViewById(R.id.buttonContinue);
        editTextCarrierNumber = (EditText) findViewById(R.id.editTextCarrierNumber);
        textViewCountryCode = findViewById(R.id.textView_countryCode);
        textViewCountryName = findViewById(R.id.textView_countryName);
        textViewCountryNameCode = findViewById(R.id.textView_countryNameCode);
        TextViewNumberWithPlus = findViewById(R.id.id_get_number_with_plus);
        TextViewNumberOnly = findViewById(R.id.id_get_number_only);
        TextViewMobileNumberStatus = findViewById(R.id.mobileStatus);
        CheckUserExpectationTextView = findViewById(R.id.check_user_expectation);
    }


    private void NewUserNext() {

        final String StringEditTextCarrierNumberWithPlus =ccp.getSelectedCountryCodeWithPlus()+editTextCarrierNumber.getText().toString();
        final String StringTextViewNumberOnly =editTextCarrierNumber.getText().toString();

        if (StringEditTextCarrierNumberWithPlus.isEmpty() || StringEditTextCarrierNumberWithPlus.length() < 10) {
            editTextCarrierNumber.setError(getResources().getString(R.string.valid_number_is_required));
            editTextCarrierNumber.requestFocus();
            return;
        }

        String phoneNumber = StringEditTextCarrierNumberWithPlus;

        Intent intent = new Intent(RegisterActivity.this, VerifyPhoneActivity.class);
        intent.putExtra("phonenumber", phoneNumber);
        intent.putExtra("phonenumberonly", StringTextViewNumberOnly);
        intent.putExtra("countryname",ccp.getSelectedCountryName());
        intent.putExtra("countrynamecode",ccp.getSelectedCountryNameCode());
        intent.putExtra("phonecode",ccp.getSelectedCountryCodeWithPlus());
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
}