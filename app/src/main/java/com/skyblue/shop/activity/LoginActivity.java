package com.skyblue.shop.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.skyblue.shop.R;
import com.skyblue.shop.SessionHandler;
import com.skyblue.shop.Utils;
import com.skyblue.shop.activity.registration.RegistrationHandler;
import com.skyblue.shop.databinding.ActivityLoginBinding;
import com.skyblue.shop.model.Login;
import com.skyblue.shop.model.RegistrationSpModel;
import com.skyblue.shop.model.User;
import com.skyblue.shop.activity.registration.RegisterActivity;
import com.skyblue.shop.retrofit.APIClient;
import com.skyblue.shop.retrofit.APIInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private SessionHandler mSession;
    private RegistrationHandler mRegisterSession;
    private static final String KEY_EMPTY = "";
    private String mName , mMobile , mPassword , mFirebaseToken , mMobileNumber;
    private static final String TAG = "Login";
    private ProgressDialog mProgressDialog;
    private APIInterface mApiInterface;
    private Context mContext = this;
    private User user;
    private RegistrationSpModel mRegistrationSpModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FULL_SCREEN_REQUEST();
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        CheckNetwork network = new CheckNetwork(getApplicationContext());
        network.registerNetworkCallback();

        mSession = new SessionHandler(getApplicationContext());
        mRegisterSession = new RegistrationHandler(getApplicationContext());
        mRegistrationSpModel = mRegisterSession.getRegisterData();

        mApiInterface = APIClient.getClient().create(APIInterface.class);

        if(mSession.isLoggedIn()){
            loadHome();
        }
        setOnClickListener();
       // mMobile = mRegistrationSpModel.getMobile();
        mMobile = getIntent().getStringExtra("mobile");
        binding.etMobile.setText(mMobile);
        binding.createBtn.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void FULL_SCREEN_REQUEST() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void setOnClickListener() {
        binding.loginBtn.setOnClickListener(view -> {
            mMobile = binding.etMobile.getText().toString().toLowerCase().trim();
            mPassword = binding.etPassword.getText().toString().trim();
            if (validateInputs()) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                // Check network connection
                if (GlobalVariables.isNetworkConnected){
                    // Internet Connected
                    login();
                }else{
                    // Not Connected
                    Utils.showMessage(mContext, "Check Internet connection!");
                }
            }
        });

        binding.idBack.setOnClickListener(v -> finish());
    }

        private void loadHome () {
            Intent i = new Intent(getApplicationContext(), Home.class);
            startActivity(i);
            finish();
        }
        private void displayLoader () {
            mProgressDialog = new ProgressDialog(LoginActivity.this, R.style.AppCompatAlertDialogStyle);
            mProgressDialog.setMessage(getResources().getString(R.string.logging_in_please_wait));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        private void login () {
            displayLoader();

            Call<Login> call = mApiInterface.login(
                    mMobile,
                    mPassword,
                    "mFirebaseToken"
            );

            call.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                    mProgressDialog.dismiss();
                    Log.d("Login_", response.code()+"");
                    if (response.code() == 200){
                        // success
                        Log.d("Login_", "Login : retrofit success 200");

                        Login login = response.body();

                        if (login.status.equals("true")){
                            List<Login.Data> dataList = login.data;

                            for (Login.Data data : dataList){
                                Log.d("Login_", "Message :" + data.message);
                                switch (Integer.parseInt(data.message)) {

                                  case 1:
                                      Utils.showMessage(mContext, "Check Username And Password!");
                                      break;

                                  case 2:
                                      Utils.showMessage(mContext, "Account not found!");
                                      break;

                                      case 3:
                                      Utils.showMessageInSnackbar(mContext, "Please Enter Empty Field");
                                      break;

                                      case 4:
                                          Log.d("Login_", "Message :" + data.name);
                                          String userId = data.id;
                                          String userName = data.name;
                                          String userArea = data.area;
                                          String userLandmark = data.landmark;
                                          String userCity = data.city;
                                          String userPinCode = data.pin_code;
                                          String userState = data.state;

                                          Toast.makeText(mContext,"success",Toast.LENGTH_SHORT).show();

                                          mSession.loginUser(userId ,mMobile, userName , userArea , userLandmark , userPinCode , userCity , userState);

                                          Intent intent = new Intent(mContext, Home.class);
                                          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                          startActivity(intent);
                                          break;

                                          default:
                                          Utils.showMessageInSnackbar(mContext, "TRY AGAIN");
                                          break;
                                }
                            }
                        }

                    }else {
                        // failure
                        Utils.showMessageInSnackbar(mContext, "Failed. Check Internet connection.");

                    }
                }

                @Override
                public void onFailure(@NonNull Call<Login> call, @NonNull Throwable t) {
                    mProgressDialog.dismiss();
                    Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                }
            });
