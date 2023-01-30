package com.skyblue.shop.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skyblue.shop.AppConstants;
import com.skyblue.shop.model.Products;
import com.skyblue.shop.adapter.ProductsAdapter;
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

public class ProductsGridViewActivity extends AppCompatActivity {
    private SessionHandler session;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<Products> productsList;
    private RecyclerView.Adapter adapter;
    ProgressBar progressDialog;
    Context context = this;
    ImageView backBtn;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_grid_view);

        session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();

        initializeVariable();
        initRecyclerView();
        setOnClickListener();

        productsList.clear();
        getProducts();
    }

    private void setOnClickListener() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initRecyclerView() {
        productsList = new ArrayList<>();
        adapter = new ProductsAdapter(context.getApplicationContext(), productsList);
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initializeVariable() {
        recyclerView = findViewById(R.id.recycler_view);
        progressDialog = findViewById(R.id.progressBar);
        backBtn = findViewById(R.id.products_grird_view_back_btn);
    }

    private void getProducts() {
        progressDialog.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.HOME_GET_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
}