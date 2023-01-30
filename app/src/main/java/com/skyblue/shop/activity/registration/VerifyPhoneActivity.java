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
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.skyblue.shop.R;
import com.skyblue.shop.databinding.ActivityVerifyPhoneBinding;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {
    private ActivityVerifyPhoneBinding binding;
    private RegistrationHandler mRegisterSession;
    private String mVerificationId , mPhonenumber;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FULL_SCREEN_REQUEST();
        binding = ActivityVerifyPhoneBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mRegisterSession = new RegistrationHandler(getApplicationContext());

        mAuth = FirebaseAuth.getInstance();
        mPhonenumber = "+91" + mRegisterSession.getRegisterData().getMobile();
        sendVerificationCode(mPhonenumber);

      binding.verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = binding.otpCode.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    binding.otpCode.setError(getResources().getString(R.string.enter_code_verificaton));
                    binding.otpCode.requestFocus();
                    return;
                }
                verifyCode(code);
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

    private void verifyCode(String code) {
        displayLoader();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mProgressDialog.dismiss();
                            Intent intent = new Intent(mContext, PasswordActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            mProgressDialog.dismiss();
                            Toast.makeText(mContext, getResources().getString(R.string.please_enter_correct_otp), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        binding.progressbar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                binding.otpCode.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}