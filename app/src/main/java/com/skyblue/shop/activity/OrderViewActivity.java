package com.skyblue.shop.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.skyblue.shop.R;
import com.skyblue.shop.SessionHandler;
import com.skyblue.shop.databinding.ActivityOrderViewBinding;
import com.skyblue.shop.model.Login;
import com.skyblue.shop.model.OrderCancel;
import com.skyblue.shop.model.User;
import com.skyblue.shop.retrofit.APIClient;
import com.skyblue.shop.retrofit.APIInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderViewActivity extends AppCompatActivity {
    private ActivityOrderViewBinding binding;
    private SessionHandler session;
    private final Context context = this;
    private ImageView imgViewProductMain;
    private TextView productNameTextView , productName2TextView , productRatingTextView;
    private TextView productSalePriceTextView , productDiscountTextView;
    private TextView productFeature1TextView , productFeature2TextView , productFeature3TextView , productFeature4TextView;
    private TextView productFeature5TextView;
    private Button placeTheOrderBtn;
    private Dialog confirmCancelOrderDialog;
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

    private APIInterface mApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderViewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mApiInterface = APIClient.getClient().create(APIInterface.class);

        session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();

        onClick();
        initializeVariable();
        loadIntent();
        initConfirmCancelOrderDialog();

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

    private void initConfirmCancelOrderDialog() {
        confirmCancelOrderDialog = new Dialog(context);

        confirmCancelOrderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confirmCancelOrderDialog.setContentView(R.layout.model_dialog_order_cancel);
        confirmCancelOrderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confirmCancelOrderDialog.setCancelable(false);

        Button cancelButton = confirmCancelOrderDialog.findViewById(R.id.order_cancel_button);
        cancelButton.setEnabled(true);

        cancelButton.setOnClickListener(view -> {
            confirmCancelOrderDialog.dismiss();
            cancelOrder();
        });
    }

    private void initializeVariable() {
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

    private void onClick() {
        binding.back.setOnClickListener(view -> {
            finish();
        });
        binding.cancelOrder.setOnClickListener(view -> {
            confirmCancelOrderDialog.show();
        });
    }

    private void cancelOrder() {

        currentDateString = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        currentTimeString = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        currentTimeZoneString = new SimpleDateFormat("z", Locale.getDefault()).format(new Date());

        timeDateString  = currentDateString +" "+currentTimeString;

        String mDate = currentDateString;

        Date date = null;
        String newDate;
        try {
            date = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").parse(timeDateString);
            newDate = new SimpleDateFormat("h:mm a").format(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        Call<OrderCancel> call = mApiInterface.cancelOrder(
                getIntentproductId,
                user.getId(),
                currentDateString,
                timeDateString,
                newDate
        );

        call.enqueue(new Callback<OrderCancel>() {
            @Override
            public void onResponse(Call<OrderCancel> call, Response<OrderCancel> response) {
                if (response.code() == 200) {
                    OrderCancel orderCancel = response.body();

                    if (orderCancel.status.equals("true")){
                        List<OrderCancel.Data> dataList = orderCancel.data;
                        for (OrderCancel.Data data : dataList){
                            Log.d("cancel__", "" + data.message);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderCancel> call, Throwable t) {
                Log.d("cancel__", t.getMessage());
            }
        });
    }
}