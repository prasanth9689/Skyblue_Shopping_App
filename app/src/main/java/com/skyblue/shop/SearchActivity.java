package com.skyblue.shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    private SessionHandler session;
    ImageView backButton;

    private RecyclerView sList;
    private LinearLayoutManager linearLayoutManager;
    private List<Products> searchList;
    private RecyclerView.Adapter adapter;


    EditText searchEditText;
    String QueryHolder;
    Context context = this;
    ProgressBar progressDialog;
    String LOGGED_USER_ID;
    RelativeLayout emptySearchContainer;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        emptySearchContainer = findViewById(R.id.empty_search_container);

        session = new SessionHandler(context);
        user = session.getUserDetails();

        backButton = findViewById(R.id.back_btn_search_view);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progressDialog = findViewById(R.id.progressBar);
        //getSupportActionBar().hide();

        sList = findViewById(R.id.recycler_view);

        searchList = new ArrayList<>();

        adapter = new ProductsAdapter(context.getApplicationContext(), searchList);
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        sList.setHasFixedSize(true);
        sList.setItemViewCacheSize(20);
        sList.setLayoutManager(linearLayoutManager);
        sList.setAdapter(adapter);

        ImageView searchImageButton = (ImageView) findViewById(R.id.id_action_search2);

        searchEditText = (EditText) findViewById(R.id.editTextSearch);

        searchEditText.requestFocus();

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                String searchTextHolder = searchEditText.getText().toString();
                if (searchTextHolder.equals(""))
                {
                    Toast.makeText(context, "Cannot be empty field", Toast.LENGTH_SHORT).show();
                }else {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        emptySearchContainer.setVisibility(View.GONE);
                        searchList.clear();
                        getData();
                        return true;
                    }
                }
                return false;
            }
        });
        searchImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptySearchContainer.setVisibility(View.GONE);
                searchList.clear();
                getData();
            }
        });
    }

    private void getData() {
        progressDialog.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.SEARCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.setVisibility(View.GONE);
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

                                searchList.add(products);
                                //       Toast.makeText(SearchActivity.this, jsonObject2.getString("id"), Toast.LENGTH_LONG).show();
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

                String Query = searchEditText.getText().toString();
                String QueryFormat = "%" + Query + "%";
                // Adding All values to Params.
                params.put("user_id", user.getId());
                params.put("query_text", Query);
                params.put("query_format", QueryFormat);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    public class Search {

        public String productId , image_url , productName , productName2 , salePrice , discountPrice , rating ;
        public String feature_1 , feature_2 , feature_3 , feature_4 , feature_5 ;


        public Search() {
            this.image_url = image_url;
            this.productName = productName;
            this.productName2 = productName2;
            this.salePrice = salePrice;
            this.discountPrice = discountPrice;
            this.rating = rating;
            this.productId = productId;
            this.feature_1 = feature_1;
            this.feature_2 = feature_2;
            this.feature_3 = feature_3;
            this.feature_4 = feature_4;
            this.feature_5 = feature_5;
        }

        public String getImage_url() {
            return image_url;
        }

        public String getProductName() {
            return productName;
        }

        public String getProductName2() {
            return productName2;
        }

        public String getSalePrice() {
            return salePrice;
        }

        public String getDiscountPrice() {
            return discountPrice;
        }

        public String getRating() {
            return rating;
        }

        public String getProductId() {
            return productId;
        }

        public String getFeature_1() {
            return feature_1;
        }

        public String getFeature_2() {
            return feature_2;
        }

        public String getFeature_3() {
            return feature_3;
        }

        public String getFeature_4() {
            return feature_4;
        }

        public String getFeature_5() {
            return feature_5;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public void setProductName2(String productName2) {
            this.productName2 = productName2;
        }

        public void setSalePrice(String salePrice) {
            this.salePrice = salePrice;
        }

        public void setDiscountPrice(String discountPrice) {
            this.discountPrice = discountPrice;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public void setFeature_1(String feature_1) {
            this.feature_1 = feature_1;
        }

        public void setFeature_2(String feature_2) {
            this.feature_2 = feature_2;
        }

        public void setFeature_3(String feature_3) {
            this.feature_3 = feature_3;
        }

        public void setFeature_4(String feature_4) {
            this.feature_4 = feature_4;
        }

        public void setFeature_5(String feature_5) {
            this.feature_5 = feature_5;
        }
    }

}