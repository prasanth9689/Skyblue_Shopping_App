package com.skyblue.shop.activity.product_view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.skyblue.shop.AppConstants;
import com.skyblue.shop.activity.LoginActivity;
import com.skyblue.shop.R;
import com.skyblue.shop.SessionHandler;
import com.skyblue.shop.databinding.ActivityProductViewBinding;
import com.skyblue.shop.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProductViewActivity extends AppCompatActivity {
    private ActivityProductViewBinding binding;
    private SessionHandler session;

    private ImageView backBtn;

    private Context context = this;
    private ImageView imgViewProductMain;
    private TextView productNameTextView , productName2TextView , productRatingTextView;
    private TextView productSalePriceTextView , productDiscountTextView;
    private TextView productFeature1TextView , productFeature2TextView , productFeature3TextView , productFeature4TextView;
    private TextView productFeature5TextView;
    private Button placeTheOrderBtn;
    private Dialog reportEmptyInputsDialog;
    private Dialog placeTheOrderDialog;
    private Dialog ratingNowDialog;
    private Dialog orderSuccessDialog;
    private String spUserIdHolder , spUserNameHolder , spAreaNameHolder , spLandMarkHolder , spPinCodeHolder , spCityNameHolder , spStateNameHolder;
    private String getIntentproductId , getIntentMainThumnailUrl , getIntentproductName , getIntentproductName2 , getIntentproductRating , getIntentproductSalePrice;
    private String getIntentproductDiscountPrice , getIntentproductFeature1 , getIntentproductFeature2 , getIntentproductFeature3 , getIntentproductFeature4;
    private String getIntentproductFeature5;
    private static final String ORDER_STATUS_SUCCESS = "1";
    private static final String ORDER_STATUS_CANCEL = "0";
    private static final String ORDER_STATUS_SUCCESS_TEXT = "ORDERED";
    private static final String ORDER_STATUS_CANCEL_TEXT = "CANCELED";
    private String currentDateString , currentTimeString , currentTimeZoneString;
    private String timeDateString;
    private LinearLayout ratingNowLL;
    int RATING_STAR_HOLDER;

    int RATING_FIVE_STAR = 5;
    int RATING_FOUR_STAR = 5;
    int RATING_THREE_STAR = 5;
    int RATING_TWO_STAR = 5;
    int RATING_ONE_STAR = 5;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();

        initializeVariable();
        loadIntent();
        initValidateEmptyDeliveryAddressDialog("");
        iniPlaceTheOrderDialog();
        iniPlaceTheOrderPlacedSuccessDialog();
        initRatingNowDialog();
        setOnClickListener();

        currentDateString = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        currentTimeString = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        currentTimeZoneString = new SimpleDateFormat("z", Locale.getDefault()).format(new Date());

        timeDateString  = currentDateString +" "+currentTimeString;

        Glide
                .with(context)
                .load(getIntentMainThumnailUrl)
                .placeholder(R.drawable.default_placeholder)
                .into(imgViewProductMain);

        productNameTextView.setText(getIntentproductName);
        productName2TextView.setText(getIntentproductName2);
        productRatingTextView.setText(getIntentproductRating);
        productSalePriceTextView.setText(getIntentproductSalePrice);
        productDiscountTextView.setPaintFlags(productDiscountTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        productDiscountTextView.setText(getIntentproductDiscountPrice);
        productFeature1TextView.setText(getIntentproductFeature1);
        productFeature2TextView.setText(getIntentproductFeature2);
        productFeature3TextView.setText(getIntentproductFeature3);
        productFeature4TextView.setText(getIntentproductFeature4);
        productFeature5TextView.setText(getIntentproductFeature5);

    }

    private void setOnClickListener() {
        ratingNowLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session = new SessionHandler(context);

                if (session.isLoggedIn()) {
                    ratingNowDialog.show();
                } else {
                    Intent ii = new Intent(context, LoginActivity.class);
                    startActivity(ii);
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        placeTheOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                session = new SessionHandler(context);

                if (session.isLoggedIn()) {

                    String user_name = user.getName();
                    String user_area_name = user.getArea();
                    String user_landmark = user.getLandmark();
                    String user_pin_code = user.getPin_code();
                    String user_city = user.getCity();

                    if (user_name.isEmpty() || user_name.length() < 2) {
                        initValidateEmptyDeliveryAddressDialog(getString(R.string.error_obj_delivery_user_name));
                        reportEmptyInputsDialog.show();
                        return;
                    }if ((user_area_name.isEmpty() || user_area_name.length() < 2))
                    {
                        initValidateEmptyDeliveryAddressDialog(getString(R.string.error_obj_delivery_area_name));
                        reportEmptyInputsDialog.show();
                        return;
                    }if ((user_landmark.isEmpty() || user_landmark.length() < 2))
                    {
                        initValidateEmptyDeliveryAddressDialog(getString(R.string.error_obj_delivery_land_mark));
                        reportEmptyInputsDialog.show();
                        return;
                    }if ((user_pin_code.isEmpty()))
                    {
                        initValidateEmptyDeliveryAddressDialog(getString(R.string.error_obj_delivery_pin_code));
                        reportEmptyInputsDialog.show();
                        return;
                    }if (( user_pin_code.length() < 2))
                    {
                        initValidateEmptyDeliveryAddressDialog(getString(R.string.error_obj_delivery_wrong_pincode));
                        reportEmptyInputsDialog.show();
                        return;
                    }
                    if (user_city.isEmpty() || user_city.length() < 2) {
                        initValidateEmptyDeliveryAddressDialog(getString(R.string.error_obj_delivery_city_name));
                        reportEmptyInputsDialog.show();
                    }else{
                        placeTheOrderNow();
                    }
                } else {
                    Intent ii = new Intent(context, LoginActivity.class);
                    startActivity(ii);
                }
            }
        });
    }

    private void loadIntent() {
        getIntentproductId = getIntent().getStringExtra("product_id");
        getIntentMainThumnailUrl = getIntent().getStringExtra("thumbnail");
        getIntentproductName = getIntent().getStringExtra("product_name");
        getIntentproductName2 = getIntent().getStringExtra("title");
        getIntentproductRating = getIntent().getStringExtra("rating");
        getIntentproductSalePrice = getIntent().getStringExtra("sale_price");
        getIntentproductDiscountPrice = getIntent().getStringExtra("discount_price");
        getIntentproductFeature1 = getIntent().getStringExtra("feature_1");
        getIntentproductFeature2 = getIntent().getStringExtra("feature_2");
        getIntentproductFeature3 = getIntent().getStringExtra("feature_3");
        getIntentproductFeature4 = getIntent().getStringExtra("feature_4");
        getIntentproductFeature5 = getIntent().getStringExtra("feature_5");
    }

    private void initializeVariable() {
        backBtn = findViewById(R.id.back_btn_product_view);
        imgViewProductMain = findViewById(R.id.img_product_view_main);
        productNameTextView = findViewById(R.id.product_name_product_view);
        productName2TextView = findViewById(R.id.product_name_2_product_view);
        productRatingTextView = findViewById(R.id.rating_text_view_product_view_acti);
        productSalePriceTextView = findViewById(R.id.sale_price_product_view_acti);
        productDiscountTextView = findViewById(R.id.discount_price_product_view_acti);
        productFeature1TextView = findViewById(R.id.feature_1_product_view_text_view);
        productFeature2TextView = findViewById(R.id.feature_2_product_view_text_view);
        productFeature3TextView = findViewById(R.id.feature_3_product_view_text_view);
        productFeature4TextView = findViewById(R.id.feature_4_product_view_text_view);
        productFeature5TextView = findViewById(R.id.feature_5_product_view_text_view);
        placeTheOrderBtn = findViewById(R.id.place_the_order_btn);
        ratingNowLL = findViewById(R.id.rating_now_ll);
    }

    public void ClickRatingFiveStar(View view){

        session = new SessionHandler(context);

        if (session.isLoggedIn()) {
            ratingNowDialog.dismiss();
            showMessageInSnackbar(getString(R.string.thank_you_so_much_for_rating));
            RATING_STAR_HOLDER = RATING_FIVE_STAR;
            ratingNow();
        } else {
            Intent ii = new Intent(context, LoginActivity.class);
            startActivity(ii);
        }


    }

    public void ClickRatingFourStar(View view){
        ratingNowDialog.dismiss();
        showMessageInSnackbar(getString(R.string.thank_you_so_much_for_rating));
        RATING_STAR_HOLDER = RATING_FOUR_STAR;
        ratingNow();
    }

    public void ClickRatingThreeStar(View view){
        ratingNowDialog.dismiss();
        showMessageInSnackbar(getString(R.string.thank_you_so_much_for_rating));
        RATING_STAR_HOLDER = RATING_THREE_STAR;
        ratingNow();
    }

    public void ClickRatingTwoStar(View view){
        ratingNowDialog.dismiss();
        showMessageInSnackbar(getString(R.string.thank_you_so_much_for_rating));
        RATING_STAR_HOLDER = RATING_TWO_STAR;
        ratingNow();
    }

    public void ClickRatingOneStar(View view){
        ratingNowDialog.dismiss();
        showMessageInSnackbar(getString(R.string.thank_you_so_much_for_rating));
        RATING_STAR_HOLDER = RATING_ONE_STAR;
        ratingNow();
    }

    private void ratingNow() {
//        AndroidNetworking.post(AppConstants.RATING_PRODUCT)
//                .addBodyParameter("logged_user_id" , spUserIdHolder)
//                .addBodyParameter("product_name" , getIntentproductName)
//                .addBodyParameter("product_id" , getIntentproductId)
//                .addBodyParameter("date" , currentDateString)
//                .addBodyParameter("date_time" , timeDateString)
//                .addBodyParameter("rating_star" , String.valueOf(RATING_STAR_HOLDER))
//                .setPriority(Priority.HIGH)
//                .build()
//                .getAsString(new StringRequestListener() {
//                    @Override
//                    public void onResponse(String response) {
//                        switch (Integer.parseInt(response )) {
//
//                            case 0:
//                                Toast.makeText(context , getResources().getText(R.string.failed_try_again), Toast.LENGTH_SHORT).show();
//                                showMessageInSnackbar(String.valueOf(getResources().getText(R.string.failed_try_again)));
//                                break;
//                            case 1:
//                              //  showMessageInSnackbar(String.valueOf(getResources().getText(R.string.success)));
//                                // updateSharedPreferences();
//                                break;
//
//                            default:
//                                break;
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        placeTheOrderDialog.dismiss();
//                        Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    private void iniPlaceTheOrderPlacedSuccessDialog() {
        orderSuccessDialog = new Dialog(context);

        orderSuccessDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        orderSuccessDialog.setContentView(R.layout.model_dialog_order_success);
        orderSuccessDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        orderSuccessDialog.setCancelable(false);

        Button backBtn = orderSuccessDialog.findViewById(R.id.order_success_back_btn);
        backBtn.setEnabled(true);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initRatingNowDialog() {
        ratingNowDialog = new Dialog(context);

        ratingNowDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ratingNowDialog.setContentView(R.layout.model_dialog_rating_now);
        ratingNowDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ratingNowDialog.setCancelable(false);

        Button ratingNowSaveBtn = ratingNowDialog.findViewById(R.id.rating_save_btn);
        Button ratingNowCancelBtn = ratingNowDialog.findViewById(R.id.rating_cancel_btn);
        ratingNowSaveBtn.setEnabled(true);

        ratingNowCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ratingNowDialog.dismiss();
            }
        });

        ratingNowSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "not imple", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void iniPlaceTheOrderDialog() {
        placeTheOrderDialog = new Dialog(context);

        placeTheOrderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        placeTheOrderDialog.setContentView(R.layout.model_dialog_place_the_order);
        placeTheOrderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        placeTheOrderDialog.setCancelable(false);

    }

    private void placeTheOrderNow() {
        placeTheOrderDialog.show();

        AndroidNetworking.post(AppConstants.PLACING_THE_ORDER)
                .addBodyParameter("user_id" , user.getId())
                .addBodyParameter("product_name" , getIntentproductName)
                .addBodyParameter("amount" , getIntentproductSalePrice)
                .addBodyParameter("product_id" , getIntentproductId)
                .addBodyParameter("order_status" , ORDER_STATUS_SUCCESS)
                .addBodyParameter("order_status_text" , ORDER_STATUS_SUCCESS_TEXT)
                .addBodyParameter("placed_order_date" , currentDateString)
                .addBodyParameter("placed_order_date_time" , timeDateString)
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        placeTheOrderDialog.dismiss();

                        switch (Integer.parseInt(response )) {

                            case 0:
                                Toast.makeText(context , getResources().getText(R.string.failed_try_again), Toast.LENGTH_SHORT).show();
                                showMessageInSnackbar(String.valueOf(getResources().getText(R.string.failed_try_again)));
                                break;
                            case 1:
                               // showMessageInSnackbar(String.valueOf(getResources().getText(R.string.success)));
                                orderSuccessDialog.show();
                               // updateSharedPreferences();
                                break;

                            default:
                                break;
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        placeTheOrderDialog.dismiss();
                        Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initValidateEmptyDeliveryAddressDialog(String errorObject) {
        reportEmptyInputsDialog = new Dialog(context);

        reportEmptyInputsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        reportEmptyInputsDialog.setContentView(R.layout.model_dialog_report_empty_delivery_address);
        reportEmptyInputsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button closeBtn = reportEmptyInputsDialog.findViewById(R.id.close_btn);
        TextView errorTextView = reportEmptyInputsDialog.findViewById(R.id.empty_field_product_view_place_the_order);
        closeBtn.setEnabled(true);

        errorTextView.setText(errorObject);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportEmptyInputsDialog.dismiss();
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