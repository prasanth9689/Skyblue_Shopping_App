package com.skyblue.shop.activity.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.skyblue.shop.R;
import com.skyblue.shop.databinding.ActivityVerifyPhoneBinding;

import java.util.concurrent.TimeUnit;


public class VerifyPhoneActivity extends AppCompatActivity {
    private ActivityVerifyPhoneBinding binding;
    private RegistrationHandler mRegisterSession;
    private FirebaseAuth mAuth;
    private String verificationId;
    private ProgressDialog mProgressDialog;
    private Context mContext = this;
    private String mPhonenumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FULL_SCREEN_REQUEST();
        binding = ActivityVerifyPhoneBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mRegisterSession = new RegistrationHandler(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();



        //  mAuth = FirebaseAuth.getInstance();
        mPhonenumber = "+91" + mRegisterSession.getRegisterData().getMobile();
        sendVerificationCode(mPhonenumber);

      binding.verifyBtn.setOnClickListener(v -> {
          String code = binding.otpCode.getText().toString().trim();
          if (code.isEmpty() || code.length() < 6) {
              binding.otpCode.setError(getResources().getString(R.string.enter_code_verificaton));
              binding.otpCode.requestFocus();
              return;
          }
          displayLoader();
          verifyCode(code);
      });
      binding.back.setOnClickListener(view1 -> finish());
    }

    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // callback method is called on Phone auth provider.
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId = s;
        }
        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                binding.otpCode.setText(code);

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                verifyCode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
                            mProgressDialog.dismiss();
                            Intent i = new Intent(mContext, PasswordActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            mProgressDialog.dismiss();
                            Toast.makeText(mContext, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

        private void FULL_SCREEN_REQUEST() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void displayLoader() {
        mProgressDialog = new ProgressDialog(mContext , R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setMessage(getResources().getString(R.string.checking_otp_please_wait));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }
}