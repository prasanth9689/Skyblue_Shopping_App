package com.skyblue.shop.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.skyblue.shop.R;
import com.skyblue.shop.SessionHandler;
import com.skyblue.shop.databinding.ActivitySplashBinding;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private static final String SHARED_PREFE_ID = "mypref";
    private static final String KEY_PREFE_LANG = "lang";
    Handler handler;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        handler = new Handler();
        handler.postDelayed(() -> {

            SharedPreferences sp = context.getSharedPreferences(SHARED_PREFE_ID, MODE_PRIVATE);
            String LANG_ID = sp.getString(KEY_PREFE_LANG, String.valueOf(3));

//            if (LANG_ID.equals("1")){
                Intent intent=new Intent(context, Home.class);
                startActivity(intent);
                finish();
//            }else{
//                Intent intent=new Intent(context, ChooseLanguageActivity.class);
//                startActivity(intent);
//                finish();
//            }
        },1500);
    }
}
