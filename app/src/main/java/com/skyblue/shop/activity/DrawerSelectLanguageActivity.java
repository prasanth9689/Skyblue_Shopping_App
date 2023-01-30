package com.skyblue.shop.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.skyblue.shop.R;
import com.skyblue.shop.helper.LocaleHelper;

import java.util.Locale;

public class DrawerSelectLanguageActivity extends AppCompatActivity {

    ImageView backButton;
    RadioButton englishLanguageBtn;
    Context context = this;

    Locale myLocale;
    String currentLanguage = "en", currentLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_select_language);

        englishLanguageBtn = findViewById(R.id.language_indian_english_radio_button);

        currentLanguage = getIntent().getStringExtra(currentLang);

        checkCurrentLanguage();

        backButton = findViewById(R.id.id_back);

        englishLanguageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("en");
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void checkCurrentLanguage() {

        Toast.makeText(context, currentLang, Toast.LENGTH_SHORT).show();
    }

    public void onClickTamilLanguage(View view){
        setLocale("ta");
    }

    public void setLocale(String localeName) {
        if (!localeName.equals(currentLanguage)) {
            Context context = LocaleHelper.setLocale(this, localeName);
            //Resources resources = context.getResources();
            myLocale = new Locale(localeName);
            Resources res = context.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(this, DrawerSelectLanguageActivity.class);
            refresh.putExtra(currentLang, localeName);
//            startActivity(refresh);
//            overridePendingTransition(0,0);
//            finish();


            // after on CLick we are using finish to close and then just after that
            // we are calling startactivity(getIntent()) to open our application
            finish();
            startActivity(getIntent());

            // this basically provides animation
            overridePendingTransition(0, 0);
        } else {
            showMessageInSnackbar(getResources().getString(R.string.language_already_selected));
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(context, Home.class);
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
//        System.exit(0);
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    private void showMessageInSnackbar(String message) {
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