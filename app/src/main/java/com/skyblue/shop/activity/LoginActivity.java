package com.skyblue.shop.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.skyblue.shop.AppConstants;
import com.skyblue.shop.R;
import com.skyblue.shop.SessionHandler;
import com.skyblue.shop.model.User;
import com.skyblue.shop.Utils;
import com.skyblue.shop.activity.registration.RegisterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String KEY_EMPTY = "";
    private EditText etMobile;
    private EditText etPassword;
    private String name , mobile , password , tokenHolder , mobileNumber;
    private ProgressDialog pDialog;
    private SessionHandler session;
    TextView ForgetPassword , tokenTxt;
    ImageView backButton;
    private ProgressDialog progressDialog;
    Context context = this;
    Button createAccountBtn , btnLogin;
    User user;
    private static final int KEY_STATUS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        session = new SessionHandler(getApplicationContext());

        if(session.isLoggedIn()){
            loadHome();
        }

        initializeVariable();
        setOnClickListener();
        genarateFirabaseToken();

        mobileNumber = getIntent().getStringExtra("mobile");
        etMobile.setText(mobileNumber);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void genarateFirabaseToken() {
        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful())
                        {
                            Log.d("TOKEN_UPDATE",task.getResult().getToken());
                            tokenTxt.setText(task.getResult().getToken());
                        }
                    }
                });
    }

    private void setOnClickListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobile = etMobile.getText().toString().toLowerCase().trim();
                password = etPassword.getText().toString().trim();
                tokenHolder = tokenTxt.getText().toString().trim();
                if (validateInputs()) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                    login();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

        private void initializeVariable () {
            etMobile = findViewById(R.id.etMobile);
            etPassword = findViewById(R.id.etPassword);
            btnLogin = findViewById(R.id.btLogin);
            tokenTxt = findViewById(R.id.firebase_token2);
            backButton = findViewById(R.id.id_back);
            createAccountBtn = findViewById(R.id.create_an_account_btn);
        }

        private void loadHome () {
            Intent i = new Intent(getApplicationContext(), Home.class);
            startActivity(i);
            finish();
        }
        private void displayLoader () {
            pDialog = new ProgressDialog(LoginActivity.this, R.style.AppCompatAlertDialogStyle);
            pDialog.setMessage(getResources().getString(R.string.logging_in_please_wait));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        private void displayLoaderRegisterToken () {
            pDialog = new ProgressDialog(LoginActivity.this, R.style.AppCompatAlertDialogStyle);
            pDialog.setMessage(getResources().getString(R.string.logging_success_please_wait));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        private void login () {
            displayLoader();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.LOGIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pDialog.dismiss();
              //              Utils.showMessage(context, response);
                                                        try {

          JSONObject jsonObject = new JSONObject(response);

        if (jsonObject.optString("status").equals("true"))
        {
            //  Utils.showMessage(context, "Step 1");
            ArrayList<CurrentUser> CurrentUserArrayList = new ArrayList<>();

            JSONArray jsonArrayst = jsonObject.getJSONArray("data");

            for (int i = 0; i < jsonArrayst.length(); i++)
            {
                CurrentUser CurrentUserDetails = new CurrentUser();
                JSONObject dataobjst = jsonArrayst.getJSONObject(i);

                CurrentUserDetails.setId(dataobjst.getString("id"));
                CurrentUserDetails.setName(dataobjst.getString("name"));
                CurrentUserDetails.setMessage(dataobjst.getString("message"));
                CurrentUserDetails.setArea(dataobjst.getString("area"));
                CurrentUserDetails.setLandmark(dataobjst.getString("landmark"));
                CurrentUserDetails.setCity(dataobjst.getString("city"));
                CurrentUserDetails.setPin_code(dataobjst.getString("pin_code"));
                CurrentUserDetails.setState(dataobjst.getString("state"));

                CurrentUserArrayList.add(CurrentUserDetails);
            }
            for (int j = 0; j < CurrentUserArrayList.size(); j++) {

             //   Utils.showMessage(context, String.valueOf(CurrentUserArrayList.get(j).getMessage()));

                switch (Integer.parseInt(CurrentUserArrayList.get(j).getMessage())) {

                    case 1:
                        Utils.showMessage(context, "Check Username And Password!");
                        break;

                    case 2:
                        Utils.showMessage(context, "Account not found!");
                        break;

                    case 3:
                        Utils.showMessageInSnackbar(context, "Please Enter Empty Field");
                        break;

                    case 4:

                        //   Utils.showMessageInSnackbar(context, "success");

                        String userId = CurrentUserArrayList.get(j).getId();
                        String userName = CurrentUserArrayList.get(j).getName();
                        String userArea = CurrentUserArrayList.get(j).getArea();
                        String userLandmark = CurrentUserArrayList.get(j).getLandmark();
                        String userCity = CurrentUserArrayList.get(j).getCity();
                        String userPinCode = CurrentUserArrayList.get(j).getPin_code();
                        String userState = CurrentUserArrayList.get(j).getState();

                        Toast.makeText(context,"success",Toast.LENGTH_SHORT).show();

                        session.loginUser(userId ,mobile, userName , userArea , userLandmark , userPinCode , userCity , userState);

                        Intent intent = new Intent(context, Home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;

                    default:
                        Utils.showMessageInSnackbar(context, "SERVER ERROR!");
                        break;
                }
                pDialog.dismiss();
            }
        } else {
            //      Toast.makeText(HomeHomeFragmentActivity.this.getActivity().getApplicationContext(), jsonObjectluser.optString("message") + "", Toast.LENGTH_SHORT).show();
        }
    } catch (JSONException e) {
        e.printStackTrace();
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
                    params.put("mobile", mobile);
                    params.put("password", password);
                    params.put("token", tokenHolder);

                    return params;
                }
            };

            // Creating RequestQueue.
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            // Adding the StringRequest object into requestQueue.
            requestQueue.add(stringRequest);
}

        /**
         * Validates inputs and shows error if any
         * @return
         */
        private boolean validateInputs () {
            if (KEY_EMPTY.equals(mobile)) {
                etMobile.setError(getResources().getString(R.string.phone_cannot_be_empty));
                etMobile.requestFocus();
                return false;
            }
            if (KEY_EMPTY.equals(password)) {
                etPassword.setError(getResources().getString(R.string.password_cannot_be_empty));
                etPassword.requestFocus();
                return false;
            }
            return true;
        }

        static class CurrentUser {
            private String id, name, area, landmark, pin_code, city, state , message;

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getArea() {
                return area;
            }

            public String getLandmark() {
                return landmark;
            }

            public String getPin_code() {
                return pin_code;
            }

            public String getCity() {
                return city;
            }

            public String getState() {
                return state;
            }

            public String getMessage() {
                return message;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setArea(String area) {
                this.area = area;
            }

            public void setLandmark(String landmark) {
                this.landmark = landmark;
            }

            public void setPin_code(String pin_code) {
                this.pin_code = pin_code;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public void setState(String state) {
                this.state = state;
            }

            public void setMessage(String message) {
                this.message = message;
            }
        }

        private void showMessageInSnackbar (String message){
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

