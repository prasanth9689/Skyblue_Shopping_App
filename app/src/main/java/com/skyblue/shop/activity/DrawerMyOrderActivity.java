package com.skyblue.shop.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skyblue.shop.AppConstants;
import com.skyblue.shop.adapter.OrderListAdapter;
import com.skyblue.shop.model.Orders;
import com.skyblue.shop.R;
import com.skyblue.shop.SessionHandler;
import com.skyblue.shop.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawerMyOrderActivity extends AppCompatActivity {
    private SessionHandler session;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<Orders> orderList;
    private RecyclerView.Adapter adapter;

    ProgressBar progressDialog;
    Context context = this;
    ImageView backButton;
    User user;
    RelativeLayout mainLayout , emptyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_my_order);

        session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();

        initVariable();
        initSetOnClicklistener();

        orderList = new ArrayList<>();

        setupRecyclerView();

        orderList.clear();
        getOrderList();

//        if (orderList.isEmpty()){
//            emptyLayout.setVisibility(View.VISIBLE);
//            mainLayout.setVisibility(View.INVISIBLE);
//        }
    }

    private void initSetOnClicklistener() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new OrderListAdapter(context.getApplicationContext(), orderList);
        GridLayoutManager layoutManager=new GridLayoutManager(this,1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initVariable() {
        backButton = findViewById(R.id.id_back);
        recyclerView = findViewById(R.id.recycler_view);
        progressDialog = findViewById(R.id.progressBar);
        mainLayout = findViewById(R.id.id_order_list_container);
        emptyLayout = findViewById(R.id.empty_layout);
    }

    private void getOrderList() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.GET_ORDER_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                                Orders orders = new Orders();

                                orders.setProduct_id(jsonObject2.getString("product_id"));
                                orders.setProduct_name(jsonObject2.getString("product_name"));
                                orders.setTitle(jsonObject2.getString("title"));
                                orders.setAmount(jsonObject2.getString("amount"));
                                orders.setThumbnail(jsonObject2.getString("thumbnail"));
                                orders.setFeature_1(jsonObject2.getString("f1"));
                                orders.setFeature_2(jsonObject2.getString("f2"));
                                orders.setFeature_3(jsonObject2.getString("f3"));
                                orders.setFeature_4(jsonObject2.getString("f4"));
                                orders.setFeature_5(jsonObject2.getString("f5"));
                                orders.setOrder_status(jsonObject2.getString("order_status"));
                                orders.setStatus_text(jsonObject2.getString("order_status_text"));
                                orders.setOrder_date_time(jsonObject2.getString("order_date_time"));
                                orders.setOrder_date(jsonObject2.getString("order_date"));
                                orders.setRating(jsonObject2.getString("rating"));
                                orders.setSale_price(jsonObject2.getString("sale_price"));
                                orders.setDiscount_price(jsonObject2.getString("discount_price"));
                                orderList.add(orders);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.setVisibility(View.INVISIBLE);
                        }
                        // adapter.clear();
                        adapter.notifyDataSetChanged();
                        progressDialog.setVisibility(View.INVISIBLE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.setVisibility(View.INVISIBLE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                params.put("user_id", user.getId());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}