//
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.LOGIN_URL,
//                    response -> {
//                        mProgressDialog.dismiss();
//                        try {
//
//                           JSONObject jsonObject = new JSONObject(response);
//
//    if (jsonObject.optString("status").equals("true"))
//    {
//        ArrayList<CurrentUser> CurrentUserArrayList = new ArrayList<>();
//
//        JSONArray jsonArrayst = jsonObject.getJSONArray("data");
//
//        for (int i = 0; i < jsonArrayst.length(); i++)
//        {
//            CurrentUser CurrentUserDetails = new CurrentUser();
//            JSONObject dataobjst = jsonArrayst.getJSONObject(i);
//
//            CurrentUserDetails.setId(dataobjst.getString("id"));
//            CurrentUserDetails.setName(dataobjst.getString("name"));
//            CurrentUserDetails.setMessage(dataobjst.getString("message"));
//            CurrentUserDetails.setArea(dataobjst.getString("area"));
//            CurrentUserDetails.setLandmark(dataobjst.getString("landmark"));
//            CurrentUserDetails.setCity(dataobjst.getString("city"));
//            CurrentUserDetails.setPin_code(dataobjst.getString("pin_code"));
//            CurrentUserDetails.setState(dataobjst.getString("state"));
//
//            CurrentUserArrayList.add(CurrentUserDetails);
//        }
//        for (int j = 0; j < CurrentUserArrayList.size(); j++) {
//
//            switch (Integer.parseInt(CurrentUserArrayList.get(j).getMessage())) {
//
//                case 1:
//                    Utils.showMessage(context, "Check Username And Password!");
//                    break;
//
//                case 2:
//                    Utils.showMessage(context, "Account not found!");
//                    break;
//
//                case 3:
//                    Utils.showMessageInSnackbar(context, "Please Enter Empty Field");
//                    break;
//
//                case 4:
//
//                    String userId = CurrentUserArrayList.get(j).getId();
//                    String userName = CurrentUserArrayList.get(j).getName();
//                    String userArea = CurrentUserArrayList.get(j).getArea();
//                    String userLandmark = CurrentUserArrayList.get(j).getLandmark();
//                    String userCity = CurrentUserArrayList.get(j).getCity();
//                    String userPinCode = CurrentUserArrayList.get(j).getPin_code();
//                    String userState = CurrentUserArrayList.get(j).getState();
//
//                    Toast.makeText(context,"success",Toast.LENGTH_SHORT).show();
//
//                    mSession.loginUser(userId ,mMobile, userName , userArea , userLandmark , userPinCode , userCity , userState);
//
//                    Intent intent = new Intent(context, Home.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    break;
//
//                default:
//                    Utils.showMessageInSnackbar(context, "SERVER ERROR!");
//                    break;
//            }
//            mProgressDialog.dismiss();
//        }
//    } else {
//        //      Toast.makeText(HomeHomeFragmentActivity.this.getActivity().getApplicationContext(), jsonObjectluser.optString("message") + "", Toast.LENGTH_SHORT).show();
//    }
//} catch (JSONException e) {
//    e.printStackTrace();
//}
//
//                    },
//                    volleyError -> {
//                        mProgressDialog.dismiss();
//                        showMessageInSnackbar(getString(R.string.error_check_internet_connection));
//                    }) {
//                @Override
//                protected Map<String, String> getParams() {
//
//                    // Creating Map String Params.
//                    Map<String, String> params = new HashMap<String, String>();
//
//                    // Adding All values to Params.
//                    params.put("mobile", mMobile);
//                    params.put("password", mPassword);
//                    params.put("token", mFirebaseToken);
//
//                    return params;
//                }
//            };
//
//            RequestQueue requestQueue = Volley.newRequestQueue(context);
//            requestQueue.add(stringRequest);
}

        /**
         * Validates inputs and shows error if any
         * @return
         */
        private boolean validateInputs () {
            if (KEY_EMPTY.equals(mMobile)) {
                binding.etMobile.setError(getResources().getString(R.string.phone_cannot_be_empty));
                binding.etMobile.requestFocus();
                return false;
            }
            if (KEY_EMPTY.equals(mPassword)) {
                binding.etPassword.setError(getResources().getString(R.string.password_cannot_be_empty));
                binding.etPassword.requestFocus();
                return false;
            }
            return true;
        }

        private void showMessageInSnackbar (String message){
            Snackbar snack = Snackbar.make(
                    (((Activity) mContext).findViewById(android.R.id.content)),
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

