package com.skyblue.shop.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.skyblue.shop.R;

public class PasswordActivity extends AppCompatActivity {

    EditText Password, ConfirmPassword;
    Button ButtonNext;
    String password , confirmpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        Password = findViewById(R.id.editTextPass1);
        ConfirmPassword = findViewById(R.id.editTextPass2);
        ButtonNext = findViewById(R.id.buttonPassNext);

        ButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password =Password.getText().toString().trim();
                confirmpassword = ConfirmPassword.getText().toString().trim();

                if (password.isEmpty() || password.length() < 1) {
                    Password.setError(getResources().getString(R.string.valid_password_is_required));
                    Password.requestFocus();
                    return;
                }

                if (confirmpassword.isEmpty() || confirmpassword.length() < 1) {
                    ConfirmPassword.setError(getResources().getString(R.string.valid_confirm_password_is_required));
                    ConfirmPassword.requestFocus();
                    return;
                }
                CheckPasswordConfirmPasswordSame();
            }
        });
    }

    private void CheckPasswordConfirmPasswordSame() {
        if (password.equals(confirmpassword))
        {
            Intent intent = new Intent(PasswordActivity.this, NameActivity.class);
            intent.putExtra("passwordkey",password);
            intent.putExtra("confirmpasswordkey",confirmpassword);
            intent.putExtra("phonenumber", getIntent().getStringExtra("phonenumber"));
            intent.putExtra("phonenumberonly", getIntent().getStringExtra("phonenumberonly"));
            intent.putExtra("countryname", getIntent().getStringExtra("countryname"));
            intent.putExtra("countrynamecode", getIntent().getStringExtra("countrynamecode"));
            intent.putExtra("phonecode", getIntent().getStringExtra("phonecode"));
            startActivity(intent);
        }else
        {
            Toast.makeText(PasswordActivity.this, getResources().getString(R.string.password_and_confirm_password_dose_not_match), Toast.LENGTH_SHORT).show();
        }
    }

    // Disable back press button
    @Override
    public void onBackPressed() {
// super.onBackPressed();
// Not calling **super**, disables back button in current screen.
    }
}
