package com.skyblue.shop.activity.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.skyblue.shop.R;
import com.skyblue.shop.databinding.ActivityPasswordBinding;

public class PasswordActivity extends AppCompatActivity {
    private ActivityPasswordBinding binding;
    private RegistrationHandler mRegisterSession;
    String mPassword , mConfirmPassword;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mRegisterSession = new RegistrationHandler(getApplicationContext());

        binding.btnNext.setOnClickListener(view1 -> {

            mPassword = binding.edPassword.getText().toString().trim();
            mConfirmPassword = binding.edConfirmPassword.getText().toString().trim();

            if (mPassword.isEmpty() || mPassword.length() < 1) {
                binding.edPassword.setError(getResources().getString(R.string.valid_password_is_required));
                binding.edPassword.requestFocus();
                return;
            }

            if (mConfirmPassword.isEmpty() || mConfirmPassword.length() < 1) {
                binding.edConfirmPassword.setError(getResources().getString(R.string.valid_confirm_password_is_required));
                binding.edConfirmPassword.requestFocus();
                return;
            }
            CheckPasswordConfirmPasswordSame();
        });
    }

    private void CheckPasswordConfirmPasswordSame() {
        if (mPassword.equals(mPassword))
        {
            mRegisterSession.savePassword(mPassword);
            Intent intent = new Intent(mContext, NameActivity.class);
            startActivity(intent);
        }else
        {
            Toast.makeText(mContext, getResources().getString(R.string.password_and_confirm_password_dose_not_match), Toast.LENGTH_SHORT).show();
        }
    }

    // Disable back press button
    @Override
    public void onBackPressed() {
// super.onBackPressed();
// Not calling **super**, disables back button in current screen.
    }
}
