package com.skyblue.shop.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.skyblue.shop.AppConstants;
import com.skyblue.shop.databinding.ActivityHomeBinding;
import com.skyblue.shop.model.Products;
import com.skyblue.shop.R;
import com.skyblue.shop.SessionHandler;
import com.skyblue.shop.model.User;
import com.skyblue.shop.adapter.ProductsAdapter;
import com.skyblue.shop.database.DatabaseManager;
import com.skyblue.shop.helper.Helper;
import com.skyblue.shop.helper.LocaleHelper;
import com.skyblue.shop.activity.registration.RegisterActivity;
import com.skyblue.shop.activity.settings.SettingsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Home extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private SessionHandler session;
    private Context context = this;
    private final static String TAG = "Home";
    private String currentLanguage = "en", currentLang;
    private Locale myLocale;
    private LinearLayoutManager linearLayoutManager;
    private List<Products> productsList;
    private RecyclerView.Adapter adapter;
    private User user;
    private DatabaseManager databaseManager;
    private Handler handler = new Handler();
    private Runnable runnable;
    int delay = 10000;

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    int id = 1;
    public static String id3 = "test_channel_03";
    NotificationManager nm;

    private boolean isLoading;
    private int visibleThreshold = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        currentLanguage = getIntent().getStringExtra(currentLang);

        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createchannel();

        session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();

        setUpRecyclerView();
        setOnClickListener();

        if(session.isLoggedIn()){
            binding.navDrawerLayout.userName.setText(user.getName());
            binding.navDrawerLayout.userName.setText(user.getName());

            binding.toolbar.usernameLayout.setVisibility(View.VISIBLE);
            binding.toolbar.loginTextLayout.setVisibility(View.INVISIBLE);
            // Drawer
            binding.navDrawerLayout.loginBtn.setVisibility(View.INVISIBLE);
        }else{
            binding.toolbar.loginTextLayout.setVisibility(View.VISIBLE);
            binding.toolbar.usernameLayout.setVisibility(View.INVISIBLE);

            // Drawer
            binding.navDrawerLayout.userName.setVisibility(View.INVISIBLE);
            binding.navDrawerLayout.usernamePlaceHolder.setVisibility(View.INVISIBLE);
            binding.navDrawerLayout.loginBtn.setVisibility(View.VISIBLE);
        }
        productsList.clear();
        getProducts();

        binding.toolbar.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItem = getLastVisibleItem(new int[]{linearLayoutManager.findLastVisibleItemPosition()});;
                if (!isLoading && totalItemCount > 1 &&
                        totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    //Loading more data from server
                    productsList.add(null);
                    adapter.notifyItemInserted(productsList.size() - 1);
                    getProductsLoadMore();
                    setLoading(true);
                }
            }
        });
    }

    private void getProductsLoadMore() {
        binding.toolbar.progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.HOME_GET_PRODUCTS,
                response -> {
                    setLoading(false);
                    productsList.remove(productsList.size() - 1);
                    adapter.notifyItemRemoved(productsList.size());
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            Products products = new Products();

                            products.setId(jsonObject2.getString("product_id"));
                            products.setProduct_name(jsonObject2.getString("product_name"));
                            products.setTitle(jsonObject2.getString("title"));
                            products.setThumbnail(jsonObject2.getString("thumbnail"));
                            products.setSale_price(jsonObject2.getString("sale_price"));
                            products.setDiscount_price(jsonObject2.getString("discount_price"));
                            products.setRating(jsonObject2.getString("rating"));
                            products.setFeature_1(jsonObject2.getString("feature_1"));
                            products.setFeature_2(jsonObject2.getString("feature_2"));
                            products.setFeature_3(jsonObject2.getString("feature_3"));
                            products.setFeature_4(jsonObject2.getString("feature_4"));
                            products.setFeature_5(jsonObject2.getString("feature_5"));
//                                products.setGategory_id(jsonObject2.getString("gategory_id"));
//                                products.setStock(jsonObject2.getString("stock"));
//                                products.setSeller_id(jsonObject2.getString("seller_id"));
//                                products.setSeller_name(jsonObject2.getString("seller_name"));

                            productsList.add(products);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        binding.toolbar.progressBar.setVisibility(View.INVISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                    binding.toolbar.progressBar.setVisibility(View.INVISIBLE);
                }, error -> {
                    Log.e(TAG, error.toString());
            binding.toolbar.progressBar.setVisibility(View.INVISIBLE);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (session.isLoggedIn()){
                    params.put("user_id", user.getId());
                }else{
                    params.put("user_id", "1");
                }
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void createchannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            new NotificationChannel(id3,
                    getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationChannel mChannel;
            mChannel = new NotificationChannel(id3,
                    getString(R.string.channel_name2),
                    NotificationManager.IMPORTANCE_HIGH);   //importance level
            mChannel.setDescription(getString(R.string.channel_description3));
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.GREEN);
            mChannel.enableVibration(true);
            mChannel.setShowBadge(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            nm.createNotificationChannel(mChannel);
        }
    }

    private void setOnClickListener() {
        binding.toolbar.bottomNavHome.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_home:
                    startActivity(new Intent(context
                            , Home.class));
                    finish();
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.item_review:
                    if (session.isLoggedIn()) {
                        Intent ii2 = new Intent(context, DrawerMyOrderActivity.class);
                        startActivity(ii2);
                        overridePendingTransition(0, 0);
                    } else {
                        Intent ii = new Intent(context, RegisterActivity.class);
                        startActivity(ii);
                    }
                    return true;

                case R.id.item_account:
                    if (session.isLoggedIn()) {
                        Intent ii = new Intent(context, AccountActivity.class);
                        startActivity(ii);
                    } else {
                        Intent ii = new Intent(context, RegisterActivity.class);
                        startActivity(ii);
                    }
                    return true;

                case R.id.item_search:
                    Intent ii = new Intent(context, SearchActivity.class);
                    startActivity(ii);
                    return true;
            }
            return false;
        });

        binding.toolbar.search.setOnClickListener(view -> {
            if (session.isLoggedIn()) {
                Intent ii = new Intent(context, SearchActivity.class);
                startActivity(ii);
                overridePendingTransition(0,0);
            } else {
                Intent ii = new Intent(context, RegisterActivity.class);
                startActivity(ii);
            }
        });

        binding.toolbar.loginTextLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, RegisterActivity.class);
            startActivity(intent);
        });

        binding.toolbar.userAccountLayoutBtn.setOnClickListener(v -> {
            session = new SessionHandler(context);
            if(session.isLoggedIn()){
                Intent intent = new Intent(context, AccountActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);
            }
        });

        binding.toolbar.usernameLayout.setOnClickListener(v -> {
            session = new SessionHandler(context);
            if(session.isLoggedIn()){
                Intent intent = new Intent(context, AccountActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpRecyclerView() {
        productsList = new ArrayList<>();
        adapter = new ProductsAdapter(context.getApplicationContext(), productsList);
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.toolbar.recyclerView.setHasFixedSize(true);
        binding.toolbar.recyclerView.setItemViewCacheSize(20);
        binding.toolbar.recyclerView.setLayoutManager(linearLayoutManager);
        binding.toolbar.recyclerView.setAdapter(adapter);
    }

    private void getProducts() {
        binding.toolbar.progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.HOME_GET_PRODUCTS,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            Products products = new Products();

                            products.setId(jsonObject2.getString("product_id"));
                            products.setProduct_name(jsonObject2.getString("product_name"));
                            products.setTitle(jsonObject2.getString("title"));
                            products.setThumbnail(jsonObject2.getString("thumbnail"));
                            products.setSale_price(jsonObject2.getString("sale_price"));
                            products.setDiscount_price(jsonObject2.getString("discount_price"));
                            products.setRating(jsonObject2.getString("rating"));
                            products.setFeature_1(jsonObject2.getString("feature_1"));
                            products.setFeature_2(jsonObject2.getString("feature_2"));
                            products.setFeature_3(jsonObject2.getString("feature_3"));
                            products.setFeature_4(jsonObject2.getString("feature_4"));
                            products.setFeature_5(jsonObject2.getString("feature_5"));
//                                products.setGategory_id(jsonObject2.getString("gategory_id"));
//                                products.setStock(jsonObject2.getString("stock"));
//                                products.setSeller_id(jsonObject2.getString("seller_id"));
//                                products.setSeller_name(jsonObject2.getString("seller_name"));

                            productsList.add(products);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        binding.toolbar.progressBar.setVisibility(View.INVISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                    binding.toolbar.progressBar.setVisibility(View.INVISIBLE);
                }, error -> {
                    Log.e(TAG, error.toString());
            binding.toolbar.progressBar.setVisibility(View.INVISIBLE);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (session.isLoggedIn()){
                    params.put("user_id", user.getId());
                }else{
                    params.put("user_id", "1");
                }
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void ClickMenu(View view){
        openDrawer(binding.drawerLayout);
    }

    private static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view){
        closeDrawer(binding.drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view){
        recreate();
    }

    public void ClickGategoryIcon(View view){
        if (session.isLoggedIn()) {
            Intent ii = new Intent(Home.this, SearchActivity.class);
            startActivity(ii);
        } else {
            Intent ii = new Intent(Home.this, RegisterActivity.class);
            startActivity(ii);
        }
    }

    public void ClickDashboard(View view){
        redirectActivity(this, DashActivity.class);
    }

    public void ClickAboutUs(View view){
        redirectActivity(this, AboutUsActivity.class);
    }

    public void ClickLogOut(View view){
        session = new SessionHandler(context);
        if (session.isLoggedIn()) {
            session = new SessionHandler(getApplicationContext());
            User user = session.getUserDetails();
            session.logoutUser();
            Intent intent = new Intent(context, Home.class);
            startActivity(intent);
            finish();
        } else {
            Intent ii = new Intent(context, RegisterActivity.class);
            startActivity(ii);
        }
    }

    public void ClickSettings(View view){
        redirectActivity(this, SettingsActivity.class);
    }

    public void ClickAllGategories(View view){
        redirectActivity(this, DrawerAllGategoriesActivity.class);
    }

    public void ClickOfferZone (View view){
        session = new SessionHandler(context);

        if(session.isLoggedIn()){
            redirectActivity(this, DrawerOfferZoneActivity.class);
        }else{
            Intent intent = new Intent(context, RegisterActivity.class);
            startActivity(intent);
        }

    }

    public void ClickSelectLanguage(View view){
        redirectActivity(this, ChooseLanguageActivity.class);
    }

    public void ClickMyOrder(View view){
        session = new SessionHandler(context);
        if(session.isLoggedIn()){
            redirectActivity(this, DrawerMyOrderActivity.class);
        }else{
            Intent intent = new Intent(context, RegisterActivity.class);
            startActivity(intent);
        }
    }

    public void ClickMyWishList(View view){
        session = new SessionHandler(context);
        if(session.isLoggedIn()){
            redirectActivity(this, DrawerWishListActivity.class);
        }else{
            Intent intent = new Intent(context, RegisterActivity.class);
            startActivity(intent);
        }
    }

    public void ClickAccount(View view){
        session = new SessionHandler(context);
        if(session.isLoggedIn()){
            redirectActivity(this, AccountActivity.class);
        }else{
            Intent intent = new Intent(context, RegisterActivity.class);
            startActivity(intent);
        }
    }

    public void ClickLogin(View view){
        redirectActivity(this, RegisterActivity.class);
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity, aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Home.closeDrawer(binding.drawerLayout);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (session.isLoggedIn()){
            binding.toolbar.userName.setText(user.getName());
            binding.navDrawerLayout.userName.setText(user.getName());
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    private void checkAppIsRunning() {
        if (Helper.isAppRunning(Home.this, "com.skyblue.skybluea")) {
            showNotification();
        } else {
            Toast.makeText(context, "App IS NOT RUNNING", Toast.LENGTH_SHORT).show();
        }
    }

    private void showNotification() {
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context,id3);
        mBuilder.setContentTitle("Machine task")
                .setContentText("App is running")
                .setSmallIcon(R.drawable.ic_launcher_foreground);
        mNotifyManager.notify(id, mBuilder.build());
    }

    private void showMessageInSnackbar(String message) {
        Snackbar snack = Snackbar.make(
                (((Activity) context).findViewById(android.R.id.content)),
                message, Snackbar.LENGTH_SHORT);
        snack.setDuration(Snackbar.LENGTH_SHORT);
        View view = snack.getView();
        TextView tv = view
                .findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);

        TextView tvAction = view
                .findViewById(com.google.android.material.R.id.snackbar_action);
        tvAction.setTextSize(16);
        tvAction.setTextColor(Color.WHITE);
        snack.show();
    }

    public void setLoading(boolean status) {
        isLoading = status;
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            }
            else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }
}