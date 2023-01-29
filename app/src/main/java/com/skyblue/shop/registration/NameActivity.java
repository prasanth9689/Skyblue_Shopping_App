package com.skyblue.shop.registration;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.skyblue.shop.Home;
import com.skyblue.shop.PrivacyPolicyActivity;
import com.skyblue.shop.R;
import com.skyblue.shop.SessionHandler;
import com.skyblue.shop.databinding.ActivityNameBinding;
import com.skyblue.shop.model.Registration;
import com.skyblue.shop.retrofit.APIClient;
import com.skyblue.shop.retrofit.APIInterface;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NameActivity extends AppCompatActivity {
   private SessionHandler session;
   private ActivityNameBinding binding;
    private String mName , mDate , mTime, mTimeZone, mDateTimeZone, mFirebaseToken;
    private String mMobile, mMobileNumberOnly, mPassword;
    private String mTodayDate , mTodayTime , mTodayTimeZone;
    String mArea , mLandmark, mCity, mState , mPinCode;
    private ProgressDialog progressDialog;
    APIInterface apiInterface;
    private Context context = this;

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

        mTodayDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        mTodayTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        mTodayTimeZone = new SimpleDateFormat("z", Locale.getDefault()).format(new Date());
    }

    private void setOnClickListener() {
        binding.relativeLayoutPrivacy.setOnClickListener((View.OnClickListener) v -> {
            Intent intent = new Intent(context, PrivacyPolicyActivity.class);
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
                mState = binding.spinnerState.getSelectedItem().toString();

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
        this.progressDialog = new ProgressDialog(context, R.style.AppCompatAlertDialogStyle);
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setMessage(getResources().getString(R.string.please_wait___));
        this.progressDialog.show();

        mName = binding.name.getText().toString().trim();
        mMobile = getIntent().getStringExtra("phonenumber");
        mMobileNumberOnly = getIntent().getStringExtra("phonenumberonly");
        mPassword =  getIntent().getStringExtra("passwordkey");

        mDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        mTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        mTimeZone = new SimpleDateFormat("z", Locale.getDefault()).format(new Date());

        mDateTimeZone = mDate +" "+ mTime +" "+ mTimeZone;

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

                Registration registration = response.body();

                List<Registration.Data> dataList = registration.data;

                for (Registration.Data data : dataList){

                    session.loginUser(data.user_id , mMobile ,mName , mArea , mLandmark , mPinCode , mCity , mState);

                    Intent intent = new Intent(context , Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                   // Toast.makeText(context,data.user_id, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Registration> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}
