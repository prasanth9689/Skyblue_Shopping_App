package com.skyblue.shop.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.skyblue.shop.AppConstants;
import com.skyblue.shop.R;
import com.skyblue.shop.SessionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewActivity extends AppCompatActivity {
    private SessionHandler session;

    RelativeLayout addNewReviewBtn;
    ImageView backButton , searchReview;
    Context context = this;
    ProgressBar progressBar;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<Reviews> productsList;
    private RecyclerView.Adapter adapter;

    RelativeLayout emptyReviewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        addNewReviewBtn = findViewById(R.id.add_photo_cam);
        backButton = findViewById(R.id.back_button_review);
        searchReview = findViewById(R.id.id_search_icon_review);
        progressBar = findViewById(R.id.progressBar_review);
        recyclerView = findViewById(R.id.recycler_view);

        emptyReviewLayout = findViewById(R.id.empty_review_layout);

        productsList = new ArrayList<>();

        adapter = new ReviewsAdapter(context.getApplicationContext(), productsList);
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        productsList.clear();
        getReviews(0);


        searchReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Toast.makeText(context, "Search clicked", Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addNewReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                session = new SessionHandler(context);

                if (session.isLoggedIn()) {
                    Intent intent = new Intent(context, PostReviewActivity.class);
                    startActivity(intent);
                } else {
                    Intent ii = new Intent(context, LoginActivity.class);
                    startActivity(ii);
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (! recyclerView.canScrollVertically(1)){ //1 for down
                    LinearLayoutManager layoutManager=LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                    int totalItemCount = layoutManager.getItemCount();
                    getReviews(totalItemCount);
                }
            }
        });
    }

    private void getReviews(int start) {
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.REVIEW_GET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

//                        if (reviews.getReviewText().equals(""))
//                        {
//                            emptyReviewLayout.setVisibility(View.VISIBLE);
//                            Toast.makeText(context, "Review is empty", Toast.LENGTH_SHORT).show();
//                            recyclerView.setVisibility(View.INVISIBLE);
//                        }else{
//                            Toast.makeText(context, "Review data ok", Toast.LENGTH_SHORT).show();
//                            emptyReviewLayout.setVisibility(View.INVISIBLE);
//                        }

                        emptyReviewLayout.setVisibility(View.VISIBLE);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                                Reviews reviews = new Reviews();

                                reviews.setImageUrl(jsonObject2.getString("url"));
                                reviews.setReviewText(jsonObject2.getString("review_text"));

                                productsList.add(reviews);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        // adapter.clear();
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressBar.setVisibility(View.INVISIBLE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                String startIn = String.valueOf(start);
                // Adding All values to Params.
                params.put("start", startIn);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
        Context context;
        private List<Reviews> list;
        View v;

        public ReviewsAdapter(Context context, List<Reviews> list) {
            this.context = context;
            this.list = list;
        }


        @NonNull
        @Override
        public ReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            v = LayoutInflater.from(context).inflate(R.layout.row_model_reviews, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ReviewsAdapter.ViewHolder holder, int position) {
            final Reviews reviews = list.get(position);

            Glide
                    .with(context)
                    .load(reviews.getImageUrl())
                    .placeholder(R.drawable.default_placeholder)
                    .into(holder.reviewImageView);

            holder.reviewTextView.setText(reviews.getReviewText());

          //  Toast.makeText(context, reviews.getReviewText(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            ImageView reviewImageView;
            TextView reviewTextView;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                reviewImageView = itemView.findViewById(R.id.id_thumbnail_review);
                reviewTextView = itemView.findViewById(R.id.review_text);
            }
        }
    }

    public static class Reviews{
         String reviewText , imageUrl;

         public Reviews(){
             this.reviewText = reviewText;
             this.imageUrl = imageUrl;
         }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getReviewText() {
            return reviewText;
        }

        public void setReviewText(String reviewText) {
            this.reviewText = reviewText;
        }
    }
}