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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class NameActivity extends AppCompatActivity {
   private SessionHandler session;

    private String mobile_ccode , password , name , mobile , country , country_name_code , phone_code;
    private String spinnerDateHolder , spinnerMonthHolder , spinnerYearHolder , getDate;
    private String dateHolder , timeHolder, timeZoneHolder, dateTimeZoneHolder;
    private String GenderIdStringHolder , GenderStringHolder , firebaseTokenHolder;
    private String currentDateString , currentTimeString , currentTimeZoneString;
    private String TempStatus = "1";
    private ProgressDialog pDialog;
    private TextView   dateTextView , GenderIdTextView , Gender , TermsAndData;
    private TextView dateText , timeText , timeZoneText , firebaseToken;
    RelativeLayout relativeLayoutPrivacy;
    Button register;
    private ProgressDialog progressDialog;
    String area , landmark, city, state , pincode;
    private EditText editTextName , editTextArea , editTextLandmark , editTextPinCode, editTextCity;
    Spinner spinnerState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        session = new SessionHandler(getApplicationContext());

        initializeVariable();
        initSpinnerState();
        genarateFirebaseToken();
        setOnClickListener();

        currentDateString = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        currentTimeString = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        currentTimeZoneString = new SimpleDateFormat("z", Locale.getDefault()).format(new Date());

        dateText.setText(currentDateString);
        timeText.setText(currentTimeString);
        timeZoneText.setText(currentTimeZoneString);
    }

    private void setOnClickListener() {
        relativeLayoutPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NameActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editTextName.getText().toString().trim();
                area = editTextArea.getText().toString();
                landmark = editTextLandmark.getText().toString();
                pincode = editTextPinCode.getText().toString();
                city = editTextCity.getText().toString();
                state = spinnerState.getSelectedItem().toString();
                dateTextView.setText(String.format("%s/%s/%s", spinnerDateHolder, spinnerMonthHolder, spinnerYearHolder));
                getDate = dateTextView.getText().toString().trim();
                dateHolder = dateText.getText().toString().trim();
                timeHolder = timeText.getText().toString().trim();
                timeZoneHolder = timeZoneText.getText().toString().trim();
                dateTimeZoneHolder = dateHolder +" "+ timeHolder +" "+ timeZoneHolder;
                firebaseTokenHolder = firebaseToken.getText().toString().trim();

                if (name.isEmpty() || name.length() < 2) {
                    editTextName.setError(getResources().getString(R.string.please_enter_name));
                    editTextName.requestFocus();
                    return;
                }if ((area.isEmpty() || area.length() < 2))
                {
                    editTextArea.setError(getResources().getString(R.string.please_enter_area_sector_name));
                    editTextArea.requestFocus();
                    return;
                }if ((landmark.isEmpty() || landmark.length() < 2))
                {
                    editTextLandmark.setError(getResources().getString(R.string.please_enter_landmark));
                    editTextLandmark.requestFocus();
                    return;
                }if ((pincode.isEmpty()))
                {
                    editTextPinCode.setError(getResources().getString(R.string.please_enter_pin_code));
                    editTextPinCode.requestFocus();
                    return;
                }if (( pincode.length() < 2))
                {
                    editTextPinCode.setError(getResources().getString(R.string.error_please_enter_valid_pin_code));
                    editTextPinCode.requestFocus();
                    return;
                }
                if (city.isEmpty() || city.length() < 2) {
                    editTextCity.setError(getResources().getString(R.string.please_enter_city_town_name));
                    editTextCity.requestFocus();
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
                            Log.d("TOKEN_UPDATE", Objects.requireNonNull(task.getResult()).getToken());
                            firebaseToken.setText(task.getResult().getToken());
                        }
                    }
                });
    }

    private void initSpinnerState() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_state, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(adapter);
    }

    private void initializeVariable() {
        editTextName = findViewById(R.id.ideditTextName);
        editTextArea = findViewById(R.id.id_area);
        editTextLandmark = findViewById(R.id.id_landmark);
        editTextCity = findViewById(R.id.id_city);
        editTextPinCode = findViewById(R.id.id_pin_code);
        spinnerState = findViewById(R.id.id_spinner_state);
        register = findViewById(R.id.buttonNameNext3);
        dateTextView =  findViewById(R.id.id_date_view);
        relativeLayoutPrivacy = findViewById(R.id.relativeLayoutPrivacy);
        TermsAndData = findViewById(R.id.text_two);
        dateText = findViewById(R.id.id_date_txt_view);
        timeText = findViewById(R.id.id_time_txt_view);
        timeZoneText = findViewById(R.id.id_time_zone_txt_view);
        firebaseToken = findViewById(R.id.firebase_token);
    }

    private void Signup() {

        this.progressDialog = new ProgressDialog(NameActivity.this, R.style.AppCompatAlertDialogStyle);
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setMessage(getResources().getString(R.string.please_wait___));
        this.progressDialog.show();

        String nameHolder = editTextName.getText().toString().trim();
        String mobileHolder = getIntent().getStringExtra("phonenumber");
        String mobileNumberOnlyHolder = getIntent().getStringExtra("phonenumberonly");
        String countryNameHolder = getIntent().getStringExtra("countryname");
        String countryNameCodeHolder = getIntent().getStringExtra("countrynamecode");
        String phoneCodeHolder = getIntent().getStringExtra("phonecode");
        String passwordHolder =  getIntent().getStringExtra("passwordkey");
        String dobHolder =   String.format("%s/%s/%s", spinnerDateHolder, spinnerMonthHolder, spinnerYearHolder);

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
