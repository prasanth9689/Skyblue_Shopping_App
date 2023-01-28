package com.skyblue.shop.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.skyblue.shop.AppConstants;
import com.skyblue.shop.Home;
import com.skyblue.shop.PrivacyPolicyActivity;
import com.skyblue.shop.R;
import com.skyblue.shop.SessionHandler;
import com.skyblue.shop.databinding.ActivityNameBinding;
import com.skyblue.shop.model.Registration;
import com.skyblue.shop.retrofit.APIClient;
import com.skyblue.shop.retrofit.APIInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NameActivity extends AppCompatActivity {
   private SessionHandler session;
   private ActivityNameBinding binding;
    private String mName , mDate , mTime, mTimeZone, mDateTimeZone, mFirebaseToken;
    private String spinnerDateHolder , spinnerMonthHolder , spinnerYearHolder , getDate;
    private String currentDateString , currentTimeString , currentTimeZoneString;
    String mArea , mLandmark, mCity, mState , mPinCode;
    private ProgressDialog progressDialog;
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNameBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        session = new SessionHandler(getApplicationContext());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        initSpinnerState();
        genarateFirebaseToken();
        setOnClickListener();

        currentDateString = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        currentTimeString = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        currentTimeZoneString = new SimpleDateFormat("z", Locale.getDefault()).format(new Date());
    }

    private void setOnClickListener() {
        binding.relativeLayoutPrivacy.setOnClickListener((View.OnClickListener) v -> {
            Intent intent = new Intent(NameActivity.this, PrivacyPolicyActivity.class);
            startActivity(intent);
        });

        binding.createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mName = binding.name.getText().toString().trim();
                mArea = binding.area.getText().toString();
                mLandmark = binding.landmark.getText().toString();
                mPinCode = binding.pinCode.getText().toString();
                mCity = binding.city.getText().toString();
                mCity = binding.spinnerState.getSelectedItem().toString();
                String dateHolderFinal = String.format("%s/%s/%s", spinnerDateHolder, spinnerMonthHolder, spinnerYearHolder);

                mDateTimeZone = mDate +" "+ mTime +" "+ mTimeZone;

                if (mName.isEmpty() || mName.length() < 2) {
                    binding.name.setError(getResources().getString(R.string.please_enter_name));
                    binding.name.requestFocus();
                    return;
                }if ((mArea.isEmpty() || mArea.length() < 2))
                {
                    binding.area.setError(getResources().getString(R.string.please_enter_area_sector_name));
                    binding.area.requestFocus();
                    return;
                }if ((mLandmark.isEmpty() || mLandmark.length() < 2))
                {
                    binding.landmark.setError(getResources().getString(R.string.please_enter_landmark));
                    binding.landmark.requestFocus();
                    return;
                }if ((mPinCode.isEmpty()))
                {
                    binding.pinCode.setError(getResources().getString(R.string.please_enter_pin_code));
                    binding.pinCode.requestFocus();
                    return;
                }if (( mPinCode.length() < 2))
                {
                    binding.pinCode.setError(getResources().getString(R.string.error_please_enter_valid_pin_code));
                    binding.pinCode.requestFocus();
                    return;
                }
                if (mCity.isEmpty() || mCity.length() < 2) {
                    binding.city.setError(getResources().getString(R.string.please_enter_city_town_name));
                    binding.city.requestFocus();
                }else{
                    Signup();
                }
            }
        });
    }

    private void genarateFirebaseToken() {
        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful())
                        {
                            mFirebaseToken = task.getResult().getToken();
                        }
                    }
                });
    }

    private void initSpinnerState() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_state, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerState.setAdapter(adapter);
    }

    private void Signup() {
        this.progressDialog = new ProgressDialog(NameActivity.this, R.style.AppCompatAlertDialogStyle);
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setMessage(getResources().getString(R.string.please_wait___));
        this.progressDialog.show();


        //-------------------------------------------------------------------------------------------------------



        //-------------------------------------------------------------------------------------------------------

        String mName = binding.name.getText().toString().trim();
        String mMobile = getIntent().getStringExtra("phonenumber");
        String mMobileNumberOnly = getIntent().getStringExtra("phonenumberonly");
        String mCountryName = getIntent().getStringExtra("countryname");
        String mCountryNameCode = getIntent().getStringExtra("countrynamecode");
        String mPhoneCode = getIntent().getStringExtra("phonecode");
        String mPassword =  getIntent().getStringExtra("passwordkey");
        String mDob =   String.format("%s/%s/%s", spinnerDateHolder, spinnerMonthHolder, spinnerYearHolder);

        Call<Registration> call = apiInterface.createNewRegister(mName,
                mMobile,
                mPassword,
                mMobileNumberOnly ,
                mArea,
                mLandmark,
                mPinCode,
                mCity,
                mState,
                mDate,
                mTime,
                mTimeZone,
                mDateTimeZone,
                mFirebaseToken);

        call.enqueue(new Callback<Registration>() {
            @Override
            public void onResponse(Call<Registration> call, Response<Registration> response) {
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Registration> call, Throwable t) {

            }
        });

        AndroidNetworking.post(AppConstants.REGISTER_URL)
                .addBodyParameter("name", nameHolder)
                .addBodyParameter("mobile", mobileHolder)
                .addBodyParameter("password", passwordHolder)
                .addBodyParameter("mobile_number_only", mobileNumberOnlyHolder)
                .addBodyParameter("area", area)
                .addBodyParameter("landmark", landmark)
                .addBodyParameter("pin_code", pincode)
                .addBodyParameter("city", city)
                .addBodyParameter("state", state)
                .addBodyParameter("date", dateHolder)
                .addBodyParameter("time", timeHolder)
                .addBodyParameter("time_zone", timeZoneHolder)
                .addBodyParameter("date_time_zone", dateTimeZoneHolder)
                .addBodyParameter("token", firebaseTokenHolder)
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                     //   session.loginUser(mobileHolder,nameHolder);
                        Toast.makeText(NameActivity.this,response,Toast.LENGTH_SHORT).show();
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.optString("status").equals("true"))
                            {
                                ArrayList<CurrentUser> CurrentUserArrayList = new ArrayList<>();

                                JSONArray jsonArrayst = jsonObject.getJSONArray("data");

                                for (int i = 0; i < jsonArrayst.length(); i++)
                                {
                                    CurrentUser CurrentUserDetails = new CurrentUser();
                                    JSONObject dataobjst = jsonArrayst.getJSONObject(i);

                                    CurrentUserDetails.setId(dataobjst.getString("id"));

                                    CurrentUserArrayList.add(CurrentUserDetails);
                                }
                                for (int j = 0; j < CurrentUserArrayList.size(); j++) {

                                    String userId = CurrentUserArrayList.get(j).getId();

                                    session.loginUser(userId , mobileHolder ,nameHolder , area , landmark , pincode , city , state);

                                    Intent intent = new Intent(NameActivity.this , Home.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                }
                            } else {
                                //      Toast.makeText(HomeHomeFragmentActivity.this.getActivity().getApplicationContext(), jsonObjectluser.optString("message") + "", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(NameActivity.this,"Error",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    static class CurrentUser
    {
        private String id;
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
    }
}
