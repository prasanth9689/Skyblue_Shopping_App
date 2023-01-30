package com.skyblue.shop.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.skyblue.shop.helper.CheckNetwork;
import com.skyblue.shop.R;

public class NoInternetActivity extends AppCompatActivity {

    Button refreshBtn;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logic

                if(CheckNetwork.isInternetAvailable(context)) //returns true if internet available
                {
                    finish();
                }

                else
                {
                    showMessageInSnackbar(getResources().getString(R.string.no_internet_connection));
                }
            }
        });
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
